package com.pascal.qris_ku.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pascal.qris_ku.common.utils.formatToRupiah
import com.pascal.qris_ku.data.entity.TransactionEntity
import com.pascal.qris_ku.databinding.ItemTransactionBinding

class AdapterTransaction(
    val data: List<TransactionEntity?>?,
    val itemClick: OnClickListener
) : RecyclerView.Adapter<AdapterTransaction.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data?.get(position)
        holder.bind(item!!)
    }

    override fun getItemCount(): Int = data!!.size

    inner class ViewHolder(val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TransactionEntity) {

            with(binding) {
                tvName.text = "${item.bank} - ${item.name}"
                tvId.text = item.idTransaction
                tvTotal.text = formatToRupiah(item.total.toDoubleOrNull() ?: 0.0)

                root.setOnClickListener {
                    itemClick.detail(item)
                }
            }

        }
    }

    interface OnClickListener {
        fun detail(item: TransactionEntity)
    }
}