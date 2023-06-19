package org.d3if3083.assessment2.ui.input_resep

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.d3if3083.assessment2.R
import org.d3if3083.assessment2.databinding.FragmentInputResepBinding
import org.d3if3083.assessment2.db.ResepDb
import org.d3if3083.assessment2.model.Resep
import org.d3if3083.assessment2.ui.resep.ResepViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class InputResepFragment : Fragment() {

    private var _binding: FragmentInputResepBinding? = null

    // varibel tombol unggah gambar
    private lateinit var uploadButton: Button

    // variabel tombol cancel
    private lateinit var cancelButton: ImageButton

    // Tambahkan variabel flag untuk memeriksa apakah gambar telah diunggah
    private var isImageUploaded = false

    private val viewModel: ResepViewModel by lazy {
        ViewModelProvider(this)[ResepViewModel::class.java]
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var storageref = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null
    private var filename: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputResepBinding.inflate(inflater, container, false)
        // menghilangkan FAB
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE

        viewModel.getData()

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

            if (imageUri != null) {
                filename = getFileName(imageUri!!)
                uploadImage(filename!!)
            }
        }

        // tombol unggah gambar
        uploadButton = binding.buttonUnggahdGambar
        uploadButton.setOnClickListener {
            pickImageFromGallery()
        }

        // agar gambar bisa diklik
        binding.gambarResepYangDiunggah.setOnClickListener {
            // Memeriksa apakah gambar telah diunggah
            if (isImageUploaded) {
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

        // tombol cancel
        cancelButton = binding.btnCancel
        cancelButton.visibility = View.GONE
        cancelButton.setOnClickListener {
            // Logika untuk menghapus gambar yang telah diunggah
            removeUploadedImage()
        }
    }

    private fun removeUploadedImage() {
        // Menghapus gambar yang telah diunggah
        binding.gambarResepYangDiunggah.setImageDrawable(null)
        // Mengatur flag menjadi false
        isImageUploaded = false
        // Mengatur visibilitas tombol upload menjadi ditampilkan
        uploadButton.visibility = View.VISIBLE
        // Mengatur visibilitas tombol cancel menjadi tidak ditampilkan
        cancelButton.visibility = View.GONE
        // Mengatur ulang ikon foto placeholder
        binding.gambarResepYangDiunggah.setImageResource(R.drawable.ic_baseline_insert_photo_24)
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
            imageUri = selectedImageUri
            // Gunakan URI gambar yang dipilih untuk memuat gambar
            loadImage(selectedImageUri)
        }
    }

    private fun loadImage(imageUri: Uri) {
        Glide.with(requireContext())
            .load(imageUri)
            .into(binding.gambarResepYangDiunggah) // ImageView yang akan menampilkan gambar

        // Set flag menjadi true ketika gambar diunggah
        isImageUploaded = true

        // jika telah mengunggah gambar, tombol unggah gambar akan hilang
        uploadButton.visibility = View.GONE

        // Menampilkan tombol cancel setelah gambar diunggah
        cancelButton.visibility = View.VISIBLE
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

    private fun uploadImage(filename: String) {
        try {
            imageUri?.let {
                storageref.child("img/${filename}").putFile(it)
                    .addOnSuccessListener {
                        Log.e("Upload Image", "Berhasil mengunggah gambar")
                    }
                    .addOnFailureListener {
                        Log.e("Upload Image", "Gagal mengunggah gambar")
                    }
            }
        } catch (e: Exception) {
            Log.e("Upload Image", e.message.toString())
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme.equals("content")) {
            val cursor: Cursor? =
                requireContext().contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result =
                        cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }

        if (result == null) {
            result = uri.path
            val cut: Int = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private fun simpanDataResep() {
        var result = false
        var resep: Resep?

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
        if (imageUri == null) {
            Toast.makeText(requireContext(), "Gambar tidak boleh kosong", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (imageUri != null) {
            resep = Resep(
                0,
                binding.namaInp.text.toString(),
                binding.descInp.text.toString(),
                binding.spinner.selectedItem.toString(),
                getFileName(imageUri!!),
            )

        } else {
            resep = Resep(
                0,
                binding.namaInp.text.toString(),
                binding.descInp.text.toString(),
                binding.spinner.selectedItem.toString(),
                "",
            )
        }

        runBlocking(Dispatchers.IO) {
            viewModel.updateData(this@InputResepFragment, resep).also {
                if (it) result = true
            }
        }

        if (result) {
            Toast.makeText(requireContext(), "Berhasil menyimpan data", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        } else {
            Toast.makeText(requireContext(), "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}