package com.teamtriad.forpets.ui.adopt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.teamtriad.forpets.data.source.network.model.AbandonmentInfo
import com.teamtriad.forpets.databinding.FragmentAdoptDetailBinding
import com.teamtriad.forpets.util.glide
import com.teamtriad.forpets.viewmodel.AdoptViewModel

class AdoptDetailFragment : Fragment() {

    private val args: AdoptDetailFragmentArgs by navArgs()

    private val adoptViewModel: AdoptViewModel by activityViewModels()

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

        val abandonmentInfo: AbandonmentInfo = adoptViewModel.abandonmentInfoList[args.index]

        with(binding) {
            ivImage.glide(abandonmentInfo.imageUrl)

            tvKind.text = "${tvKind.text} ${abandonmentInfo.kind}"
            tvSex.text = "${tvSex.text} ${abandonmentInfo.sex}"
            tvDate.text = "${tvDate.text} ${abandonmentInfo.happenDate}"

            tvAge.text = "${tvAge.text} ${abandonmentInfo.age}"
            tvColor.text = "${tvColor.text} ${abandonmentInfo.color}"
            tvWeight.text = "${tvWeight.text} ${abandonmentInfo.weight}"
            tvState.text = "${tvState.text} ${abandonmentInfo.processState}"
            tvNeuter.text = "${tvNeuter.text} ${abandonmentInfo.neuterYn}"
            tvSpecialMark.text = when (abandonmentInfo.noticeComment.isNullOrBlank()) {
                true -> abandonmentInfo.specialMark
                false -> abandonmentInfo.specialMark + "\n\n" + abandonmentInfo.noticeComment
            }

            tvCareNm.text = "${tvCareNm.text} ${abandonmentInfo.careNm}"
            tvCareTel.text = "${tvCareTel.text} ${abandonmentInfo.careTel}"
            tvCareAddr.text = "${tvCareAddr.text} ${abandonmentInfo.careAddr}"
            tvChargeNm.text = "${tvChargeNm.text} ${abandonmentInfo.chargeNm}"
            tvOfficeTel.text = "${tvOfficeTel.text} ${abandonmentInfo.officetel}"

            tvHappenPlace.text = "${tvHappenPlace.text} ${abandonmentInfo.happenPlace}"
            tvNoticeNo.text = "${tvNoticeNo.text} ${abandonmentInfo.noticeNo}"

            btnCare.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_DIAL, Uri.fromParts(
                        "tel",
                        abandonmentInfo.careTel,
                        null
                    )
                )

                startActivity(intent)
            }
            btnCharge.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_DIAL, Uri.fromParts(
                        "tel",
                        abandonmentInfo.officetel,
                        null
                    )
                )

                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}