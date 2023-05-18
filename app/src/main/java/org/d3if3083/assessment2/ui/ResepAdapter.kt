package org.d3if3083.assessment2.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3if3083.assessment2.databinding.ListItemBinding
import org.d3if3083.assessment2.model.Resep

class ResepAdapter(private val data: List<Resep>) :
    RecyclerView.Adapter<ResepAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(resep: Resep) = with(binding) {
            namaTextView.text = resep.namaResep
            kategoriTextView.text = resep.kategori
            descTextView.text = resep.descResep
            imageView.setImageResource(resep.gambar)

            root.setOnClickListener {
//                val message = root.context.getString(R.string.message, resep.namaHewan)
//                Toast.makeText(root.context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}