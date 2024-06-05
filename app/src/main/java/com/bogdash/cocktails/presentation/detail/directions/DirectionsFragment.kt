package com.bogdash.cocktails.presentation.detail.directions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdash.cocktails.databinding.FragmentDirectionsBinding

class DirectionsFragment : Fragment() {

    private lateinit var binding: FragmentDirectionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDirectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun updateInstruction(instruction: String) {
        binding.tvInstruction.text = instruction
    }

    companion object {
        @JvmStatic
        fun newInstance() = DirectionsFragment()
    }
}
