package es.informaticoya.lgpd01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import com.google.android.play.core.integrity.i
import es.informaticoya.lgpd01.databinding.ActivityFormularioBinding

class FormularioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioBinding
    private lateinit var metodoRespuestaSpinner: Spinner
    private lateinit var layoutRespuestas: LinearLayout
    private val preguntasRespuestas = mutableListOf<PreguntaRespuesta>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        metodoRespuestaSpinner = findViewById(R.id.spinnerMetodoRespuesta)
        layoutRespuestas = findViewById(R.id.layoutRespuestas)

        binding.btnAgregarRespuesta.setOnClickListener { view ->
            agregarRespuesta(view)
        }

        binding.btnGuardarPregunta.setOnClickListener { view ->
            guardarPregunta(view)
        }
    }

    fun agregarRespuesta(view: View) {
        val preguntaEditText = findViewById<EditText>(R.id.editTextPregunta)
        val pregunta = preguntaEditText.text.toString()

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupRespuestas)

        val radioButton = RadioButton(this)
        radioButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        radioButton.text = ""

        val respuestaEditText = EditText(this)
        respuestaEditText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )


        val radioButtonLayout = LinearLayout(this)
        radioButtonLayout.orientation = LinearLayout.HORIZONTAL
        radioButtonLayout.addView(radioButton)
        radioButtonLayout.addView(respuestaEditText)

        radioGroup.addView(radioButtonLayout)

        preguntasRespuestas.add(PreguntaRespuesta(pregunta, listOf(respuestaEditText.text.toString())))
    }

    fun guardarPregunta(view: View) {
        val preguntaEditText = findViewById<EditText>(R.id.editTextPregunta)
        val pregunta = preguntaEditText.text.toString()

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupRespuestas)

        val respuestas = mutableListOf<String>()

        for (i in 0 until radioGroup.childCount) {
            val radioButton = radioGroup.getChildAt(i) as RadioButton
            val respuesta = radioButton.text.toString()
            respuestas.add(respuesta)
        }

        preguntasRespuestas[preguntasRespuestas.size - 1] = PreguntaRespuesta(pregunta, respuestas)
        mostrarFormulario()
    }


    private fun mostrarFormulario() {
        layoutRespuestas.removeAllViews()

        for (preguntaRespuesta in preguntasRespuestas) {
            val preguntaTextView = TextView(this)
            preguntaTextView.text = preguntaRespuesta.pregunta
            layoutRespuestas.addView(preguntaTextView)

            for (respuesta in preguntaRespuesta.respuestas) {
                val respuestaTextView = TextView(this)
                respuestaTextView.text = respuesta
                layoutRespuestas.addView(respuestaTextView)
            }
        }
    }

    private fun obtenerNumeroRespuestas(): Int {
        // Aquí puedes implementar la lógica para obtener el número de respuestas deseado, por ejemplo, mostrar un cuadro de diálogo para que el usuario ingrese la cantidad de respuestas o utilizar un EditText adicional en la actividad para que el usuario escriba la cantidad.
        // Retorna el número de respuestas ingresado por el usuario.
        return 0
    }
}






