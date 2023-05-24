package es.informaticoya.lgpd01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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


        binding.topAppBarClose.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.edit -> {
                    clickEdit()
                    true
                }
                R.id.save -> {
                    clickSave()
                    true
                }
                R.id.save -> {
                    clickSave()
                    true
                }
                R.id.more -> {
                    clickMore()
                    true
                }
                R.id.company -> {
                    clickCompany()
                    true
                }
                R.id.sector -> {
                    clickSector()
                    true
                }
                R.id.process -> {
                    clickProcess()
                    true
                }
                R.id.closed -> {
                    clickClosed()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun clickEdit(){

    }

    private fun clickSave(){

    }

    private fun clickMore(){

    }

    private fun clickCompany(){
       startActivity(Intent(this, EmpresaActivity::class.java))
    }

    private fun clickSector(){
        startActivity(Intent(this, SectorActivity::class.java))
    }

    private fun clickProcess(){
        startActivity(Intent(this, ProcesoActivity::class.java))
    }

    private fun clickClosed(){
        startActivity(Intent(this, LogInActivity::class.java))
        Toast.makeText(this, "Has cerrado sesión", Toast.LENGTH_SHORT).show()
    }

}