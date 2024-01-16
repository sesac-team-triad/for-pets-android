package com.teamtriad.forpets.ui.adopt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.teamtriad.forpets.databinding.FragmentAdoptDetailBinding

class AdoptDetailFragment : Fragment() {

    private val args: AdoptDetailFragmentArgs by navArgs()

    private var _binding: FragmentAdoptDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdoptDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            Glide.with(requireContext())
                .load(args.imageUrl.replaceFirst("http:", "https:"))
                .into(ivImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}