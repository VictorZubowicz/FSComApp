package com.fs.fscomapp

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothState
import co.nedim.maildroidx.MaildroidX
import co.nedim.maildroidx.MaildroidXType
import com.fs.fscomapp.auxiliares.LoadListToFirebase
import com.fs.fscomapp.control.Control.Companion.procControl
import com.fs.fscomapp.fragments.fragment_puntos_vm
import com.fs.fscomappbapi.fragments.ConstEstado
import com.fs.fscomappbapi.fragments.ConstIndexList
import kotlinx.coroutines.*
import java.util.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    lateinit var bt: BluetoothSPP
    lateinit var vM_puntos: fragment_puntos_vm  //tomo una instancia de VM puntos


    val mainHandlerTickCom = Handler(Looper.getMainLooper())

    lateinit var address: String

//    var arrStreamComRq = listOf( "020202bbaa130040$$$$","020202bbaa134080$$$$","020202bbaa1380C0$$$$")
    var arrStreamComRq = listOf( "QA0T\r' ","QA0B'\\r' ") //"020202bbaa130040$$$$","020202bbaa134080$$$$","020202bbaa1380C0$$$$")
    lateinit var listaValorRP : MutableList<Int> //64) { index -> -1} //'A' + index }

    var estado: Int = 0

    var curStreamCom:Int =0
    var stringRP: String =""

    var cuenta:Int=0

//    val TEXT_SIMUL_RESPONSE: String = "0202020186124080" +  //16
//            "00FA01006001011E0100EB0100CD0100FA0100BE0100DC01000A01000501" +
//            "000A0100140100000100000100F50100D70101040100C80100E601000A01" +
//            "000501000A0100140100000100000100F50100D70101040100C80100E601" +
//            "000A01000501000A0100140100000100000100F50100D70101040100C801" +
//            "00E601000A01000501000A0100140100000100000100F50100D701010401" +
//            "00C80100E601000A01000501000A0100140100000100000100F50100D701" +
//            "01040100C80100E601000A01000501FFFF"        //total 410
    val TEXT_SIMUL_RESPONSE: String = "Td\r"

    val  ARRAYrP_iPRIMER_DATO = 16
    val  ARRAYrP_SIZE_DATO = 6
    val  ARRAYrP_SIZE_VALOR = 4
    val  ARRAYrP_SIZE_ESTADO = 2


    var auxstr: String= ""
    var StrValor: String = "-1"
    var Valor: Int =-1

    class estadosCom {
        companion object {
            val STBY = 0
            val WAIT = 1
            val RQ_RD = 2
            val RP_RD = 3
        }
    }

    companion object {

        val EXTRA_ADDRESS: String = "Device_Address"
        val BluetoothSIMLULATE: Int = 8031
        var m_myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        var m_isConnected: Boolean = false
        lateinit var m_address: String

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        this.supportActionBar!!.hide()

        vM_puntos = ViewModelProvider(this).get(fragment_puntos_vm::class.java)
        listaValorRP= vM_puntos.getValoresPuntosCom()?: arrayListOf()

        checkPermission()


        bt = BluetoothSPP(this)

        if (!bt.isBluetoothEnabled) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT)
        } else {
            if (!bt.isServiceAvailable) {
                bt.setupService()
                bt.startService(BluetoothState.DEVICE_OTHER)
                // setup();
            }
        }


        bt.setBluetoothConnectionListener(object : BluetoothSPP.BluetoothConnectionListener {

            override fun onDeviceConnected(name: String, address: String) {
                vM_puntos.vm_isConnected=true
                Toast.makeText(
                    applicationContext
                    , "Connected to $name\n$address"
                    , Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDeviceDisconnected() {
                vM_puntos.vm_isConnected=false
                Toast.makeText(
                    applicationContext
                    , "Connection lost", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDeviceConnectionFailed() {
                vM_puntos.vm_isConnected=false
                Toast.makeText(
                    applicationContext
                    , "Unable to connect", Toast.LENGTH_SHORT
                ).show()
            }
        })

        bt.setOnDataReceivedListener { data, message ->
            stringRP = message.toString()
            Log.d("Test", "RESPUESTA: ${stringRP} ")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var rqCode = (requestCode and  0xFFF)

        if (rqCode == BluetoothSIMLULATE) {
                if (vM_puntos.vm_isConnected)
                    mainHandlerTickCom.post(updateTextTaskCom)
        }
        if ((rqCode and  0xFFF) == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK) {
                bt.connect(data)
                vM_puntos.vm_isConnected = true
                Toast.makeText(
                    this,
                    "Bluetooth fue reconectado",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                Toast.makeText(
                    this,
                    "No se ha seleccionado ningon dispositivo Bluetooth",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else
            if (rqCode == BluetoothState.REQUEST_ENABLE_BT) {
                if (resultCode == Activity.RESULT_OK) {
                    bt.setupService()
                    bt.startService(BluetoothState.DEVICE_OTHER)
                    Toast.makeText(
                        this,
                        "Bluetooth se conecta a nuevo dispositivo", Toast.LENGTH_SHORT
                    )
                        .show()
                    //setup();
                } else {
                    // Do something if user doesn't choose any device (Pressed back)
                    Toast.makeText(
                        this,
                        "No se ha seleccionado ningon dispositivo Bluetooth",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }


    /*-----------------------------------------------------------------------------------------------
    SCHEDULER   TIMER LOOP
    ----------------------------------------------------------------------------------------------- */

    val updateTextTaskCom = object : Runnable {
        override fun run() {
            DispacherTask()
            mainHandlerTickCom.postDelayed(this, 2000)
        }
    }

    override fun onPause() {
        super.onPause()
        if (!vM_puntos.vm_isConnected)
            mainHandlerTickCom.removeCallbacks(updateTextTaskCom)
    }

    override fun onResume() {
        super.onResume()
        if (vM_puntos.vm_isConnected) {
            listaValorRP= vM_puntos.getValoresPuntosCom()?: arrayListOf()
            mainHandlerTickCom.post(updateTextTaskCom)

            if(vM_puntos.demoraToPhone!=0) vM_puntos.demoraToPhone=0
            vM_puntos.enPhoneCall = false
        }
    }

    private fun DispacherTask () {

        vM_puntos.setValoresVmPuntos(listaValorRP)
        Log.d("Test", "Cargo dato en LIVEDATA: ${curStreamCom} ")
        //listaValorRP=vM_puntos.getValoresPuntosCom()?: arrayListOf()

        val parentJob = Job()
        val handler = CoroutineExceptionHandler { _, throwable ->
            Log.d("corrutine Com", "handler: $throwable") // Prints "handler: java.io.IOException"
        }

        if (estado== estadosCom.RP_RD) {
            vM_puntos.setValoresVmPuntos(listaValorRP)
            estado= estadosCom.STBY
            procControl(listaValorRP)       //esta en el modulo control

            when (listaValorRP.get(ConstIndexList.INDEXeSTADO)) {
                ConstEstado.OFFLINE ->{
                    vM_puntos.enPreAlarma= false
                    vM_puntos.enAlarma = false
                    vM_puntos.enPhoneCall = false
                }

                ConstEstado.NORMAL -> {

                    if(vM_puntos.demoraToPhone!=0) vM_puntos.demoraToPhone=0

                    if(vM_puntos.enPreAlarma) {
                        vM_puntos.enPreAlarma= false
                        sendMail(vM_puntos.getPunto(ConstIndexList.INDEXmAIL).unidad,
                            vM_puntos.getPunto(ConstIndexList.INDEXsETpREALARMA).descripcion,
                            "VALOR NORMALIZADO." + " Temperatura: " +
                                    listaValorRP.get(ConstIndexList.INDEXtEMPERATURA))
                    }

                }
                ConstEstado.PREaLARMA -> {
                    vM_puntos.enAlarma = false
                    vM_puntos.enPhoneCall = false
                    if (!vM_puntos.enPreAlarma) {
                        vM_puntos.enPreAlarma = true
                        sendMail(
                            vM_puntos.getPunto(ConstIndexList.INDEXmAIL).unidad,
                            vM_puntos.getPunto(ConstIndexList.INDEXsETpREALARMA).descripcion,
                            "Temperatura: " + listaValorRP.get(ConstIndexList.INDEXtEMPERATURA)
                        )
                    }

                }
                ConstEstado.ALARMA -> {
                    vM_puntos.enPreAlarma = true
                    if (!vM_puntos.enAlarma) {
                        vM_puntos.enAlarma = true
                        sendMail(
                            vM_puntos.getPunto(ConstIndexList.INDEXmAIL).unidad,
                            vM_puntos.getPunto(ConstIndexList.INDEXsETaLARMA).descripcion,
                            "Temperatura: " + listaValorRP.get(ConstIndexList.INDEXtEMPERATURA)
                        )
                    }
                    //LoadListToFirebase(vM_puntos)    // Esta en FireBaseClass
                    if (!vM_puntos.enPhoneCall){    //COS
                        if(controlDemora( vM_puntos.getValorListaPuntos(ConstIndexList.INDEXdEMORA) )==true) {
                            vM_puntos.enPhoneCall=true
                            callPhone(vM_puntos.getPunto(ConstIndexList.INDEXtELEFONO1).unidad)
                        }
                    }
               }
            }
        }


        val scope = CoroutineScope(Dispatchers.IO + parentJob)

        if (estado == estadosCom.STBY) {
            estado = estadosCom.WAIT
            val excecutionTime = measureTimeMillis {

                scope.launch {
                    val newRQ = async {
                        curStreamCom = task_procNextListComRQ(curStreamCom) //
                    }
                    newRQ.await()

                    val procCom = async {
                        Log.d("Test", "Consulta: ${curStreamCom} ")
                        task_procComRQ(curStreamCom)
                        estado = estadosCom.RQ_RD
                    }
                    procCom.await()

                    val upDate = async {
                        //task_procComRP(curStreamCom)
                        task_procComRpTTO(curStreamCom)
                        estado = estadosCom.RP_RD
                    }.await()

                }
            }
        }
    }

    private fun controlDemora(demoraSet: Int): Boolean {

     Handler().postDelayed(  { vM_puntos.demoraToPhone = vM_puntos.demoraToPhone + 1 } , 1000 )

    if (vM_puntos.demoraToPhone>=demoraSet) {
            vM_puntos.demoraToPhone=0   //reset
            return true
    }
    else{
        return false
    }
    }


    /*-----------------------------------------------------------------------------------------------
    FUNCIONES COMUNICACION
    ----------------------------------------------------------------------------------------------- */
    private suspend fun task_procNextListComRQ ( curIndex:Int): Int {
        var auxIndex= curIndex +1
        if (auxIndex>=arrStreamComRq.size) auxIndex=0
        //Log.d("taskCom", "task_procNextListComRQ: {$auxIndex}")
        return auxIndex
    }

    private suspend fun task_procComRQ(indexCom:Int): Int {
        var strComRq=""
        //Log.d("Test", "fetchDataFromServerTwo()")
        delay(500)     // ojo con delay no tiene que ser mayor a Time DispacherTask
        if (indexCom< arrStreamComRq.size)
            strComRq= ArmarStringComRq(arrStreamComRq[indexCom])
        Log.d("taskCom", "task_procNewCom: {$strComRq}")
        if (!vM_puntos.vm_isSimulation)
            bt.send(strComRq,true)

        return 1
    }

    private suspend fun task_procComRP(indexCom:Int): Int {
        //val listaValoresPuntos : MutableList<Int> = ArrayList<Int>()
        //Log.d("Test", "fetchDataFromServerTwo()")
        Log.d("taskCom", "task_upDateCurComRQ: {$indexCom}")

        if (!vM_puntos.vm_isSimulation)
            vM_puntos.stringRP= stringRP
        else {
            delay(1000)     // ojo con delay no tiene que ser mayor a Time DispacherTask
            vM_puntos.stringRP = TEXT_SIMUL_RESPONSE
        }

        //arrayStringRp= vM_puntos.stringRP.toCharArray()

        //auxstr= vM_puntos.stringRP.toList()
        if (stringRP.length>ARRAYrP_iPRIMER_DATO) {
            auxstr =
                vM_puntos.stringRP.drop(ARRAYrP_iPRIMER_DATO).toString()    //Saco la parte inicial

            for (index in listaValorRP.indices) {
                StrValor = auxstr.take(ARRAYrP_SIZE_VALOR).toLowerCase()  //tomo valor
                auxstr = auxstr.drop(ARRAYrP_SIZE_DATO)  //desplazo string de analisis
                Valor = Integer.parseInt(StrValor, 16) //+ cuenta
//                cuenta = cuenta + 1
                listaValorRP[index] = Valor
                //arrayStringRp.get(ARRAYrP_PRIMER_DATO + ARRAYrP_SIZE_DATO * i).toInt()
            }
            return 1
        }
        else return 0
    }

    private suspend fun task_procComRpTTO(indexCom:Int): Int {
        var chrOpCode: Char = 'T'
        var chrValor: Char = 'a'
        var chrTecla: Char = '0'

        Log.d("taskCom", "task_upDateCurComRQ: {$indexCom}")

        if (!vM_puntos.vm_isSimulation)
            vM_puntos.stringRP= stringRP
        else {
            delay(1000)     // ojo con delay no tiene que ser mayor a Time DispacherTask
            vM_puntos.stringRP = TEXT_SIMUL_RESPONSE
        }

        auxstr = vM_puntos.stringRP //.drop(3).toString()    //Saco la parte inicial
            //chrOpCode = auxstr.first()
        if (auxstr.length>=2) {

            chrOpCode = auxstr.get(0)  //tomo valor

            if (chrOpCode == 'T') {
                chrValor = auxstr.get(1)  //tomo valor
                Valor = chrValor.toByte().toInt()
                Valor= (Valor*28/100) - 10
                listaValorRP[0] = Valor
                Log.d("task RP", "ValorTemp: {$Valor}, ")

            }
            if (chrOpCode == 'B') {
                chrTecla = auxstr.get(1)  //tomo valor
                Log.d("task RP", "Tecla: {$chrTecla}, ")
            }

            return 1
        }else return 0
    }

    private fun ArmarStringComRq(formatoStrComRq: String): String {
        var oldValue= "aa"
        var newValue= "%02x".format(vM_puntos.vm_addressTT)
        var strComRq =formatoStrComRq.replace(oldValue, newValue)
        oldValue= "bb"
        newValue= "%02x".format(vM_puntos.vm_bridgeTT)
        strComRq =strComRq.replace(oldValue, newValue)

        return (strComRq)
    }


/*-----------------------------------------------------------------------------------------------
CALL PHONE
----------------------------------------------------------------------------------------------- */
    fun callPhone(numero:String){

        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" .plus(numero)))
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        startActivity(intent)
    }


    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    42)
            }
        } else {
            // Permission has already been granted
            //   callPhone()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 42) {
            // If request is cancelled, the result arrays   are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay!
                //  callPhone()
            } else {
                // permission denied, boo! Disable the
                // functionality
            }
            return
        }
    }




/*-----------------------------------------------------------------------------------------------
EMAIL
----------------------------------------------------------------------------------------------- */

fun sendMail(to:String, subject:String, text:String) {
    val pd: ProgressDialog = ProgressDialog(this@MainActivity)

    pd.setTitle("Send email")
    pd.setMessage("Sending...")
    pd.show()

    MaildroidX.Builder()
        .smtp("ci2.toservers.com")
        .smtpUsername("victor@factory-solutions.com.ar")
        .smtpPassword("Daniel_8031")
        .port("465")
        .type(MaildroidXType.HTML)
        .to(to)
        .from("victor@factory-solutions.com.ar")
        .subject(subject)
        .body(text)
        .isJavascriptDisabled(true)
        //.attachment(this@MainActivity.filesDir.path +  "/abc.txt")
        .onCompleteCallback(object : MaildroidX.onCompleteCallback {
            override val timeout: Long = 4000 //Add timeout accordingly

            override fun onSuccess() {
                Toast.makeText(this@MainActivity, "Mail sent!", Toast.LENGTH_SHORT).show()
                pd.cancel()

            }

            override fun onFail(errorMessage: String) {

                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                pd.cancel()
            }

        })

        .mail()
    }


}
