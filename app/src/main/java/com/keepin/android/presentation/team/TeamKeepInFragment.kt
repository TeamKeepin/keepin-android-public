package com.keepin.android.presentation.team

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentTeamKeepInBinding
import com.keepin.android.util.popBackStack

class TeamKeepInFragment :
    BindingFragment<FragmentTeamKeepInBinding>(R.layout.fragment_team_keep_in) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setBtnBackClickListener()
        setBtnTeamKeepInClickListener()
        return binding.root
    }

    private fun setBtnBackClickListener() {
        binding.btnTeamKeepInBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun setBtnTeamKeepInClickListener() {
        binding.btnTeamKeepIn.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.link_service_link_tree))
                )
            )
        }
    }
}
