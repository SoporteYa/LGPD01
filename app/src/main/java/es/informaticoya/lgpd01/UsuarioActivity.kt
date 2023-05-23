package es.informaticoya.lgpd01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import es.informaticoya.lgpd01.databinding.ActivityUsuarioBinding

class UsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsuarioBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.topAppBarClose.setNavigationOnClickListener {
            auth.signOut()
            finish()
            startActivity(Intent(this, LogInActivity::class.java))
        }

    }
}