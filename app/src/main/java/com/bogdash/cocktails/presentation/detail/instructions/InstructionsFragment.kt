package com.bogdash.cocktails.presentation.detail.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdash.cocktails.databinding.FragmentInstructionsBinding

class InstructionsFragment : Fragment() {

    private lateinit var binding: FragmentInstructionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInstructionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val instruction = it.getString(ARG_INSTRUCTION)
            binding.tvInstruction.text = instruction
        }
    }

    companion object {
        private const val ARG_INSTRUCTION = "instruction"

        @JvmStatic
        fun newInstance(instruction: String) = InstructionsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_INSTRUCTION, instruction)
            }
        }
    }
}
