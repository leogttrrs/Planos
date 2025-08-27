package br.com.gutterres.planos_api

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository // Anotação que marca esta interface como um componente do Spring
interface PlanoRepository : JpaRepository<Plano, Long> {
    // A mágica do Spring Data JPA acontece aqui!
    // Nós podemos declarar métodos e, pelo nome deles, o Spring já sabe
    // como criar a consulta no banco de dados.

    // Exemplo: Este método vai buscar todos os planos de uma determinada categoria.
    fun findByCategoria(categoria: Categoria): List<Plano>
}
