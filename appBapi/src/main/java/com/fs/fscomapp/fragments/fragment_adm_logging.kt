package com.fs.fscomapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fs.fscomapp.MainActivity
import com.fs.fscomapp.R
import com.fs.fscomapp.dataBase.userDatabase
import com.fs.fscomapp.dataBase.userDao
import com.fs.fscomapp.entities.UserLogClass
import com.wajahatkarim3.roomexplorer.RoomExplorer

class fragment_adm_logging : Fragment() {

    lateinit var v : View

    private var db: userDatabase? = null
    private var userDao: userDao? = null

    lateinit var edtName : EditText
    lateinit var edtTel : EditText
    lateinit var edtPass : EditText
    lateinit var edtPrior : EditText

    lateinit var btnAdd : Button
    lateinit var btnDelete : Button
    lateinit var btnEdit : Button
    lateinit var btnSearch : Button
    lateinit var btnDebug : Button
    lateinit var  userLogList :MutableList<UserLogClass>

    var indexUser : Int =0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as MainActivity).supportActionBar!!.hide()

        v = inflater.inflate(R.layout.fragment_adm_logging, container, false)

        edtName = v.findViewById(R.id.edt_name)
        edtPass = v.findViewById(R.id.edt_password)
        edtTel = v.findViewById(R.id.edt_telefono)
        edtPrior = v.findViewById(R.id.edt_prior)

        btnAdd = v.findViewById(R.id.btn_add)
        btnDelete = v.findViewById(R.id.btn_delete)
        btnEdit = v.findViewById(R.id.btn_editar)
        btnSearch = v.findViewById(R.id.btn_search)
        btnDebug = v.findViewById(R.id.btn_debug)

        return v
    }


    override fun onStart() {
        super.onStart()

        db = userDatabase.getAppDataBase(v.context)
        userDao = db?.userDao()
        userLogList = userDao?.loadAllUser() as MutableList<UserLogClass>

        btnAdd.setOnClickListener {

            // verifico que el nombre no este repetido
            for ( indexList in userLogList) {
                if (indexList.name == edtName.text.toString()) {
                    Log.d("AdminLogNameExist", indexList.name)
                    break
                }
            }
            indexUser=CrearNuevoIndexUser()
            userDao?.insertPerson(
                UserLogClass(indexUser,edtName.text.toString(),edtTel.text.toString(),
                    Integer.parseInt(edtPrior.text.toString()), edtPass.text.toString()))
        }

        btnDelete.setOnClickListener {
            userDao?.delete(UserLogClass(indexUser, "", "", 0,""))

            // Ajusto valor index para ordenar creciente a causa del eliminado
            for ((index) in userLogList.withIndex()) {
                userLogList[index].id = index
                indexUser = index
            }
        }

        btnSearch.setOnClickListener {

            // Busqueda por nombre
            var fg_forOk:Boolean = false

            for ( indexList in userLogList) {
                if (indexList.name == edtName.text.toString()) {
                    indexUser = indexList.id;
                    fg_forOk=true
                }
            }
            if (fg_forOk) {
                MuestraDatosUserByIndex(indexUser)
                btnEdit.visibility = Button.VISIBLE
                btnDelete.visibility = Button.VISIBLE
            }
            else {
                btnEdit.visibility = Button.INVISIBLE
                btnDelete.visibility = Button.INVISIBLE
                Toast.makeText(context,
                    "No se encuentra Ningun Usuario con es nombre",
                    Toast.LENGTH_SHORT ).show()
            }
            //Log.d("Test", userDao?.loadPersonById(indexUser)?.name.toString())
        }

        btnEdit.setOnClickListener {
            userDao?.updatePerson(UserLogClass( indexUser , edtName.text.toString(),
                edtTel.text.toString(), Integer.parseInt(edtPrior.text.toString()),
                edtPass.text.toString() ))
        }


        btnDebug.setOnClickListener {
            RoomExplorer.show(context, userDatabase::class.java, "myDBlog")


        }
    }

    private fun MuestraDatosUserByIndex(index: Any) {
        edtName.setText(userLogList[indexUser].name)
        edtTel.setText(userLogList[indexUser].tel.toString())
        edtPrior.setText(userLogList[indexUser].prior.toString())
        edtPass.setText(userLogList[indexUser].pass)
    }

    private fun CrearNuevoIndexUser(): Int {
        return userLogList.size
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
