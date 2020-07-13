package com.fs.fscomapp.auxiliares

import com.fs.fscomapp.R

class  tipoDatoClass ( nombre:String, ic_imagen: Int)  {

    var nombre:String="" //""DIGITAL"
    var ic_imagen:Int=0  //"@android:drawable/btn_radio"


    class ConstImageDato {
        companion object {
            val imgNoDef = R.drawable.ic_no_def_foreground
            val imgError = R.drawable.ic_error_foreground
            val imgDigIn = R.drawable.ic_dig_in
            val imgDigInPwr = R.drawable.ic_power
            val imgDigOut = R.drawable.ic_dig_out
            val imgCompressor = R.drawable.ic_compressor
            val imgFan = R.drawable.ic_fan
            val imgAnalogIn = R.drawable.ic_analog_in
            val imgAnalogOut = R.drawable.ic_analog_out
            val imgSet = R.drawable.ic_tune
            val imgAnalog = R.drawable.ic_analog_in
            val imgDecimal = R.drawable.ic_decimal
            val imgHexagesimal = R.drawable.ic_data_hex
            val imgTime = R.drawable.ic_time
            val imgDate = R.drawable.ic_date
            val imgPhone = R.drawable.ic_set_phone
            val imgMail = R.drawable.ic_mail
        }
    }

    class ConstTipoDato {
        companion object {
            val typeDigitalInput = "DI"
            val typeDigitalInputPower = "DI PWR"
            val typeDigitalOuput = "DO"
            val typeDigitalOuputCmp = "DO CMP"
            val typeDigitalOuputFan = "DO FAN"
            val typeAnalogInput = "AI"
            val typeAnalogOutput = "AO"
            val typeSetpoint = "SP"
            val typeDecimal = "DEC"
            val typeDecimalx10 = "Dx10"
            val typeHexagesimal = "HEX"
            val typeTimeHour ="HRS"     // para paquetes que formato time
            val typeTimeMin = "MIN"     // para paquetes que formato time
            val typeTimeSec = "SEC"     // para paquetes que formato time
            val typeDate = "DTE"
            val typeMonth = "MTH"
            val typeYear = "YAR"
            val typeDay = "DAY"
            val typePhone = "PHONE"
            val typeMail = "MAIL"
        }
    }

}
