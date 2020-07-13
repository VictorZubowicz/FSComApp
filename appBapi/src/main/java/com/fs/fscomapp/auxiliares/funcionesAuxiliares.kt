package com.fs.fscomapp.auxiliares

import com.fs.fscomapp.entities.PuntoCom


fun ObtenerIconoSegunTipoDato(formato: String): Int {
        var imagen: Int=0

        when(formato){
            tipoDatoClass.ConstTipoDato.typeDigitalInput -> imagen= tipoDatoClass.ConstImageDato.imgDigIn
            tipoDatoClass.ConstTipoDato.typeDigitalOuput -> imagen= tipoDatoClass.ConstImageDato.imgDigOut
            tipoDatoClass.ConstTipoDato.typeDigitalInputPower -> imagen= tipoDatoClass.ConstImageDato.imgDigInPwr
            tipoDatoClass.ConstTipoDato.typeDigitalOuputFan -> imagen= tipoDatoClass.ConstImageDato.imgFan
            tipoDatoClass.ConstTipoDato.typeDigitalOuputCmp -> imagen= tipoDatoClass.ConstImageDato.imgCompressor
            tipoDatoClass.ConstTipoDato.typeAnalogInput -> imagen= tipoDatoClass.ConstImageDato.imgAnalogOut
            tipoDatoClass.ConstTipoDato.typeAnalogOutput -> imagen= tipoDatoClass.ConstImageDato.imgCompressor
            tipoDatoClass.ConstTipoDato.typeSetpoint -> imagen= tipoDatoClass.ConstImageDato.imgSet
            tipoDatoClass.ConstTipoDato.typeDecimal -> imagen= tipoDatoClass.ConstImageDato.imgDecimal
            tipoDatoClass.ConstTipoDato.typeDate -> imagen= tipoDatoClass.ConstImageDato.imgDate
            tipoDatoClass.ConstTipoDato.typeDay -> imagen= tipoDatoClass.ConstImageDato.imgTime
            tipoDatoClass.ConstTipoDato.typeDecimalx10 -> imagen= tipoDatoClass.ConstImageDato.imgDecimal
            tipoDatoClass.ConstTipoDato.typeAnalogInput -> imagen= tipoDatoClass.ConstImageDato.imgAnalogIn
            tipoDatoClass.ConstTipoDato.typeAnalogOutput -> imagen= tipoDatoClass.ConstImageDato.imgAnalogOut
            tipoDatoClass.ConstTipoDato.typeHexagesimal -> imagen= tipoDatoClass.ConstImageDato.imgHexagesimal
            tipoDatoClass.ConstTipoDato.typeMonth -> imagen= tipoDatoClass.ConstImageDato.imgTime
            tipoDatoClass.ConstTipoDato.typeTimeHour -> imagen= tipoDatoClass.ConstImageDato.imgTime
            tipoDatoClass.ConstTipoDato.typeTimeMin -> imagen= tipoDatoClass.ConstImageDato.imgTime
            tipoDatoClass.ConstTipoDato.typeTimeSec -> imagen= tipoDatoClass.ConstImageDato.imgTime
            tipoDatoClass.ConstTipoDato.typeYear -> imagen= tipoDatoClass.ConstImageDato.imgTime
            tipoDatoClass.ConstTipoDato.typeYear -> imagen= tipoDatoClass.ConstImageDato.imgTime
            tipoDatoClass.ConstTipoDato.typePhone -> imagen= tipoDatoClass.ConstImageDato.imgPhone
            tipoDatoClass.ConstTipoDato.typeMail -> imagen= tipoDatoClass.ConstImageDato.imgMail
            else   -> imagen= tipoDatoClass.ConstImageDato.imgNoDef
        }
        return imagen
    }

fun Boolean.toInt() = if (this) 1 else 0


public fun ObtenerDisplayValorSegunTipoDeDato(itemPuntoCom: PuntoCom, it: Int): String {
    var strValor: String = ""


    when (itemPuntoCom.tipoDato) {

        tipoDatoClass.ConstTipoDato.typeDigitalInput,
        tipoDatoClass.ConstTipoDato.typeDigitalOuput,
        tipoDatoClass.ConstTipoDato.typeDigitalInputPower,
        tipoDatoClass.ConstTipoDato.typeDigitalOuputCmp,
        tipoDatoClass.ConstTipoDato.typeDigitalOuputFan -> strValor = ObtenerDisplayValorDigital(itemPuntoCom.unidad,it)

        tipoDatoClass.ConstTipoDato.typeSetpoint,
        tipoDatoClass.ConstTipoDato.typeAnalogInput,
        tipoDatoClass.ConstTipoDato.typeAnalogOutput -> strValor = ObtenerDisplayValorAnalogico(itemPuntoCom.unidad,it)

        tipoDatoClass.ConstTipoDato.typeHexagesimal-> strValor = ObtenerDisplayValorBinario(itemPuntoCom.unidad,it)

        tipoDatoClass.ConstTipoDato.typePhone,
        tipoDatoClass.ConstTipoDato.typeMail -> strValor = itemPuntoCom.unidad

        else -> strValor = it.toString() // undefined"
        }
    return strValor
}


fun ObtenerDisplayValorDigital(strUnidad: String, it: Int): String {

    var strValor: String=""

    if (it==-1) {
        return ("OFFLINE")
    }

    if (strUnidad != "") {
        var palabras: List<String> = strUnidad.split(" ") //\\W+")
        if (it != 0 && palabras.size >= 2) {
            strValor = palabras[0]
        }  //      it.toString())
        else {
            if (palabras.size>1)
            strValor = palabras[1]
        }
    } else {
        strValor = it.toString()
    }
    return strValor
}

fun ObtenerDisplayValorBinario(strUnidad: String, it: Int): String {

    var strValor: String=""

    if (it==-1) {
        return ("OFFLINE")
    }

    if (strUnidad != "") {
        var palabras: List<String> = strUnidad.split(" ") //\\W+")

        when(it) {
            0x00 -> { strValor = palabras[0]}
            0x01 -> { if (palabras.size >= 2) strValor = palabras[1]}
            0x02 -> { if (palabras.size >= 3) strValor = palabras[2]}
            0x04 -> { if (palabras.size >= 4) strValor = palabras[3]}
            0x08 -> { if (palabras.size >= 5) strValor = palabras[4]}
            0x10 -> { if (palabras.size >= 6) strValor = palabras[5]}
            0x20 -> { if (palabras.size >= 7) strValor = palabras[6]}
            else ->  strValor = it.toString()
        }
    } else {
        strValor = it.toString()
    }
    return strValor
}


fun ObtenerDisplayValorAnalogico(strUnidad: String, it: Int): String {
    var strValor: String=""

    if (it==-1) {
        return ("OFFLINE")
    }

        strValor = it.toString().plus(strUnidad)
    return strValor
}
