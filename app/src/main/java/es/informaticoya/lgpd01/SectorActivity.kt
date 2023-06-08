package es.informaticoya.lgpd01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import es.informaticoya.lgpd01.databinding.ActivitySectorBinding

class SectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySectorBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        //FirebaseAuth.getInstance().currentUser?.email
        FirebaseAuth.getInstance().currentUser?.uid


        binding.btnSave.setOnClickListener {
            seleccionarSector()
            startActivity(Intent(this, UsuarioActivity::class.java))
        }

    }

    private fun seleccionarSector() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val empresaId = RegistroEmpresaActivity.empresaId
        val sectorCollection = db.collection("sectores")

            val checkbox1 = binding.checkbox
            val checkbox2 = binding.checkbox2
            val checkbox3 = binding.checkbox3
            val checkbox4 = binding.checkbox4
            val checkbox5 = binding.checkbox5
            val checkbox6 = binding.checkbox6
            val checkbox7 = binding.checkbox7

            // Crea un objeto de datos con las selecciones
        val datosSector = hashMapOf<String, String>()

            if (checkbox1.isChecked) {
                datosSector["administracion_gestion"]
            }
            if (checkbox2.isChecked) {
                datosSector["telecomunicaciones"]
            }
            if (checkbox3.isChecked) {
                datosSector["comercio"]
            }
            if (checkbox4.isChecked) {
                datosSector["finanzas_seguros"]
            }
            if (checkbox5.isChecked) {
                datosSector["transporte_logistica"]
            }
            if (checkbox6.isChecked) {
                datosSector["sanidad"]
            }
            if (checkbox7.isChecked) {
                datosSector["comunicacion_marketing"]
            }

            // Guarda los datos en Firestore
            sectorCollection.add(datosSector)
                .addOnSuccessListener { documentReference ->
                    val sectorId = documentReference.id
                    Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
                    if (userId != null && empresaId != null) {
                        val usuarioDocument = db.collection("usuarios").document(userId)
                        usuarioDocument.update("sectorId", sectorId)
                            .addOnSuccessListener {
                                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "El sector no pudo ser guardado", Toast.LENGTH_SHORT).show()
                }
        }

    }