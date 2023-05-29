package es.informaticoya.lgpd01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import es.informaticoya.lgpd01.databinding.ActivityEmpresasBinding

class EmpresasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmpresasBinding
    private lateinit var recyclerViewEmpresas: RecyclerView
    private lateinit var companiesArrayList: ArrayList<Companies>
    private lateinit var myAdapter: MyAdapter
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmpresasBinding.inflate(layoutInflater)

        setContentView(binding.root)

        recyclerViewEmpresas = findViewById(R.id.recyclerEmpresas)
        recyclerViewEmpresas.layoutManager = LinearLayoutManager(this)
        recyclerViewEmpresas.setHasFixedSize(true)
        companiesArrayList = arrayListOf()

        myAdapter = MyAdapter(companiesArrayList)

        db = FirebaseFirestore.getInstance()


    }


    private fun EventChangeListener(){

        db = FirebaseFirestore.getInstance()
        db.collection("empresas").get().
                addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null ){

                            Log.e("Firestore error", error.message.toString())
                            return

                        }

                        for (dc : DocumentChange in value?.documentChanges!!){

                            if (dc.type == DocumentChange.Type.ADDED){

                                companiesArrayList.add(dc.document.toObject(Companies::class.java))

                            }

                        }

                        myAdapter.notifyDataSetChanged()

                    }

                })

    }

}