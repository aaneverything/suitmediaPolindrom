package com.aantrvnnta.suitmediapalindrome.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aantrvnnta.suitmediapalindrome.R
import com.aantrvnnta.suitmediapalindrome.viewmodel.SecondViewModel

class SecondFragment : Fragment() {
    private val args: SecondFragmentArgs by navArgs()
    private val viewModel: SecondViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvSelectedUser = view.findViewById<TextView>(R.id.tvSelectedUser)
        val btnChooseUser = view.findViewById<Button>(R.id.btnChooseUser)

        tvName.text = args.name

        viewModel.selectedUserName.observe(viewLifecycleOwner, Observer { userName ->
            tvSelectedUser.text = userName ?: "Selected User Name"
        })

        btnChooseUser.setOnClickListener {
            findNavController().navigate(SecondFragmentDirections.actionSecondFragmentToThirdFragment())
        }

        setFragmentResultListener("selectedUser") { _, bundle ->
            val userName = bundle.getString("userName")
            viewModel.setSelectedUserName(userName ?: "Selected User Name")
        }
    }
}