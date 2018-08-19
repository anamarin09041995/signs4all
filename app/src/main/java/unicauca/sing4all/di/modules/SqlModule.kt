package unicauca.sing4all.di.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.fstyle.library.helper.AssetSQLiteOpenHelperFactory
import dagger.Module
import dagger.Provides
import unicauca.sing4all.data.sql.AppDatabase
import unicauca.sing4all.data.sql.dao.CharConstraintDao
import javax.inject.Singleton

@Module
class SqlModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "chars.db")
            .openHelperFactory(AssetSQLiteOpenHelperFactory())
            .build()

    @Singleton
    @Provides
    fun provideCharDao(db: AppDatabase): CharConstraintDao = db.charDao()


}
