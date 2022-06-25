package com.example.tabbedproject.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.tabbedproject.R
import com.example.tabbedproject.databinding.FragmentTabBinding
import com.google.android.material.tabs.TabLayoutMediator

class TabFragment : Fragment() {
    private lateinit var binding: FragmentTabBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            binding.viewPager.apply {
            adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "ToDo"
                1 -> tab.text = "Doing"
                2 -> tab.text = "Done"
            }
        }.attach()
    }
}