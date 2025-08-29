package br.com.gutterres.planos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.gutterres.planos.ui.theme.PlanosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlanosApp()
        }
    }
}

@Composable
fun PlanosApp() {
    PlanosTheme {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(navController = navController, startDestination = "categorias") {
                composable("categorias") {
                    TelaCategorias(navController = navController)
                }
                composable("listaPlanos/{categoria}") { backStackEntry ->
                    val categoria = backStackEntry.arguments?.getString("categoria")
                    TelaListaDePlanos(categoria = categoria, navController = navController)
                }
            }
        }
    }
}

@Composable
fun TelaCategorias(navController: NavController) {
    val categorias = listOf("Filmes", "Séries", "Jogos", "Receitas", "Viagens", "Outros")
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Nossos planos 💖",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        categorias.forEach { categoria ->
            CartaoCategoria(
                nomeCategoria = categoria,
                onClick = { navController.navigate("listaPlanos/$categoria") }
            )
        }
    }
}

@Composable
fun CartaoCategoria(nomeCategoria: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = nomeCategoria, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaListaDePlanos(
    categoria: String?,
    navController: NavController,
    planosViewModel: PlanosViewModel = viewModel() // Injeção do ViewModel
) {
    // Observamos o estado dos planos e do carregamento do ViewModel
    val planos by planosViewModel.planos.collectAsState()
    val carregando by planosViewModel.carregando.collectAsState()

    // LaunchedEffect garante que a busca de dados ocorra apenas uma vez
    // quando a tela é carregada pela primeira vez com uma categoria.
    LaunchedEffect(categoria) {
        categoria?.let {
            planosViewModel.carregarPlanos(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Planos de: ${categoria ?: "Desconhecido"}") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Navegar para tela de adicionar plano */ }) {
                Icon(Icons.Filled.Add, "Adicionar novo plano")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (carregando) {
                CircularProgressIndicator()
            } else if (planos.isEmpty()) {
                Text("Nenhum plano nesta categoria ainda. Que tal adicionar um?")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(planos) { plano ->
                        CartaoPlanoItem(plano = plano)
                    }
                }
            }
        }
    }
}

@Composable
fun CartaoPlanoItem(plano: Plano) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = plano.nome, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Status: ${plano.status}", fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaListaDePlanosPreview() {
    PlanosTheme {
        val navController = rememberNavController()
        // O preview continuará usando dados estáticos, pois não pode fazer chamadas de API.
        TelaListaDePlanos(categoria = "Filmes", navController = navController)
    }
}