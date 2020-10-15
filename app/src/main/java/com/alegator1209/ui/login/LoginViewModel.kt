package com.alegator1209.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alegator1209.datasource.remote.api.ApiClient
import com.alegator1209.interactors.LoginUseCase
import com.alegator1209.utils.Constants
import com.alegator1209.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var username: String = Constants.DEFAULT_LOGIN
    var password: String = Constants.DEFAULT_PASSWORD
    private val _refreshing = MutableLiveData<Boolean>(false)
    val refreshing: LiveData<Boolean> = _refreshing
    val canLogin: Boolean
        get() = username.isNotBlank() && password.isNotBlank()

    val loginResult = MutableLiveData<Result<String>>()
    lateinit var loginUseCase: LoginUseCase

    fun login() {
        loginUseCase = LoginUseCase(username, password, ApiClient().getAuth())
        _refreshing.value = true
        viewModelScope.launch {
            loginResult.value = loginUseCase.login()
            _refreshing.value = false
        }
    }
}
