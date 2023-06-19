package org.d3if3083.assessment2.ui.detail_resep

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.d3if3083.assessment2.R
import org.d3if3083.assessment2.databinding.FragmentDetailResepBinding
import org.d3if3083.assessment2.model.Resep
import org.d3if3083.assessment2.ui.resep.ResepViewModel
import org.d3if3083.galerihewan.network.ResepApi

class DetailResepFragment : Fragment() {

    private val viewModel: ResepViewModel by lazy {
        ViewModelProvider(this)[ResepViewModel::class.java]
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

        viewModel.getData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val data = arguments?.getParcelable<Resep>("currentResep")

        // get data from bundle
        if (data != null) {
            (activity as AppCompatActivity).supportActionBar?.title = data.namaResep

            Glide.with(binding.recipeImage.context)
                .load(ResepApi.getResepUrl(data.gambarId))
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(binding.recipeImage)
            binding.recipeCategory.text = "Kategori: ${data.kategori}"
            binding.recipeDescription.text = data.descResep
        }

        binding.shareButton.setOnClickListener {
            val sendIntent: String =
                """
                Resep ${data?.namaResep}
                ${data?.kategori}
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
        var result = false
        val result1 = MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.konfirmasi_hapus_item)
            .setPositiveButton(getString(R.string.hapus)) { _, _ ->
                val data = arguments?.getParcelable<Resep>("currentResep")
                if (data != null) {
                    runBlocking(Dispatchers.IO) {
                        viewModel.deleteResep(data).also {
                            result = it
                        }
                    }
                    if (result) {
                        Toast.makeText(requireContext(), "Berhasil dihapus", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    } else {
                        Toast.makeText(requireContext(), "Gagal dihapus", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            .setNegativeButton(getString(R.string.batal)) { dialog, _ ->
                result = false
                dialog.cancel()
            }
            .show()
    }
}