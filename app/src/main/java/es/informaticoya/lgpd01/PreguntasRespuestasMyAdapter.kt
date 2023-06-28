package es.informaticoya.lgpd01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PreguntasRespuestasMyAdapter(private val preguntasRespuestas: List<PreguntaRespuesta>) : RecyclerView.Adapter<PreguntasRespuestasMyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val preguntaTextView: TextView = itemView.findViewById(R.id.preguntaTextView)
        val respuestasLayout: LinearLayout = itemView.findViewById(R.id.respuestasLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pregunta_respuesta, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val preguntaRespuesta = preguntasRespuestas[position]

        holder.preguntaTextView.text = preguntaRespuesta.pregunta

        // Limpiar las opciones anteriores
        holder.respuestasLayout.removeAllViews()

        // Agregar las nuevas opciones
        for (opcion in preguntaRespuesta.pregunta) {
            val opcionTextView = TextView(holder.itemView.context)
            opcionTextView.text = opcion.toString()

            holder.respuestasLayout.addView(opcionTextView)
        }
    }


    override fun getItemCount(): Int {
        return preguntasRespuestas.size
    }
}
