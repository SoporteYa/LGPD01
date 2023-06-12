package es.informaticoya.lgpd01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import es.informaticoya.lgpd01.databinding.ActivityRegistroUsuariosBinding
import es.informaticoya.lgpd01.FirestoreManager


class RegistroUsuariosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroUsuariosBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    companion object {
        var empleadoId: String? = null
        var procesoId: String? = null
        var sectorId: String? = null
        var empresaId: String? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        //FirebaseAuth.getInstance().currentUser?.email
        FirebaseAuth.getInstance().currentUser?.uid


        binding.btnSave.setOnClickListener {
            registrarEmpleados()
            startActivity(Intent(this, UsuarioActivity::class.java))
        }


        binding.btnSector.setOnClickListener {
            if (binding.checkboxSector.getVisibility() == View.GONE) {
                binding.checkboxSector.setVisibility(View.VISIBLE);
            } else {
                binding.checkboxSector.setVisibility(View.GONE);
            }
        }

        binding.btnSaveSector.setOnClickListener {
            guardarSector()
            startActivity(Intent(this, RegistroUsuariosActivity::class.java))
        }

        binding.btnProcesos.setOnClickListener {
            binding.openET.visibility =
                View.VISIBLE // Mostrar el LinearLayout que contiene el EditText y el botón
            binding.etProceso.visibility = View.VISIBLE // Mostrar el EditText
            binding.btnSaveProceso.visibility = View.VISIBLE
        }

        binding.btnSaveProceso.setOnClickListener {
            guardarProceso()
            startActivity(Intent(this, RegistroUsuariosActivity::class.java))
        }
    }


    private fun registrarEmpleados() {
        val userId: String? = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { uid ->
            val usuarioDocument = db.collection("usuarios").document(uid)
            val empleadosCollection = usuarioDocument.collection("empleados")

            val empleadosDatos = hashMapOf(
                "name" to binding.etName.text.toString(),
                "lastName" to binding.etLastName.text.toString(),
                "userId" to userId,
                "empresaId" to RegistroEmpresaActivity.empresaId
            )

            usuarioDocument.get()
                .addOnSuccessListener { usuarioSnapshot ->
                    //val empresaId = usuarioSnapshot.getString("empresaId")

                    //if (empresaId != null) {
                    // empleadosDatos["empresaId"] = empresaId

                    empleadosCollection.add(empleadosDatos)
                        .addOnSuccessListener { documentReference ->
                            val empleadoId =
                                documentReference.id // Obtener el ID del empleado recién creado
                            Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()

                            RegistroUsuariosActivity.empleadoId =
                                empleadoId // Asignar el ID del empleado a la variable empleadoId en el companion object
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                "El empleado no pudo ser registrado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }

                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Error al obtener la información del usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }


    /*private fun registrarEmpleados() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val empresaId = RegistroEmpresaActivity.empresaId
        val empleadosCollection = db.collection("empleados")
        val usuariosCollection = FirestoreManager.getUsuariosCollection()
        val empresasCollection = FirestoreManager.getEmpresasCollection()

        val empleadosDatos = hashMapOf(
            "name" to binding.etName.text.toString(),
            "lastName" to binding.etLastName.text.toString(),
            "userId" to userId,
            "empresaId" to empresaId,
            "procesoId" to procesoId,
            "sectorId" to sectorId
        )

        empleadosCollection.add(empleadosDatos)
            .addOnSuccessListener { documentReference ->
                val empleadoId = documentReference.id
                Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
                if (userId != null && empresaId != null) {
                    val usuarioDocument = usuariosCollection.document(userId)
                    usuarioDocument.update("empleadoId", empleadoId)
                        .addOnSuccessListener {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }

                    val empresaDocument = empresasCollection.document(empresaId)
                    empresaDocument.update("empleadoId", empleadoId)
                        .addOnSuccessListener {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }
                }
                RegistroUsuariosActivity.empleadoId =
                    empleadoId
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "El empleado no pudo ser guardado", Toast.LENGTH_SHORT).show()
            }
    }*/

    private fun guardarSector() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val usuarioDocument = db.collection("usuarios").document(userId!!)
        val sectoresCollection = usuarioDocument.collection("sectores")

        // Crea un objeto de datos con las selecciones
        val datosSector = hashMapOf<String, Boolean>()

        binding.checkbox.apply {
            if (isChecked) {
                datosSector["administracion_gestion"] = true
            }
        }
        binding.checkbox2.apply {
            if (isChecked) {
                datosSector["telecomunicaciones"] = true
            }
        }
        binding.checkbox3.apply {
            if (isChecked) {
                datosSector["comercio"] = true
            }
        }
        binding.checkbox4.apply {
            if (isChecked) {
                datosSector["finanzas_seguros"] = true
            }
        }
        binding.checkbox5.apply {
            if (isChecked) {
                datosSector["transporte_logistica"] = true
            }
        }
        binding.checkbox6.apply {
            if (isChecked) {
                datosSector["sanidad"] = true
            }
        }
        binding.checkbox7.apply {
            if (isChecked) {
                datosSector["comunicacion_marketing"] = true
            }
        }

        binding.checkbox8.apply {
            if (isChecked) {
                datosSector["IT_Informatica"] = true
            }
        }


        // Guarda los datos en Firestore
        usuarioDocument.get()
            .addOnSuccessListener { usuarioSnapshot ->
               // val empleadoId = usuarioSnapshot.getString("empleadoId")

               // if (empleadoId != null) {
                    // empleadosDatos["empresaId"] = empresaId

                    sectoresCollection.add(datosSector)
                        .addOnSuccessListener { documentReference ->
                            val sectorId =
                                documentReference.id // Obtener el ID del sector recién creado
                            Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()

                            RegistroUsuariosActivity.sectorId =
                                sectorId // Asignar el ID del sector a la variable sectorId en el companion object
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                "El empleado no pudo ser registrado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

            }

            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error al obtener la información del usuario",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }



    /* private fun guardarSector(){
        val sectoresCollection = db.collection("sectores")
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val empresaId = RegistroEmpresaActivity.empresaId
        val empleadoId = RegistroUsuariosActivity.empleadoId
        val usuariosCollection = FirestoreManager.getUsuariosCollection()
        val empresasCollection = FirestoreManager.getEmpresasCollection()
        val empleadosCollection = FirestoreManager.getEmpleadosCollection()

        // Crea un objeto de datos con las selecciones
        val datosSector = hashMapOf<String, Boolean>()

        binding.checkbox.apply {
            if (isChecked) {
                datosSector["administracion_gestion"] = true
            }
        }
        binding.checkbox2.apply {
            if (isChecked) {
                datosSector["telecomunicaciones"] = true
            }
        }
        binding.checkbox3.apply {
            if (isChecked) {
                datosSector["comercio"] = true
            }
        }
        binding.checkbox4.apply {
            if (isChecked) {
                datosSector["finanzas_seguros"] = true
            }
        }
        binding.checkbox5.apply {
            if (isChecked) {
                datosSector["transporte_logistica"] = true
            }
        }
        binding.checkbox6.apply {
            if (isChecked) {
                datosSector["sanidad"] = true
            }
        }
        binding.checkbox7.apply {
            if (isChecked) {
                datosSector["comunicacion_marketing"] = true
            }
        }

        binding.checkbox8.apply {
            if (isChecked) {
                datosSector["IT_Informatica"] = true
            }
        }

        // Guarda los datos en Firestore
        sectoresCollection.add(datosSector)
            .addOnSuccessListener { documentReference ->
                val sectorId = documentReference.id
                Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
                if (userId != null && empresaId != null && empleadoId != null) {
                    val usuarioDocument = usuariosCollection.document(userId)
                    usuarioDocument.update("datosSector", datosSector)
                        .addOnSuccessListener {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }

                    val empresaDocument = empresasCollection.document(empresaId)
                    empresaDocument.update("datosSector", datosSector)
                        .addOnSuccessListener {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }

                    val empleadoDocument = empleadosCollection.document(empleadoId)
                    empleadoDocument.update("datosSector", datosSector)
                        .addOnSuccessListener {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }

                }
                RegistroUsuariosActivity.sectorId =
                    sectorId
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "El sector no pudo ser guardado", Toast.LENGTH_SHORT).show()
            }

    }*/

    private fun guardarProceso() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val usuarioDocument = db.collection("usuarios").document(userId!!)
        val procesosCollection = usuarioDocument.collection("procesos")

        // Crea un objeto de datos con las selecciones
        val registroProceso = hashMapOf(
            "proceso" to binding.etProceso.text.toString()
        )

        // Guarda los datos en Firestore
        usuarioDocument.get()
            .addOnSuccessListener { usuarioSnapshot ->
                //val sectorId = usuarioSnapshot.getString("sectorId")

                //if (sectorId != null) {
                    // empleadosDatos["empresaId"] = empresaId

                    procesosCollection.add(registroProceso)
                        .addOnSuccessListener { documentReference ->
                            val procesoId =
                                documentReference.id // Obtener el ID del sector recién creado
                            Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()

                            RegistroUsuariosActivity.procesoId =
                                procesoId // Asignar el ID del proceso a la variable procesoId en el companion object
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                "El empleado no pudo ser registrado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
            }

            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error al obtener la información del usuario",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}



    /*private fun guardarProceso(){
        val procesosCollection = db.collection("procesos")
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val empresaId = RegistroEmpresaActivity.empresaId
        val empleadoId = RegistroUsuariosActivity.empleadoId
        val sectorId = SectorActivity.sectorId
        val usuariosCollection = FirestoreManager.getUsuariosCollection()
        val sectoresCollection = FirestoreManager.getSectoresCollection()
        val empresasCollection = FirestoreManager.getEmpresasCollection()
        val empleadosCollection = FirestoreManager.getEmpleadosCollection()

        // Crea un objeto de datos con las selecciones
        val registroProceso = hashMapOf(
            "proceso" to binding.etProceso.text.toString(),
        )

        // Guarda los datos en Firestore
        procesosCollection.add(registroProceso)
            .addOnSuccessListener { documentReference ->
                val procesoId = documentReference.id
                Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
                if (userId != null && empresaId != null && empleadoId != null && sectorId != null) {
                    val usuarioDocument = usuariosCollection.document(userId)
                    usuarioDocument.update("registroProceso", registroProceso)
                        .addOnSuccessListener {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }

                    val empresaDocument = empresasCollection.document(empresaId)
                    empresaDocument.update("registroProceso", registroProceso)
                        .addOnSuccessListener {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }

                    val empleadoDocument = empleadosCollection.document(empleadoId)
                    empleadoDocument.update("registroProceso", registroProceso)
                        .addOnSuccessListener {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }

                    val sectorDocument = sectoresCollection.document(sectorId)
                    sectorDocument.update("registroProceso", registroProceso)
                        .addOnSuccessListener {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                        }
                }
                RegistroUsuariosActivity.procesoId =
                    procesoId
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "El sector no pudo ser guardado", Toast.LENGTH_SHORT).show()
            }

    }
}*/





