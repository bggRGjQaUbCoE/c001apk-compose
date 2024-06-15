package com.example.c001apk.compose.ui.coolpic

import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.ui.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * Created by bggRGjQaUbCoE on 2024/6/12
 */
@HiltViewModel(assistedFactory = CoolPicContentViewModel.ViewModelFactory::class)
class CoolPicContentViewModel @AssistedInject constructor(
    @Assisted("title") val title: String,
    @Assisted("type") val type: String,
    private val networkRepo: NetworkRepo
) : BaseViewModel(networkRepo) {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(
            @Assisted("title") title: String,
            @Assisted("type") type: String,
        ): CoolPicContentViewModel
    }

    init {
        fetchData()
    }

    override suspend fun customFetchData() = networkRepo.getCoolPic(title, type, page, lastItem)

}