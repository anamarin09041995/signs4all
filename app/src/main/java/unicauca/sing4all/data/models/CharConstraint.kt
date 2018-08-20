package unicauca.sing4all.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class CharConstraint{
    @PrimaryKey
    var id:Long = 0
    lateinit var letter:String
    lateinit var constraint:String
}