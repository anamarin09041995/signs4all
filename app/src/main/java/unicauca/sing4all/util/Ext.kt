package unicauca.sing4all.util

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(layout:Int): View = LayoutInflater
        .from(context)
        .inflate(layout, this, false)

fun SharedPreferences.save(vararg data: Pair<String, Any>) {
    val editor = edit()
    data.forEach { (key, value) ->
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Long -> editor.putLong(key, value)

        }

    }
    editor.apply()
}