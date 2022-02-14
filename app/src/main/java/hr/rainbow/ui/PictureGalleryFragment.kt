package hr.rainbow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import hr.rainbow.R
import hr.rainbow.databinding.FragmentPictureGalleryBinding
import hr.rainbow.domain.model.Picture
import hr.rainbow.domain.view_models.PictureGalleryViewModel
import hr.rainbow.ui.adapters.PictureGalleryAdapter
import hr.rainbow.util.UiEvents
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class PictureGalleryFragment : Fragment() {

    private var _binding: FragmentPictureGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PictureGalleryViewModel by activityViewModels()
    private var gallery: HashMap<String, MutableList<Picture>> = HashMap(0)
    private lateinit var adapter: PictureGalleryAdapter

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        subscribeForDataObservable()
        subscribeForErrorObservable()
        viewModel.fetchPictureGalleryData()
        binding.progressBar.visibility = View.VISIBLE

        adapter = PictureGalleryAdapter(this.requireContext(), gallery, glide) {
            viewModel.chosenPictureClick(it)
            Navigation.findNavController(binding.root).navigate(R.id.menuPictureViewer)
        }
        binding.rvPictureGallery.adapter = adapter

        binding.rvPictureGallery.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setupListeners() {
        binding.btnImportFromGallery.setOnClickListener {
            UploadImageHandlerActivity.startActivityForGallery(this.requireContext())
        }
        binding.btnTakeImage.setOnClickListener {
            UploadImageHandlerActivity.startActivityForCamera(this.requireContext())
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun subscribeForDataObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.pictureGallery.collect {
                gallery = it.gallery

                if (it.isLoading) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }

                if (this@PictureGalleryFragment::adapter.isInitialized) {
                    adapter.loadGallery(gallery)
                }
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