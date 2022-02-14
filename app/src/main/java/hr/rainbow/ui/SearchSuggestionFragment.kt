package hr.rainbow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import hr.rainbow.R
import hr.rainbow.databinding.FragmentSearchSuggestionBinding
import hr.rainbow.domain.model.SearchSuggestionType
import hr.rainbow.domain.model.SuggestionItem
import hr.rainbow.domain.view_models.SearchViewModel
import hr.rainbow.ui.adapters.SearchSuggestionItemAdapter
import hr.rainbow.util.UiEvents
import kotlinx.coroutines.flow.collect

private const val FRAGMENT_TYPE = "hr.rainbow.ui.arg.param"

@AndroidEntryPoint
class SearchSuggestionFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance(type: Int) =
            SearchSuggestionFragment().apply {
                arguments = Bundle().apply {
                    putInt(FRAGMENT_TYPE, type)
                }
            }
    }

    private lateinit var binding: FragmentSearchSuggestionBinding
    private lateinit var fragmentType: SearchSuggestionType

    private val viewModel: SearchViewModel by activityViewModels()
    private var suggestions: List<SuggestionItem> = emptyList()
    private lateinit var adapter: SearchSuggestionItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentType = SearchSuggestionType.fromInt(it.getInt(FRAGMENT_TYPE))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchSuggestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (fragmentType) {
            SearchSuggestionType.RECENT -> {
                subscribeForRecentObservable()
            }
            SearchSuggestionType.TAG -> {
                subscribeForTagObservable()
            }
        }

        subscribeForErrorObservable()

        adapter = SearchSuggestionItemAdapter(
            requireContext(),
            suggestions
        ) { queryItem ->
            handleQuery(queryItem)
            Navigation.findNavController(binding.root).navigate(R.id.menuSearchResult)

        }
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun handleQuery(queryItem: SuggestionItem) {
        when (fragmentType) {
            SearchSuggestionType.RECENT -> {
                viewModel.recentSearch(requireContext(), queryItem)
            }
            SearchSuggestionType.TAG -> {
                viewModel.tagSearch(requireContext(), queryItem)
            }
        }
    }

    private fun subscribeForRecentObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.recentSuggestion.collect {
                suggestions = it.data

                if (it.isLoading) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }

                if (suggestions.isEmpty() && !it.isLoading){
                    toggleNotingToShowLabelVisibility(true)
                }else{
                    toggleNotingToShowLabelVisibility(false)
                }

                if (this@SearchSuggestionFragment::adapter.isInitialized) {
                    adapter.loadItems(suggestions)
                }
            }
        }
    }

    private fun subscribeForTagObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.tagSuggestion.collect {
                suggestions = it.data

                if (it.isLoading) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }

                if (suggestions.isEmpty()){
                    toggleNotingToShowLabelVisibility(true)
                }else{
                    toggleNotingToShowLabelVisibility(false)
                }


                if (this@SearchSuggestionFragment::adapter.isInitialized) {
                    adapter.loadItems(suggestions)
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

    private fun toggleNotingToShowLabelVisibility(shouldBeVisible: Boolean){
        if (shouldBeVisible){
            binding.etNothingToShow.visibility = View.VISIBLE
        }else{
            binding.etNothingToShow.visibility = View.GONE
        }
    }
}