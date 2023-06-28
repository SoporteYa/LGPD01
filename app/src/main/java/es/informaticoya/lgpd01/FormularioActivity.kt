package es.informaticoya.lgpd01

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import es.informaticoya.lgpd01.databinding.ActivityFormularioBinding
import org.json.JSONArray
import org.json.JSONObject
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.SetOptions


class FormularioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioBinding
    private lateinit var metodoRespuestaSpinner: Spinner
    private lateinit var layoutRespuestas: LinearLayout
    private val preguntasRespuestas = mutableListOf<PreguntaRespuesta>()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: PreguntasRespuestasMyAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        metodoRespuestaSpinner = findViewById(R.id.spinnerMetodoRespuesta)
        layoutRespuestas = findViewById(R.id.layoutRespuestas)
        recyclerView = findViewById(R.id.recyclerView)
        adapter = PreguntasRespuestasMyAdapter(preguntasRespuestas)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configurar el Spinner
        val opcionesSpinner = arrayOf("Respuesta de texto", "Respuesta de opciones")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesSpinner)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        metodoRespuestaSpinner.adapter = spinnerAdapter


        binding.btnAgregarRespuesta.setOnClickListener { view ->
            agregarRespuesta(view)
        }

        binding.btnEliminarRespuesta.setOnClickListener { view ->
            eliminarRespuesta(view)
        }

        binding.btnGuardarPregunta.setOnClickListener { view ->
            guardarPregunta(view)
        }

        binding.btnMostrarPreguntas.setOnClickListener {
            mostrarRespuestas()
        }

        binding.btnIniciarPreguntas.setOnClickListener { view ->
            iniciarPreguntas(view)
        }
    }


    fun agregarRespuesta(view: View) {
        val preguntaEditText = findViewById<EditText>(R.id.editTextPregunta)
        val pregunta = preguntaEditText.text.toString()

        val respuestaLayout = when (metodoRespuestaSpinner.selectedItemPosition) {
            0 -> createEditTextLayout()
            1 -> createRadioGroupLayout()
            else -> null
        }

        respuestaLayout?.let {
            layoutRespuestas.addView(it)
            preguntasRespuestas.add(PreguntaRespuesta(pregunta, getRespuestas(it)))
        }
    }

    fun eliminarRespuesta(view: View) {
        if (layoutRespuestas.childCount > 0) {
            layoutRespuestas.removeViewAt(layoutRespuestas.childCount - 1)
            preguntasRespuestas.removeAt(preguntasRespuestas.size - 1)
        }
    }

    private fun createEditTextLayout(): View {
        val respuestaEditText = EditText(this)
        respuestaEditText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        return respuestaEditText
    }

    private fun createRadioGroupLayout(): View {
        val radioGroup = RadioGroup(this)
        radioGroup.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val addButton = Button(this)
        addButton.text = "Agregar opción"
        addButton.setOnClickListener {
            val radioButtonLayout = LinearLayout(this)
            radioButtonLayout.orientation = LinearLayout.HORIZONTAL

            val radioButton = RadioButton(this)
            radioButton.text = ""

            val respuestaEditText = EditText(this)
            respuestaEditText.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            radioButtonLayout.addView(radioButton)
            radioButtonLayout.addView(respuestaEditText)

            radioGroup.addView(radioButtonLayout)
        }

        val removeButton = Button(this)
        removeButton.text = "Eliminar opción"
        removeButton.setOnClickListener {
            if (radioGroup.childCount > 0) {
                radioGroup.removeViewAt(radioGroup.childCount - 1)
            }
        }

        val buttonLayout = LinearLayout(this)
        buttonLayout.addView(addButton)
        buttonLayout.addView(removeButton)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(radioGroup)
        layout.addView(buttonLayout)

        return layout
    }


    private fun getRespuestas(view: View): List<String> {
        val respuestas = mutableListOf<String>()

        when (view) {
            is EditText -> respuestas.add(view.text.toString())
            is RadioGroup -> {
                for (i in 0 until view.childCount) {
                    val radioButton = view.getChildAt(i) as? RadioButton
                    radioButton?.let {
                        respuestas.add(it.text.toString())
                    }
                }
            }
        }

        return respuestas
    }

    fun guardarPregunta(view: View) {
        val preguntaEditText = findViewById<EditText>(R.id.editTextPregunta)
        val pregunta = preguntaEditText.text.toString()


        // Guardar la pregunta y respuestas en Firestore
        val preguntaRespuesta = PreguntaRespuesta(pregunta, preguntasRespuestas.last().respuestas)
        db.collection("preguntas").add(preguntaRespuesta)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Pregunta guardada en Firestore", Toast.LENGTH_SHORT).show()

                preguntaEditText.text.clear()
                layoutRespuestas.removeAllViews()
                preguntasRespuestas.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error al guardar la pregunta: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun obtenerRespuestas(): List<String> {
        val respuestas = mutableListOf<String>()

        val respuestasCount = layoutRespuestas.childCount
        for (i in 0 until respuestasCount) {
            val respuestaView = layoutRespuestas.getChildAt(i)

            when (respuestaView) {
                is EditText -> {
                    val respuesta = respuestaView.text.toString().trim()
                    if (respuesta.isNotEmpty()) {
                        respuestas.add(respuesta)
                    }
                }
                is RadioGroup -> {
                    val radioButtonId = respuestaView.checkedRadioButtonId
                    if (radioButtonId != -1) {
                        val radioButton = respuestaView.findViewById<RadioButton>(radioButtonId)
                        val respuesta = radioButton.text.toString().trim()
                        if (respuesta.isNotEmpty()) {
                            respuestas.add(respuesta)
                        }
                    }
                }
            }
        }

        return respuestas
    }

    private fun mostrarRespuestas() {
        db.collection("preguntas")
            .get()
            .addOnSuccessListener { result ->
                val layout = LinearLayout(this)
                layout.orientation = LinearLayout.VERTICAL

                for (document in result) {
                    val pregunta = document.getString("pregunta")
                    val respuestas = document.get("respuestas") as? List<HashMap<String, Any>>?

                    pregunta?.let {
                        val preguntaTextView = TextView(this)
                        preguntaTextView.text = "Pregunta: $pregunta"
                        layout.addView(preguntaTextView)
                    }

                    respuestas?.let {
                        val radioGroup = RadioGroup(this)
                        for (respuestaMap in respuestas) {
                            val respuesta = respuestaMap["texto"] as? String
                            val radioButton = RadioButton(this)
                            respuesta?.let {
                                radioButton.text = respuesta
                                radioGroup.addView(radioButton)
                            }
                        }
                        layout.addView(radioGroup)
                    }

                    val separador = View(this)
                    separador.setBackgroundColor(Color.parseColor("#CCCCCC"))
                    separador.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2
                    )
                    layout.addView(separador)
                }

                if (layout.childCount > 0) {
                    val scrollView = ScrollView(this)
                    scrollView.addView(layout)

                    AlertDialog.Builder(this)
                        .setTitle("Preguntas y Respuestas")
                        .setView(scrollView)
                        .setPositiveButton("Cerrar", null)
                        .show()
                } else {
                    Toast.makeText(this, "No hay preguntas para mostrar", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error al cargar las preguntas: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun iniciarPreguntas(view: View) {
        val intent = Intent(this, MostrarPreguntasActivity::class.java)
        intent.putExtra("preguntasRespuestas", preguntasRespuestas.toTypedArray())
        startActivity(intent)
    }

}



   /* private fun mostrarFormulario() {
        layoutRespuestas.removeAllViews()

        for (preguntaRespuesta in preguntasRespuestas) {
            val preguntaTextView = TextView(this)
            preguntaTextView.text = preguntaRespuesta.pregunta
            layoutRespuestas.addView(preguntaTextView)

            val radioGroup = RadioGroup(this)
            radioGroup.orientation = RadioGroup.VERTICAL

            for (respuesta in preguntaRespuesta.respuestas) {
                val radioButton = RadioButton(this)
                radioButton.text = respuesta

                radioGroup.addView(radioButton)
            }

            layoutRespuestas.addView(radioGroup)
        }
    }
}*/





/* private fun cargarFormularios() {
     db.collection("formularios")
         .get()
         .addOnSuccessListener { result ->
             preguntasRespuestas.clear()
             for (document in result) {
                 val pregunta = document.getString("pregunta") ?: ""
                 val respuestasArray = JSONArray(document.getString("respuestas"))
                 val respuestas = mutableListOf<String>()
                 val campos = mutableListOf<String>()
                 for (i in 0 until respuestasArray.length()) {
                     val respuestaObject = respuestasArray.getJSONObject(i)
                     val tipo = respuestaObject.getString("tipo") ?: ""

                     val opcionesArray = respuestaObject.getJSONArray("opciones")
                     for (j in 0 until opcionesArray.length()) {
                         respuestas.add(opcionesArray.getString(j) ?: "")
                     }

                     val camposArray = respuestaObject.getJSONArray("campos")
                     for (j in 0 until camposArray.length()) {
                         campos.add(camposArray.getString(j) ?: "")
                     }
                 }
                 preguntasRespuestas.add(PreguntaRespuesta(pregunta, respuestas, campos))
             }

             // Notificar al adaptador que los datos han cambiado
             adapter.notifyDataSetChanged()
         }
         .addOnFailureListener { e ->
             Toast.makeText(
                 this,
                 "Error al cargar los formularios: ${e.message}",
                 Toast.LENGTH_SHORT
             ).show()
         }
 }
}*/



/*private fun guardarPreguntaEnFirestore(pregunta: String, respuestas: List<String>) {
    val respuestasData = respuestas.mapIndexed { index, respuesta ->
        hashMapOf(
            "id" to index,
            "respuesta" to respuesta
        )
    }

    val preguntaData = hashMapOf(
        "pregunta" to pregunta,
        "respuestas" to respuestasData
    )

    db.collection("preguntas")
        .add(preguntaData)
        .addOnSuccessListener { documentReference ->
            val preguntaId = documentReference.id
            Toast.makeText(this, "Pregunta guardada en Firestore", Toast.LENGTH_SHORT).show()

            val textViewPregunta = findViewById<TextView>(R.id.textViewPregunta)
            val radioGroupRespuestas = findViewById<RadioGroup>(R.id.radioGroupRespuestas)

            textViewPregunta.text = pregunta

            for (respuestaData in respuestasData) {
                val radioButton = RadioButton(this)
                radioButton.id = respuestaData["id"] as Int
                radioButton.text = respuestaData["respuesta"] as String
                radioGroupRespuestas.addView(radioButton)
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(this, "Error al guardar la pregunta: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}*/


    /*private fun mostrarRespuestas() {
        db.collection("preguntas")
            .get()
            .addOnSuccessListener { result ->
                val layout = LinearLayout(this)
                layout.orientation = LinearLayout.VERTICAL

                for (document in result) {
                    val pregunta = document.getString("pregunta")
                    val respuestas = document.get("respuestas") as? List<HashMap<String, Any>>?

                    pregunta?.let {
                        val preguntaTextView = TextView(this)
                        preguntaTextView.text = "Pregunta: $pregunta"
                        layout.addView(preguntaTextView)
                    }

                    respuestas?.let {
                        val radioGroup = RadioGroup(this)
                        for (respuestaMap in respuestas) {
                            val respuesta = respuestaMap["texto"] as? String
                            val radioButton = RadioButton(this)
                            respuesta?.let {
                                radioButton.text = respuesta
                                radioGroup.addView(radioButton)
                            }
                        }
                        layout.addView(radioGroup)
                    }

                    val separador = View(this)
                    separador.setBackgroundColor(Color.parseColor("#CCCCCC"))
                    separador.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2
                    )
                    layout.addView(separador)
                }

                if (layout.childCount > 0) {
                    val scrollView = ScrollView(this)
                    scrollView.addView(layout)

                    AlertDialog.Builder(this)
                        .setTitle("Preguntas y Respuestas")
                        .setView(scrollView)
                        .setPositiveButton("Cerrar", null)
                        .show()
                } else {
                    Toast.makeText(this, "No hay preguntas para mostrar", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error al cargar las preguntas: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}*/

/* private fun mostrarFormulario() {
     layoutRespuestas.removeAllViews()

     for (preguntaRespuesta in preguntasRespuestas) {
         val preguntaTextView = TextView(this)
         preguntaTextView.text = preguntaRespuesta.pregunta
         layoutRespuestas.addView(preguntaTextView)

         val radioGroup = RadioGroup(this)
         radioGroup.orientation = RadioGroup.VERTICAL

         for (respuesta in preguntaRespuesta.respuestas) {
             val radioButton = RadioButton(this)
             radioButton.text = respuesta

             radioGroup.addView(radioButton)
         }

         layoutRespuestas.addView(radioGroup)
     }
 }
}*/

















