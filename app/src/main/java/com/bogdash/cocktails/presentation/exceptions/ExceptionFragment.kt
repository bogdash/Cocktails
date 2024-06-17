package com.bogdash.cocktails.presentation.exceptions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdash.cocktails.databinding.FragmentExceptionBinding

class ExceptionFragment : Fragment() {

    private lateinit var binding: FragmentExceptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExceptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            binding.tvException.text = it.getString(ARG_EX_ID)
        }
    }

    companion object {
        const val ARG_EX_ID = "ex_id"

        @JvmStatic
        fun newInstance(exceptionText: String) = ExceptionFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_EX_ID, exceptionText)
            }
        }
    }
}