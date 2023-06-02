package es.informaticoya.lgpd01

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore



class MyAdapter(private val companyList: ArrayList<Company>, private val context: Context) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCompany: TextView = itemView.findViewById(R.id.tvCompany)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return companyList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvCompany.text = companyList[position].company
        holder.tvAddress.text = companyList[position].address

        holder.btnDelete.setOnClickListener {
            // Llamar a la función borrarEmpresa() en la actividad principal
            val company = companyList[position]
            (holder.itemView.context as EmpresasActivity).borrarEmpresas(company)
        }
      }
    }









