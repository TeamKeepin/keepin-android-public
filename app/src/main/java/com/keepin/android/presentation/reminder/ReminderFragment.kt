package com.keepin.android.presentation.reminder

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.keepin.android.R
import com.keepin.android.base.BindingFragment
import com.keepin.android.databinding.FragmentReminderBinding
import com.keepin.android.util.UtilDialog
import com.keepin.android.util.UtilDialog.Companion.BEFORE_DELETE_POST
import com.keepin.android.util.navigateWithData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReminderFragment : BindingFragment<FragmentReminderBinding>(R.layout.fragment_reminder) {
    private val reminderViewModel by viewModels<ReminderViewModel>()
    private var enableTabMediator: TabLayoutMediator? = null
    private var disableTabMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = reminderViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        reminderViewModel.initReminderMonth()
        setReminderVpAdapter()
        setReminderVpScrollListener()
        setSelectedYearObserve()
        setReminderListObserve()
        setReminderMonthObserve()
        setIsEditObserve()
        setAddBtnClickListener()
        setDeleteTvClickListener()
        return binding.root
    }

    private fun setReminderVpAdapter() {
        with(binding.vpReminderItem) {
            adapter = ReminderContainerAdapter(reminderViewModel)
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun setReminderVpScrollListener() {
        binding.vpReminderItem.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    reminderViewModel.setSelectedMonth(position + 1)
                }
            }
        )
    }

    private fun setReminderListObserve() {
        reminderViewModel.reminderList.observe(viewLifecycleOwner) { list ->
            with(binding.vpReminderItem.adapter as ReminderContainerAdapter) {
                setReminderOfYear(list)
                setVpReminderCurrentItem()
            }
        }
    }

    private fun setReminderMonthObserve() {
        reminderViewModel.reminderMonth.observe(viewLifecycleOwner) { reminderMonth ->
            if (reminderMonth != null) {
                notifyVpDataSetChanged()
            }
        }
    }

    private fun notifyVpDataSetChanged() {
        with(binding.vpReminderItem.adapter as ReminderContainerAdapter) {
            notifyItemChanged(requireNotNull(reminderViewModel.selectedMonth.value).toInt() - 1)
        }
    }

    private fun setVpReminderCurrentItem() {
        binding.vpReminderItem.currentItem =
            requireNotNull(reminderViewModel.selectedMonth.value).toInt() - 1
    }

    private fun setSelectedYearObserve() {
        reminderViewModel.selectedYear.observe(viewLifecycleOwner) {
            reminderViewModel.getReminder()
            binding.vpReminderItem.currentItem = reminderViewModel.currentMonth - 1
        }
    }

    private fun setIsEditObserve() {
        reminderViewModel.isEdit.observe(viewLifecycleOwner) { isEdit ->
            setVpUserInputEnabled(isEdit)
            setTabLayoutTouchListener(isEdit)
            notifyVpDataSetChanged()
        }
    }

    private fun setVpUserInputEnabled(isEnabled: Boolean) {
        binding.vpReminderItem.isUserInputEnabled = !isEnabled
    }

    private fun setEnableTabMediator() {
        disableTabMediator?.detach()
        enableTabMediator =
            TabLayoutMediator(binding.tabReminderMonth, binding.vpReminderItem) { tab, position ->
                tab.text = "${position + 1}월"
                tab.view.isClickable = true
            }.apply { attach() }
    }

    private fun setDisableTabMediator() {
        enableTabMediator?.detach()
        disableTabMediator =
            TabLayoutMediator(binding.tabReminderMonth, binding.vpReminderItem) { tab, position ->
                tab.text = "${position + 1}월"
                tab.view.isClickable = false
            }.apply { attach() }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTabLayoutTouchListener(isEdit: Boolean) {
        binding.tabReminderMonth.setOnTouchListener { _, _ -> isEdit }
        if (isEdit) {
            setDisableTabMediator()
        } else {
            setEnableTabMediator()
        }
    }

    private fun setAddBtnClickListener() {
        binding.btnReminderAdd.setOnClickListener {
            reminderViewModel.setIsGoReminderUpdate(true)
            navigateWithData(ReminderFragmentDirections.actionReminderFragmentToReminderUpdateFragment())
        }
    }

    private fun setDeleteTvClickListener() {
        binding.tvReminderDelete.setOnClickListener {
            UtilDialog(BEFORE_DELETE_POST) {
                reminderViewModel.deleteReminder()
                reminderViewModel.setIsEdit(false)
            }.show(
                childFragmentManager,
                "delete_reminder"
            )
        }
    }
}
