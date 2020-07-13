package com.fs.fscomapp.auxiliares

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.fs.fscomapp.entities.PuntoCom
import com.fs.fscomapp.fragments.fragment_puntos_vm

// Access a Cloud Firestore instance from your Activity
//val dbFb = Firebase.firestore
var dbFb = FirebaseFirestore.getInstance()

var list_puntos : MutableList<PuntoCom> = ArrayList<PuntoCom>()

/*  ------------------   FIREBASE   -------------------------------------------------
--------------------------------------------------------------------------------------- */

fun LoadListToFirebase( vM_puntos: fragment_puntos_vm ) {

    list_puntos = vM_puntos.getListLiveData().value!!

    for (auxpuntoCom in list_puntos) {
        dbFb.collection("FsComAppBapi").document(auxpuntoCom.nombre).set(auxpuntoCom)
            .addOnSuccessListener {
                Log.d("Test", "FsComAppBapi/ Load Data:  ${auxpuntoCom} Save Success")
            }
            .addOnFailureListener { exception ->
                Log.d("Test", "Failure FsComAppBapi Load Data:  ${auxpuntoCom} ")
                Log.d("Test", "Load Data failed with ", exception)
            }
    }
}


/*  ------------------   FIREBASE   -------------------------------------------------
--------------------------------------------------------------------------------------- */


fun LoadAllPuntosComFromFirebase( vM_puntos: fragment_puntos_vm) {

    var docRef = dbFb.collection("FsComAppBapi")
    //var docRef = dbFb.collection("baseDatosFsComApp").document("puntosCom")
    docRef.orderBy("id").get()
        .addOnSuccessListener { result ->
            list_puntos.clear()
            for (document in result) {
                Log.d("Test", "DocumentSnapshot data: ${document.data}")
                var i = document.getLong("id")?.toInt() ?: -1
                val nombre = document.getString("nombre") ?: "SinNombre"
                val descripcion = document.getString("descripcion") ?: ""
                val unidad = document.getString("unidad") ?: ""
                var valorIni = document.getLong("valorIni")?.toInt() ?: -1
                var grupo = document.getLong("grupo")?.toInt() ?: -1
                val tipoDato = document.getString("tipoDato") ?: ""
                var dirmem = document.getLong("dirmem")?.toInt() ?: -1

                list_puntos.add(PuntoCom(i ?: 0, nombre, descripcion, unidad, valorIni, grupo, tipoDato,
                    dirmem,""))
            }
            vM_puntos.setListPuntos(list_puntos)
        }

}
