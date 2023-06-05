package dev.sergiomisas.futdam.services.storage.json


import com.google.gson.GsonBuilder
import dev.sergiomisas.futdam.dto.EquipoDto
import dev.sergiomisas.futdam.services.storage.StorageService
import java.io.File

class JsonStorageService : StorageService<EquipoDto> {


    override fun load(file: File): EquipoDto {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val json = file.readText()
        val item = gson.fromJson(json, EquipoDto::class.java)
        return item
    }

    override fun save(file: File, item: EquipoDto) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val json = gson.toJson(item)
        file.writeText(json)
    }

}
