package org.d3if3083.assessment2.ui.resep

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3if3083.assessment2.databinding.ItemResepBinding
import org.d3if3083.assessment2.db.ResepEntity

class ResepAdapter() :
    RecyclerView.Adapter<ResepAdapter.ViewHolder>() {

    val data = mutableListOf<ResepEntity>()
    private var listener: OnItemClickListener? = null

    // interface untuk click listener
    interface OnItemClickListener {
        fun onItemClick(resepEntity: ResepEntity)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    // update data of rycycler view
    fun updateData(newData: List<ResepEntity>) {
        data.clear()
        for (item in newData) {
            data.add(item)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemResepBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ViewHolder(
        private val binding: ItemResepBinding,
        itemClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            // click listener
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(data[position])
                }
            }
        }

        fun bind(resepEntity: ResepEntity) {
            // Bind the data to the views in the layout
            binding.apply {
                imageResep.setImageResource(resepEntity.gambar)
                labelResep.text = resepEntity.namaResep
            }
        }
    }

}