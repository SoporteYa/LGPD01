package es.informaticoya.lgpd01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.informaticoya.lgpd01.databinding.ActivityFormularioBinding

class FormularioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    companion object {
        var preguntaId: String? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        FirebaseAuth.getInstance().currentUser?.uid

        FirebaseFirestore.setLoggingEnabled(true)


        val spinner = findViewById<Spinner>(R.id.spinner)
        val lista = resources.getStringArray(R.array.componentes)
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista)
        spinner.adapter = adaptador

        binding.btnGuardarPreguntas.setOnClickListener {
            guardarPreguntas()
        }


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedOption = lista[position]

                when (selectedOption) {
                    "editText" -> {
                        binding.editText.visibility = View.VISIBLE
                        binding.checkbox.visibility = View.GONE
                        binding.checkbox2.visibility = View.GONE
                        binding.checkbox3.visibility = View.GONE
                        binding.radioButton1.visibility = View.GONE
                        binding.radioButton2.visibility = View.GONE
                        binding.radioButton3.visibility = View.GONE
                    }

                    "checkbox" -> {
                        binding.editText.visibility = View.GONE
                        binding.checkbox.visibility = View.VISIBLE
                        binding.checkbox2.visibility = View.VISIBLE
                        binding.checkbox3.visibility = View.VISIBLE
                        binding.radioButton1.visibility = View.GONE
                        binding.radioButton2.visibility = View.GONE
                        binding.radioButton3.visibility = View.GONE
                    }

                    "radioButton" -> {
                        binding.editText.visibility = View.GONE
                        binding.checkbox.visibility = View.GONE
                        binding.checkbox2.visibility = View.GONE
                        binding.checkbox3.visibility = View.GONE
                        binding.radioButton1.visibility = View.VISIBLE
                        binding.radioButton2.visibility = View.VISIBLE
                        binding.radioButton3.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.editText.visibility = View.GONE
                        binding.checkbox.visibility = View.GONE
                        binding.checkbox2.visibility = View.GONE
                        binding.checkbox3.visibility = View.GONE
                        binding.radioButton1.visibility = View.GONE
                        binding.radioButton2.visibility = View.GONE
                        binding.radioButton3.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.editText.visibility = View.GONE
                binding.checkbox.visibility = View.GONE
                binding.checkbox2.visibility = View.GONE
                binding.checkbox3.visibility = View.GONE
                binding.radioButton1.visibility = View.GONE
                binding.radioButton2.visibility = View.GONE
                binding.radioButton3.visibility = View.GONE
            }
        }
    }


    private fun guardarPreguntas() {


        val pregunta = hashMapOf(
            "texto" to binding.etPreguntas.text.toString()
        )


        db.collection("preguntas")
            .add(pregunta)
            .addOnSuccessListener { documentReference ->
                val preguntaId = documentReference.id
                Toast.makeText(this, "Pregunta guardada con ID: $preguntaId", Toast.LENGTH_SHORT)
                    .show()
                FormularioActivity.preguntaId = preguntaId
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al guardar la pregunta", e)
                Toast.makeText(this, "Error al guardar la pregunta", Toast.LENGTH_SHORT).show()
            }
    }
}






        /*spinner.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(this@FormularioActivity, lista[position], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@FormularioActivity, "Seleccionar una opción", Toast.LENGTH_LONG).show()
            }

        }

    }*/
