package com.wj.kotlin.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.wj.kotlin.R
import com.wj.kotlin.ui.ui.theme.KotlinApplicationTheme

class FirstComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //对ComponentActivity的扩展函数
        setContent {
            Greeting("zhang san")
            GreetingImage()
        }
    }
}

@Composable //代表式Compose的微件
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable

fun GreetingImage() {
    Image(painter = painterResource(id = R.mipmap.ic_launcher), contentDescription = null)
}

@Preview(showBackground = false)
@Composable
fun DefaultPreview() {
    //KotlinApplicationTheme {
        Greeting("Android")
        GreetingImage()
   /// }
}