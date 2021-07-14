package com.example.flowtrandingsystem.gui.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.flowtrandingsystem.R
import com.example.flowtrandingsystem.gui.model.ProductType
import com.example.flowtrandingsystem.gui.ui.DatasheetActivity
import com.example.flowtrandingsystem.gui.ui.InventoryActivity

class ProductsCategoriesAdapter (val context: Context): RecyclerView.Adapter<ProductsCategoriesAdapter.Holder>() {

    var listCategories = emptyList<ProductType>()

    fun updateCategoryList(list: List<ProductType>){
        listCategories = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsCategoriesAdapter.Holder {
        val view = LayoutInflater
            .from(context)
            .inflate(R.layout.holder_product_types, parent, false)

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return listCategories.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val recentCategories = listCategories[position]

        holder.productCategory.text = recentCategories.type

        holder.cardInventoryItems.setOnClickListener {
            val intent = Intent(context, InventoryActivity::class.java)
            intent.putExtra("productType", recentCategories.type)
            context.startActivity(intent)

            val prefs: SharedPreferences = context.getSharedPreferences(
                "preferencias",
                Context.MODE_PRIVATE
            )

            //Tentar passar a permissao usuario inteiro
            prefs.edit().putString("TYPE", recentCategories.type).apply()
        }
    }

    class Holder(view: View): RecyclerView.ViewHolder(view){
        val productCategory = view.findViewById<TextView>(R.id.name_product_type)
        val cardInventoryItems = view.findViewById<CardView>(R.id.card_product_type)
    }
}