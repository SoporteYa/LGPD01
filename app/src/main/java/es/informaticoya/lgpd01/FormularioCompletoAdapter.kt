package es.informaticoya.lgpd01

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class FormularioCompletoAdapter(private var preguntasRespuestas: MutableList<PreguntaRespuesta>) :
    RecyclerView.Adapter<FormularioCompletoAdapter.MyViewHolder>() {

    interface EditarPreguntaRespuestaListener {
        fun onEditarPreguntaRespuesta(preguntaRespuesta: PreguntaRespuesta)
    }

    private var editarPreguntaRespuestaListener: EditarPreguntaRespuestaListener? = null


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

    fun actualizarLista(nuevaLista: MutableList<PreguntaRespuesta>) {
        preguntasRespuestas.clear()
        preguntasRespuestas.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    fun setEditarPreguntaRespuestaListener(listener: EditarPreguntaRespuestaListener) {
        editarPreguntaRespuestaListener = listener
    }

    private val db = FirebaseFirestore.getInstance()

    fun borrarPregunta(position: Int) {
        val preguntaRespuesta = preguntasRespuestas[position]
        val preguntaId = preguntaRespuesta.id

        db.collection("preguntas").document(preguntaId)
            .delete()
            .addOnSuccessListener {
               // Toast.makeText(context, "Pregunta eliminada correctamente", Toast.LENGTH_LONG) .show()
                preguntasRespuestas.removeAt(position)
                notifyItemRemoved(position)
            }
            .addOnFailureListener { e ->
                //Toast.makeText(context, "La pregunta no ha sido borrada", Toast.LENGTH_LONG).show()
                Log.e(TAG, "Error al eliminar la pregunta: ${e.message}")
            }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val preguntaTextView: TextView = itemView.findViewById(R.id.preguntaTextView)
        private val opcionesRadioGroup: RadioGroup = itemView.findViewById(R.id.radioGroupRespuestas)
        private val botonEditar: Button = itemView.findViewById(R.id.botonEditar)
        private val botonBorrar: Button = itemView.findViewById(R.id.botonBorrar)

        init {
            botonEditar.setOnClickListener {
                // Cambiar el modo de edición al hacer clic en el botón de editar
                val nuevaPregunta = preguntaTextView.text.toString()
                val nuevasRespuestas = obtenerRespuestasDesdeRadioGroup(opcionesRadioGroup)
                val nuevaPreguntaRespuesta = PreguntaRespuesta(nuevaPregunta, nuevasRespuestas)
                val adapterPosition = bindingAdapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    editarPreguntaRespuestaListener?.onEditarPreguntaRespuesta(
                        nuevaPreguntaRespuesta
                    )
                }
            }

            botonBorrar.setOnClickListener {
                val adapterPosition = bindingAdapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    preguntasRespuestas.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                    eliminarPreguntaFirestore(adapterPosition)
                }
            }
        }

        fun bind(preguntaRespuesta: PreguntaRespuesta) {
            preguntaTextView.text = preguntaRespuesta.pregunta

            opcionesRadioGroup.removeAllViews()

            // Agregar los RadioButtons dinámicamente
            for (respuesta in preguntaRespuesta.respuestas) {
                val radioButton = RadioButton(itemView.context)
                radioButton.text = respuesta
                opcionesRadioGroup.addView(radioButton)
            }
        }
    }

    private fun obtenerRespuestasDesdeRadioGroup(radioGroup: RadioGroup): List<String> {
            val respuestas = mutableListOf<String>()
            for (i in 0 until radioGroup.childCount) {
                val radioButton = radioGroup.getChildAt(i) as? RadioButton
                if (radioButton != null && radioButton.isChecked) {
                    respuestas.add(radioButton.text.toString())
                }
            }
            return respuestas
        }

        fun actualizarPreguntaRespuesta(
            preguntaRespuestaAntigua: PreguntaRespuesta,
            preguntaRespuestaNueva: PreguntaRespuesta
        ) {
            val posicion = preguntasRespuestas.indexOf(preguntaRespuestaAntigua)
            if (posicion != -1) {
                preguntasRespuestas[posicion] = preguntaRespuestaNueva
                notifyItemChanged(posicion)
            }
        }

    private fun eliminarPreguntaFirestore(position: Int) {
        val preguntaRespuesta = preguntasRespuestas[position]
        val preguntaId = preguntaRespuesta.id

        val db = FirebaseFirestore.getInstance()
        db.collection("preguntas").document(preguntaId)
            .delete()
            .addOnSuccessListener {
               // Toast.makeText(context, "Pregunta eliminada correctamente", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
               // Toast.makeText(context, "Error al eliminar la pregunta: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e(TAG, "Error al eliminar la pregunta: ${e.message}")
            }
    }

    }


