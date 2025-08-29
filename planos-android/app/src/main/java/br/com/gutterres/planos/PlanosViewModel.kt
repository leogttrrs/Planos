package br.com.gutterres.planos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlanosViewModel : ViewModel() {

    // Um "StateFlow" para guardar a lista de planos e notificar a UI quando ela mudar.
    private val _planos = MutableStateFlow<List<Plano>>(emptyList())
    val planos: StateFlow<List<Plano>> = _planos

    // Um StateFlow para controlar o estado de carregamento
    private val _carregando = MutableStateFlow(false)
    val carregando: StateFlow<Boolean> = _carregando

    fun carregarPlanos(categoria: String) {
        // Usamos viewModelScope para garantir que a chamada de rede
        // seja cancelada se o usuário sair da tela.
        viewModelScope.launch {
            _carregando.value = true
            try {
                // Chamamos nossa API através do Retrofit
                _planos.value = RetrofitClient.instance.getPlanosPorCategoria(categoria)
            } catch (e: Exception) {
                // Em um app real, trataríamos o erro de forma mais robusta (ex: mostrando um Toast)
                e.printStackTrace()
                _planos.value = emptyList()
            } finally {
                _carregando.value = false
            }
        }
    }
}