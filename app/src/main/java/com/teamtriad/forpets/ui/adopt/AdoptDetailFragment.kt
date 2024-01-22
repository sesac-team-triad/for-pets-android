package com.teamtriad.forpets.ui.adopt

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.teamtriad.forpets.databinding.FragmentAdoptDetailBinding
import com.teamtriad.forpets.util.glide

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

        with(binding) { // TODO: ViewModel을 사용하여 데이터를 전달받기.
            ivImage.glide(args.imageUrl)

            btnCare.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)

                startActivity(intent)
            }
            btnCharge.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)

                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}