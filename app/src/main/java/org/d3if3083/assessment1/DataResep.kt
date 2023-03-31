package org.d3if3083.assessment1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.d3if3083.assessment1.databinding.ResepDataBinding

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

        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = MainAdapter(getData())
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
            binding.recyclerView.adapter = MainAdapter(data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getData(): List<Resep> {
        return listOf(
            Resep("Baso Bakar", "Bakso yang dibakar dengan bumbu kacang", "Makanan", R.drawable.bakso),
            Resep("Es Saos", "Es campur saos", "Minuman", R.drawable.es),
            Resep("Tutuk", "Nasi Kelutuk, penasaran ya?", "Makanan", R.drawable.tutuk),
            Resep("Mangga Yakult", "Mangga yang dicampur dengan Yakult", "Minuman", R.drawable.mangga),
            Resep("Pizza", "Pizza dengan topping", "Makanan", R.drawable.pizza),
            Resep("Soda Ceria","Soda yang manis", "Minuman", R.drawable.soda),
        )
    }

    // agar FAB tidak hilang setelah membuka second fragment (input resep)
    override fun onResume() {
        super.onResume()
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.VISIBLE
    }
}