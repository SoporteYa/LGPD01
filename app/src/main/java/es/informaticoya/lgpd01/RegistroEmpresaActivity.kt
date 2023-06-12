package es.informaticoya.lgpd01

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.informaticoya.lgpd01.databinding.ActivityRegistroEmpresaBinding
import com.google.firebase.firestore.DocumentReference
import es.informaticoya.lgpd01.databinding.ActivityRegistroUsuariosBinding


class RegistroEmpresaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroEmpresaBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    companion object {
        var empresaId: String? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroEmpresaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        //FirebaseAuth.getInstance().currentUser?.email
        FirebaseAuth.getInstance().currentUser?.uid



        binding.btnSave.setOnClickListener {
            guardarEmpresa()
            startActivity(Intent(this, EmpresasActivity::class.java))
        }

    }


    private fun guardarEmpresa() {
        val userId: String? = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { uid ->
            val usuarioDocument = db.collection("usuarios").document(uid)
            val empresasCollection = usuarioDocument.collection("empresas")

            val empresaDatos = hashMapOf(
                "company" to binding.etCompany.text.toString(),
                "address" to binding.etAddress.text.toString()
            )

            empresasCollection.add(empresaDatos)
                .addOnSuccessListener { documentReference ->
                    val empresaId = documentReference.id // Obtener el ID de la empresa recién creada
                    Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
                    RegistroEmpresaActivity.empresaId = empresaId // Asignar el ID de la empresa a la variable empresaId en el companion object
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "La empresa no pudo ser guardada", Toast.LENGTH_SHORT).show()
                }
        }
    }



   /* private fun guardarEmpresa() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val empresasCollection = db.collection("empresas")


        val empresaDatos = hashMapOf(
            "company" to binding.etCompany.text.toString(),
            "address" to binding.etAddress.text.toString(),
            "userId" to userId
        )
        empresasCollection.add(empresaDatos)
            .addOnSuccessListener { documentReference ->
                val empresaId = documentReference.id // Obtener el ID de la empresa recién creada
                Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
                if (userId != null) {
                    val usuarioDocument = db.collection("usuarios").document(userId)
                    usuarioDocument.update("empresaId", empresaId)
                        .addOnSuccessListener {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }
                }
                RegistroEmpresaActivity.empresaId =
                    empresaId // Asignar el ID de la empresa a la variable empresaId en el companion object
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "La empresa no pudo ser guardada", Toast.LENGTH_SHORT).show()
            }
    }*/
}




