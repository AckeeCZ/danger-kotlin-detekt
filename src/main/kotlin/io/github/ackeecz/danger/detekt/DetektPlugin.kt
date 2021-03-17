package io.github.ackeecz.danger.detekt

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import systems.danger.kotlin.sdk.DangerPlugin
import java.io.File
import java.io.FileInputStream

/**
 * Plugin for a danger-kotlin processing outputs of detekt tool.
 */
object DetektPlugin : DangerPlugin() {

    override val id = "danger-kotlin-detekt"

    fun parseAndReport(vararg reportFiles: File) {
        val mapper = XmlMapper()
        reportFiles.forEach { file ->
            FileInputStream(file).use { fileInputStream ->
                val report = mapper.readValue(
                    fileInputStream,
                    DetektReport::class.java
                )
                report(report)
            }
        }
    }

    private fun report(report: DetektReport) {
        report.files.forEach { file ->
            val realFile = File(file.name)
            file.errors.forEach { error ->
                context.warn(
                    message = "Detekt: ${error.message}, rule: ${error.source}",
                    file = realFile.name,
                    line = error.line.toIntOrNull() ?: 0
                )
            }
        }
    }
}

@JacksonXmlRootElement(namespace = "checkstyle")
@JsonIgnoreProperties(ignoreUnknown = true)
data class DetektReport(
    @field:JsonProperty("file")
    @field:JacksonXmlElementWrapper(useWrapping = false)
    val files: List<ReportFile> = emptyList()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReportFile(
    @field:JacksonXmlProperty val name: String = "",
    @field:JsonProperty("error")
    @field:JacksonXmlElementWrapper(useWrapping = false) val errors: List<ReportError> = emptyList()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReportError(
    @field:JacksonXmlProperty val line: String = "",
    @field:JacksonXmlProperty val column: String = "",
    @field:JacksonXmlProperty val severity: String = "",
    @field:JacksonXmlProperty val message: String = "",
    @field:JacksonXmlProperty val source: String = "",
)