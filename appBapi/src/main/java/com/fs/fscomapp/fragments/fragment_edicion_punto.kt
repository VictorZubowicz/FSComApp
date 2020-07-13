package com.fs.fscomapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.fs.fscomapp.MainActivity

import com.fs.fscomapp.R
import com.fs.fscomapp.auxiliares.tipoDatoClass
import com.fs.fscomapp.dataBase.puntoComDatabase
import com.fs.fscomapp.dataBase.puntoComDao
import com.fs.fscomapp.entities.PuntoCom


var listTipoDato = listOf(
    tipoDatoClass.ConstTipoDato.typeDigitalInput,
    tipoDatoClass.ConstTipoDato.typeDigitalInputPower,      // = "DI PWR"
    tipoDatoClass.ConstTipoDato.typeDigitalOuput,      // = "DO"
    tipoDatoClass.ConstTipoDato.typeDigitalOuputCmp,      // = "DO CMP"
    tipoDatoClass.ConstTipoDato.typeDigitalOuputFan,      // = "DO FAN"
    tipoDatoClass.ConstTipoDato.typeAnalogInput,      //= "AI"
    tipoDatoClass.ConstTipoDato.typeAnalogOutput,      // = "AO"
    tipoDatoClass.ConstTipoDato.typeSetpoint,      // = "SP"
    tipoDatoClass.ConstTipoDato.typeDecimal,      // = "DEC"
    tipoDatoClass.ConstTipoDato.typeDecimalx10,      // = "Dx10"
    tipoDatoClass.ConstTipoDato.typeHexagesimal,      // = "HEX"
    tipoDatoClass.ConstTipoDato.typeTimeHour,      // ="HRS"     // para paquetes que formato time
    tipoDatoClass.ConstTipoDato.typeTimeMin,      // = "MIN"     // para paquetes que formato time
    tipoDatoClass.ConstTipoDato.typeTimeSec,      // = "SEC"     // para paquetes que formato time
    tipoDatoClass.ConstTipoDato.typeDate,      // = "DTE"
    tipoDatoClass.ConstTipoDato.typeMonth,      // = "MTH"
    tipoDatoClass.ConstTipoDato.typeYear,      // = "YAR"
    tipoDatoClass.ConstTipoDato.typeDay,      // = "DAY"
    tipoDatoClass.ConstTipoDato.typePhone,      // Telefonos"
    tipoDatoClass.ConstTipoDato.typeMail      // Telefonos"
)


class fragment_edicion_punto : Fragment() {

        lateinit var v : View

    lateinit var edNombre: EditText
    lateinit var edDescrip: EditText
    lateinit var edUnidad: EditText
    lateinit var edGrupo: EditText
    lateinit var edTipoDato: EditText
    lateinit var edFormato: EditText
    lateinit var edDirmem: EditText
    lateinit var edValorIni: EditText


    lateinit var butAcept: Button
    lateinit var butCancel: Button
    lateinit var viewModel1: fragment_puntos_vm
    private var puntoComDao: puntoComDao? = null

    private var db: puntoComDatabase? = null
    private var daoPuntosCom: puntoComDao? = null

    private lateinit var spinTipoDato: Spinner

    private lateinit var vM_puntos: fragment_puntos_vm  //tomo una instancia de VM puntos
    var list_puntos : MutableList<PuntoCom> = ArrayList<PuntoCom>()

    var auxPuntoCom : PuntoCom =PuntoCom (-1,"Nombre","descripcion","",0,1,"DI",0, "" )

    override fun onStart() {
        super.onStart()

        db = puntoComDatabase.getAppDatabasePuntosCom(v.context)
        daoPuntosCom = db?.puntosDao()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_edicion_punto, container, false)

        return v

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // IMPORTANTE INCREIBLE PARA QUE NO SUBA LA PANTALLA AL EDITAR HAY Q PONER ESTO
        (requireActivity() as MainActivity).supportActionBar!!.hide()

        edNombre= v.findViewById(R.id.edTxt_edit_nombre)
        edDescrip =v.findViewById(R.id.edTxt_edit_descrip)
        edUnidad= v.findViewById(R.id.edTxt_edit_unidad)
        edGrupo= v.findViewById(R.id.edTxt_edit_grupo)
        edTipoDato =v.findViewById(R.id.edTxt_edit_tipoDato)
        edFormato =v.findViewById(R.id.edTxt_edit_formato)
        edDirmem= v.findViewById(R.id.edTxt_edit_dirmem)
        edValorIni= v.findViewById(R.id.edTxt_edit_valor_ini)


        butAcept =v.findViewById(R.id.but_edit_accept)
        butCancel =v.findViewById(R.id.but_edit_cancel)



        //Tomo ViewModel (Memoria compartida)
        vM_puntos = ViewModelProvider(requireActivity()).get(fragment_puntos_vm::class.java)

        if (!vM_puntos.flag_NewItem==true) {
            auxPuntoCom = vM_puntos.getPunto(vM_puntos.vmindexPunto)    //Tomo VM data
        }
        else {
            auxPuntoCom.dirmem = vM_puntos.sizeList
            auxPuntoCom.id = vM_puntos.sizeList
        }
        // a traves de vM_puntos.auxPuntoCom. tomo los valores


        edNombre.setText(auxPuntoCom.nombre)
        edDescrip.setText(auxPuntoCom.descripcion)
        edValorIni.setText(auxPuntoCom.valorIni.toString())
        edGrupo.setText(auxPuntoCom.grupo.toString())
        edTipoDato.setText(auxPuntoCom.tipoDato)  // 1=Digital
        edFormato.setText(auxPuntoCom.formato)
        edDirmem.setText(auxPuntoCom.dirmem.toString())
        edUnidad.setText(auxPuntoCom.unidad)

        butAcept.setOnClickListener {

            auxPuntoCom= PuntoCom(auxPuntoCom.id, edNombre.text.toString(),
                    edDescrip.text.toString(),
                    edUnidad.text.toString(),
                    Integer.parseInt("0" + edValorIni.text.toString()),
                    Integer.parseInt("0" + edGrupo.text.toString()),
                    edTipoDato.text.toString(), //Llamar a tipodato funcion conversion vmTipoDato: tipoDatoClass.ConstTipoDato
                    Integer.parseInt("0" + edDirmem.text.toString()),
                    edFormato.text.toString()
                )

            if (vM_puntos.flag_NewItem==true) {
                daoPuntosCom?.insertPuntoCom(auxPuntoCom)

                vM_puntos.addItemListPuntos(auxPuntoCom)

                val directions = fragment_edicion_puntoDirections.actionFragmentEdicionPuntoToFragmentPuntos()
                v.findNavController().navigate(directions)

            }
            else {
                daoPuntosCom?.updatePuntoCom(auxPuntoCom)

                list_puntos= vM_puntos.getListPuntos()!!
                list_puntos[vM_puntos.vmindexPunto]= auxPuntoCom
                vM_puntos.setListPuntos(list_puntos)

                val directions = fragment_edicion_puntoDirections.actionFragmentEdicionPuntoToFragmentPuntos()
                v.findNavController().navigate(directions)
            }

        }

        // SPINNER -------------------------------------------------------------------------------
        spinTipoDato= v.findViewById(R.id.spin_tipo_dato)
        val aa = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, listTipoDato)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinTipoDato.setAdapter(aa)
        if (edTipoDato.text!=null)  aa.getPosition(edTipoDato.text.toString())
        spinTipoDato.setSelection(aa.getPosition(edTipoDato.text.toString()), false) // evita la primer falsa entrada
        spinTipoDato.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                edTipoDato.setText(listTipoDato[position])             }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })
        // ----------------------------------------------------------------------------------------


        butCancel.setOnClickListener {

            vM_puntos.flag_NewItem=false    //reset x selec cancel
            val directions = fragment_edicion_puntoDirections.actionFragmentEdicionPuntoToFragmentPuntos()
            v.findNavController().navigate(directions)
            // verifico que el nombre no este repetido

        }
    }

}



