package unicauca.sing4all.data.sql

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import unicauca.sing4all.data.models.CharConstraint
import unicauca.sing4all.data.sql.dao.CharConstraintDao

@Database(version = 1, entities = [CharConstraint::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun charDao(): CharConstraintDao

}