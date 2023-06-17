package org.d3if3083.assessment2.ui.resep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import org.d3if3083.assessment2.R
import org.d3if3083.assessment2.data.SettingDataStore
import org.d3if3083.assessment2.data.dataStore
import org.d3if3083.assessment2.databinding.FragmentResepBinding
import org.d3if3083.assessment2.db.ResepDb
import org.d3if3083.assessment2.db.ResepEntity


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ResepFragment : Fragment(), ResepAdapter.OnItemClickListener {

    // datastore
    private val layoutDataStore: SettingDataStore by lazy {
        SettingDataStore(requireContext().dataStore)
    }

    private val viewModel: ResepViewModel by lazy {
        val db = ResepDb.getInstance(requireContext())
        val factory = ResepViewModelFactory(db.resepDao)
        ViewModelProvider(this, factory)[ResepViewModel::class.java]
    }

    private var _binding: FragmentResepBinding? = null
    private lateinit var myAdapter: ResepAdapter
    private var isFirstTime = false
        set

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // datastore
        layoutDataStore.isFirstTime.asLiveData().observe(viewLifecycleOwner) {
            isFirstTime = it
        }

        _binding = FragmentResepBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        myAdapter = ResepAdapter()
        myAdapter.setOnItemClickListener(this)

        with(binding.recyclerView) {
            layoutManager = GridLayoutManager(context, 2)
            adapter = myAdapter
            setHasFixedSize(true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // mengembalikan is first time data store ke bentuk aplikasi pertama kali terbuka (yang menampilkan data dummy)
        layoutDataStore.isFirstTime.asLiveData().observe(viewLifecycleOwner) {
            isFirstTime = it

            if (isFirstTime) {
                viewModel.initData()
                lifecycleScope.launch {
                    layoutDataStore.saveFirstTime(false, requireContext())
                }
            }
            activity?.invalidateOptionsMenu()
        }

        viewModel.getResep().observe(viewLifecycleOwner) {
            myAdapter.updateData(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_about) {
            findNavController().navigate(
                R.id.action_resepFragment_to_aboutFragment
            )
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // agar FAB tidak hilang setelah membuka fragment lainnya
    override fun onResume() {
        super.onResume()
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.VISIBLE
    }

    override fun onItemClick(resepEntity: ResepEntity) {
        // Bundle untuk mengirim data ke DetailResepFragment
        val bundle = Bundle()
        bundle.putParcelable("currentResep", resepEntity)
        findNavController().navigate(
            R.id.action_resepFragment_to_detailResepFragment, bundle
        )
    }
}

