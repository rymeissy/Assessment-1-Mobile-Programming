package org.d3if3083.assessment2.ui.input_resep

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.d3if3083.assessment2.R
import org.d3if3083.assessment2.databinding.FragmentInputResepBinding
import org.d3if3083.assessment2.db.ResepDb
import org.d3if3083.assessment2.db.ResepEntity
import org.d3if3083.assessment2.ui.resep.ResepViewModel
import org.d3if3083.assessment2.ui.resep.ResepViewModelFactory

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class InputResepFragment : Fragment() {

    private var _binding: FragmentInputResepBinding? = null

    private lateinit var uploadButton: Button

    private val viewModel: ResepViewModel by lazy {
        val db = ResepDb.getInstance(requireContext())
        val factory = ResepViewModelFactory(db.resepDao)
        ViewModelProvider(this, factory)[ResepViewModel::class.java]
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputResepBinding.inflate(inflater, container, false)
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

        // tombol simpan
        binding.buttonSimpan.setOnClickListener {
            simpanDataResep()
        }

        // tombol unggah gambar
        uploadButton = binding.buttonUnggahdGambar
        uploadButton.setOnClickListener {
            pickImageFromGallery()
        }

        // agar gambar bisa diklik
        binding.gambarResepYangDiunggah.setOnClickListener {
            // Mengambil sumber gambar dari ImageView
            val drawable = binding.gambarResepYangDiunggah.drawable

            // Memeriksa apakah ImageView memiliki gambar
            if (drawable != null) {
                // Membuat dialog untuk menampilkan gambar
                val dialog = Dialog(requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.dialog_image)

                val imageView = dialog.findViewById<ImageView>(R.id.dialogImageView)
                imageView.setImageDrawable(drawable)

                // Mengatur dialog agar dapat ditutup saat diklik
                imageView.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    }

    private fun pickImageFromGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Jika izin belum diberikan, tampilkan dialog permintaan izin
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Tampilkan penjelasan mengapa akses diperlukan jika pengguna telah menolak izin sebelumnya
                AlertDialog.Builder(requireContext())
                    .setTitle("Izin Diperlukan")
                    .setMessage("Aplikasi ini membutuhkan akses ke galeri untuk mengunggah gambar.")
                    .setPositiveButton("OK") { _, _ ->
                        requestGalleryPermission()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            } else {
                // Permintaan izin untuk pertama kali
                requestGalleryPermission()
            }
        } else {
            // Jika izin sudah diberikan, lanjutkan dengan pemilihan gambar dari galeri
            startGalleryIntent()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data!!
            // Gunakan URI gambar yang dipilih untuk melakukan aksi sesuai kebutuhan
            // Misalnya, menampilkan gambar menggunakan Glide atau mengunggahnya ke server
            loadImage(selectedImageUri)
        }
    }

    private fun loadImage(imageUri: Uri) {
        Glide.with(requireContext())
            .load(imageUri)
            .into(binding.gambarResepYangDiunggah) // ImageView yang akan menampilkan gambar

        // jika telah mengunggah gambar, tombol unggah gambar akan hilang
        uploadButton.visibility = View.GONE
    }

    private fun requestGalleryPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_GALLERY
        )
    }

    private fun startGalleryIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Jika izin diberikan, lanjutkan dengan pemilihan gambar dari galeri
                startGalleryIntent()
            } else {
                // Jika izin ditolak, berikan notifikasi
                Toast.makeText(
                    requireContext(),
                    "Akses ke galeri ditolak. Tidak dapat mengunggah gambar.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val REQUEST_PERMISSION_GALLERY = 1002
        private const val REQUEST_IMAGE_PICK = 1001
    }

    private fun simpanDataResep() {
        // validasi input
        if (binding.namaInp.text.toString().isEmpty()) {
            binding.namaInp.error = "Nama resep tidak boleh kosong"
            return
        }
        if (binding.descInp.text.toString().isEmpty()) {
            binding.descInp.error = "Deskripsi resep tidak boleh kosong"
            return
        }
        if (binding.spinner.selectedItem.toString() == "Kategori") {
            Toast.makeText(requireContext(), "Kategori tidak boleh kosong", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (binding.gambarResepYangDiunggah.drawable == null) {
            Toast.makeText(requireContext(), "Gambar tidak boleh kosong", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // size of data before adding new data
        // val size = viewModel.data.value?.size

        val resep = ResepEntity(
            namaResep = binding.namaInp.text.toString(),
            descResep = binding.descInp.text.toString(),
            kategori = binding.spinner.selectedItem.toString(),
            gambar = R.drawable.bakso,
        )

        viewModel.insertData(resep)

        /*        // check if data is added to database
                if (viewModel.data.value?.size == size?.plus(1)) {
                    Toast.makeText(requireContext(), "Data berhasil ditambahkan dan size = $size", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "Data gagal ditambahkan dan size $size", Toast.LENGTH_SHORT)
                        .show()
                }*/

        findNavController().navigate(R.id.action_InputResep_to_TampilanResep)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}