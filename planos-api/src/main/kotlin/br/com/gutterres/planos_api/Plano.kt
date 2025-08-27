package br.com.gutterres.planos_api

import jakarta.persistence.*
import java.time.LocalDate

// Enum para representar as categorias de forma segura
enum class Categoria {
    FILMES,
    SERIES,
    JOGOS,
    RECEITAS,
    VIAGENS,
    OUTROS
}

// Enum para os status
enum class Status {
    NAO_INICIADO,
    EM_ANDAMENTO,
    CONCLUIDO
}

@Entity // Anotação que diz ao JPA que esta classe é uma tabela no banco de dados
data class Plano(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // Chave primária, gerada automaticamente

    var nome: String,

    @Enumerated(EnumType.STRING) // Diz para guardar o nome do enum (ex: "FILMES") no banco
    var categoria: Categoria,

    @Enumerated(EnumType.STRING)
    var status: Status = Status.NAO_INICIADO, // Valor padrão

    var icone: String? = null, // O '?' indica que o campo é opcional (pode ser nulo)

    var nota: Int? = null, // Opcional, só para planos concluídos

    // Para as fotos, vamos guardar uma lista de links (URLs) para elas.
    // O armazenamento de arquivos é um tópico mais avançado, por enquanto faremos assim.
    @ElementCollection
    var fotosRecordacao: MutableList<String> = mutableListOf(),

    var dataPlanejada: LocalDate? = null, // Opcional

    var dataConclusao: LocalDate? = null, // Opcional

    @Column(length = 1000) // Define um tamanho maior para o campo de texto
    var observacao: String? = null
)
