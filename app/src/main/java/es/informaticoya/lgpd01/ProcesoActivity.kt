package es.informaticoya.lgpd01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.informaticoya.lgpd01.databinding.ActivityProcesoBinding

class ProcesoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProcesoBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProcesoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        //FirebaseAuth.getInstance().currentUser?.email
        FirebaseAuth.getInstance().currentUser?.uid

        binding.btnSave.setOnClickListener {
            registrarProceso()
            startActivity(Intent(this, UsuarioActivity::class.java))
        }
    }



    private fun registrarProceso() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val procesoCollection = db.collection("sectores")

        val registroProceso = hashMapOf(
            "proceso" to binding.etProceso.text.toString(),
            "userId" to userId

        )
        procesoCollection.add(registroProceso)
            .addOnSuccessListener {
                Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
                //val companyId = documentReference.id

                //startActivity(Intent(this, UsuarioActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
    }
}
