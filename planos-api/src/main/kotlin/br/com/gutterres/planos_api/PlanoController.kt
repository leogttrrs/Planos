package br.com.gutterres.planos_api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController // Anotação que combina @Controller e @ResponseBody, preparando a classe para receber requisições web
@RequestMapping("/planos") // Define que todos os endpoints nesta classe começarão com "/planos"
class PlanoController(private val repository: PlanoRepository) { // Injetamos nosso repositório aqui

    // Endpoint para LISTAR todos os planos ou filtrar por categoria
    // Ex: GET /planos -> lista todos
    // Ex: GET /planos?categoria=FILMES -> lista só os de filmes
    @GetMapping
    fun listarPlanos(@RequestParam categoria: Categoria?): List<Plano> {
        return if (categoria != null) {
            repository.findByCategoria(categoria)
        } else {
            repository.findAll()
        }
    }

    // Endpoint para BUSCAR um plano específico pelo seu ID
    // Ex: GET /planos/1
    @GetMapping("/{id}")
    fun buscarPlanoPorId(@PathVariable id: Long): ResponseEntity<Plano> {
        return repository.findById(id)
            .map { plano -> ResponseEntity.ok(plano) } // Se encontrar, retorna o plano com status 200 OK
            .orElse(ResponseEntity.notFound().build()) // Se não encontrar, retorna 404 Not Found
    }

    // Endpoint para CRIAR um novo plano
    // Ex: POST /planos (com os dados do plano no corpo da requisição)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Define que a resposta de sucesso será 201 Created
    fun criarPlano(@RequestBody plano: Plano): Plano {
        return repository.save(plano)
    }

    // Endpoint para ATUALIZAR um plano existente
    // Ex: PUT /planos/1 (com os dados atualizados no corpo da requisição)
    @PutMapping("/{id}")
    fun atualizarPlano(@PathVariable id: Long, @RequestBody planoAtualizado: Plano): ResponseEntity<Plano> {
        return repository.findById(id)
            .map { planoExistente ->
                val novoPlano = planoExistente.copy(
                    nome = planoAtualizado.nome,
                    categoria = planoAtualizado.categoria,
                    status = planoAtualizado.status,
                    icone = planoAtualizado.icone,
                    nota = planoAtualizado.nota,
                    fotosRecordacao = planoAtualizado.fotosRecordacao,
                    dataPlanejada = planoAtualizado.dataPlanejada,
                    dataConclusao = planoAtualizado.dataConclusao,
                    observacao = planoAtualizado.observacao
                )
                ResponseEntity.ok(repository.save(novoPlano))
            }
            .orElse(ResponseEntity.notFound().build())
    }

    // Endpoint para DELETAR um plano
    // Ex: DELETE /planos/1
    @DeleteMapping("/{id}")
    fun deletarPlano(@PathVariable id: Long): ResponseEntity<Void> {
        return repository.findById(id).map { plano ->
            repository.delete(plano)
            ResponseEntity.noContent().build<Void>() // Retorna 204 No Content
        }.orElse(ResponseEntity.notFound().build())
    }
}
