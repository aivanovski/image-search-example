package com.aivanouski.example.imsearch.presentation.images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aivanouski.example.imsearch.databinding.ImagesFragmentBinding
import com.aivanouski.example.imsearch.model.Image
import com.aivanouski.example.imsearch.presentation.images.dialog.ShowDetailsDialog
import com.aivanouski.example.imsearch.presentation.images.recyclerView.ImagesAdapter
import com.aivanouski.example.imsearch.utils.EventObserver
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImagesFragment : Fragment() {

    private val viewModel: ImagesViewModel by viewModel()

    private lateinit var binding: ImagesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ImagesFragmentBinding.inflate(inflater, container, false)
            .also {
                it.lifecycleOwner = viewLifecycleOwner
                it.viewModel = viewModel
            }

        binding.recyclerView.apply {
            adapter = ImagesAdapter(
                context = requireContext()
            )
            itemAnimator = null
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeLiveData()
        subscribeToEvents()

        viewModel.loadData()
    }

    private fun subscribeLiveData() {
        viewModel.items.observe(viewLifecycleOwner) { items ->
            (binding.recyclerView.adapter as ImagesAdapter).updateItems(items)
        }
    }

    private fun subscribeToEvents() {
        viewModel.showDetailsDialog.observe(
            viewLifecycleOwner,
            EventObserver { image ->
                showDetailsDialog(image)
            }
        )
    }

    private fun showDetailsDialog(image: Image) {
        val dialog = ShowDetailsDialog.newInstance(
            onConfirm = { viewModel.onShowDetailsConfirmed(image) }
        )
        dialog.show(childFragmentManager, ShowDetailsDialog.TAG)
    }

    companion object {

        fun newInstance(): ImagesFragment =
            ImagesFragment()
    }
}