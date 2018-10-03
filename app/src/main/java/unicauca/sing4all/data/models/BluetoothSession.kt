package unicauca.sing4all.data.models

import android.content.SharedPreferences
import unicauca.sing4all.util.save
import javax.inject.Inject

class BluetoothSession @Inject constructor(val prefs: SharedPreferences){

    var address:String
        get() = prefs.getString(KEY_BTADDRESS,"")
        set(value) = prefs.save(KEY_BTADDRESS to value)


    companion object {
        private val KEY_BTADDRESS = "address"
}}