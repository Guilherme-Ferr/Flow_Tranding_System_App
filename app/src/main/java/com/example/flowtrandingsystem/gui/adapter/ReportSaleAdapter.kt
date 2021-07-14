package com.example.flowtrandingsystem.gui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flowtrandingsystem.R
import com.example.flowtrandingsystem.gui.model.ReportSale
import com.example.flowtrandingsystem.gui.model.Sale

class ReportSaleAdapter(val context: Context): RecyclerView.Adapter<ReportSaleAdapter.Holder>() {

    var listSale = emptyList<Sale>()

    fun updateSaleList(list: List<Sale>){
        listSale = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater
            .from(context)
            .inflate(R.layout.holder_report_items, parent, false)

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return listSale.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val recentSale = listSale[position]


        holder.itemName.text = recentSale.items?.get(position)?.product_id.toString()
        holder.itemQuantity.text = recentSale.items?.get(position)?.product_id.toString()

    }

    class Holder(view: View): RecyclerView.ViewHolder(view){
        val itemName = view.findViewById<TextView>(R.id.report_product_name)
        val itemQuantity = view.findViewById<TextView>(R.id.report_product_quantity)
    }
}