package com.keepin.android.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.keepin.android.BuildConfig
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentSplashBinding
import com.keepin.android.presentation.main.MainActivity
import com.keepin.android.presentation.signin.SignInViewModel
import com.keepin.android.util.UtilDialog
import com.keepin.android.util.navigate
import com.keepin.android.util.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import kotlin.system.exitProcess

@AndroidEntryPoint
class SplashFragment : BindingFragment<FragmentSplashBinding>(R.layout.fragment_splash) {
    private val signInViewModel by viewModels<SignInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        signInViewModel.getFcmToken()
        readLatestVersionFromFireBase()
        setEmailLoginBtnClickListener()
        setSignUpTextClickListener()
        setIsSuccessLoginCollect()
        return binding.root
    }

    private fun readLatestVersionFromFireBase() {
        Firebase.database.reference.child(FIREBASE_BUILD_CONFIG).child(FIREBASE_ANDROID_VERSION)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (isUpdateRequire(snapshot.value.toString())) {
                            showRequireUpdateDialog()
                        } else {
                            startAutoLogin()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.d(error.toString())
                        startAutoLogin()
                    }
                }
            )
    }

    private fun startAutoLogin() {
        signInViewModel.canAutoSignIn.observe(viewLifecycleOwner) { canSignIn ->
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                delay(1000)
                when (canSignIn) {
                    true -> signInViewModel.postLogin()
                    false -> onBoardingCheck()
                }
            }
        }
    }

    private fun onBoardingCheck() {
        when (signInViewModel.getOnBoarding()) {
            true -> startAnimation()
            false -> navigate(R.id.action_splashFragment_to_onBoardingFragment)
        }
    }

    private fun startAnimation() {
        with(binding) {
            layoutSplash.animate().apply {
                translationY(getSplashTranslationYPx())
                duration = 1000
            }.withEndAction {
                btnSignInEmailLogin.splashAnimation()
                layoutSplashSignUp.splashAnimation()
            }
        }.start()
    }

    private fun getSplashTranslationYPx() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            -100f,
            requireContext().resources.displayMetrics
        )

    private fun View.splashAnimation() {
        with(this) {
            animate().apply {
                alpha(1f)
                duration = 1000
            }
            isEnabled = true
        }
    }

    private fun setEmailLoginBtnClickListener() {
        binding.btnSignInEmailLogin.setOnClickListener {
            navigate(R.id.action_splashFragment_to_signInFragment)
        }
    }

    private fun setSignUpTextClickListener() {
        binding.tvSignUp.setOnClickListener {
            navigate(R.id.action_splashFragment_to_signUpFirstFragment)
        }
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    private fun setIsSuccessLoginCollect() {
        repeatOnLifecycle {
            signInViewModel.isSuccessLogin.collect { isSuccessLogin ->
                when (isSuccessLogin) {
                    true -> {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finish()
                    }
                    false -> {
                        UtilDialog(UtilDialog.LOGIN_ERROR) {}.show(
                            childFragmentManager,
                            "login_error"
                        )
                    }
                }
            }
        }
    }

    private fun isUpdateRequire(firebaseValue: String): Boolean {
        val installedVersion = (BuildConfig.VERSION_NAME).split(".").map { it.toInt() }
        val latestVersion = firebaseValue.split(".").map { it.toInt() }

        for (i in 0 until 2) {
            if (installedVersion[i] < latestVersion[i])
                return true
        }
        return false
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun showRequireUpdateDialog() {
        UtilDialog(UtilDialog.UPDATE_REQUIRED) {
            val url = getString(R.string.link_google_play_store) + requireActivity().packageName
            val intent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(url)
            }

            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }

            requireActivity().finishAffinity()
            exitProcess(0)
        }.show(
            childFragmentManager,
            "update_required"
        )
    }

    companion object {
        private const val FIREBASE_BUILD_CONFIG = "build_config"
        private const val FIREBASE_ANDROID_VERSION = "android_version"
    }
}
