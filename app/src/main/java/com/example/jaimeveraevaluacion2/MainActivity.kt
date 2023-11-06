package com.example.jaimeveraevaluacion2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.jaimeveraevaluacion2.db.AppDatabase
import com.example.jaimeveraevaluacion2.db.Compra
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch (Dispatchers.IO){
            val compraDao = AppDatabase.getInstance(this@MainActivity).compraDao()
            val cantRegistros = compraDao.contar()
            //Se insertan estos registros si no hay nada
            if (cantRegistros < 1){
                compraDao.insertar(Compra(0, "Verduras", false))
                compraDao.insertar(Compra(0, "Leche", false))
                compraDao.insertar(Compra(0, "Articulos de Aseo", false))
                compraDao.insertar(Compra(0, "Comida para mascota", false))
            }
        }


        setContent {
            ListaComprasUI()
        }
    }
}

@Composable
fun ListaComprasUI() {
    val contexto = LocalContext.current
    val (compras, setCompras) = remember { mutableStateOf(emptyList<Compra>()) }
    val textoboton = LocalContext.current.resources.getString(R.string.agregar)

    LaunchedEffect(compras) {
        withContext(Dispatchers.IO) {
            val dao = AppDatabase.getInstance(contexto).compraDao()
            setCompras(dao.findAll())
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment =Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().weight(1f)
        ) {
            items(compras) { compra ->
                CompraItemUI(compra) {
                    setCompras(emptyList<Compra>())
                }
            }
        }

        Spacer(modifier = Modifier.width(20.dp))
        //Boton para ir a la otra actividad
        Button(onClick = {
            val intent = Intent(contexto, AgregaActivity::class.java)
            contexto.startActivity(intent)
        }) {
            Text(textoboton)
            //Text("Agregar más productos")
        }
    }
}

@Composable
fun CompraItemUI(compra:Compra, onSave:() -> Unit ={} ){
    val contexto = LocalContext.current
    val alcanceCorrutina = rememberCoroutineScope()

    Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 20.dp)
        ) {
        if (compra.realizada) {
           //Recurso dfrawable para imagen de compra realizada
            // Es un carrito con ticket
            Image(
                painter = painterResource(id = R.drawable.comprado3),
                contentDescription = "Icono para comprado",
                modifier = Modifier.clickable {
                    alcanceCorrutina.launch(Dispatchers.IO) {
                        val dao = AppDatabase.getInstance(contexto).compraDao()
                        compra.realizada = false
                        dao.actualizar(compra)
                        onSave()
                    }
                }
            )
        } else {
            Icon(
                Icons.Filled.AddCircle,
                contentDescription = "compra por hacer",
                modifier = Modifier.clickable {
                    alcanceCorrutina.launch(Dispatchers.IO) {
                        val dao = AppDatabase.getInstance(contexto).compraDao()
                        compra.realizada = true
                        dao.actualizar(compra)
                        onSave()
                    }
                }
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = compra.compra,
            modifier = Modifier.weight(2f)
        )
        Icon(
            Icons.Filled.Delete,
            contentDescription = "Eliminar compra",
            modifier = Modifier.clickable {
                alcanceCorrutina.launch(Dispatchers.IO) {
                    val dao = AppDatabase.getInstance(contexto).compraDao()
                    dao.eliminar(compra)
                    onSave()
                }
            }
        )
    }
}

//Utilicé las vistas previas para ver como avanzaban los cambios
@Preview(showBackground = true)
@Composable
fun CompraItemUIPreview(){
    val compra = Compra(1, "Verduras", true)
    CompraItemUI(compra)
}

@Preview(showBackground = true)
@Composable
fun CompraItemUIPreview2(){
    val compra2 = Compra(2, "Comida para mascota", false)
    CompraItemUI(compra2 )
}

