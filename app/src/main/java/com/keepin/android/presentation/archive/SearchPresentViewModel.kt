package com.keepin.android.presentation.archive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.entity.response.KeepInData
import com.keepin.android.data.repository.ArchiveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SearchPresentViewModel @Inject constructor(private val archiveRepository: ArchiveRepository) :
    ViewModel() {
    private val _presentList = MutableLiveData<List<KeepInData>>()
    val presentList: LiveData<List<KeepInData>> = _presentList

    private val searchResultList = ArrayList<KeepInData>()

    fun requestPresentSearchList(keyword: String) {
        searchResultList.clear()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = archiveRepository.getSearchKeyword(keyword)
                _presentList.postValue(response?.data?.keepIns)
            } catch (e: HttpException) {
            }
        }
    }
}
