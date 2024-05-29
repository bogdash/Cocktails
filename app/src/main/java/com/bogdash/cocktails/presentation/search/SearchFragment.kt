package com.bogdash.cocktails.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.bogdash.cocktails.R
import com.bogdash.cocktails.databinding.FragmentSearchBinding


class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchView()
    }
    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }

    private fun setupSearchView(){
        val searchView = binding.searchSv
        val txtSearch = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        txtSearch.setHintTextColor(resources.getColor(R.color.submarine,null))
        txtSearch.setTextColor(resources.getColor(R.color.black,null))
    }
}