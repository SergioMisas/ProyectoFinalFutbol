package dev.sergiomisas.futdam.services.storage

import java.io.File

interface StorageService<T> {
    fun load(file: File): T
    fun save(file: File, item: T)

}
