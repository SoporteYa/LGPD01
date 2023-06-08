package es.informaticoya.lgpd01

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MyAdapter(private val companyList: ArrayList<Company>, private val context: Context) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    interface OnEmpleadoClickListener {
        fun onEmpleadoClick(position: Int)
    }

    private var empleadoClickListener: OnEmpleadoClickListener? = null

    fun setOnEmpleadoClickListener(listener: OnEmpleadoClickListener) {
        empleadoClickListener = listener
    }


    interface OnProcesoClickListener {
        fun onProcesoClick(position: Int)
    }

    private var procesoClickListener: OnProcesoClickListener? = null

    fun setOnProcesoClickListener(listener: OnProcesoClickListener) {
        procesoClickListener = listener
    }

    interface OnSectorClickListener {
        fun onSectorClick(position: Int)
    }

    private var sectorClickListener: OnSectorClickListener? = null

    fun setOnSectorClickListener(listener: OnSectorClickListener) {
        sectorClickListener = listener
    }



    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCompany: TextView = itemView.findViewById(R.id.tvCompany)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnSector: Button = itemView.findViewById(R.id.btnSector)
        val btnProcesos: Button = itemView.findViewById(R.id.btnProcesos)
        val btnEmpleados: Button = itemView.findViewById(R.id.btnEmpleados)

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

        holder.btnEmpleados.setOnClickListener {
            empleadoClickListener?.onEmpleadoClick(position)
        }


        holder.btnProcesos.setOnClickListener {
            procesoClickListener?.onProcesoClick(position)
        }

        holder.btnSector.setOnClickListener {
            sectorClickListener?.onSectorClick(position)
        }

    }
}











