package com.example.multiversegads.ui.stories

import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is gallery Fragment"
//    }
//    val text: LiveData<String> = _text



}

private fun getItemsLists(): ArrayList<String>{
    val list = ArrayList<String>()

    for (i in 1..60){
        list.add("Item $i")
    }

    return list
}
