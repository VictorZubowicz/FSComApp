package com.fs.fscomapp.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.github.nikartm.button.FitButton
import com.google.android.material.snackbar.Snackbar

import com.fs.fscomapp.R


class dialog_fragment :  DialogFragment() {


    lateinit var v: View

    lateinit var title: TextView
    lateinit var description: TextView
    lateinit var edtCode: EditText
    lateinit var btnAccept: FitButton
    lateinit var btnCancel: FitButton

    lateinit var dialog_vmodel: Dialog_viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.dialog_fragment, container, false)

        title=v.findViewById(R.id.txtv_dialog_title)
        description=v.findViewById(R.id.txtv_dialog_description)
        edtCode = v.findViewById(R.id.edt_code_dialog)
        btnAccept = v.findViewById(R.id.btn_acept_dialog)
        btnCancel = v.findViewById(R.id.btn_cancel_dialog)
        return v
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onStart() {
        super.onStart()

        dialog_vmodel = ViewModelProvider(requireActivity()).get(Dialog_viewModel::class.java)

        title.text=dialog_vmodel.title
        description.text=dialog_vmodel.description
        btnCancel.setOnClickListener() {
            dismiss()
        }

        btnAccept.setOnClickListener {
            if (edtCode.length() > 0) {
                dialog_vmodel.input.value = edtCode.text.toString()
                dismiss()
            } else {
                Snackbar.make(v, "no ha ingresado ningun dato", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}



