package com.github.novotnyr.presentr

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.Serializable

data class User(val login: String = "") : Serializable

class UserViewModel : ViewModel() {
    val users = MutableLiveData<List<User>>()

    fun login(user: User) {
        viewModelScope.launch {
            presentr.login(user.login)
        }
    }

    fun refresh() {
        // 1. ziska udaje z REST APU
        // 2. zosynchronizuje ju so zivymi datami
        // 3. cele to zbehne v ramci korutiny
        viewModelScope.launch {
            users.postValue(presentr.getUsers())

            Log.d("UserViewModel", "Refreshing live data")
        }
    }

}