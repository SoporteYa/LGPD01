package es.informaticoya.lgpd01


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FormularioActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var preguntaList: ArrayList<Pregunta>
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapterPreguntas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)

        db = FirebaseFirestore.getInstance()
        preguntaList = ArrayList()

        recyclerView = findViewById(R.id.recyclerViewFormulario)
        recyclerView.layoutManager = LinearLayoutManager(this)

        myAdapter = MyAdapterPreguntas(preguntaList, this)
        recyclerView.adapter = myAdapter

        mostrarPreguntasDesdeDB()

        val btnSaveFormulario: Button = findViewById(R.id.btnSaveFormulario)
        btnSaveFormulario.setOnClickListener {
            guardarFormulario()
        }
    }

    private fun mostrarPreguntasDesdeDB() {
        db.collection("preguntas")
            .get()
            .addOnSuccessListener { querySnapshot ->
                preguntaList.clear()

                for (document in querySnapshot) {
                    val pregunta = document.toObject(Pregunta::class.java)
                    preguntaList.add(pregunta)
                }

                myAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Manejar el error de acuerdo a tus necesidades
            }
    }

    private fun guardarFormulario() {

        preguntaList.clear() // Limpiamos la lista actual de preguntas

        // Agregamos las preguntas predefinidas
        preguntaList.add(Pregunta("Pregunta 1", listOf("Opción 1", "Opción 2", "Opción 3", "Opción 4")))
        preguntaList.add(Pregunta("Pregunta 2", listOf("Opción A", "Opción B", "Opción C", "Opción D")))
        preguntaList.add(Pregunta("Pregunta 3", listOf("Opción X", "Opción Y", "Opción Z")))
        preguntaList.add(Pregunta("Pregunta 4", listOf("Opción 1", "Opción 2", "Opción 3")))
        preguntaList.add(Pregunta("Pregunta 5", listOf("Opción A", "Opción B", "Opción C")))


        for (pregunta in preguntaList) {
            db.collection("preguntas")
                .add(pregunta)
                .addOnSuccessListener { documentReference ->
                    // La pregunta se guardó exitosamente en la base de datos
                }
                .addOnFailureListener { e ->
                    // Hubo un error al guardar la pregunta en la base de datos
                    // Maneja el error de acuerdo a tus necesidades
                }
        }
    }
}




