package com.herasymchuk.andrushchenko.apps.recyclerview.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.herasymchuk.andrushchenko.apps.recyclerview.UsersAdapter
import com.herasymchuk.andrushchenko.apps.recyclerview.navigator
import com.herasymchuk.andrushchenko.apps.recyclerview.tasks.Result
import com.herasymchuk.andrushchenko.databinding.FragmentUserListBinding
import com.herasymchuk.andrushchenko.insets.applyInsets

class UserListFragment : Fragment() {
    private lateinit var binding: FragmentUserListBinding
    private lateinit var adapter: UsersAdapter
    private val viewModel: UserListViewModel by viewModels<UserListViewModel.Default>(
        factoryProducer = UserListViewModel.Companion::Factory
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        adapter = UsersAdapter(viewModel)

        viewModel.users.observe(viewLifecycleOwner) {
            hideAll()
            when (it) {
                is Result.Success -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    adapter.updateUsers(it.data)
                }

                is Result.Error -> {
                    binding.tryAgainContainer.visibility = View.VISIBLE
                }

                is Result.Pending -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Empty -> {
                    binding.noUsersTextView.visibility = View.VISIBLE
                }
            }
        }

        viewModel.actionShowDetails.observe(viewLifecycleOwner) {
            it.getValue()?.let { user -> navigator().showDetails(user) }
        }

        viewModel.actionShowToast.observe(viewLifecycleOwner) {
            it.getValue()?.let { stringId -> Toast.makeText(requireContext(), stringId, Toast.LENGTH_SHORT).show() }
        }

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(), DividerItemDecoration.VERTICAL
            )
        )
        binding.recyclerView.applyInsets(bottom = true)
        val itemAnimator: RecyclerView.ItemAnimator? = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    private fun hideAll() {
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.tryAgainContainer.visibility = View.GONE
        binding.noUsersTextView.visibility = View.GONE
    }
}
