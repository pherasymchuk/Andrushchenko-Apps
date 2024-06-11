package com.herasymchuk.andrushchenko.apps.recyclerview.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.herasymchuk.andrushchenko.apps.recyclerview.UserActionListener
import com.herasymchuk.andrushchenko.apps.recyclerview.UsersAdapter
import com.herasymchuk.andrushchenko.apps.recyclerview.model.User
import com.herasymchuk.andrushchenko.apps.recyclerview.navigator
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
        adapter = UsersAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                viewModel.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                viewModel.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                navigator().showDetails(user)
            }

            override fun onUserFire(user: User) {
                viewModel.fireUser(user)
            }
        })

        viewModel.users.observe(viewLifecycleOwner) {
            adapter.updateUsers(it)
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
}
