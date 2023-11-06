package com.example.jaimeveraevaluacion2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jaimeveraevaluacion2.ui.theme.JaimeVeraEvaluacion2Theme

class AgregaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            agregarProducto()
        }
    }
}

@Composable
fun agregarProducto(){
    val contexto = LocalContext.current

    Text("panatalla2")
    Spacer(modifier = Modifier.height(200.dp))

    Button(onClick = {
        val intent = Intent(contexto, MainActivity::class.java)
        contexto.startActivity(intent)
    }){
        Text(text = "Home")
    }
}