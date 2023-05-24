package es.informaticoya.lgpd01

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
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
            if (binding.etEmail.text.isEmpty() && binding.etPassword.text.isEmpty()){
                Toast.makeText(this, "Rellenar datos", Toast.LENGTH_LONG).show()
            }else{
                authentication()
            }
        }
    }

    private fun authentication() {
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
    }


