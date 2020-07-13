package com.fs.fscomapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fs.fscomapp.R
import com.fs.fscomapp.auxiliares.ObtenerDisplayValorSegunTipoDeDato
import com.fs.fscomapp.auxiliares.ObtenerIconoSegunTipoDato

import com.fs.fscomapp.entities.PuntoCom


/* PuntosListAdapter es un adaptador de Room ENTITY */
class PuntosRecyclerAdapter(private var puntosList: MutableList<PuntoCom>,
                            private var valorList: MutableList<Int>, val
                            adapterOnClick: (Int, Int) -> Unit,
                            adapterOnLongClick: (Int) -> Unit)
    : RecyclerView.Adapter<PuntosRecyclerAdapter.ViewHolder>() {

    var values: List<Int> = ArrayList<Int>()

    companion object {
        private val TAG = "PuntosListAdapter"
        var index = -1

        val actionClick = 1
        val actionLongClick = 2
        val actionLongClickNoSelect = 3
        //var ListSelectAdapter: ArrayList<Int>  =  ArrayList()
        var itemSelected:Int=-1
    }

    /* 3 Metodos que usa el Recycler -------------------------------------------------------------*/

    // Aca se infla el grupo (Parent) de los Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.content_item,parent,false)
        return ( ViewHolder( view ))
    }

    override fun getItemCount(): Int {
        return puntosList.size
    }

    fun updateListValues (listValues : MutableList<Int>) { //  ListaValores : List<Int>) {
        this.values=listValues
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val funcionAux= funcionesAuxiliares()
        val v:View

        holder.setName(puntosList[position].nombre)
        holder.setDescripcion(puntosList[position].descripcion)
        holder.setImage(ObtenerIconoSegunTipoDato(puntosList[position].tipoDato))

        // CARGA EL VALOR DEL ARRAY VALORES
        holder.setTextValue ( ObtenerDisplayValorSegunTipoDeDato(puntosList[position], valorList[position]))
        holder.getCardLayout().setOnClickListener {
            adapterOnClick(position,actionClick)

            holder.setCardBackGround(Color.TRANSPARENT) //por si estaba seleccionado
            itemSelected=-1     //Lo saco de seleccion
        }

        holder.getCardLayout().setOnLongClickListener {
            if (position==itemSelected) {
                //Saco select
                holder.setCardBackGround(Color.TRANSPARENT)
                itemSelected=-1
                // si presiona 2 veces Libero Selecction
                adapterOnClick(position, actionLongClickNoSelect)
            }
            else {
                if (itemSelected == -1) {
                    holder.setCardBackGround(Color.CYAN)
                    itemSelected = position   //actualiza
                    adapterOnClick(position, actionLongClick) //notifyDataSetChanged()
                }
            }
            true
        }

    }



    // ------------------------------------------------------------------------------------------

    /*RECIBE (View xml) LO QUE VA A CARGAR EN EL RECYCLER*/
    class ViewHolder (v: View) : RecyclerView.ViewHolder(v){

        private var view: View=v

        fun setName(name : String) {
            val txt : TextView = view.findViewById(R.id.txtv_name_item_tab1)
            txt.text = name
        }

        fun setDescripcion(descripcion : String) {
            val txt : TextView = view.findViewById(R.id.txtv_descrip_item_tab1)
            txt.text = descripcion
        }

        fun setTextValue (strvalor: String) {
            val txt : TextView = view.findViewById(R.id.txtv_valor_set_tab1)
            txt.text = strvalor
        }

        fun setImage(image : Int) {
            val Imagen : ImageView = view.findViewById(R.id.img_ic_Item_tab1)
            Imagen.setImageResource(image)
        }

        fun getCardLayout ():CardView{
            return view.findViewById(R.id.item_card)
        }

        fun setCardBackGround ( ColorInt: Int) {
            var card : CardView = view.findViewById(R.id.item_card)
            card.setCardBackgroundColor(ColorInt) //
        }


    }

}