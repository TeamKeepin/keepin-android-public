package com.keepin.android.presentation.frienddetail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentFriendDetailBinding
import com.keepin.android.util.KeyBoardUtil
import com.keepin.android.util.UtilDialog
import com.keepin.android.util.navigateWithData
import com.keepin.android.util.popBackStack
import com.keepin.android.util.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FriendDetailFragment :
    BindingFragment<FragmentFriendDetailBinding>(R.layout.fragment_friend_detail),
    PopupMenu.OnMenuItemClickListener {
    private val friendDetailViewModel by viewModels<FriendDetailViewModel>()
    private val args by navArgs<FriendDetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = friendDetailViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        friendDetailViewModel.initFriendId(args.friendId)
        setBtnBackClickListener()
        setLayoutClickListener()
        setBtnOptionVisibility()
        setBtnFriendDetailOptionClickListener()
        setEtFriendMemoMaxLines()
        setMemoClickListener()
        setMemoCollect()
        initRvFriendDetailKeepIn()
        setIsDeletedSuccessCollect()
    }

    private fun setBtnBackClickListener() {
        binding.btnFriendDetailBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun setLayoutClickListener() {
        binding.layoutFriendDetail.setOnClickListener {
            KeyBoardUtil.hide(requireActivity())
        }
    }

    private fun setBtnOptionVisibility() {
        binding.previousIsFriendList = args.previousIsFriendList
    }

    private fun setBtnFriendDetailOptionClickListener() {
        binding.btnFriendDetailOption.setOnClickListener {
            val wrapper = ContextThemeWrapper(requireContext(), R.style.friend_option_popup_menu)
            val menu = PopupMenu(wrapper, it).apply {
                setOnMenuItemClickListener(this@FriendDetailFragment)
                inflate(R.menu.menu_friend_detail_option)
            }
            menu.show()
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_friend_detail_update -> {
                navigateWithData(
                    FriendDetailFragmentDirections.actionFriendDetailFragmentToFriendUpdateFragment(
                        friendId = args.friendId,
                        friendName = binding.tvFriendDetailNickname.text.toString()
                    )
                )
                true
            }
            R.id.menu_friend_detail_delete -> {
                UtilDialog(4) {
                    friendDetailViewModel.deleteFriend()
                }.show(childFragmentManager, "DELETE_FRIEND")
                true
            }
            else -> false
        }
    }

    private fun setEtFriendMemoMaxLines() {
        with(binding.etFriendDetailMemo) {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                    setTvFriendMemoLinesText()
                }

                override fun afterTextChanged(p0: Editable) {
                    if (lineCount >= 6) {
                        text.delete(text.length - 1, text.length)
                    }
                }
            })
        }
    }

    private fun setTvFriendMemoLinesText() {
        binding.etFriendDetailMemo.post {
            with(binding) {
                tvFriendDetailMemoLine.text = String.format(
                    getString(R.string.friend_detail_memo_line),
                    when (etFriendDetailMemo.text.length) {
                        0 -> 0
                        else -> etFriendDetailMemo.lineCount
                    }
                )
            }
        }
    }

    private fun setMemoClickListener() {
        binding.btnFriendDetailPen.setOnClickListener {
            friendDetailViewModel.setIsWriting(FriendDetailViewModel.Memo.MEMO)
        }
        binding.btnFriendDetailMemo.setOnClickListener {
            val memo = binding.etFriendDetailMemo.text.toString()
            friendDetailViewModel.putFriendMemo(memo)
        }
    }

    private fun setMemoCollect() {
        repeatOnLifecycle {
            friendDetailViewModel.isWritingMemo.collect { isWriting ->
                with(binding.etFriendDetailMemo) {
                    isEnabled = isWriting.enabled
                    setSelection(text.length)
                    when (isWriting.enabled) {
                        true -> KeyBoardUtil.show(requireContext(), this)
                        false -> KeyBoardUtil.hide(requireActivity())
                    }
                }
            }
        }
    }

    private fun initRvFriendDetailKeepIn() {
        val adapter = FriendDetailAdapter()
        binding.rvFriendDetailKeepin.adapter = adapter
        binding.rvFriendDetailKeepin.setHasFixedSize(true)
        setFriendKeepInListCollect(adapter)
    }

    private fun setFriendKeepInListCollect(adapter: FriendDetailAdapter) {
        repeatOnLifecycle {
            friendDetailViewModel.keepInList.collect { keepInList ->
                keepInList?.let {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun setIsDeletedSuccessCollect() {
        repeatOnLifecycle {
            friendDetailViewModel.isDeleted.collect { isDeleted ->
                if (isDeleted) {
                    popBackStack()
                }
            }
        }
    }
}
