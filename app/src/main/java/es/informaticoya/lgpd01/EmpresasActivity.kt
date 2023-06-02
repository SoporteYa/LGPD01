package es.informaticoya.lgpd01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import es.informaticoya.lgpd01.databinding.ActivityEmpresasBinding


class EmpresasActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var companyList: ArrayList<Company>
    private lateinit var binding: ActivityEmpresasBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var myAdapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmpresasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rv = findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this)
        companyList = arrayListOf()
        myAdapter = MyAdapter(companyList, this)



        db = FirebaseFirestore.getInstance()


        mostrarEmpresas()


    }

    private fun mostrarEmpresas() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            db.collection("empresas")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    //companyList.clear() // Limpiar la lista antes de agregar nuevos elementos

                    for (document in snapshot.documents) {
                        val company: Company? = document.toObject(Company::class.java)
                        if (company != null) {
                            companyList.add(company)
                        }
                    }

                    rv.adapter = MyAdapter(
                        companyList,
                        this
                    ) // Configurar el adaptador con la lista actualizada
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Error al obtener las empresas: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

     fun borrarEmpresas(company: Company) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            db.collection("empresas")
                .document()
                .delete()
                .addOnSuccessListener { snapshot ->
                    //companyList.clear() // Limpiar la lista antes de agregar nuevos elementos

                    Toast.makeText(this, "Empresa eliminada correctamente", Toast.LENGTH_SHORT).show()

                    companyList.remove(company)
                    rv.adapter?.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Error al obtener las empresas: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}





















