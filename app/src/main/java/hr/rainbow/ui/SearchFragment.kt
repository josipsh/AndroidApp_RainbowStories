package hr.rainbow.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import hr.rainbow.R
import hr.rainbow.databinding.FragmentSearchBinding
import hr.rainbow.domain.model.SearchSuggestionType
import hr.rainbow.domain.model.SuggestionItem
import hr.rainbow.domain.model.UiHandler
import hr.rainbow.domain.view_models.SearchViewModel
import hr.rainbow.ui.adapters.SearchSuggestionPagerAdapter
import hr.rainbow.util.hideKeyboard

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var uiHandler: UiHandler

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        uiHandler = context as UiHandler
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        uiHandler.hideActionBar()
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

        viewModel.getSuggestions(requireContext(), "")

        binding.viewPager.adapter = SearchSuggestionPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (SearchSuggestionType.fromInt(position)) {
                SearchSuggestionType.TAG -> {
                    tab.text = requireContext().getString(R.string.tag)
                }
                else -> {
                    tab.text = requireContext().getString(R.string.recent)
                }
            }
        }.attach()

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setupListeners() {
        binding.query.setStartIconOnClickListener {
            uiHandler.showActionBar()
            Navigation.findNavController(binding.root).popBackStack()
        }

        binding.query.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.getSuggestions(requireContext(), text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.query.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                searchQuery()
                binding.query.hideKeyboard()
                true
            } else {
                false
            }
        }

        binding.query.editText?.setOnTouchListener { view, _ ->
            binding.query.hint = ""
            view.performClick()
        }
    }

    private fun searchQuery() {
        if (binding.query.editText?.text.toString().isNotEmpty()) {
            when (SearchSuggestionType.fromInt(binding.viewPager.currentItem)) {
                SearchSuggestionType.RECENT -> {
                    viewModel.tagSearch(
                        requireContext(),
                        SuggestionItem(
                            type = SearchSuggestionType.TAG,
                            query = binding.query.editText?.text.toString()
                        )
                    )
                }
                SearchSuggestionType.TAG -> {
                    viewModel.tagSearch(
                        requireContext(),
                        SuggestionItem(
                            type = SearchSuggestionType.TAG,
                            query = binding.query.editText?.text.toString()
                        )
                    )
                }
            }
            Navigation.findNavController(binding.root).navigate(R.id.menuSearchResult)
        }
    }

    override fun onDestroy() {
        uiHandler.showActionBar()
        super.onDestroy()
    }
}