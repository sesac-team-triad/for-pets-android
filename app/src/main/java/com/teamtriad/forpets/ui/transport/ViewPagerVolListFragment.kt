package com.teamtriad.forpets.ui.transport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.teamtriad.forpets.databinding.FragmentViewPagerVolListBinding
import com.teamtriad.forpets.tmp.Volunteers
import com.teamtriad.forpets.ui.transport.adpater.RecyclerviewVolListAdapter

class ViewPagerVolListFragment : Fragment() {

    private var _binding: FragmentViewPagerVolListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerVolListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerview()
    }

    private fun setRecyclerview() {
        val volList = Volunteers.loadList()
        val recyclerview = binding.rvVolList
        val adapter = RecyclerviewVolListAdapter()
        adapter.submitList(volList)
        recyclerview.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}