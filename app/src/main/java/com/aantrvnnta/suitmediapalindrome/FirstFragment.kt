package com.aantrvnnta.suitmediapalindrome

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class FirstFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etSentence = view.findViewById<EditText>(R.id.etSentence)
        val btnCheck = view.findViewById<Button>(R.id.btnCheck)
        val btnNext = view.findViewById<Button>(R.id.btnNext)

        btnCheck.setOnClickListener {
            val sentence = etSentence.text.toString()
            if (isPalindrome(sentence)) {
                showDialog("isPalindrome")
            } else {
                showDialog("not palindrome")
            }
        }

        btnNext.setOnClickListener {
            val name = etName.text.toString()
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(name)
            findNavController().navigate(action)
        }
    }

    private fun isPalindrome(input: String): Boolean {
        val clean = input.replace(" ", "").lowercase()
        return clean == clean.reversed()
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}

