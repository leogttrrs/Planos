package br.com.gutterres.planos

data class Plano(
    val id: Long,
    val nome: String,
    val categoria: String,
    val status: String,
    val icone: String?,
    val nota: Int?,
    val fotosRecordacao: List<String>,
    val dataPlanejada: String?,
    val dataConclusao: String?,
    val observacao: String?
)