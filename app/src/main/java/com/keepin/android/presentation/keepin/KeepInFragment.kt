package com.keepin.android.presentation.keepin

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentKeepInBinding
import com.keepin.android.util.DatePickerDialog
import com.keepin.android.util.KeyBoardUtil
import com.keepin.android.util.MultiPartResolver
import com.keepin.android.util.UtilDialog
import com.keepin.android.util.UtilDialog.Companion.STOP_MODIFY
import com.keepin.android.util.UtilDialog.Companion.STOP_WRITE
import com.keepin.android.util.navigate
import com.keepin.android.util.navigateWithData
import com.keepin.android.util.popBackStack
import com.keepin.android.util.toastMessageUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class KeepInFragment :
    BindingFragment<FragmentKeepInBinding>(R.layout.fragment_keep_in) {
    @Inject
    lateinit var multiPartResolver: MultiPartResolver
    private val keepInViewModel by viewModels<KeepInViewModel>()
    private var isTitleEmpty = true
    private var isDateEmpty = true
    private var isFriendEmpty = true
    private var isPhotoEmpty = true
    private val args by navArgs<KeepInFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.keepInViewModel = keepInViewModel
        binding.lifecycleOwner = this@KeepInFragment
        initFragmentResultListener()
        if (args.detailPresent != null) {
            setModifyView()
        }
        setCloseBtnClickListener()
        setBtnSearchFriendClickListener()
        setBtnRegisterClickListener()
        setLayoutKeepInClickListener()
        setBtnGiveGetClickListener()
        setBtnGoGalleryClickEvent()
        setKeepInPhotoAdapter()
        setImageUrlListObserve()
        setBtnCategoryClickListener()
        setDateClickListener()
        setKeepInTitleObserve()
        setKeepInWhenObserve()
        setKeepInFriendObserve()
        setKeepInCategoryObserve()
        setIsUpdateObserve()
        setIsModifyCompleteObserve()
        return binding.root
    }

    private fun setModifyView() {
        setTvFriendModifyActive()
        with(keepInViewModel) {
            setKeepInTitleEditText(args.detailPresent?.data?.title!!)

            when (args.detailPresent?.data?.taken) {
                true -> {
                    binding.btnKeepInTypeGet.isChecked = true
                    setIsGet(true)
                }
                else -> {
                    binding.btnKeepInTypeGive.isChecked = true
                    setIsGet(false)
                }
            }

            setSelectedKeepInDate(
                args.detailPresent?.data?.date!!,
                args.detailPresent?.data?.date!!.replace(".", "-")
            )

            binding.layoutKeepInPhoto.visibility = View.GONE
            binding.icKeepInEssentialPhoto.visibility = View.INVISIBLE

            setKeepInMemoEditText(args.detailPresent?.data?.record!!)

            clearKeepInFriendText()
            args.detailPresent?.data?.friends?.joinToString(", ") { it.name }?.let {
                setKeepInFriendEditText(it)
            }
            args.detailPresent?.data?.friends?.forEach {
                initModifyFriendIdList(it.id)
            }

            args.detailPresent?.data?.category?.forEach {
                with(binding) {
                    when (it) {
                        "생일" -> btnKeepInCategoryBirth.setModifyCategory(getString(R.string.category_birth))
                        "기념일" -> btnKeepInCategoryAnniversary.setModifyCategory(getString(R.string.category_anniversary))
                        "축하" -> btnKeepInCategoryCeleb.setModifyCategory(getString(R.string.category_celeb))
                        "칭찬" -> btnKeepInCategoryCompliment.setModifyCategory(getString(R.string.category_compliment))
                        "응원" -> btnKeepInCategorySupport.setModifyCategory(getString(R.string.category_support))
                        "감사" -> btnKeepInCategoryThanks.setModifyCategory(getString(R.string.category_thanks))
                        "깜짝" -> btnKeepInCategorySurprise.setModifyCategory(getString(R.string.category_surprise))
                        "기타" -> btnKeepInCategoryEtc.setModifyCategory(getString(R.string.category_etc))
                    }
                }
            }
        }
    }

    private fun initFragmentResultListener() {
        setFragmentResultListener("friendSearch") { _, result ->
            keepInViewModel.setFriendSearchResult(
                result.getString("friendName"),
                result.getStringArrayList("friendIdList")
            )
        }
    }

    private fun setEmptyKeepInView() {
        with(keepInViewModel) {
            clearKeepInTitleText()
            clearKeepInFriendText()
            clearKeepInMemo()
            clearKeepInDate()
            clearCategory()
            clearImages()
            clearImageList()
        }
    }

    private fun setCloseBtnClickListener() {
        binding.btnKeepInClose.setOnClickListener {
            when (args.detailPresent) {
                null -> {
                    if (!isTitleEmpty || !isDateEmpty || !isFriendEmpty || !isPhotoEmpty) {
                        UtilDialog(STOP_WRITE) {
                            requireActivity().onBackPressed()
                            setEmptyKeepInView()
                        }.show(childFragmentManager, "stop_write_keep_in")
                    } else {
                        requireActivity().onBackPressed()
                    }
                }
                else -> {
                    setKeepInMemoModifyObserve()
                    setKeepInTitleModifyObserve()
                    with(keepInViewModel) {
                        if (isModify.value == true || isTitleModify.value == true || isMemoModify.value == true) {
                            UtilDialog(STOP_MODIFY) {
                                popBackStack()
                                setEmptyKeepInView()
                            }.show(childFragmentManager, "stop_modify_keep_in")
                        } else if (isModify.value == false && isTitleModify.value == false && isMemoModify.value == false) {
                            popBackStack()
                        }
                    }
                }
            }
            KeyBoardUtil.hide(requireActivity())
        }
    }

    private fun setBtnSearchFriendClickListener() {
        binding.etKeepInFriend.setOnClickListener {
            KeyBoardUtil.hide(requireActivity())
            keepInViewModel.setIsModify(true)
            when (args.detailPresent) {
                null -> navigate(R.id.action_keepInFragment_to_friendSearchFragment)
                else -> navigateWithData(
                    KeepInFragmentDirections.actionKeepInFragmentToFriendSearchFragment(
                        requireNotNull(args.detailPresent).data.friends.toTypedArray()
                    )
                )
            }
        }
    }

    private fun setBtnRegisterClickListener() {
        var isClickable = true
        binding.tvKeepInRegister.setOnClickListener {
            if (isClickable) {
                isClickable = false
                when (args.detailPresent) {
                    null -> {
                        keepInViewModel.postKeepIn()
                    }
                    else -> {
                        keepInViewModel.modifyKeepIn(args.detailPresent!!.data._id)
                    }
                }
                KeyBoardUtil.hide(requireActivity())
                it.postDelayed(
                    {
                        isClickable = true
                    },
                    3000
                )
            }
        }
    }

    private fun setLayoutKeepInClickListener() {
        binding.layoutKeepInContent.setOnClickListener {
            KeyBoardUtil.hide(requireActivity())
        }
        binding.layoutKeepIn.setOnClickListener {
            KeyBoardUtil.hide(requireActivity())
        }
    }

    private fun setBtnGiveGetClickListener() {
        with(binding) {
            if (args.detailPresent == null) {
                btnKeepInTypeGet.isChecked = true
            }
            btnKeepInTypeGive.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    keepInViewModel?.setIsGet(false)
                }
                btnKeepInTypeGet.isChecked = !btnKeepInTypeGive.isChecked
                if (args.detailPresent != null) {
                    if (isChecked == args.detailPresent!!.data.taken) {
                        keepInViewModel?.setIsModify(true)
                    } else {
                        keepInViewModel?.setIsModify(false)
                    }
                }
            }
            btnKeepInTypeGet.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    keepInViewModel?.setIsGet(true)
                }
                btnKeepInTypeGive.isChecked = !btnKeepInTypeGet.isChecked
                if (args.detailPresent != null) {
                    if (isChecked != args.detailPresent!!.data.taken) {
                        keepInViewModel?.setIsModify(true)
                    } else {
                        keepInViewModel?.setIsModify(false)
                    }
                }
            }
        }
    }

    private fun setKeepInPhotoAdapter() {
        binding.rvKeepInPhoto.adapter = KeepInAdapter(keepInViewModel)
    }

    private fun setImageUrlListObserve() {
        keepInViewModel.imgList.observe(viewLifecycleOwner) { imgList ->
            imgList?.let {
                with(binding.rvKeepInPhoto.adapter as KeepInAdapter) {
                    submitList(imgList)
                }
                if (imgList.size != 0) {
                    isPhotoEmpty = false
                    checkTvKeepInRegisterActive()
                } else {
                    isPhotoEmpty = true
                    setTvFriendRegisterUnActive()
                }
            }
        }
    }

    private fun setBtnGoGalleryClickEvent() {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    intent.type = "image/*"
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    getContent.launch(intent)
                }
            }

        binding.btnKeepInNewPhoto.setOnClickListener {
            requestPermissionLauncher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private val getContent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result: ActivityResult ->
        if (result.data?.clipData != null) {
            val count = result.data?.clipData!!.itemCount
            for (i in 0 until count) {
                if (keepInViewModel.tempImgList.size < 3) {
                    val imageUri = result.data?.clipData!!.getItemAt(i).uri
                    keepInViewModel.addImageList(imageUri)
                    setImage()
                } else {
                    requireContext().toastMessageUtil(getString(R.string.keep_in_max_photo_toast))
                    break
                }
            }
        } else {
            if (result.data != null) {
                val imageUri = result.data?.data
                keepInViewModel.addImageList(requireNotNull(imageUri))
                setImage()
            }
        }
    }

    private fun setImage() {
        val index = requireNotNull(keepInViewModel.imgList.value).size - 1
        val imageURI = requireNotNull(keepInViewModel.imgList.value)[index]
        keepInViewModel.images.add(multiPartResolver.createImgMultiPart(imageURI))
    }

    private fun setDateClickListener() {
        binding.etKeepInWhen.setOnClickListener {
            DatePickerDialog { date, networkDate ->
                keepInViewModel.setSelectedKeepInDate(date, networkDate)
                keepInViewModel.setIsModify(true)
            }.show(childFragmentManager, "keep_in")
        }
    }

    private fun setBtnCategoryClickListener() {
        with(binding) {
            btnKeepInCategoryBirth.setCheckBoxClickListener(getString(R.string.category_birth))
            btnKeepInCategoryAnniversary.setCheckBoxClickListener(getString(R.string.category_anniversary))
            btnKeepInCategoryCeleb.setCheckBoxClickListener(getString(R.string.category_celeb))
            btnKeepInCategoryCompliment.setCheckBoxClickListener(getString(R.string.category_compliment))
            btnKeepInCategorySupport.setCheckBoxClickListener(getString(R.string.category_support))
            btnKeepInCategoryThanks.setCheckBoxClickListener(getString(R.string.category_thanks))
            btnKeepInCategorySurprise.setCheckBoxClickListener(getString(R.string.category_surprise))
            btnKeepInCategoryEtc.setCheckBoxClickListener(getString(R.string.category_etc))
        }
    }

    private fun setKeepInCategoryObserve() {
        keepInViewModel.selectedCategory.observe(viewLifecycleOwner) { keepInCategory ->
            with(binding) {
                when (keepInCategory.size) {
                    2 -> {
                        btnKeepInCategoryBirth.setCategoryClickUnable()
                        btnKeepInCategoryAnniversary.setCategoryClickUnable()
                        btnKeepInCategoryCeleb.setCategoryClickUnable()
                        btnKeepInCategoryCompliment.setCategoryClickUnable()
                        btnKeepInCategorySupport.setCategoryClickUnable()
                        btnKeepInCategoryThanks.setCategoryClickUnable()
                        btnKeepInCategorySurprise.setCategoryClickUnable()
                        btnKeepInCategoryEtc.setCategoryClickUnable()
                    }
                    else -> {
                        btnKeepInCategoryBirth.isClickable = true
                        btnKeepInCategoryAnniversary.isClickable = true
                        btnKeepInCategoryCeleb.isClickable = true
                        btnKeepInCategoryCompliment.isClickable = true
                        btnKeepInCategorySupport.isClickable = true
                        btnKeepInCategoryThanks.isClickable = true
                        btnKeepInCategorySurprise.isClickable = true
                        btnKeepInCategoryEtc.isClickable = true
                    }
                }
            }
        }
    }

    private fun CheckBox.setCheckBoxClickListener(categoryString: String) {
        this.setOnClickListener {
            if (this.isChecked) {
                keepInViewModel.addSelectedCategory(categoryString)
            } else {
                keepInViewModel.removeCategory(categoryString)
            }
            keepInViewModel.setIsModify(true)
        }
    }

    private fun CheckBox.setCategoryClickUnable() {
        if (!this.isChecked) {
            this.isClickable = false
        }
    }

    private fun CheckBox.setModifyCategory(categoryString: String) {
        this.isChecked = true
        keepInViewModel.addSelectedCategory(categoryString)
    }

    private fun setKeepInTitleObserve() {
        keepInViewModel.keepInTitle.observe(viewLifecycleOwner) { keepInTitle ->
            when (keepInTitle.length) {
                0 -> {
                    isTitleEmpty = true
                    setEtKeepInTitleIsEmptyBackground()
                    setTvFriendRegisterUnActive()
                }
                else -> {
                    isTitleEmpty = false
                    setEtKeepInTitleIsNotEmptyBackground()
                    checkTvKeepInRegisterActive()
                }
            }
        }
    }

    private fun setKeepInTitleModifyObserve() {
        keepInViewModel.keepInTitle.observe(viewLifecycleOwner) { keepInTitle ->
            when (requireNotNull(args.detailPresent).data.title != keepInTitle) {
                true -> keepInViewModel.setIsTitleModify(true)
                false -> {
                    keepInViewModel.setIsTitleModify(false)
                }
            }
        }
    }

    private fun setKeepInWhenObserve() {
        keepInViewModel.selectedKeepInData.observe(viewLifecycleOwner) { keepInWhen ->
            when (keepInWhen.length) {
                0 -> {
                    isDateEmpty = true
                    setEtKeepInWhenIsEmptyBackground()
                    setTvFriendRegisterUnActive()
                }
                else -> {
                    isDateEmpty = false
                    setEtKeepInWhenIsNotEmptyBackground()
                    checkTvKeepInRegisterActive()
                }
            }
        }
    }

    private fun setKeepInFriendObserve() {
        keepInViewModel.keepInFriendName.observe(viewLifecycleOwner) { friendName ->
            when (friendName.length) {
                0 -> {
                    isFriendEmpty = true
                    binding.icFriendSearch.setImageResource(R.drawable.ic_search_gray)
                    setTvFriendRegisterUnActive()
                }
                else -> {
                    isFriendEmpty = false
                    binding.icFriendSearch.setImageResource(R.drawable.ic_search_black)
                    checkTvKeepInRegisterActive()
                }
            }
        }
    }

    private fun setKeepInMemoModifyObserve() {
        keepInViewModel.keepInMemo.observe(viewLifecycleOwner) { memo ->
            if (requireNotNull(args.detailPresent).data.record != memo) {
                keepInViewModel.setIsMemoModify(true)
            } else {
                keepInViewModel.setIsMemoModify(false)
            }
        }
    }

    private fun setIsUpdateObserve() {
        with(keepInViewModel) {
            isKeepInRegister.observe(viewLifecycleOwner) { isKeepInRegister ->
                if (isKeepInRegister) {
                    navigate(R.id.action_keepInFragment_to_keepInCompleteFragment)
                    setEmptyKeepInView()
                    resetIsKeepInRegister()
                }
            }
        }
    }

    private fun setIsModifyCompleteObserve() {
        with(keepInViewModel) {
            isKeepInModifyComplete.observe(viewLifecycleOwner) { isModifyComplete ->
                if (isModifyComplete) {
                    popBackStack()
                    setKeepInTitleModifyObserve()
                    setKeepInMemoModifyObserve()
                    if (isModify.value == true || isTitleModify.value == true || isMemoModify.value == true) {
                        requireContext().toastMessageUtil(getString(R.string.keep_in_modify_toast))
                    }
                    setEmptyKeepInView()
                    resetIsKeepInModify()
                }
            }
        }
    }

    private fun checkTvKeepInRegisterActive() {
        when (args.detailPresent) {
            null -> {
                if (!isTitleEmpty && !isDateEmpty && !isFriendEmpty && !isPhotoEmpty) {
                    setTvFriendRegisterActive()
                } else {
                    setTvFriendRegisterUnActive()
                }
            }
            else -> {
                if (!isTitleEmpty && !isDateEmpty && !isFriendEmpty) {
                    setTvFriendRegisterActive()
                } else {
                    setTvFriendRegisterUnActive()
                }
            }
        }
    }

    private fun setEtKeepInWhenIsEmptyBackground() {
        binding.etKeepInWhen.setBackgroundResource(R.drawable.border_gray_line_under)
    }

    private fun setEtKeepInWhenIsNotEmptyBackground() {
        binding.etKeepInWhen.setBackgroundResource(R.drawable.border_black_line_under)
    }

    private fun setEtKeepInTitleIsEmptyBackground() {
        binding.etKeepInTitle.setBackgroundResource(R.drawable.border_gray_line_under)
    }

    private fun setEtKeepInTitleIsNotEmptyBackground() {
        binding.etKeepInTitle.setBackgroundResource(R.drawable.border_black_line_under)
    }

    private fun setTvFriendRegisterActive() {
        binding.tvKeepInRegister.setTextColor(requireContext().getColor(R.color.green_15bd6f))
        binding.tvKeepInRegister.isClickable = true
    }

    private fun setTvFriendRegisterUnActive() {
        binding.tvKeepInRegister.setTextColor(requireContext().getColor(R.color.gray_cccccc))
        binding.tvKeepInRegister.isClickable = false
    }

    private fun setTvFriendModifyActive() {
        binding.tvKeepInRegister.setText(R.string.keep_in_modify_complete)
    }
}
