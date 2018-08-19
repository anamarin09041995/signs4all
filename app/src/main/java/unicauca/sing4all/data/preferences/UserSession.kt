package unicauca.sing4all.data.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor(private val preferences: SharedPreferences) {


    var algorithm: String
        get() = preferences.getString("algorithm", Algorithm.STAGES)!!
        set(value) { preferences.edit().putString("algorithm", value).apply() }

    var testMode: Boolean
        get() = preferences.getBoolean("testMode", false)
        set(value) { preferences.edit().putBoolean("testMode", value).apply() }

}