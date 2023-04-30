package com.cocahonka.saltomaru.events.cauldron

import com.cocahonka.saltomaru.base.patterns.CachedPool
import com.cocahonka.saltomaru.config.SaltomaruConfig as Config
import com.cocahonka.saltomaru.database.entities.Locate
import com.cocahonka.saltomaru.database.repositories.CauldronRepository

/**
 * Класс [Cauldron] представляет собой объект котла с определенным местоположением [Locate].
 * Этот класс имеет закрытый конструктор и использует компаньон-объект [Cache] для кеширования и
 * переиспользования объектов котлов.
 *
 * Компаньон-объект [Cache] наследует [CachedPool], предоставляя доступ к объектам [Cauldron] с
 * помощью указанного местоположения [Locate]. Он также синхронизирует удаленные и созданные
 * объекты котлов с базой данных через [CauldronRepository].
 *
 * @property locate местоположение котла типа [Locate]
 */
class Cauldron private constructor(locate: Locate) {
    var locate: Locate = locate
        private set

    companion object Cache : CachedPool<Cauldron, Locate>(
        factory = ::Cauldron,
        update = { cauldron, locate ->
            cauldron.locate = locate
        }
    ) {
        private val repository = CauldronRepository()

        init {
            val mockLocate = Locate(0, 0, 0, 0)
            val objects = List(Config.ObjectPool.CAULDRONS_INIT_SIZE) { Cauldron(mockLocate) }
            initPool(objects)

            val data = repository.loadDataFromDatabase()
            val instances = data.associateWith { Cauldron(it) }
            initInstances(instances)
        }

        private val deletedLocations: HashSet<Locate> = HashSet()
        private val createdLocations: HashSet<Locate> = HashSet()

        override fun onDelete(item: Cauldron) {
            createdLocations.remove(item.locate)
            deletedLocations.add(item.locate)
        }

        override fun onCreate(item: Cauldron) {
            deletedLocations.remove(item.locate)
            createdLocations.add(item.locate)
        }

        /**
         * Синхронизирует кешированные объекты [Cauldron] с базой данных.
         * Загружает удаленные и созданные объекты котлов в [CauldronRepository].
         */
        fun synchronizeCachedWithDatabase() {
            repository.synchronizeCachedWithDatabase(deletedLocations, createdLocations)
            deletedLocations.clear()
            createdLocations.clear()
        }
    }

}