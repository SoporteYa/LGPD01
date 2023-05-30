package es.informaticoya.lgpd01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val companyList: ArrayList<Company> ):RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val tvCompany: TextView = itemView.findViewById(R.id.tvCompany)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
          val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,
          parent, false)
          return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return companyList.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvCompany.text = companyList[position].company
        holder.tvAddress.text = companyList[position].address
    }
}