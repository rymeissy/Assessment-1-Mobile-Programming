package org.d3if3083.assessment2.ui.resep

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.d3if3083.assessment2.R
import org.d3if3083.assessment2.databinding.ResepDataBinding
import org.d3if3083.assessment2.model.Resep

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DataResep : Fragment() {

    private val viewModel: ResepViewModel by lazy {
        ViewModelProvider(this)[ResepViewModel::class.java]
    }

    private var _binding: ResepDataBinding? = null
    private lateinit var myAdapter: ResepAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ResepDataBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        myAdapter = ResepAdapter()

        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = myAdapter
            setHasFixedSize(true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getData().observe(viewLifecycleOwner) {
            myAdapter.updateData(it)
            // tambahkan string data yang sudah dikirim dari input resep ke dalam list data
            val resep = arguments?.getSerializable("resep") as? Resep
            if (resep != null) {
                val data = it.toMutableList()
                data.add(resep)
                // refresh recyclerview agar data yang baru ditambahkan muncul
                myAdapter.updateData(data)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_about) {
            findNavController().navigate(
                R.id.action_TampilanResep_to_aboutFragment)
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
}