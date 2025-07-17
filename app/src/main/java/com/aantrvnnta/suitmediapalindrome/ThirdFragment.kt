package com.aantrvnnta.suitmediapalindrome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.concurrent.thread

class ThirdFragment : Fragment() {
    private lateinit var adapter: UserAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var tvEmptyState: TextView
    private var isLoading = false
    private var currentPage = 1
    private var totalPages = 1
    private val users = mutableListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        tvEmptyState = view.findViewById(R.id.tvEmptyState)

        adapter = UserAdapter(users) { user ->
            setFragmentResult(
                "selectedUser",
                bundleOf("userName" to "${user.first_name} ${user.last_name}")
            )
            requireActivity().onBackPressed()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            loadUsers(reset = true)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                if (!isLoading && currentPage < totalPages && lastVisible == users.size - 1) {
                    currentPage++
                    loadUsers(reset = false)
                }
            }
        })

        loadUsers(reset = true)
    }

    private fun loadUsers(reset: Boolean) {
        isLoading = true
        if (reset) {
            users.clear()
            adapter.notifyDataSetChanged()
            tvEmptyState.visibility = View.GONE
        }
        swipeRefreshLayout.isRefreshing = true
        thread {
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
                requireActivity().runOnUiThread {
                    if (reset) {
                        users.clear()
                        users.addAll(newUsers)
                        adapter.notifyDataSetChanged()
                    } else {
                        val start = users.size
                        users.addAll(newUsers)
                        adapter.notifyItemRangeInserted(start, newUsers.size)
                    }
                    swipeRefreshLayout.isRefreshing = false
                    isLoading = false
                    tvEmptyState.visibility = if (users.isEmpty()) View.VISIBLE else View.GONE
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    swipeRefreshLayout.isRefreshing = false
                    isLoading = false
                    if (users.isEmpty()) tvEmptyState.visibility = View.VISIBLE
                }
            }
        }
    }
}

