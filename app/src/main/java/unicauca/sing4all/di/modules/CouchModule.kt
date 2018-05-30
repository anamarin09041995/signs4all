package unicauca.sing4all.di.modules

import android.content.Context
import com.couchbase.lite.*

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import dagger.Module
import dagger.Provides
import unicauca.sing4all.R
import java.io.File
import java.net.URI
import javax.inject.Named
import javax.inject.Singleton

@Module
class CouchModule {

    @Provides
    @Singleton
    @Named("dbName")
    fun provideDataBaseName(): String = "ganko-database"

    @Provides
    @Singleton
    fun provideFolderName(context: Context): File = context.filesDir

    @Provides
    fun provideDataBase(context: Context, @Named("dbName") name: String): Database {
        val config = DatabaseConfiguration(context)
        val db = Database(name, config)
        db.createIndex("TypeIndex", IndexBuilder.valueIndex(ValueIndexItem.property("type")))
        return db
    }

    @Provides
    @Singleton
    fun provideMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        return mapper
    }

    @Provides
    @Singleton
    fun provideReplicator(context: Context, database: Database): Replicator {
        val config = ReplicatorConfiguration(database, URLEndpoint(URI(context.getString(R.string.url_sync))))
        config.replicatorType = ReplicatorConfiguration.ReplicatorType.PUSH_AND_PULL
        config.isContinuous = true
        //config.authenticator = BasicAuthenticator("demo", "123456")
        config.channels = mutableListOf("word")
        return Replicator(config)
    }


}
