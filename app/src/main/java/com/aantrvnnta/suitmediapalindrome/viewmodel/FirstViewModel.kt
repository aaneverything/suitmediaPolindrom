package com.aantrvnnta.suitmediapalindrome.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FirstViewModel : ViewModel() {
    private val _palindromeResult = MutableLiveData<String>()
    val palindromeResult: LiveData<String> = _palindromeResult

    fun checkPalindrome(sentence: String) {
        if (sentence.isBlank()) {
            _palindromeResult.value = "Input cannot be empty"
        } else if (isPalindrome(sentence)) {
            _palindromeResult.value = "isPalindrome"
        } else {
            _palindromeResult.value = "not palindrome"
        }
    }

    private fun isPalindrome(input: String): Boolean {
        val clean = input.replace(" ", "").lowercase()
        return clean == clean.reversed()
    }
}

