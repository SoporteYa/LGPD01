package es.informaticoya.lgpd01

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import es.informaticoya.lgpd01.FirestoreManager



class FirestoreManager {
    private val db = FirebaseFirestore.getInstance()


    companion object {
        fun getUsuariosCollection(): CollectionReference {
            val db = FirebaseFirestore.getInstance()
            return db.collection("usuarios")
        }

        fun getEmpresasCollection(): CollectionReference {
            val db = FirebaseFirestore.getInstance()
            return db.collection("empresas")
        }

        fun getEmpleadosCollection(): CollectionReference {
            val db = FirebaseFirestore.getInstance()
            return db.collection("empleados")
        }

        fun getSectorCollection(): CollectionReference {
            val db = FirebaseFirestore.getInstance()
            return db.collection("sectores")
        }

        fun getProcesoCollection(): CollectionReference {
            val db = FirebaseFirestore.getInstance()
            return db.collection("procesos")
        }

    }
}





