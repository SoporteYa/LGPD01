package es.informaticoya.lgpd01

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.informaticoya.lgpd01.databinding.ActivityEmpresaBinding

class EmpresaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmpresaBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmpresaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        FirebaseAuth.getInstance().currentUser?.email




        binding.btnSave.setOnClickListener {
            insertarEmpresa()
            startActivity(Intent(this, SectorActivity::class.java))
        }


        binding.btnEdit.setOnClickListener {

        }

        binding.btnDelete.setOnClickListener {

        }
    }

    private fun insertarEmpresa() {
        FirebaseAuth.getInstance().currentUser?.email
        val empresaDatos = hashMapOf(
            "company" to binding.etCompany.text.toString(),
            "address" to binding.etAddress.text.toString()
        )
        db.collection("empresas").document()
            .set(empresaDatos)
            .addOnSuccessListener {
                Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
    }
}




/*private fun insertarEmpresa() {
        FirebaseAuth.getInstance().currentUser?.email
        db.collection("empresas").document().set(
            hashMapOf("company" to binding.etCompany.text.toString(), "address" to
                    binding.etAddress.text.toString())).addOnSuccessListener { documentReference ->
            Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
      }
   }*/


