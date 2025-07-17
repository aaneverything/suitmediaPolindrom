package com.aantrvnnta.suitmediapalindrome.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aantrvnnta.suitmediapalindrome.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class ThirdViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private var currentPage = 1
    private var totalPages = 1
    private val userList = mutableListOf<User>()

    fun loadUsers(reset: Boolean = false) {
        if (reset) {
            currentPage = 1
            userList.clear()
        }
        _isLoading.value = true
        _isError.value = false
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val url = "https://reqres.in/api/users?page=$currentPage&per_page=10"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body = response.body?.string()
                val json = JSONObject(body)
                val data = json.getJSONArray("data")
                totalPages = json.getInt("total_pages")
                val newUsers = mutableListOf<User>()
                for (i in 0 until data.length()) {
                    val obj = data.getJSONObject(i)
                    newUsers.add(
                        User(
                            id = obj.getInt("id"),
                            email = obj.getString("email"),
                            first_name = obj.getString("first_name"),
                            last_name = obj.getString("last_name"),
                            avatar = obj.getString("avatar")
                        )
                    )
                }
                userList.addAll(newUsers)
                _users.postValue(userList.toList())
                _isLoading.postValue(false)
            } catch (e: Exception) {
                _isLoading.postValue(false)
                _isError.postValue(true)
            }
        }
    }

    fun loadNextPage() {
        if (currentPage < totalPages) {
            currentPage++
            loadUsers(reset = false)
        }
    }
}

