package org.d3if3083.assessment2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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

    private var _binding: ResepDataBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ResepDataBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = ResepAdapter(getData())
            setHasFixedSize(true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // tambahkan string data yang sudah dikirim dari input resep ke dalam list data
        val resep = arguments?.getSerializable("resep") as? Resep
        if (resep != null) {
            val data = getData().toMutableList()
            data.add(resep)
            // refresh recyclerview agar data yang baru ditambahkan muncul
            binding.recyclerView.adapter = ResepAdapter(data)
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

    private fun getData(): List<Resep> {
        return listOf(
            Resep("Baso Bakar", "Baso yang dibakar dengan bumbu kacang", "Makanan",
                R.drawable.bakso
            ),
            Resep("Es Jeruk", "Es jeruk segeerrr", "Minuman", R.drawable.es),
            Resep("Nasi Goreng Spesial", "Rasanya spesial banget dan nagih", "Makanan", R.drawable.tutuk),
            Resep("Mangga Yakult", "Mangga yang dicampur dengan Yakult", "Minuman",
                R.drawable.mangga
            ),
            Resep("Pizza Yummy", "Pizza dengan topping", "Makanan", R.drawable.pizza),
            Resep("Soda Ceria","Soda yang manis", "Minuman", R.drawable.soda),
        )
    }

    // agar FAB tidak hilang setelah membuka second fragment (input resep)
    override fun onResume() {
        super.onResume()
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.VISIBLE
    }
}