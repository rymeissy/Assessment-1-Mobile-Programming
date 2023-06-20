package org.d3if3083.assessment2.ui.histori

import SettingDataStore
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dataStore
import kotlinx.coroutines.launch
import org.d3if3083.assessment2.R
import org.d3if3083.assessment2.databinding.FragmentHistoriBinding
import org.d3if3083.assessment2.db.ResepDb
import org.d3if3083.assessment2.db.ResepEntity

class HistoriFragment : Fragment(), HistoriAdapter.OnItemClickListener {

    private val layoutDataStore by lazy { SettingDataStore(requireActivity().dataStore) }

    private val viewModel: HistoriViewModel by lazy {
        val db = ResepDb.getInstance(requireContext())
        val factory = HistoriViewModelFactory(db.resepDao)
        ViewModelProvider(this, factory)[HistoriViewModel::class.java]
    }

    private lateinit var binding: FragmentHistoriBinding
    private lateinit var myAdapter: HistoriAdapter

    private var isLinearLayout = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoriBinding.inflate(
            layoutInflater, container, false
        )

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myAdapter = HistoriAdapter()
        myAdapter.setOnItemClickListener(this)

        layoutDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner) {
            isLinearLayout = it
            setLayout()
            activity?.invalidateOptionsMenu()
        }

        // menyembunyikan FAB
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE

        with(binding.recyclerView) {
            addItemDecoration(
                DividerItemDecoration(
                    context, RecyclerView.VERTICAL
                )
            )
            adapter = myAdapter
            setHasFixedSize(true)
        }

        viewModel.getResep().observe(viewLifecycleOwner) {
            myAdapter.submitList(it)
            binding.emptyView.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }

    }

    private fun setLayout() {
        binding.recyclerView.layoutManager = if (isLinearLayout) LinearLayoutManager(context)
        else GridLayoutManager(context, 2)
    }

    private fun setIcon(menuItem: MenuItem) {
        val iconId = if (isLinearLayout) R.drawable.baseline_grid_view_24
        else R.drawable.baseline_list_24
        menuItem.icon = ContextCompat.getDrawable(requireContext(), iconId)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_history, menu)
        val menuItem = menu.findItem(R.id.action_switch_layout)
        setIcon(menuItem)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_switch_layout) {
            lifecycleScope.launch {
                layoutDataStore.saveLayout(!isLinearLayout, requireContext())
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(itemHistori: ResepEntity) {
    }
}
