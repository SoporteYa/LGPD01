package es.informaticoya.lgpd01

data class Pregunta(
    val enunciado: String = "",
    val opciones: List<String> = emptyList()
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "enunciado" to enunciado,
            "opciones" to opciones
        )
    }
}


