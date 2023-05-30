package es.informaticoya.lgpd01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import es.informaticoya.lgpd01.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebase = FirebaseDatabase.getInstance().getReference()

        binding.btnNewUser.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.btnEnter.setOnClickListener {
            if (binding.etEmail.text.isEmpty() && binding.etPassword.text.isEmpty()) {
                Toast.makeText(this, "Rellenar datos", Toast.LENGTH_LONG).show()
            } else {
                loginUser()
            }
        }

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, UsuarioActivity::class.java))
        }
    }


    private fun loginUser() {
        auth.signInWithEmailAndPassword(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        ).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                startActivity(Intent(this, UsuarioActivity::class.java))
                FirebaseAuth.getInstance().currentUser?.uid
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, UsuarioActivity::class.java))
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }



   /* override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user != null) {
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }*/
}






