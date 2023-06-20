package es.informaticoya.lgpd01

import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapterPreguntas(var preguntaList: ArrayList<Pregunta>, private val context: Context) : RecyclerView.Adapter<MyAdapterPreguntas.MyViewHolder>() {
    fun actualizarPreguntas(preguntas: List<Pregunta>) {
        preguntaList.clear()
        preguntaList.addAll(preguntas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_preguntas,
            parent, false
        )

        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pregunta = preguntaList[position]

        holder.tvEnunciado.text = pregunta.enunciado

        holder.checkBoxOpcion1.visibility = View.VISIBLE
        holder.checkBoxOpcion2.visibility = View.VISIBLE
        holder.checkBoxOpcion3.visibility = View.VISIBLE
        holder.checkBoxOpcion4.visibility = View.VISIBLE

        // Restablecer el estado de los checkboxes antes de asignar nuevos valores
        holder.checkBoxOpcion1.isChecked = false
        holder.checkBoxOpcion2.isChecked = false
        holder.checkBoxOpcion3.isChecked = false
        holder.checkBoxOpcion4.isChecked = false

        // Asignar las opciones de respuesta a los checkboxes
        val opciones = pregunta.opciones
        if (opciones.size >= 1) {
            holder.checkBoxOpcion1.text = opciones[0]
        }
        if (opciones.size >= 2) {
            holder.checkBoxOpcion2.text = opciones[1]
        }
        if (opciones.size >= 3) {
            holder.checkBoxOpcion3.text = opciones[2]
        }
        if (opciones.size >= 4) {
            holder.checkBoxOpcion4.text = opciones[3]
        }
    }

    override fun getItemCount(): Int {
        return preguntaList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEnunciado : TextView = itemView.findViewById(R.id.tvEnunciado)
        val checkBoxOpcion1 : CheckBox = itemView.findViewById(R.id.checkBoxOpcion1)
        val checkBoxOpcion2 : CheckBox = itemView.findViewById(R.id.checkBoxOpcion2)
        val checkBoxOpcion3 : CheckBox = itemView.findViewById(R.id.checkBoxOpcion3)
        val checkBoxOpcion4 : CheckBox = itemView.findViewById(R.id.checkBoxOpcion4)

    }
}
