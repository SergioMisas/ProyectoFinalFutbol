package dev.sergiomisas.futdam.repositories

interface CrudRepository<T, ID> {
    fun save(item: T): T
    fun saveAll(items: List<T>): List<T>
    fun findAll(): List<T>
    fun findById(id: ID): T?
    fun update(item: T): T?
    fun delete(item: T): T?
}
