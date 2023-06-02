package es.informaticoya.lgpd01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.informaticoya.lgpd01.databinding.ActivityRegistroUsuariosBinding


class RegistroUsuariosActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegistroUsuariosBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        //FirebaseAuth.getInstance().currentUser?.email
        FirebaseAuth.getInstance().currentUser?.uid


        binding.btnSave.setOnClickListener {
            registrarUsuario()
            startActivity(Intent(this, UsuarioActivity::class.java))
        }
    }


    private fun registrarUsuario() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val usuariosDatos = hashMapOf(
            "name" to binding.etName.text.toString(),
            "lastName" to binding.etLastName.text.toString(),
            "company" to binding.etCompany.text.toString(),
            "sector" to binding.etSector.text.toString(),
            "userId" to userId
        )
        db.collection("usuarios").document()
            .set(usuariosDatos)
            .addOnSuccessListener {
                Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
                //val companyId = documentReference.id

               // startActivity(Intent(this, UsuarioActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
       }
    }
