package es.informaticoya.lgpd01


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import es.informaticoya.lgpd01.databinding.ActivityFormularioBinding
import android.widget.LinearLayout

class FormularioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioBinding
    private lateinit var metodoRespuestaSpinner: Spinner
    private lateinit var layoutRespuestas: LinearLayout

    //private val preguntasRespuestas = mutableListOf<PreguntaRespuesta>()
    private val db = FirebaseFirestore.getInstance()
    //private var preguntaGuardada = false
    //private val respuestasMap: MutableMap<String, String> = mutableMapOf()
    //private val respuestasList: MutableList<Pair<String, Boolean>> = mutableListOf()

    private val respuestasList: MutableList<Pair<EditText?, CheckBox?>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        metodoRespuestaSpinner = findViewById(R.id.spinnerMetodoRespuesta)
        layoutRespuestas = findViewById(R.id.layoutRespuestas)

        // Configurar el Spinner
        val opcionesSpinner = arrayOf("Respuesta de texto", "Respuesta de opciones")
        val spinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesSpinner)
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

        binding.btnIniciarPreguntas.setOnClickListener { view ->
            iniciarPreguntas(view)
        }

        binding.btnFormulario.setOnClickListener {
            startActivity(Intent(this, FormularioCompletoActivity::class.java))
        }
    }


    fun agregarRespuesta(view: View) {
        val selectedOption = metodoRespuestaSpinner.selectedItem.toString()

        if (selectedOption == "Respuesta de texto") {
            val respuestaEditText = EditText(this)
            respuestaEditText.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutRespuestas.addView(respuestaEditText)

            respuestasList.add(Pair(respuestaEditText, null))
        } else if (selectedOption == "Respuesta de opciones") {
            val linearLayout = LinearLayout(this)
            linearLayout.orientation = LinearLayout.HORIZONTAL

            val checkBox = CheckBox(this)
            checkBox.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            linearLayout.addView(checkBox)

            val respuestaEditText = EditText(this)
            respuestaEditText.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            linearLayout.addView(respuestaEditText)

            layoutRespuestas.addView(linearLayout)

            respuestasList.add(Pair(respuestaEditText, checkBox))
        }

        // Mostrar u ocultar el EditText de respuesta libre según el tipo de respuesta seleccionada
        val respuestaLibreEditText = findViewById<EditText>(R.id.editTextRespuesta)
        respuestaLibreEditText.visibility =
            if (selectedOption == "Respuesta de texto") View.VISIBLE else View.GONE
    }

// ...

    fun guardarPregunta(view: View) {
        val preguntaEditText = findViewById<EditText>(R.id.editTextPregunta)
        val pregunta = preguntaEditText.text.toString()

        // Obtén las respuestas ingresadas por el usuario
        val respuestas = ArrayList<String>()
        for (respuestaPair in respuestasList) {
            val respuestaEditText = respuestaPair.first
            val respuestaCheckBox = respuestaPair.second

            if (respuestaEditText != null) {
                val respuestaText = respuestaEditText.text.toString()
                respuestas.add("\"$respuestaText\"")
            } else if (respuestaCheckBox != null && respuestaCheckBox.isChecked) {
                respuestas.add("\"\"")
            }
        }

        // Resto del código para guardar la pregunta y respuestas en Firestore
        // .// Guarda la pregunta y respuestas en Firestore
        val preguntaRespuesta = PreguntaRespuesta(pregunta, respuestas)
        db.collection("preguntas").add(preguntaRespuesta)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(
                    this,
                    "Pregunta y respuestas guardadas en Firestore",
                    Toast.LENGTH_SHORT
                ).show()

                preguntaEditText.text.clear()
                layoutRespuestas.removeAllViews()
                respuestasList.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error al guardar la pregunta y respuestas: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        // Limpia la lista de respuestas después de guardar en Firestore
        respuestasList.clear()
    }


    private fun eliminarRespuesta(view: View) {
        val childCount = layoutRespuestas.childCount
        if (childCount > 0) {
            layoutRespuestas.removeViewAt(childCount - 1)
        }
    }

    private fun <E> MutableList<E>.add(element: Pair<EditText, CheckBox>) {
        add(element as E)
    }

    fun iniciarPreguntas(view: View) {
        val intent = Intent(this, MostrarPreguntasActivity::class.java)
        //intent.putExtra("preguntasRespuestas", preguntasRespuestas.toTypedArray())
        startActivity(intent)
    }

}

private fun <E> MutableList<E>.add(element: Pair<EditText?, CheckBox?>) {
    element.first?.let { editText ->
        this.add(editText as E)
    }
    element.second?.let { checkBox ->
        this.add(checkBox as E)
    }
}


/* private fun getRespuestas(view: View): List<String> {
     val respuestas = mutableListOf<String>()

     when (view) {
         is EditText -> {
             val respuesta = view.text.toString().trim()
             if (respuesta.isNotEmpty()) {
                 respuestas.add(respuesta)
             }
         }
         is LinearLayout -> {
             for (i in 0 until view.childCount) {
                 val childView = view.getChildAt(i)
                 if (childView is LinearLayout) {
                     val respuestaEditText = childView.getChildAt(1) as EditText
                     val respuesta = respuestaEditText.text.toString().trim()
                     if (respuesta.isNotEmpty()) {
                         respuestas.add(respuesta)
                     }
                 }
             }
         }
     }

     return respuestas
 }*/

/* private fun createCheckBoxLayout(): View {
     val linearLayout = LinearLayout(this)
     linearLayout.orientation = LinearLayout.VERTICAL

     val addButton = Button(this)
     addButton.text = "Agregar opción"
     addButton.setOnClickListener {
         val checkBoxLayout = LinearLayout(this)
         checkBoxLayout.orientation = LinearLayout.HORIZONTAL

         val checkBox = CheckBox(this)
         checkBox.layoutParams = LinearLayout.LayoutParams(
             LinearLayout.LayoutParams.WRAP_CONTENT,
             LinearLayout.LayoutParams.WRAP_CONTENT
         )
         checkBoxLayout.addView(checkBox)

         val respuestaEditText = EditText(this)
         respuestaEditText.layoutParams = LinearLayout.LayoutParams(
             LinearLayout.LayoutParams.MATCH_PARENT,
             LinearLayout.LayoutParams.WRAP_CONTENT
         )
         checkBoxLayout.addView(respuestaEditText)

         linearLayout.addView(checkBoxLayout)
     }

     layoutRespuestas.addView(addButton)

     return linearLayout
 }


 private fun createEditTextLayout(): View {
     val editText = EditText(this)
     editText.layoutParams = LinearLayout.LayoutParams(
         LinearLayout.LayoutParams.MATCH_PARENT,
         LinearLayout.LayoutParams.WRAP_CONTENT
     )
     editText.hint = "Respuesta"

     return editText
 }
}*/





