package org.d3if3083.assessment2.ui.resep

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3if3083.assessment2.databinding.ListItemBinding
import org.d3if3083.assessment2.db.ResepEntity

class ResepAdapter() :
    RecyclerView.Adapter<ResepAdapter.ViewHolder>() {

    val data = mutableListOf<ResepEntity>()

    // init data

    init {
        initData()
    }

    private fun initData() {

    }

    // update data of rycycler view
    fun updateData(newData: List<ResepEntity>) {
        data.clear()
        for (item in newData) {
            data.add(item)
        }
        notifyDataSetChanged()
    }

    /* // method use in DataResep.kt
     fun updateData(newData: List<ResepEntity>) {
         data.clear()
         for (item in newData) {
             data.add(item)
         }
     }*/

    class ViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(resepEntity: ResepEntity) {
            // Bind the data to the views in the layout
            binding.apply {
                namaTextView.text = resepEntity.namaResep
                kategoriTextView.text = resepEntity.kategori
                descTextView.text = resepEntity.descResep
                imageView.setImageResource(resepEntity.gambar)
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