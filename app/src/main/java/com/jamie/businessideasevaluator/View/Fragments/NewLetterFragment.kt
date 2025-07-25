package com.jamie.businessideasevaluator.View.Fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.ViewModel.NewLetterViewModel

class NewLetterFragment : Fragment() {

    companion object {
        fun newInstance() = NewLetterFragment()
    }

    private val viewModel: NewLetterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_new_letter, container, false)
    }
}