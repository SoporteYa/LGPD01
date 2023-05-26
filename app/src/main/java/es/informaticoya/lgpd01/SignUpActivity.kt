package es.informaticoya.lgpd01

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import es.informaticoya.lgpd01.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        binding.btnSignIn.setOnClickListener {
            if (binding.etEmail.text.isEmpty() && binding.etPassword.text.isEmpty()) {
                Toast.makeText(this, "Rellenar datos", Toast.LENGTH_LONG).show()
            } else {
                authentication()
            }
        }
    }

    private fun authentication() {
        auth.createUserWithEmailAndPassword(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Registrado", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, LogInActivity::class.java))
                } else {
                    val user = auth.currentUser
                    user!!.sendEmailVerification()
                }
            }
    }

        private fun Datos(email: String){
            val datosIntent:Intent = Intent (this, EmpresaActivity::class.java).apply{
                putExtra("email", email)
        }
    }

   /* private fun authentication() {
        auth.createUserWithEmailAndPassword(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    user.sendEmailVerification().addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            Toast.makeText(this, "Correo de verificación enviado", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, LogInActivity::class.java))
                        } else {
                            Toast.makeText(this, "Error al enviar el correo de verificación", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Error al obtener el usuario actual", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Error al crear la cuenta", Toast.LENGTH_LONG).show()
            }
        }
    }*/
}

    /*private fun authentication() {
        auth.createUserWithEmailAndPassword(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString())
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_LONG).show()
                } else {
                    val user = auth.currentUser
                    user!!.sendEmailVerification()
                }
            }
         }
    }*/


