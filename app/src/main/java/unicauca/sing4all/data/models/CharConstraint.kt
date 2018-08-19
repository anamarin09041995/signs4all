package unicauca.sing4all.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class CharConstraint(@PrimaryKey val id:Long, val char:String, val constraint:String)