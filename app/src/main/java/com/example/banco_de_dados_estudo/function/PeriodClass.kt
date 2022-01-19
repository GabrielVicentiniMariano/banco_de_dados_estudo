package com.example.banco_de_dados_estudo.function

import android.util.Log
import java.lang.Exception
import java.time.LocalDate
import java.time.Period

class PeriodClass {
    fun period(str: String): Period?{
        val now = LocalDate.now()
        val year = str.split("/")
        var date: LocalDate? = null
        try {
            date = LocalDate.of(year[2].toInt(), year[1].toInt(),year[0].toInt())
        }catch (e: Exception){
            Log.i("Erro data: ", e.toString())
            return null
        }
        val period = Period.between(date,now)
        return period
    }
    fun checkPeriod(str: String): Boolean{
        return period(str) !=null && period(str)!!.days >=0
    }
}