package com.bogdash.cocktails.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bogdash.cocktails.R

class DirectionsFragment : Fragment() {

    private lateinit var tvInstruction: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_directions, container, false)
        tvInstruction = view.findViewById(R.id.tvInstruction)
        return view
    }

    fun updateInstruction(instruction: String) {
        tvInstruction.text = instruction
    }

    companion object {
        @JvmStatic
        fun newInstance() = DirectionsFragment()
    }
}