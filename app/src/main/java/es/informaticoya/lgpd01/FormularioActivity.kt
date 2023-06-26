package es.informaticoya.lgpd01

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import es.informaticoya.lgpd01.databinding.ActivityFormularioBinding

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
        adapter = PreguntasRespuestasMyAdapter(preguntasRespuestas)
        recyclerView.adapter = adapter

        // Configurar el Spinner
        val opcionesSpinner = arrayOf("Respuesta de texto", "Respuesta de opciones")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesSpinner)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        metodoRespuestaSpinner.adapter = adapter

        binding.btnAgregarRespuesta.setOnClickListener { view ->
            agregarRespuesta(view)
        }

        binding.btnEliminarRespuesta.setOnClickListener { view ->
            eliminarRespuesta(view)
        }

        binding.btnGuardarPregunta.setOnClickListener { view ->
            guardarPregunta(view)
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

        mostrarFormulario()

        // Guardar la pregunta y respuestas en Firestore
        val preguntaRespuesta = PreguntaRespuesta(pregunta, preguntasRespuestas.last().respuestas)
        db.collection("preguntas").add(preguntaRespuesta)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Pregunta guardada en Firestore", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error al guardar la pregunta: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    private fun mostrarFormulario() {
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
}









