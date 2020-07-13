package com.fs.fscomapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.fs.fscomapp.MainActivity
import com.fs.fscomapp.R
import com.fs.fscomapp.adapters.PuntosRecyclerAdapter
import com.fs.fscomapp.auxiliares.LoadAllPuntosComFromFirebase
import com.fs.fscomapp.auxiliares.LoadListToFirebase
import com.fs.fscomapp.dataBase.puntoComDatabase
import com.fs.fscomapp.dataBase.puntoComDao
import com.fs.fscomapp.dialogs.Dialog_viewModel
import com.fs.fscomapp.entities.PuntoCom
import com.fs.fscomappbapi.fragments.ConstEstado
import com.fs.fscomappbapi.fragments.ConstIndexList


class fragment_puntos : Fragment() {


    private var db: puntoComDatabase? = null
    private var daoPuntosCom: puntoComDao? = null

    lateinit var v: View
    lateinit var butAdd : FloatingActionButton
    lateinit var butDel : FloatingActionButton
    lateinit var butEstado: Button

    lateinit var recvPuntos : RecyclerView
    private lateinit var RecyAdptPuntos: PuntosRecyclerAdapter
    private lateinit var vM_puntos: fragment_puntos_vm
    //private val vM_puntos: fragment_puntos_vm by activityViewModels()

    lateinit var dialogVmodel: Dialog_viewModel
    private lateinit var txtv_temp: TextView
    private lateinit var txtv_demora: TextView

    var list_puntos : MutableList<PuntoCom> = ArrayList<PuntoCom>()


    var dbFb = FirebaseFirestore.getInstance()


    companion object {
        fun newInstance() = fragment_puntos()
    }

    fun mainHandlerTickCom_Detener () {
        vM_puntos.vm_isConnected=false
        //mainHandlerTickCom.removeCallbacks(updateTextTaskCom)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menutoolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as MainActivity).supportActionBar!!.show()

        v =  inflater.inflate(R.layout.fragment_puntos, container, false)
        butAdd = v.findViewById(R.id.fBut_Add)
        butDel = v.findViewById(R.id.fBut_Delete)
        butEstado = v.findViewById(R.id.but_estado)
        recvPuntos = v.findViewById(R.id.recvPuntos)

        txtv_temp= v.findViewById(R.id.txtv_puntos_temp)
        txtv_demora= v.findViewById(R.id.txtv_puntos_demora)


        return v
    }

    override fun onStart() {
        super.onStart()

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        Log.d("Test", prefs.getBoolean("sync", false).toString())
        Log.d("Test", prefs.getBoolean("sync_auto", false).toString())
        Log.d("Test", prefs.getString("signature", "default signature"))


        db = puntoComDatabase.getAppDatabasePuntosCom(v.context)
        daoPuntosCom = db?.puntosDao()


        // ViewModel LIVEDATA ---------------------------------------------------------------
        // Actualizacion de Items
       vM_puntos = ViewModelProvider(requireActivity()).get(fragment_puntos_vm::class.java)
        val observerListPuntosCom = Observer<List<PuntoCom>> {
            ActualizarListaAdapterPuntosCom()    //ViewModel carga el Recycler View
        }
        vM_puntos.getListLiveData().observe(viewLifecycleOwner, observerListPuntosCom)

        // Actualizacion de Valores
        val observerValueListPuntosCom = Observer <MutableList<Int>> {
            ActualizarValoresListaAdapterPuntosCom()
        }
        vM_puntos.getValoresLiveData().observe(viewLifecycleOwner, observerValueListPuntosCom)


        //---------------------------------------------------------------------------------
        if (!vM_puntos.flag_PuntosInicializados) {
            vM_puntos.flag_PuntosInicializados = true

            var useExternaltable: Boolean = false

            if (prefs.getBoolean("sync", false) == true)
                if (prefs.getBoolean("sync_auto", false) == true)
                    useExternaltable = true
                else {
                    dialogVmodel =
                        ViewModelProvider(requireActivity()).get(Dialog_viewModel::class.java)
                    dialogVmodel.title = "Actualizar tabla Local puntosCom"
                    dialogVmodel.description =
                        "Escriba SI, para actualizar desde Firebase, o Cancelar" +
                                " para utilizar tabla local"
                    val action = fragment_puntosDirections.actionFragmentPuntosToFragmentDialog()
                    v.findNavController().navigate(action)
                    var rp_dialog: String = ""
                    dialogVmodel.input.observe(viewLifecycleOwner, Observer { result ->
                        rp_dialog = result.toString()
                        if (rp_dialog.toUpperCase() == "SI")
                            LoadAllPuntosComFromFirebase(vM_puntos)      //esta en FireBaseClass.kt
                    })
                }
            if (useExternaltable) {
                LoadAllPuntosComFromFirebase(vM_puntos)
            } else {
                vM_puntos.setListPuntos(daoPuntosCom?.loadAllPuntosCom())
                //list_puntos = viewModel_puntos.getListLiveData().value!!    // uso list_puntos como Array Local
            }
        }


        //Si no hay elementos "Value" en la lista hay q inicializar lista "Value"VM
        if (vM_puntos.sizeListValues==0) {
            //Nota setListPuntos, tambien inicializa lista setListValor<INT>
            var arrValues : MutableList<Int> = ArrayList<Int>()
            // Cargo el array de valores con valor inicial -1
            for (itemPunto in 0 until  vM_puntos.sizeList) {
                arrValues.add(-1)
            }
            vM_puntos.setValoresVmPuntos(arrValues)  //le paso los valores al VM Inicializacion
        }

        // RecyclerView = Linear Layout ---------------------------
        recvPuntos = v.findViewById(R.id.recvPuntos) as RecyclerView
        recvPuntos.setHasFixedSize(true)
        recvPuntos.layoutManager = LinearLayoutManager(context)

        // Adaptador: Le mando la listaPuntos y una funcion Lambda {parametros -> funcion} por ej { x: Int, y: Int -> x + y }

        ActualizarListaAdapterPuntosCom()    //ViewModel carga el Recycler View
        //---------------------------------------------------------------------------------

        // Botones flotantes
        butAdd.setOnClickListener {
            val indexPunto: Int = daoPuntosCom?.getCount()?:0

            vM_puntos.vmindexPunto=indexPunto   //actualizo vmodel

            vM_puntos.auxPuntoCom= PuntoCom(indexPunto,"NuevoPuntoCom","","ON OFF",
                0, 1, "DI",indexPunto, ":bbaaOO????xxxx")

            vM_puntos.flag_NewItem=true
            //viewModel_puntos.getListLiveData()  //Actualiza
            var action = fragment_puntosDirections.actionFragmentPuntosToFragmentEdicionPunto()
            v.findNavController().navigate(action)
        }

        butDel.setOnClickListener {

            // Fragment Dialog
            dialogVmodel = ViewModelProvider(requireActivity()).get(Dialog_viewModel::class.java)
            dialogVmodel.title="Eliminar Item"
            dialogVmodel.description="Escriba SI, para eliminar Item"

            val action = fragment_puntosDirections.actionFragmentPuntosToFragmentDialog()
            v.findNavController().navigate(action)

            var rp_dialog: String = ""

            dialogVmodel.input.observe(viewLifecycleOwner, Observer { result ->
                rp_dialog= result.toString()

                if (rp_dialog.toUpperCase()== "SI") {
                    daoPuntosCom?.deletePuntoCom(vM_puntos.getPunto(vM_puntos.vmindexPunto))

                    list_puntos = vM_puntos.getListLiveData().value!!
                    list_puntos.remove(vM_puntos.getPunto(vM_puntos.vmindexPunto))
                    vM_puntos.setListPuntos(list_puntos)

                    butDel.visibility=FloatingActionButton.INVISIBLE
                    butAdd.visibility=FloatingActionButton.INVISIBLE
                }
            })

        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            //En caso de querer volver para atrás, no hago nada
        }

    }



    private fun ActualizarListaAdapterPuntosCom() {

        Log.d("Test", "ActualizarListaAdapterPuntosCom ")

        RecyAdptPuntos = PuntosRecyclerAdapter(vM_puntos.getListPuntos() ?: ArrayList<PuntoCom>(),
            vM_puntos.getValoresPuntosCom() ?: ArrayList<Int>(), { position, action: Int ->
                this.listOnItemClick(position, action)
            }, {     //position: Int -> this.listOnLongClick(position)
            }
        )
        recvPuntos.adapter = RecyAdptPuntos

    }

    private fun ActualizarValoresListaAdapterPuntosCom() {

        //RecyAdptPuntos.values
        Log.d("Test", "Actualizando ValoresListaAdapterPuntosCom ")
        RecyAdptPuntos.updateListValues(vM_puntos.getValoresPuntosCom() ?:ArrayList<Int>())
        RecyAdptPuntos.notifyDataSetChanged()

        txtv_temp.setText( (vM_puntos.getValorListaPuntos(0).toFloat()).toString() + "ºC" )

        if (vM_puntos.enAlarma){
            txtv_demora.visibility=TextView.VISIBLE
            if (vM_puntos.enPhoneCall){
                txtv_demora.setText( "llamando..." )
            }
            else {
                var demoraDec: Int //vM_puntos.getValorListaPuntos(ConstIndexList.INDEXdEMORA)
                demoraDec= vM_puntos.getValorListaPuntos(ConstIndexList.INDEXdEMORA)
                demoraDec= demoraDec - vM_puntos.demoraToPhone

                txtv_demora.setText( "llamando en: " +
                        demoraDec.toString() + " s" )
            }
        }
        else  txtv_demora.visibility=TextView.INVISIBLE


        var  auxValPuntoCom= vM_puntos.getValorListaPuntos(ConstIndexList.INDEXeSTADO)
        when (auxValPuntoCom)  {
            ConstEstado.OFFLINE  ->  {       butEstado.setBackgroundResource(R.drawable.luz_estado_celeste)
                            butEstado.setText("OFFLINE") }

            ConstEstado.NORMAL->  {       butEstado.setBackgroundResource(R.drawable.luz_estado_verde)
                butEstado.setText("NORMAL") }

            ConstEstado.PREaLARMA->  {       butEstado.setBackgroundResource(R.drawable.luz_estado_amarillo)
                butEstado.setText("PREaLARMA") }

            ConstEstado.ALARMA->  {       butEstado.setBackgroundResource(R.drawable.luz_estado_rojo)
                butEstado.setText("ALARMA") }
        }

    }

    private fun listOnItemClick (posicion: Int, accionClick: Int ){
        //lateinit var recvPuntos: RecyclerView

        //Snackbar.make(v,position.toString(), Snackbar.LENGTH_SHORT).show()

        vM_puntos.vmindexPunto=posicion
        //valorItem= viewModel_puntos.getValorListaPuntos(position)    //Carga VmValorPunto

        if (accionClick==PuntosRecyclerAdapter.actionClick) {
            var action = fragment_puntosDirections.actionFragmentPuntosToFragmentDetailsContainer()
            v.findNavController().navigate(action)
        }

        if (accionClick==PuntosRecyclerAdapter.actionLongClick) {
            butDel.visibility=FloatingActionButton.VISIBLE
            butAdd.visibility=FloatingActionButton.VISIBLE
        }
        if (accionClick==PuntosRecyclerAdapter.actionLongClickNoSelect) {
            butDel.visibility=FloatingActionButton.INVISIBLE
            butAdd.visibility=FloatingActionButton.INVISIBLE
        }

    }



    /*  ------------------   MENU OPTIONS   -------------------------------------------------
    --------------------------------------------------------------------------------------- */

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {

            R.id.action_conectar -> {
                var listaValorRP = MutableList(64) { index -> -1} //'A' + index }
                if (!vM_puntos.vm_isConnected) {
                    vM_puntos.setValoresVmPuntos(listaValorRP)
                    //listaValorRP=vM_puntos.getValoresPuntosCom()?: arrayListOf()

                    //vM_puntos.vm_isConnected=true
                    //Snackbar.make(v, "CONEXION", Snackbar.LENGTH_SHORT).show()
                    //mainHandlerTickCom.post(updateTextTaskCom)
                }
                else {
                    //vM_puntos.vm_isConnected=false
                    //Snackbar.make(v, "DESCONEXION", Snackbar.LENGTH_SHORT).show()
                    //mainHandlerTickCom.removeCallbacks(updateTextTaskCom)
                }
            }

            R.id.action_config_com -> {
                Snackbar.make(v, "Config", Snackbar.LENGTH_SHORT).show()
                //mainHandlerTickCom_Detener()
                var action = fragment_puntosDirections.actionFragmentPuntosToFragmentModCom()
                v.findNavController().navigate(action)

            }

            R.id.action_setup_bluetooth -> {
                //Snackbar.make(v, "Config", Snackbar.LENGTH_SHORT).show()
                //mainHandlerTickCom_Detener()
                //var action = fragment_puntosDirections.actionFragmentPuntosToBlueToothActivity()
                //v.findNavController().navigate(action)

            }

            R.id.action_preferencias -> {
                mainHandlerTickCom_Detener()
                val action = fragment_puntosDirections.actionFragmentPuntosToSettingsActivity()
                v.findNavController().navigate(action)
            }

            R.id.action_upload -> {
                LoadListToFirebase(vM_puntos)    // Esta en FireBaseClass
            }

            R.id.action_download -> {
                LoadAllPuntosComFromFirebase(vM_puntos)      //esta en FireBaseClass.kt
            }

            R.id.action_load_in -> {
                if  (!vM_puntos.getListPuntos().isNullOrEmpty())
                    daoPuntosCom?.deleteAllPuntosCom()
                    for (punto in vM_puntos.getListPuntos()!!.iterator() ) {
                        daoPuntosCom?.insertPuntoCom(punto)
                    }
            }

            R.id.action_close -> {
                System.exit(0)
            }

            //var action = fragment_puntosDirections.actionFragmentPuntosToFragmentDialog()
            //v.findNavController().navigate(action)

        }
        return super.onOptionsItemSelected(item)
    }


}

