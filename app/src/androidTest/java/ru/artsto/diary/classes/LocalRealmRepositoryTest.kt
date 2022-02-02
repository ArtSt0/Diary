package ru.artsto.diary.classes

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.artsto.diary.realmModels.Task
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class LocalRealmRepositoryTest {

    private lateinit var realm:Realm
    private lateinit var localRealmRepository: LocalRealmRepository
    private lateinit var task: Task
    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Realm.init(appContext)
        val config = RealmConfiguration.Builder()
            .inMemory()
            .name("test_realm")
            .allowWritesOnUiThread(true) //разрешение работать в потоке UI
            .allowQueriesOnUiThread(true)
            .schemaVersion(0)
            .build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()

        localRealmRepository = LocalRealmRepository()
        task = Task()
        task.name = "test_name"
        task.description = "test_description"
        task.date_start = "164546640"
        task.date_finish = "164547000"
        localRealmRepository.create(task = task)
    }

    @After
    fun tearDown() {
        realm.close()
    }

    @Test
    fun searchTaskTest(){
        val actual = localRealmRepository.searchTask(task = task)
        assertEquals("должны быть равны", task.id, actual?.id)
    }
}