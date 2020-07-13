package com.fs.fscomapp.fragments

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fs.fscomapp.entities.PuntoCom
import java.util.*


class fragment_puntos_vm : ViewModel() {

    //lateinit var auxListValores: MutableList<ArrayList<Int>>
    lateinit var auxListValores: MutableList<Int> //<Int> = MutableLiveData<Int>()

    var sizeList: Int = 0
    var sizeListValues: Int = 0
    var flag_NewItem: Boolean= false
    var vmindexPunto : Int=-1 //<Int> = MutableLiveData<Int>()
    var enAlarma: Boolean = false
    var enPreAlarma: Boolean = false
    var enPhoneCall: Boolean = false

    private var vmPuntoCom : MutableLiveData<PuntoCom> = MutableLiveData<PuntoCom>()
    var auxPuntoCom : PuntoCom =PuntoCom (-1,"","","",0, 1, "",-1, "" )
    var demoraToPhone: Int =10

    private val list_puntos = MutableLiveData <MutableList<PuntoCom>>()

    private val list_valores_puntos = MutableLiveData <MutableList<Int>>()

    var flag_PuntosInicializados=false

    var vm_myUUID= UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    var vm_bluetoothSocket: BluetoothSocket?=null
    lateinit var vm_bluetoothAdapter: BluetoothAdapter
    var vm_isConnected:Boolean=false
    var vm_isSimulation:Boolean=false

    lateinit var vm_address: String

    var vm_addressTT: Int=0
    var vm_bridgeTT: Int=0

    var stringRP: String =""

    var AjustandoSetpoint: Boolean=false

    fun getValorListaPuntos (position: Int): Int {
        if (list_valores_puntos.value!!.size  > position) {
            return (list_valores_puntos.value?.get(position) ?: -1)  //-1.0) as Float)
        }
        else return -1
    }

    fun setValorListaPuntos (position: Int, valor: Int)  {
        auxListValores= list_valores_puntos.value!!
        auxListValores[position]= valor //actualiza valor en lista
        list_valores_puntos.value =auxListValores
    }

    fun setValoresVmPuntos (listValPuntos: MutableList<Int>?)  {
        sizeListValues=list_puntos.value?.size?: 0
        list_valores_puntos.value= listValPuntos
    }

    fun getValoresPuntosCom () : MutableList<Int>? {
        return (list_valores_puntos.value)
    }

    fun getValoresLiveData () : LiveData<MutableList<Int>> {
        return (list_valores_puntos)
    }

    //fun getValorPunto (position: Int): Int {
    //    return (list_valor_puntos.value?.get(position)?:-1)  // -1.0) as Float)
    //}


    fun getListLiveData (): LiveData<MutableList<PuntoCom>> {
        sizeList=list_puntos.value?.size?: 0
        return list_puntos
    }

    fun getListPuntos (): MutableList<PuntoCom>? {
        return list_puntos.value
    }

    fun getvmPuntoCom () : LiveData<PuntoCom> {
        return vmPuntoCom
    }

    //Solo carga un registro PuntoCom para pasarle al Toolbar (no es LIVEDATA)
    fun getPunto (position: Int):PuntoCom  {
        var punto: PuntoCom
        punto= list_puntos.value?.get(position)?: PuntoCom(-1,"","","",0,1,"",-1,"")
        vmindexPunto=position
        return punto
    }

    // Aca carga el LIVEDATA para tener entre los fragments del Toolbar
    // Trabaja en conjunto con selecPunto, solo que aca ya hay que tener un observer cargado
    //fun loadVmPunto (auxPuntoCom:PuntoCom) {
    //    vmPuntoCom.value=auxPuntoCom
    //}


    fun setListPuntos (listPuntos: MutableList<PuntoCom>?)  {
        list_puntos.value = listPuntos
        sizeList=list_puntos.value?.size?: 0
        //debo cargar los lista de valores tambien
        if (listPuntos != null) {
            for (i in listPuntos) {
                list_valores_puntos.value?.add(-1)
            }
        }

    }

    fun addItemListPuntos (nuevoItem: PuntoCom)     {
        vmPuntoCom.value=nuevoItem

        list_puntos.value?.add(nuevoItem)
        //Agrega tambien un item valor automaticamente
        list_valores_puntos.value?.add(-1)
    }


}
