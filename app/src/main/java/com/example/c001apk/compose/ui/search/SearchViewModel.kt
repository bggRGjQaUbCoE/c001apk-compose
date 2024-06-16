package com.example.c001apk.compose.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.logic.model.StringEntity
import com.example.c001apk.compose.logic.repository.SearchHistoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by bggRGjQaUbCoE on 2024/6/16
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchHistoryRepo: SearchHistoryRepo
) : ViewModel() {

    val searchHistory: Flow<List<StringEntity>> = searchHistoryRepo.loadAllListFlow()

    fun saveHistory(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (searchHistoryRepo.isExist(keyword)) {
                searchHistoryRepo.updateHistory(keyword)
            } else {
                searchHistoryRepo.saveHistory(keyword)
            }
        }
    }

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            searchHistoryRepo.deleteAllHistory()
        }
    }

    fun delete(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchHistoryRepo.deleteHistory(keyword)
        }
    }

}