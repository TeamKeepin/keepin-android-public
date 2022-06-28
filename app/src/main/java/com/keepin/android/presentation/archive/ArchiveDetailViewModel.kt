package com.keepin.android.presentation.archive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepin.android.data.entity.request.RequestDeletePresent
import com.keepin.android.data.entity.response.ResponseDetailPresent
import com.keepin.android.data.repository.ArchiveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ArchiveDetailViewModel @Inject constructor(
    private val repository: ArchiveRepository
) : ViewModel() {
    private val _presentDetail = MutableLiveData<ResponseDetailPresent>()
    val presentDetail: LiveData<ResponseDetailPresent> = _presentDetail

    private val _isUpdate = MutableLiveData<Boolean>()
    val isUpdate: LiveData<Boolean> = _isUpdate

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    private val _taken = MutableLiveData<Boolean>()
    val taken: LiveData<Boolean> = _taken

    fun resetIsUpdate() {
        _isUpdate.value = false
    }

    fun requestPresentDetail(keepinIdx: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getArchiveDetail(keepinIdx)
                _presentDetail.postValue(response!!)
                Timber.d(response.toString())
                _taken.postValue(response.data.taken)
                _loading.postValue(false)
            } catch (e: HttpException) {
            }
        }
    }

    fun deletePresent(keepinIdx: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteArchive(RequestDeletePresent(listOf(keepinIdx)))
                _isUpdate.postValue(true)
            } catch (e: HttpException) {
            }
        }
    }
}
