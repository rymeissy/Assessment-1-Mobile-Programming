package org.d3if3083.assessment2.ui.resep

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.d3if3083.assessment2.R
import org.d3if3083.assessment2.databinding.ResepInputBinding
import org.d3if3083.assessment2.model.Resep

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class InputResep : Fragment() {

    private var _binding: ResepInputBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ResepInputBinding.inflate(inflater, container, false)
        // menghilangkan FAB
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Buat daftar item yang akan ditampilkan pada spinner
        val items = listOf("Kategori", "Makanan", "Minuman")

        // Buat adapter dan tambahkan item ke dalam adapter
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)

        // Set adapter pada spinner
        binding.spinner.adapter = adapter

        binding.buttonSimpan.setOnClickListener {
            // validasi input
            if (binding.namaInp.text.toString().isEmpty()) {
                binding.namaInp.error = "Nama resep tidak boleh kosong"
                return@setOnClickListener
            }
            if (binding.descInp.text.toString().isEmpty()) {
                binding.descInp.error = "Deskripsi resep tidak boleh kosong"
                return@setOnClickListener
            }
            if (binding.spinner.selectedItem.toString() == "Kategori") {
                Toast.makeText(requireContext(), "Kategori tidak boleh kosong", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // kirim data ke fragment data resep berbentuk object
            val resep = Resep(
                binding.namaInp.text.toString(),
                binding.descInp.text.toString(),
                binding.spinner.selectedItem.toString()
            )

            // kirim data ke fragment data resep berbentuk bundle
            val bundle = Bundle().apply {
                putSerializable("resep", resep)
            }

            val fragment = DataResep()
            fragment.arguments = bundle

            findNavController().navigate(R.id.action_InputResep_to_TampilanResep, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}