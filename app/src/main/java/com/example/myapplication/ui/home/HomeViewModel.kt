package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Steps to test this app :" +
                "\n\n1. click on Map menu [at bottom]." +
                "\n2.Map Screen will open." +
                "\n3. Provide Location permission." +
                "\n4. In Few seconds, map will show your current location." +
                "\n5. create a marker by long-pressing , anywhere on the map." +
                "\n6. Path(Ployline) will be shown if there is more than 1 markers." +
                "\n\n" +
                "Note: App is not stable.If something went wrong then try to restart the app."


    }
    val text: LiveData<String> = _text
}