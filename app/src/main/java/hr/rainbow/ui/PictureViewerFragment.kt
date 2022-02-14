package hr.rainbow.ui

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.work.*
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import hr.rainbow.R
import hr.rainbow.databinding.FragmentPictureViewerBinding
import hr.rainbow.domain.model.Picture
import hr.rainbow.domain.view_models.PictureGalleryViewModel
import hr.rainbow.domain.workers.DeleteImageWorker
import hr.rainbow.util.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class PictureViewerFragment : Fragment() {

    private lateinit var binding: FragmentPictureViewerBinding

    private val viewModel: PictureGalleryViewModel by activityViewModels()

    @Inject
    @Named("NoCache")
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPictureViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeForDataObservable()
        subscribeForErrorObservable()

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnDelete.setOnClickListener {
            requireContext().showConfirmDialog(
                title = requireContext().getString(R.string.media_delete),
                message = getString(R.string.are_you_sure),
                DialogInterface.OnClickListener { _, _ ->
                    viewModel.deleteChosenPicture()
                    Navigation.findNavController(binding.root).popBackStack()
                }
            )
        }
    }

    private fun subscribeForDataObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.chosenPicture.collect {
                glide.load(it.url).into(binding.ivPicture)
            }
        }
    }

    private fun subscribeForErrorObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiEvents.collect {
                when (it) {
                    is UiEvents.ShowSnackBar -> {
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT)
                            .setAction(it.action) {

                            }.show()
                    }
                }
            }
        }

    }
}