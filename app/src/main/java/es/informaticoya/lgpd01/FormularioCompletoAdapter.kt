package es.informaticoya.lgpd01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class FormularioCompletoAdapter(emptyList: List<Any>) : RecyclerView.Adapter<FormularioCompletoAdapter.MyViewHolder>() {

    private var preguntasRespuestas: MutableList<PreguntaRespuesta> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pregunta_respuesta, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val preguntaRespuesta = preguntasRespuestas[position]
        holder.bind(preguntaRespuesta)
    }

    override fun getItemCount(): Int {
        return preguntasRespuestas.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val preguntaTextView: TextView = itemView.findViewById(R.id.preguntaTextView)
        private val opcionesRadioGroup: RadioGroup = itemView.findViewById(R.id.radioGroupRespuestas)
        private val botonEditar: Button = itemView.findViewById(R.id.botonEditar)
        private val botonBorrar: Button = itemView.findViewById(R.id.botonBorrar)

        fun bind(preguntaRespuesta: PreguntaRespuesta) {
            preguntaTextView.text = preguntaRespuesta.pregunta

            opcionesRadioGroup.removeAllViews()

            // Agregar los RadioButtons dinámicamente
            for (respuesta in preguntaRespuesta.respuestas) {
                val radioButton = RadioButton(itemView.context)
                radioButton.text = respuesta
                opcionesRadioGroup.addView(radioButton)
            }


            botonEditar.setOnClickListener {
                // Acción para el botón "Editar"
                // Puedes implementar aquí la lógica para editar la pregunta y respuestas
            }

            botonBorrar.setOnClickListener {
                // Acción para el botón "Borrar"
                // Puedes implementar aquí la lógica para borrar la pregunta y respuestas
            }
        }


        fun actualizarLista(nuevaLista: List<PreguntaRespuesta>) {
            preguntasRespuestas = nuevaLista as MutableList<PreguntaRespuesta>
            notifyDataSetChanged()
        }
    }
}
