package es.informaticoya.lgpd01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val companiesList : ArrayList<Companies>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item,
        parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {

        val empresas : Companies = companiesList[position]
        holder.company.text = empresas.company
        holder.address.text = empresas.address
    }

    override fun getItemCount(): Int {

        return companiesList.size
    }


    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val company : TextView = itemView.findViewById(R.id.tvCompany)
        val address : TextView = itemView.findViewById(R.id.tvAddress)

    }
}