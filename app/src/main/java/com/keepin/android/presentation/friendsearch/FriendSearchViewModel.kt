package com.keepin.android.presentation.friendsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.entity.response.Friend
import com.keepin.android.data.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendSearchViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
) : ViewModel() {
    val query = MutableLiveData("")

    private var initFriendList: List<Friend> = listOf()

    private val _friendSearchedList = MutableLiveData<MutableList<Friend>>()
    val friendSearchedList: LiveData<MutableList<Friend>> = _friendSearchedList

    private val _friendSelectedList = MutableLiveData<MutableList<Friend>>(mutableListOf())
    val friendSelectedList: LiveData<MutableList<Friend>> = _friendSelectedList

    fun getFriendsList() {
        viewModelScope.launch {
            myPageRepository.getFriendsList().collect { friendsList ->
                friendsList?.let {
                    initFriendList = friendsList
                    setSearchedList()
                }
            }
        }
    }

    fun setSelectedList(friendsList: List<Friend>) {
        _friendSelectedList.value = friendsList.toMutableList()
    }

    fun setSearchedList() {
        _friendSearchedList.value = initFriendList.filter { friend ->
            (friend.name.contains(requireNotNull(query.value))) && (!requireNotNull(_friendSelectedList.value).contains(friend))
        }.toMutableList()
    }

    fun selectFriend(position: Int) {
        if (requireNotNull(_friendSelectedList.value).size < 5) {
            _friendSelectedList.value =
                requireNotNull(_friendSelectedList.value).toMutableList().apply {
                    add(0, requireNotNull(_friendSearchedList.value)[position])
                }
            _friendSearchedList.value = removeFriend(_friendSearchedList.value, position)
        }
    }

    fun unSelectFriend(position: Int) {
        _friendSearchedList.value =
            requireNotNull(friendSearchedList.value).toMutableList().apply {
                add(requireNotNull(_friendSelectedList.value)[position])
            }
        _friendSelectedList.value = removeFriend(_friendSelectedList.value, position)
    }

    private fun removeFriend(list: MutableList<Friend>?, position: Int) =
        requireNotNull(list).filterIndexed { index, _ ->
            (index != position)
        }.toMutableList()

    fun getSelectedFriendName() =
        requireNotNull(_friendSelectedList.value).reversed().joinToString(", ") { friend -> friend.name }

    fun getSelectedFriendIdList() = mutableListOf<String>().apply {
        requireNotNull(_friendSelectedList.value).forEach { friend ->
            add(friend.id)
        }
    }
}
