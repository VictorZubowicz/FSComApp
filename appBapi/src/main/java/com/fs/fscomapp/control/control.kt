package com.fs.fscomapp.control

import com.fs.fscomapp.entities.PuntoCom
import com.fs.fscomappbapi.fragments.ConstEstado
import com.fs.fscomappbapi.fragments.ConstIndexList

public class Control {
    companion object {
        var temperatura = 22
        var sp_prealarma = 22
        var sp_alarma = 22
        var estado=ConstEstado.NORMAL


    fun procControl (listValPuntos: MutableList<Int>?)  {

        temperatura= listValPuntos?.get(ConstIndexList.INDEXtEMPERATURA) ?: 22
        sp_prealarma= listValPuntos?.get(ConstIndexList.INDEXsETpREALARMA) ?: 20
        sp_alarma= listValPuntos?.get(ConstIndexList.INDEXsETaLARMA) ?: 22

        if (temperatura >(sp_prealarma)) {
            if (temperatura>sp_alarma) {
                listValPuntos?.set(ConstIndexList.INDEXeSTADO, ConstEstado.ALARMA)
            }
            else {
                listValPuntos?.set(ConstIndexList.INDEXeSTADO, ConstEstado.PREaLARMA)
            }
        }
        else {
            listValPuntos?.set(ConstIndexList.INDEXeSTADO, ConstEstado.NORMAL)
        }
    }



    }


}
