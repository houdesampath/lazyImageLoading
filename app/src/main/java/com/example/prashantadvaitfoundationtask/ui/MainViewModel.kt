package com.example.prashantadvaitfoundationtask.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.prashantadvaitfoundationtask.data.model.MainResponse
import com.example.prashantadvaitfoundationtask.data.repo.MainRepo
import com.example.prashantadvaitfoundationtask.utils.ImagePagingSource
import com.example.prashantadvaitfoundationtask.utils.Status
import com.example.prashantadvaitfoundationtask.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepo
) : ViewModel() {

    val mainState = MutableStateFlow(
        ViewState(
            Status.LOADING,
            emptyList<MainResponse>(), ""
        )
    )

    init {
        fetchMainData()
    }

    fun fetchMainData() {
        mainState.value = ViewState.loading()
        viewModelScope.launch {

            repository.fetchMainData()
                .catch {
                    mainState.value =
                        ViewState.error(it.message.toString())
                }
                .collect {
                    mainState.value = ViewState.success(it.data)
                }
        }
    }

    private val _imageUrlList = MutableStateFlow<List<String>>(emptyList())// Initialize with your image URLs
    val imageUrlList: StateFlow<List<String>> = _imageUrlList

    val pager = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { ImagePagingSource(imageUrlList.value) }
    ).flow.cachedIn(viewModelScope)

    fun updateImageUrlList(newImageUrlList: List<String>) {
        _imageUrlList.value = newImageUrlList
    }


}