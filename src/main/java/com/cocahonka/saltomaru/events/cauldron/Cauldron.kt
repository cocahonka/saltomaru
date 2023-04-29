package com.cocahonka.saltomaru.events.cauldron

import com.cocahonka.saltomaru.base.patterns.CachedPool
import com.cocahonka.saltomaru.database.entities.Locate
import com.cocahonka.saltomaru.database.repositories.CauldronRepository

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

        private val deletedLocations: HashSet<Locate> = HashSet()
        private val createdLocations: HashSet<Locate> = HashSet()

        override fun onDelete(item: Cauldron) {
            deletedLocations.add(item.locate)
        }

        override fun onCreate(item: Cauldron) {
            deletedLocations.remove(item.locate)
            createdLocations.add(item.locate)
        }

        fun synchronizeCachedWithDatabase() {
            repository.synchronizeCachedWithDatabase(deletedLocations, createdLocations)
            deletedLocations.clear()
            createdLocations.clear()
        }
    }

}