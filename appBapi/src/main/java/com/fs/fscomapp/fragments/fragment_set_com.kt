package com.fs.fscomapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import app.akexorcist.bluetotohspp.library.BluetoothState
import app.akexorcist.bluetotohspp.library.DeviceList
import com.fs.fscomapp.MainActivity

import com.fs.fscomapp.R
import com.fs.fscomapp.dataBase.puntoComDatabase
import com.fs.fscomapp.dataBase.puntoComDao
import com.fs.fscomappbapi.fragments.ConstIndexList


class fragment_set_com : Fragment() {

    lateinit var v : View

    lateinit var edtxtBridge: EditText

    lateinit var switchCom: Switch
    lateinit var butCerrar: Button

    lateinit var butselectBT: Button
    lateinit var chkRQ: CheckBox
    lateinit var chkWR: CheckBox
    lateinit var chkRP: CheckBox
    lateinit var chkSim: CheckBox

    lateinit var txtv_estado: TextView

    private lateinit var sp_address_tt: Spinner

    private var db: puntoComDatabase? = null
    private var daoPuntosCom: puntoComDao? = null

    private lateinit var vM_puntos: fragment_puntos_vm  //tomo una instancia de VM puntos

    var addressList = listOf("1", "2","3","4","5","6","7","8","9","10","11","12","13","14","15")


    override fun onResume() {
        super.onResume()
        MostrarEstadosCom()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // IMPORTANTE INCREIBLE PARA QUE NO SUBA LA PANTALLA AL EDITAR HAY Q PONER ESTO
        (requireActivity() as MainActivity).supportActionBar!!.hide()

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_set_com, container, false)

        edtxtBridge= v.findViewById(R.id.edtxt_set_com_bridge)
        butCerrar =v.findViewById(R.id.but_set_com_cerrar)
        switchCom=v.findViewById(R.id.switch_set_com_run)


        butselectBT= v.findViewById(R.id.but_set_com_selectBT)
        chkRQ=v.findViewById(R.id.chk_bt_RQ)
        chkWR=v.findViewById(R.id.chk_bt_WR)
        chkRP=v.findViewById(R.id.chk_bt_RP)
        chkSim=v.findViewById(R.id.chk_bt_Sim)


        txtv_estado=v.findViewById(R.id.txtv_set_com_estado)

        //Tomo ViewModel (Memoria compartida)
        vM_puntos = ViewModelProvider(requireActivity()).get(fragment_puntos_vm::class.java)


        butselectBT.setOnClickListener(View.OnClickListener {
        //    if (bt.serviceState == BluetoothState.STATE_CONNECTED) {
        //        bt.disconnect()
        //    } else {
            val intent = Intent(requireActivity(), DeviceList::class.java)
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE)
        })

        chkSim.setOnClickListener(View.OnClickListener {
            vM_puntos.vm_isConnected=chkSim.isChecked
            vM_puntos.vm_isSimulation=chkSim.isChecked

            MostrarEstadosCom()
            val intent = Intent(requireActivity(), DeviceList::class.java)
            startActivityForResult(intent, MainActivity.BluetoothSIMLULATE)
        })


        // SPINNER -------------------------------------------------------------------------------
        sp_address_tt= v.findViewById(R.id.spin_set_com_address)
        val aa = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, addressList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_address_tt.setAdapter(aa)
        if (vM_puntos.vm_addressTT!=null)  aa.getPosition(vM_puntos.vm_addressTT.toString())
        sp_address_tt.setSelection(aa.getPosition(vM_puntos.vm_addressTT.toString()), false) // evita la primer falsa entrada
        sp_address_tt.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                vM_puntos.vm_addressTT/= Integer.parseInt(addressList[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })
        // ----------------------------------------------------------------------------------------

        // SWITCH -------------------------------------------------------------------------------
        switchCom.setOnCheckedChangeListener { buttonView, isChecked ->
            vM_puntos.vm_isConnected = isChecked

            if (vM_puntos.vm_isConnected){
                InicializarPuntosCom()
            }
            }
        // ----------------------------------------------------------------------------------------

        butCerrar.setOnClickListener {
            val action = fragment_set_comDirections.actionFragmentModComToFragmentPuntos()
            v.findNavController().navigate(action)
        }

        return v

    }

    private fun InicializarPuntosCom() {

        vM_puntos.setValorListaPuntos(ConstIndexList.INDEXtEMPERATURA,
            vM_puntos.getPunto(ConstIndexList.INDEXtEMPERATURA).valorIni)

        vM_puntos.setValorListaPuntos(ConstIndexList.INDEXsETpREALARMA,
            vM_puntos.getPunto(ConstIndexList.INDEXsETpREALARMA).valorIni)

        vM_puntos.setValorListaPuntos(ConstIndexList.INDEXsETaLARMA,
            vM_puntos.getPunto(ConstIndexList.INDEXsETaLARMA).valorIni)

        vM_puntos.setValorListaPuntos(ConstIndexList.INDEXdEMORA,
            vM_puntos.getPunto(ConstIndexList.INDEXdEMORA).valorIni)

        vM_puntos.setValorListaPuntos(ConstIndexList.INDEXeSTADO,
            vM_puntos.getPunto(ConstIndexList.INDEXeSTADO).valorIni)

    }

    private fun MostrarEstadosCom() {

        chkSim.isChecked=vM_puntos.vm_isSimulation
        switchCom.isChecked=vM_puntos.vm_isConnected

        if (vM_puntos.vm_isConnected) {
            txtv_estado.setText("OnLine")
            txtv_estado.setBackgroundResource(R.drawable.luz_estado_verde)
        }
        else{
            txtv_estado.setText("Offline")
            txtv_estado.setBackgroundResource(R.drawable.luz_estado_celeste)
        }

    }


}



