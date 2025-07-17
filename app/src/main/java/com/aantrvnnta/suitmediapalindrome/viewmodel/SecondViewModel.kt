package com.aantrvnnta.suitmediapalindrome.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SecondViewModel : ViewModel() {
    private val _selectedUserName = MutableLiveData<String>()
    val selectedUserName: LiveData<String> = _selectedUserName

    fun setSelectedUserName(name: String) {
        _selectedUserName.value = name
    }
}

