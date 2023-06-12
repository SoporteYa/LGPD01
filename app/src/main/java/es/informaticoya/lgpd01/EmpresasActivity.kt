package es.informaticoya.lgpd01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import es.informaticoya.lgpd01.databinding.ActivityEmpresasBinding


class EmpresasActivity : AppCompatActivity(), MyAdapter.OnProcesoClickListener,
    MyAdapter.OnSectorClickListener, MyAdapter.OnEmpleadoClickListener {

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
        myAdapter.setOnProcesoClickListener(this)
        myAdapter.setOnSectorClickListener(this)
        myAdapter.setOnEmpleadoClickListener(this)
        rv.adapter = myAdapter



        mostrarEmpresas()


    }

    private fun mostrarEmpresas() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            db.collection("usuarios").document(userId)
                .collection("empresas")
                .get()
                .addOnSuccessListener { snapshot ->
                    companyList.clear() // Limpiar la lista antes de agregar nuevos elementos

                    for (document in snapshot.documents) {
                        val empresaId = document.id
                        val company = document.toObject(Company::class.java)
                        company?.let {
                            it.empresaId = empresaId
                            companyList.add(it)
                        }
                    }

                    myAdapter.notifyDataSetChanged()
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

    /*private fun mostrarEmpresas() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            db.collection("empresas")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    companyList.clear() // Limpiar la lista antes de agregar nuevos elementos

                    for (document in snapshot.documents) {
                        val company: Company? = document.toObject(Company::class.java)
                        if (company != null) {
                            companyList.add(company)
                        }
                    }

                    myAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Error al obtener las empresas: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }*/


     fun borrarEmpresas(company: Company) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            db.collection("empresas")
                .document()
                .delete()
                .addOnSuccessListener { snapshot ->
                    companyList.clear() // Limpiar la lista antes de agregar nuevos elementos

                    Toast.makeText(this, "Empresa eliminada correctamente", Toast.LENGTH_SHORT).show()

                    companyList.remove(company)
                    rv.adapter?.notifyDataSetChanged()
                    startActivity(Intent(this, UsuarioActivity::class.java))
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

    override fun onEmpleadoClick(position: Int) {
        Log.d("EmpresasActivity", "onItemClick: $position")
        // Realiza las acciones necesarias cuando se pulsa el btn en el recyclerView
        startActivity(Intent(this, RegistroUsuariosActivity::class.java))
    }

    override fun onProcesoClick(position: Int) {
        Log.d("EmpresasActivity", "onItemClick: $position")
        // Realiza las acciones necesarias cuando se pulsa el btn en el recyclerView
       startActivity(Intent(this, ProcesoActivity::class.java))
    }

    override fun onSectorClick(position: Int) {
        Log.d("EmpresasActivity", "onItemClick: $position")
        // Realiza las acciones necesarias cuando se pulsa el btn en el recyclerView
        startActivity(Intent(this, SectorActivity::class.java))
    }
}
























