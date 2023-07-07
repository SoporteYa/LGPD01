package es.informaticoya.lgpd01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import es.informaticoya.lgpd01.databinding.ActivityFormularioCompletoBinding

class FormularioCompletoActivity : AppCompatActivity(),
    FormularioCompletoAdapter.EditarPreguntaRespuestaListener {

    private lateinit var binding: ActivityFormularioCompletoBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FormularioCompletoAdapter
    private val db = FirebaseFirestore.getInstance()
    private val preguntaRespuestasList = ArrayList<PreguntaRespuesta>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioCompletoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.rvFormulario
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = FormularioCompletoAdapter(mutableListOf())
        recyclerView.adapter = adapter
        adapter.setEditarPreguntaRespuestaListener(this)
        cargarPreguntasDesdeFirestore()
    }

    private fun cargarPreguntasDesdeFirestore() {
        db.collection("preguntas").get()
            .addOnSuccessListener { querySnapshot ->
                val preguntasRespuestas = mutableListOf<PreguntaRespuesta>()
                for (document in querySnapshot) {
                    val pregunta = document.getString("pregunta")
                    val respuestas = document.get("respuestas") as? List<String>
                    if (pregunta != null && respuestas != null) {
                        preguntasRespuestas.add(PreguntaRespuesta(pregunta, respuestas))
                    }
                }
                adapter.actualizarLista(preguntasRespuestas)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error al cargar las preguntas: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onEditarPreguntaRespuesta(preguntaRespuesta: PreguntaRespuesta) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.editar_preguntas_respuestas, null)
        val editTextPregunta = dialogView.findViewById<EditText>(R.id.editTextPregunta)
        editTextPregunta.setText(preguntaRespuesta.pregunta)

        val layoutRespuestas = dialogView.findViewById<LinearLayout>(R.id.layoutRespuestas)
        val botonAgregarRespuesta = dialogView.findViewById<Button>(R.id.botonAgregarRespuesta)

        // Agregar respuestas existentes
        for (respuesta in preguntaRespuesta.respuestas) {
            val respuestaLayout = LayoutInflater.from(this).inflate(R.layout.item_respuesta, null)
            val checkBox = respuestaLayout.findViewById<CheckBox>(R.id.checkBox)
            val editTextRespuesta = respuestaLayout.findViewById<EditText>(R.id.editTextRespuesta)
            editTextRespuesta.setText(respuesta)
            layoutRespuestas.addView(respuestaLayout)
        }

        // Agregar listener al botón "Agregar Respuesta"
        botonAgregarRespuesta.setOnClickListener {
            val respuestaLayout = LayoutInflater.from(this).inflate(R.layout.item_respuesta, null)
            layoutRespuestas.addView(respuestaLayout)
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle("Editar pregunta y respuestas")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevaPregunta = editTextPregunta.text.toString()
                val nuevasRespuestas = mutableListOf<String>()
                for (i in 0 until layoutRespuestas.childCount) {
                    val respuestaLayout = layoutRespuestas.getChildAt(i)
                    val editTextRespuesta =
                        respuestaLayout.findViewById<EditText>(R.id.editTextRespuesta)
                    val respuesta = editTextRespuesta.text.toString()
                    if (respuesta.isNotBlank()) {
                        nuevasRespuestas.add(respuesta)
                    }
                }
                val nuevaPreguntaRespuesta = PreguntaRespuesta(nuevaPregunta, nuevasRespuestas)
                adapter.actualizarPreguntaRespuesta(preguntaRespuesta, nuevaPreguntaRespuesta)
            }
            .setNegativeButton("Cancelar", null)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}

