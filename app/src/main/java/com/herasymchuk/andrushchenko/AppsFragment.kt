package com.herasymchuk.andrushchenko

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.herasymchuk.andrushchenko.databinding.FragmentAppsBinding
import com.herasymchuk.andrushchenko.repository.AppItemRepository

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppsFragment : Fragment() {
    private var _binding: FragmentAppsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAppsBinding.inflate(inflater, container, false)
        with(binding.recyclerView) {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }

            val typedValue = TypedValue()
            val theme: Resources.Theme =
                requireActivity().theme
                    ?: throw IllegalStateException("Was not able to get the theme from Activity")
            theme.resolveAttribute(
                com.google.android.material.R.attr.colorSurface,
                typedValue,
                true
            )
            val color: Int = typedValue.data

            val decoration = MaterialDividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            decoration.dividerThickness = 4
            decoration.dividerColor = color
            decoration.isLastItemDecorated = false
            addItemDecoration(decoration)
            setHasFixedSize(true)
            adapter = AppsAdapter(AppItemRepository(requireContext()).getAllApps())
        }

        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AppsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AppsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
