package com.teamtriad.forpets.ui.transport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.teamtriad.forpets.data.source.network.model.TransportReq
import com.teamtriad.forpets.databinding.FragmentTransportReqDetailBinding
import com.teamtriad.forpets.viewmodel.TransportViewModel

class TransportReqDetailFragment : Fragment() {

    private val args: TransportReqDetailFragmentArgs by navArgs()

    private val transportViewModel: TransportViewModel by activityViewModels()

    private var _binding: FragmentTransportReqDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransportReqDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val transportReq: TransportReq = transportViewModel.transportReqMap.value!!.values
            .elementAt(args.index)

        with(binding) {
            tvTitle.text = transportReq.title

            tvName.text = transportReq.name
            tvAge.text = transportReq.age
            tvWeight.text = transportReq.weight
            tvKind.text = transportReq.kind

            tvCharacterCaution.text = transportReq.characterCaution
            tvMessage.text = transportReq.message

            tvDate.text = transportReq.startDate.replace('/', '-') + " ~ " +
                    transportReq.endDate.replace('/', '-')
            tvFrom.text = transportReq.from
            tvTo.text = transportReq.to
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}