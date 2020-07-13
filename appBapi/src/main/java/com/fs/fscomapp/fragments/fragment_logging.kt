package com.fs.fscomapp.fragments

import androidx.appcompat.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import com.fs.fscomapp.MainActivity
import com.fs.fscomapp.R
import com.fs.fscomapp.dataBase.userDatabase
import com.fs.fscomapp.dataBase.userDao
import com.fs.fscomapp.entities.UserLogClass

class fragment_logging : Fragment() {

    private var db: userDatabase? = null
    private var userDao: userDao? = null
    lateinit var  userList :MutableList<UserLogClass>

    lateinit var v:View
    lateinit var butAcept: Button
    lateinit var butCancel: Button
    lateinit var textName: TextView
    lateinit var textPass: TextView
    lateinit var textPrior: TextView
    lateinit var textValidation: TextView

    var logOk: Boolean = false

    //var usuarios : MutableList<user_log> = ArrayList<user_log>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // IMPORTANTE INCREIBLE PARA QUE NO SUBA LA PANTALLA AL EDITAR HAY Q PONER ESTO
        (requireActivity() as MainActivity).supportActionBar!!.hide()

        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_logging, container, false)
        butAcept=v.findViewById(R.id.but_log_accept)
        butCancel=v.findViewById(R.id.but_log_cancel)
        textName=v.findViewById(R.id.edtxt_log_name)
        textPass=v.findViewById(R.id.edtxt_log_pass)

        textValidation=v.findViewById(R.id.txt_log_msg)

        return v
    }

    override fun onStart() {
        super.onStart()

        db = userDatabase.getAppDataBase(v.context)
        userDao = db?.userDao()

        butAcept.setOnClickListener {

            if (textName.toString() == "")
                textName.setError("No se ingreso Usuario")
            else {
                Log.d(
                    "Logging User Class",
                    textName.text.toString() + " " + textPass.text.toString() + "\n"
                )

                userList = userDao?.loadAllUser() as MutableList<UserLogClass>

                for (userActual in userList) {
                    //Log.d("Test", actualUser.name)
                    if (userActual.name == textName.text.toString()) {
                        if (userActual.pass == textPass.text.toString()) {
                            logOk = true
                            textValidation.setTextColor(Color.GREEN)
                            textValidation.text = "VALIDACION OK"
                            UserGlobal.usuario = textName.text.toString()
                            UserGlobal.userprior = Integer.parseInt(userActual.prior.toString())
                            break
                        }
                    }
                }
                //Si la tabla esta vacia devuelve Ok, par que pueda ingresar a administrar
                if (userList.size==0) {
                    logOk=true
                    //Poner mensaje de alerta
                }

                if (logOk==true) {
                    Log.d("Logging User Ok", textName.text.toString() + "\n")
                    val action = fragment_loggingDirections.actionFragmentLoggingToFragmentMain()
                    v.findNavController().navigate(action)
                    //Toast.makeText(getActivity(), "Usuario Correcto", Toast.LENGTH_SHORT)
                } else {
                    Log.d("Logging User Failed", textName.text.toString() + "\n")
                    textValidation.setTextColor(Color.RED);
                    textValidation.text = "LOGGING INVALIDO"

                    val alertDialog: AlertDialog? = activity?.let {

                        val builder = AlertDialog.Builder(it)
                        builder.apply {
                            setPositiveButton("OK",
                                DialogInterface.OnClickListener { dialog, id ->
                                    // User clicked OK button
                                })
                            setNegativeButton("Salir",
                                DialogInterface.OnClickListener { dialog, id ->
                                    // User cancelled the dialog
                                })
                        }
                        // Set other dialog properties
                        //...

                        // Create the AlertDialog
                        builder.create()
                    }
                }
            }
        }

        butCancel.setOnClickListener {
            val action = fragment_loggingDirections.actionFragmentLoggingToFragmentMain()
            v.findNavController().navigate(action)
        }

    }

}