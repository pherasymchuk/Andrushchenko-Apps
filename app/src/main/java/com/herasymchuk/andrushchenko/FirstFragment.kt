package com.herasymchuk.andrushchenko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.herasymchuk.andrushchenko.databinding.FragmentFirstBinding
import com.herasymchuk.andrushchenko.insets.applyInsets
import com.herasymchuk.andrushchenko.repository.AppItemRepository

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val repository = AppItemRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = AppsAdapter(repository.getAllApps(), findNavController())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.applyInsets(bottom = true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
