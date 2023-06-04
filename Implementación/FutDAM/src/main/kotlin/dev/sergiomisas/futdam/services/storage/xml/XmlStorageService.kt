package dev.sergiomisas.futdam.services.storage.xml

import dev.sergiomisas.futdam.dto.EquipoDto
import dev.sergiomisas.futdam.services.storage.StorageService
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.stream.Format
import java.io.File

class XmlStorageService: StorageService<EquipoDto> {
    override fun load(file: File): EquipoDto {
        return Persister().read(EquipoDto::class.java, file)
    }

    override fun save(file: File, item: EquipoDto) {
        Persister(Format("""<?xml version="1.0" encoding="UTF-8"?>""")).write(item, file)
    }
}
