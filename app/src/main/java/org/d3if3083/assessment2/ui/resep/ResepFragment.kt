package org.d3if3083.assessment2.ui.resep

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import org.d3if3083.assessment2.MainActivity
import org.d3if3083.assessment2.R
import org.d3if3083.assessment2.data.SettingDataStore
import org.d3if3083.assessment2.data.dataStore
import org.d3if3083.assessment2.databinding.FragmentResepBinding
import org.d3if3083.assessment2.db.ResepDb
import org.d3if3083.assessment2.db.ResepEntity
import org.d3if3083.assessment2.model.Resep
import org.d3if3083.galerihewan.network.ApiStatus


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ResepFragment : Fragment(), ResepAdapter.OnItemClickListener {

    // datastore
    private val layoutDataStore: SettingDataStore by lazy {
        SettingDataStore(requireContext().dataStore)
    }

    private val viewModel: ResepViewModel by lazy {
        ViewModelProvider(this)[ResepViewModel::class.java]
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
        viewModel.retrieveData()
        viewModel.getData().observe(viewLifecycleOwner) {
            myAdapter.updateData(it)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStatus().observe(viewLifecycleOwner) {
            updateProgress(it)
        }

        viewModel.scheduleUpdater(requireActivity().application)
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

    override fun onItemClick(resep: Resep) {
        // Bundle untuk mengirim data ke DetailResepFragment
        val bundle = Bundle()
        bundle.putParcelable("currentResep", resep)
        findNavController().navigate(
            R.id.action_resepFragment_to_detailResepFragment, bundle
        )
    }

    private fun updateProgress(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
                // hilangkan FAB ketika loading
                activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE

            }
            ApiStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermission()
                }
                // tampilkan FAB ketika sukses
                activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.VISIBLE

            }
            ApiStatus.FAILED -> {
                binding.progressBar.visibility = View.GONE
                binding.networkError.visibility = View.VISIBLE
                // hilangkan FAB ketika error
                activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                MainActivity.PERMISSION_REQUEST_CODE
            )
        }
    }
}

