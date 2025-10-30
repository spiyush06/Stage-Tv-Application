package com.stage.tv.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stage.tv.api.Response
import com.stage.tv.api.TmdbRepo
import com.stage.tv.model.CastResponse
import com.stage.tv.model.DetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class DetailViewmodel(val repo: TmdbRepo, id: Int) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getMovieDetails(id)
            repo.getMovieCast(id)
        }
    }

    val movieDetails: LiveData<Response<DetailResponse>>
        get() = repo.movieDetail

    val castDetails: LiveData<Response<CastResponse>>
        get() = repo.castDetail
}