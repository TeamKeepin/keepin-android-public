package com.keepin.android.presentation.frienddetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.entity.response.FriendDetail
import com.keepin.android.data.entity.response.KeepInData
import com.keepin.android.data.repository.FriendDetailRepository
import com.keepin.android.util.KeepInListSelector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendDetailViewModel @Inject constructor(
    private val repository: FriendDetailRepository
) : ViewModel() {
    private val friendId = MutableStateFlow<String?>(null)

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    private val keepInListMap = HashMap<KeepInListSelector, List<KeepInData>?>()

    private val _giveOrGet = MutableStateFlow<KeepInListSelector?>(null)
    val giveOrGet = _giveOrGet.asStateFlow()

    private val _isWritingMemo = MutableStateFlow(Memo.PEN)
    val isWritingMemo = _isWritingMemo.asStateFlow()

    private val _isDeleted = MutableSharedFlow<Boolean>()
    val isDeleted = _isDeleted.asSharedFlow()

    fun initFriendId(id: String) {
        friendId.value = id
    }

    @ExperimentalCoroutinesApi
    val friendDetail = friendId.filterNotNull().transform { id ->
        initKeepInListMap(id)
        setListSelectorKey(KeepInListSelector.TAKEN)
        emit(initFriendDetail(id))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private suspend fun initKeepInListMap(id: String) {
        keepInListMap[KeepInListSelector.TAKEN] = repository.getFriendKeepIn(id, true).first()
        keepInListMap[KeepInListSelector.GIVEN] = repository.getFriendKeepIn(id, false).first()
    }

    private suspend fun initFriendDetail(id: String): FriendDetail? {
        repository.getFriendDetail(id).first()?.let { friendDetail ->
            _loading.value = false
            return friendDetail
        }
        return null
    }

    val keepInList = giveOrGet.transform { value ->
        emit(keepInListMap[value])
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun setListSelectorKey(value: KeepInListSelector) {
        _giveOrGet.value = value
    }

    fun putFriendMemo(memo: String) {
        viewModelScope.launch {
            friendId.value?.let { id ->
                val result = repository.putFriendMemo(memo, id).first()
                _isWritingMemo.value = if (result) Memo.PEN else Memo.MEMO
            }
        }
    }

    fun setIsWriting(value: Memo) {
        _isWritingMemo.value = value
    }

    fun deleteFriend() {
        viewModelScope.launch {
            friendId.value?.let { id ->
                val result = repository.deleteFriend(id).first()
                _isDeleted.emit(result)
            }
        }
    }

    enum class Memo(val enabled: Boolean) {
        PEN(false),
        MEMO(true)
    }
}
