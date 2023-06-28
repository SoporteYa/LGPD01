package es.informaticoya.lgpd01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import es.informaticoya.lgpd01.databinding.ActivityMostrarPreguntasBinding

class MostrarPreguntasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMostrarPreguntasBinding
    private lateinit var preguntasRespuestas: MutableList<PreguntaRespuesta>
    private lateinit var preguntasIterator: Iterator<PreguntaRespuesta>
    private lateinit var preguntaActual: PreguntaRespuesta
    private lateinit var radioGroupRespuestas: RadioGroup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMostrarPreguntasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preguntasRespuestas = mutableListOf()
        preguntasIterator = preguntasRespuestas.iterator()
        radioGroupRespuestas = findViewById(R.id.radioGroupRespuestas)

        cargarPreguntasDesdeFirestore()


       binding.btnSiguiente.setOnClickListener {
            mostrarSiguientePregunta()
        }

    }

    private fun cargarPreguntasDesdeFirestore() {
        val db = FirebaseFirestore.getInstance()
        val preguntasRef = db.collection("preguntas")

        preguntasRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val preguntaRespuesta = document.toObject(PreguntaRespuesta::class.java)
                    preguntasRespuestas.add(preguntaRespuesta)
                }

                preguntasIterator = preguntasRespuestas.iterator()

                // Iniciar la visualización de las preguntas
                mostrarSiguientePregunta()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al cargar las preguntas", Toast.LENGTH_SHORT).show()
            }
    }


    private fun mostrarSiguientePregunta() {
        if (preguntasIterator.hasNext()) {
            preguntaActual = preguntasIterator.next()
            mostrarPregunta(preguntaActual)
        } else {
            Toast.makeText(this, "No hay más preguntas para mostrar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarPregunta(preguntaRespuesta: PreguntaRespuesta) {
        val preguntaTextView = findViewById<TextView>(R.id.preguntaTextView)
        preguntaTextView.text = preguntaRespuesta.pregunta

        radioGroupRespuestas.removeAllViews()

        for (respuesta in preguntaRespuesta.respuestas) {
            val radioButton = RadioButton(this)
            radioButton.text = respuesta

            radioGroupRespuestas.addView(radioButton)
        }
    }

}


