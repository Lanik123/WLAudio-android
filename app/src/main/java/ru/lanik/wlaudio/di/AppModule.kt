package ru.lanik.wlaudio.di

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import ru.lanik.wlaudio.data.SettingsManager
import ru.lanik.wlaudio.di.qualifier.SettingsCoroutineScopeQualifier
import ru.lanik.wlaudio.model.SettingsModel
import ru.lanik.wlaudio.ui.screen.main.MainViewModelFactory
import ru.lanik.wlaudio.ui.screen.settings.SettingsViewModelFactory
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @SettingsCoroutineScopeQualifier
    fun provideSettingsCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    @Provides
    @Singleton
    fun provideSettingsModelSerializer(): Serializer<SettingsModel> = object : Serializer<SettingsModel> {
        override val defaultValue: SettingsModel = SettingsModel()
        override suspend fun readFrom(input: InputStream): SettingsModel {
            try {
                return Json.decodeFromString(
                    SettingsModel.serializer(),
                    input.readBytes().decodeToString(),
                )
            } catch (serialization: SerializationException) {
                throw CorruptionException("Unable to read SettingsModel", serialization)
            }
        }
        override suspend fun writeTo(t: SettingsModel, output: OutputStream) {
            output.write(
                Json.encodeToString(SettingsModel.serializer(), t)
                    .encodeToByteArray(),
            )
        }
    }

    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context,
        @SettingsCoroutineScopeQualifier coroutineScope: CoroutineScope,
        settingsModelSerializer: Serializer<SettingsModel>,
    ): DataStore<SettingsModel> = DataStoreFactory.create(
        serializer = settingsModelSerializer,
        scope = coroutineScope,
        produceFile = { context.dataStoreFile("settings.json") },
    )

    @Provides
    @Singleton
    fun provideSettingsManager(
        settingsDataStore: DataStore<SettingsModel>,
    ): SettingsManager = SettingsManager(settingsDataStore)

    @Provides
    @Singleton
    fun provideMainViewModelFactory(): MainViewModelFactory = MainViewModelFactory()

    @Provides
    @Singleton
    fun provideSettingsViewModelFactory(): SettingsViewModelFactory = SettingsViewModelFactory()
}