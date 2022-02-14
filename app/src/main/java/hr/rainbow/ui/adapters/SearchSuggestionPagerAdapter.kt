package hr.rainbow.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import hr.rainbow.domain.model.SearchSuggestionType
import hr.rainbow.ui.SearchSuggestionFragment

class SearchSuggestionPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return SearchSuggestionType.values().size
    }

    override fun createFragment(position: Int): Fragment {
        return SearchSuggestionFragment.newInstance(position)
    }
}