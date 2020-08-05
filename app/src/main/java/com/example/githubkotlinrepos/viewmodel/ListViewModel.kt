package com.example.githubkotlinrepos.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubkotlinrepos.model.GitHubRepo
import com.example.githubkotlinrepos.model.GitHubService
import kotlinx.coroutines.*

class ListViewModel : ViewModel() {

//    private val githubService = GitHubService.getRepoService()

    private var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception: ${throwable.printStackTrace()}")
    }

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    private val _repos = MutableLiveData<List<GitHubRepo>>()
    val repos: LiveData<List<GitHubRepo>>
        get() = _repos

    private val _repoLoadError = MutableLiveData<String?>()
    val repoLoadError: LiveData<String?>
        get() = _repoLoadError

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading


//    fun callRepos() {
//        fetchRepos(_token.value.toString())
//    }

    fun getToken(clientId: String, clientSecret: String, code: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val responseToken = GitHubService
                .getUnauthorizedApi().getAuthToken(clientId, clientSecret, code)

            withContext(Dispatchers.Main) {
                if (responseToken.isSuccessful) {
                    _token.value = responseToken.body().toString()
                    fetchRepos(_token.value.toString())
                } else {
                    onError("Error: ${responseToken.message()}")
                }
            }
        }
    }

    private fun fetchRepos(token: String) {
        _loading.value = true

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = GitHubService
                .getAuthorizedApi(token).getReposApi()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _repos.value = response.body()
                    _repoLoadError.value = null
                    _loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                    Log.i("Inside", response.message())
                }
            }
        }
    }


    private fun onError(message: String) {
        Log.i("onError", message)
        _repoLoadError.value = message
        _loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}