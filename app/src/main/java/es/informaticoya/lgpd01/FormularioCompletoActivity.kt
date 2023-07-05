package es.informaticoya.lgpd01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import es.informaticoya.lgpd01.databinding.ActivityFormularioCompletoBinding

class FormularioCompletoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioCompletoBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FormularioCompletoAdapter
    private val db = FirebaseFirestore.getInstance()
    private val preguntaRespuestasList = ArrayList<PreguntaRespuesta>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioCompletoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.rvFormulario)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = FormularioCompletoAdapter(emptyList()) // Inicialmente vacío
        recyclerView.adapter = adapter

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
}