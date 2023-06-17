package org.d3if3083.assessment2.ui.detail_resep

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.d3if3083.assessment2.R
import org.d3if3083.assessment2.databinding.FragmentDetailResepBinding
import org.d3if3083.assessment2.db.ResepDb
import org.d3if3083.assessment2.db.ResepEntity
import org.d3if3155.hitungbmi.ui.histori.DetailResepViewModel
import org.d3if3155.hitungbmi.ui.histori.DetailResepViewModelFactory

class DetailResepFragment : Fragment() {

    // view model
    private val viewModel: DetailResepViewModel by lazy {
        val db = ResepDb.getInstance(requireContext())
        val factory = DetailResepViewModelFactory(db.resepDao)
        ViewModelProvider(this, factory)[DetailResepViewModel::class.java]
    }

    private var _binding: FragmentDetailResepBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailResepBinding.inflate(inflater, container, false)

        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val data = arguments?.getParcelable<ResepEntity>("currentResep")

        // get data from bundle
        if (data != null) {
            (activity as AppCompatActivity).supportActionBar?.title = data.namaResep
            binding.productImage.setImageResource(data.gambar)
            binding.productDescription.text = data.descResep
        }

        binding.shareButton.setOnClickListener {
            val sendIntent: String =
                """
                Resep ${data?.namaResep}
                ${data?.descResep}
           """.trimIndent()
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, sendIntent)
                type = "text/plain"
            }
            startActivity(shareIntent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_resep_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_hapus) {
            // back to resep fragment
            hapusResep()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hapusResep() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.konfirmasi_hapus_item)
            .setPositiveButton(getString(R.string.hapus)) { _, _ ->
                val data = arguments?.getParcelable<ResepEntity>("currentResep")
                if (data != null) {
                    viewModel.deleteResep(data)
                    findNavController().navigateUp()
                }

            }
            .setNegativeButton(getString(R.string.batal)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

}