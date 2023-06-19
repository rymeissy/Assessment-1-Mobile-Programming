package org.d3if3083.assessment2.ui.histori

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3if3083.assessment2.R
import org.d3if3083.assessment2.databinding.ItemHistoriBinding
import org.d3if3083.assessment2.db.ResepEntity
import org.d3if3083.galerihewan.network.ResepApi

class HistoriAdapter : ListAdapter<ResepEntity, HistoriAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(transaction: ResepEntity)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResepEntity>() {
            override fun areItemsTheSame(
                oldData: ResepEntity, newData: ResepEntity
            ): Boolean {
                return oldData.id == newData.id
            }

            override fun areContentsTheSame(
                oldData: ResepEntity, newData: ResepEntity
            ): Boolean {
                return oldData == newData
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoriBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemHistoriBinding, itemClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    itemClickListener?.onItemClick(item)
                }
            }
        }

        // bind untuk menampilkan data
        fun bind(item: ResepEntity) = with(binding) {
            namaResep.text = item.namaResep

            Glide.with(imageView.context).load(ResepApi.getResepUrl(item.gambarId))
                .error(R.drawable.ic_baseline_broken_image_24).into(imageView)

            kategori.text = item.kategori
            descResep.text = item.descResep
        }
    }
}
