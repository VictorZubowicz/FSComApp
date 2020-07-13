package com.fs.fscomapp.dialogs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Dialog_viewModel: ViewModel() {

    var title : String="Dialog"
    var description : String="DialogDescrip"
    var input : MutableLiveData<String> = MutableLiveData<String>()
}
