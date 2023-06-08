package es.informaticoya.lgpd01

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Inicializa Firebase Firestore
        FirebaseApp.initializeApp(this)
    }

}