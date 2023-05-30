package es.informaticoya.lgpd01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import es.informaticoya.lgpd01.databinding.ActivityEmpresasBinding

class EmpresasActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var companyList: ArrayList<Company>
    private lateinit var binding: ActivityEmpresasBinding
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmpresasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rv = findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this)

        companyList = arrayListOf()

        db = FirebaseFirestore.getInstance()

        db.collection("empresas").get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    for (data in it.documents){
                        val company: Company? = data.toObject(Company::class.java)
                        if (company != null) {
                            companyList.add(company)
                        }
                    }
                    rv.adapter = MyAdapter(companyList)
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}