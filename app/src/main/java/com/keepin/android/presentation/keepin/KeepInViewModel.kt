package com.keepin.android.presentation.keepin

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.repository.KeepInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class KeepInViewModel @Inject constructor(
    private val keepInRepository: KeepInRepository
) : ViewModel() {
    val keepInTitle = MutableLiveData("")
    val keepInMemo = MutableLiveData("")

    var images = ArrayList<MultipartBody.Part>()

    private val map = mutableMapOf<String, @JvmSuppressWildcards RequestBody>()

    private val _isModify = MutableLiveData(false)
    val isModify: LiveData<Boolean> = _isModify

    private val _isTitleModify = MutableLiveData(false)
    val isTitleModify: LiveData<Boolean> = _isTitleModify

    private val _isMemoModify = MutableLiveData(false)
    val isMemoModify: LiveData<Boolean> = _isMemoModify

    private val _isKeepInRegister = MutableLiveData<Boolean>()
    val isKeepInRegister: LiveData<Boolean> = _isKeepInRegister

    private val _isKeepInModifyComplete = MutableLiveData<Boolean>()
    val isKeepInModifyComplete: LiveData<Boolean> = _isKeepInModifyComplete

    private val _isGet = MutableLiveData(true)
    private val isGet: LiveData<Boolean> = _isGet

    private val _selectedCategory = MutableLiveData<MutableList<String>>()
    val selectedCategory: LiveData<MutableList<String>> = _selectedCategory

    private val tempCategory = mutableListOf<String>()

    private val _imgList = MutableLiveData<MutableList<Uri>>()
    val imgList: LiveData<MutableList<Uri>> = _imgList

    val tempImgList = mutableListOf<Uri>()

    private val _selectedKeepInDate = MutableLiveData("")
    val selectedKeepInData: LiveData<String> = _selectedKeepInDate

    private val _keepInFriendName = MutableLiveData<String>()
    val keepInFriendName: LiveData<String> = _keepInFriendName

    private var selectedFriendIdList = mutableListOf<String>()

    private var networkDate = ""

    fun setIsModify(isModify: Boolean) {
        _isModify.value = isModify
    }

    fun setIsTitleModify(isTitleModify: Boolean) {
        _isTitleModify.value = isTitleModify
    }

    fun setIsMemoModify(isMemoModify: Boolean) {
        _isMemoModify.value = isMemoModify
    }

    fun setKeepInTitleEditText(title: String) {
        keepInTitle.value = title
    }

    fun initModifyFriendIdList(friendName: String) {
        selectedFriendIdList.add(friendName)
    }

    fun setKeepInMemoEditText(memo: String) {
        keepInMemo.value = memo
    }

    fun setKeepInFriendEditText(friendName: String) {
        _keepInFriendName.value += friendName
    }

    fun setIsGet(isGetClick: Boolean) {
        _isGet.value = isGetClick
    }

    fun addSelectedCategory(category: String) {
        tempCategory.add(category)
        _selectedCategory.value = tempCategory.toMutableList()
    }

    fun addImageList(keepInData: Uri) {
        tempImgList.add(keepInData)
        _imgList.value = tempImgList.toMutableList()
    }

    fun removeCategory(category: String) {
        tempCategory.remove(category)
        _selectedCategory.value = tempCategory.toMutableList()
    }

    fun removeImageList(keepInData: Uri) {
        tempImgList.remove(keepInData)
        _imgList.value = tempImgList.toMutableList()
    }

    fun removeImageMultipartList(position: Int) {
        images.removeAt(position)
    }

    fun setSelectedKeepInDate(keepInDate: String, networkDate: String) {
        _selectedKeepInDate.value = keepInDate
        this.networkDate = networkDate
    }

    fun resetIsKeepInRegister() {
        _isKeepInRegister.value = false
    }

    fun resetIsKeepInModify() {
        _isKeepInModifyComplete.value = false
    }

    fun clearCategory() {
        tempCategory.clear()
        _selectedCategory.value = tempCategory.toMutableList()
    }

    fun clearKeepInTitleText() {
        keepInTitle.value = ""
    }

    fun clearKeepInFriendText() {
        _keepInFriendName.value = ""
    }

    fun clearKeepInMemo() {
        keepInMemo.value = ""
    }

    fun clearImages() {
        images.clear()
    }

    fun clearKeepInDate() {
        _selectedKeepInDate.value = ""
    }

    fun clearImageList() {
        tempImgList.clear()
        _imgList.value = mutableListOf()
    }

    fun setFriendSearchResult(friendName: String?, friendIdList: MutableList<String>?) {
        friendName?.let { name ->
            _keepInFriendName.value = name
        }
        friendIdList?.let {
            selectedFriendIdList = friendIdList
        }
    }

    fun setMultiPart() {
        map["title"] =
            RequestBody.create(MediaType.parse("text/plain"), keepInTitle.value.toString())
        map["date"] = RequestBody.create(MediaType.parse("text/plain"), networkDate)
        map["record"] =
            RequestBody.create(MediaType.parse("text/plain"), keepInMemo.value.toString())
        map["taken"] = RequestBody.create(
            MediaType.parse("text/plain"),
            requireNotNull(isGet.value).toString()
        )
        for ((index, friendIdx) in selectedFriendIdList.withIndex()) {
            map["friendIdx[$index]"] =
                RequestBody.create(MediaType.parse("text/plain"), friendIdx)
        }
        if (!selectedCategory.value.isNullOrEmpty()) {
            for ((index, category) in requireNotNull(selectedCategory.value).withIndex()) {
                map["category[$index]"] =
                    RequestBody.create(MediaType.parse("text/plain"), category)
            }
        }
    }

    fun postKeepIn() {
        viewModelScope.launch {
            setMultiPart()
            keepInRepository.postKeepIn(
                map,
                images
            )
            _isKeepInRegister.postValue(true)
        }
    }

    fun modifyKeepIn(keepInIdx: String) {
        if (keepInIdx != "") {
            viewModelScope.launch {
                setMultiPart()
                keepInRepository.modifyKeepIn(map, keepInIdx)
                _isKeepInModifyComplete.postValue(true)
            }
        }
    }
}
