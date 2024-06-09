package com.herasymchuk.andrushchenko.apps.recyclerview.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.herasymchuk.andrushchenko.R
import com.herasymchuk.andrushchenko.databinding.FragmentUserDetailsBinding

class UserDetailsFragment : Fragment() {
    private lateinit var binding: FragmentUserDetailsBinding
    private val viewModel: UserDetailsViewModel by viewModels<UserDetailsViewModel.Default>(
        factoryProducer = UserDetailsViewModel::Factory
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUserDetails(requireArguments().getLong(ARG_USER_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)

        viewModel.userDetails.observe(viewLifecycleOwner) {
            binding.userNameTextView.text = it.user.name
            if (it.user.photo.isNotBlank()) {
                Glide.with(this)
                    .load(it.user.photo)
                    .circleCrop()
                    .into(binding.photoImageView)
            } else {
                Glide.with(this)
                    .load(R.drawable.ic_user_avatar)
                    .into(binding.photoImageView)
            }
            binding.userNameTextView.text = it.details
        }

        binding.deleteButton.setOnClickListener {
            viewModel.deleteUser()
            TODO("Not yet implemented")
        }
        return binding.root
    }

    companion object {
        private const val ARG_USER_ID = "ARG_USER_ID"

        fun newInstance(userId: Long): UserDetailsFragment {
            val fragment = UserDetailsFragment()
            fragment.arguments = bundleOf(ARG_USER_ID to userId)
            return fragment
        }
    }
}
