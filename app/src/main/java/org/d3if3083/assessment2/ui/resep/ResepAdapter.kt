package org.d3if3083.assessment2.ui.resep

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3if3083.assessment2.R
import org.d3if3083.assessment2.databinding.ItemResepBinding
import org.d3if3083.assessment2.model.Resep
import org.d3if3083.galerihewan.network.ResepApi

class ResepAdapter() :
    RecyclerView.Adapter<ResepAdapter.ViewHolder>() {
    private val data = mutableListOf<Resep>()
    private var listener: OnItemClickListener? = null

    // interface untuk click listener
    interface OnItemClickListener {
        fun onItemClick(resep: Resep)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    // update data of rycycler view
    fun updateData(newData: List<Resep>) {
        data.clear()
        for (item in newData) {
            data.add(item)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemResepBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ViewHolder(
        private val binding: ItemResepBinding,
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

        fun bind(resep: Resep) {
            // Bind the data to the views in the layout
            binding.apply {
                labelResep.text = resep.namaResep
            }

            Glide.with(binding.imageResep.context)
                .load(ResepApi.getResepUrl(resep.gambarId))
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(binding.imageResep)
        }
    }

}