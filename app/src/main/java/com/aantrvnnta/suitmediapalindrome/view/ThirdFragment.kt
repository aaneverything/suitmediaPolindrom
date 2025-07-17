package com.aantrvnnta.suitmediapalindrome.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aantrvnnta.suitmediapalindrome.R
import com.aantrvnnta.suitmediapalindrome.model.UserAdapter
import com.aantrvnnta.suitmediapalindrome.orFalse
import com.aantrvnnta.suitmediapalindrome.viewmodel.ThirdViewModel

class ThirdFragment : Fragment() {
    private lateinit var adapter: UserAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var tvEmptyState: TextView
    private val viewModel: ThirdViewModel by viewModels()

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

        adapter = UserAdapter(mutableListOf()) { user ->
            setFragmentResult(
                "selectedUser",
                bundleOf("userName" to "${user.first_name} ${user.last_name}")
            )
            requireActivity().onBackPressed()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadUsers(reset = true)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                if (!viewModel.isLoading.value.orFalse() && lastVisible == adapter.itemCount - 1) {
                    viewModel.loadNextPage()
                }
            }
        })

        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            adapter.setData(users)
            tvEmptyState.visibility = if (users.isEmpty()) View.VISIBLE else View.GONE
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            swipeRefreshLayout.isRefreshing = isLoading
        })
        viewModel.isError.observe(viewLifecycleOwner, Observer { isError ->
            if (isError && viewModel.users.value.isNullOrEmpty()) {
                tvEmptyState.visibility = View.VISIBLE
            }
        })

        viewModel.loadUsers(reset = true)
    }
}