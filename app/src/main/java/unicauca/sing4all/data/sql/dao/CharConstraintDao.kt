package unicauca.sing4all.data.sql.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Single
import unicauca.sing4all.data.models.CharConstraint

@Dao
interface CharConstraintDao{

    @Query("SELECT * FROM charconstraint WHERE `constraint` = :constraint")
    fun getChars(constraint:String):Single<List<CharConstraint>>

}