package com.wj.kotlin.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.buildSpannedString
import com.wj.kotlin.R
import com.wj.kotlin.ui.ui.theme.KotlinApplicationTheme

class FirstComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //对ComponentActivity的扩展函数
        setContent {
            BoxLayout()
            Spacer(
                modifier = Modifier
                    .background(Color.Gray)
                    .padding(10.dp)
                    .fillMaxWidth()
            )
            clickableText()
        }
    }
}

@Composable //代表式Compose的微件
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun GreetingImage() {
    Image(painter = painterResource(id = R.drawable.ic_launcher123), contentDescription = null)
}

@Composable
fun Item() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher123),
            contentDescription = null
        )
        Spacer(
            modifier = Modifier
                .width(12.dp)
                .height(12.dp)
                .padding(10.dp, 20.dp)
        )
        Column {
            Text(
                text = "title", modifier = Modifier
                    .background(color = Color.Red)
                    .fillMaxWidth()
                    .sizeIn()
            )
            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .background(color = Color.Red)
            )
            Text(text = "message")
        }
    }
}

@Composable
fun Column() {
    Row(
        modifier = Modifier
            .background(Color.Cyan)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "123", modifier = Modifier
                .background(Color.Red, CircleShape)
                .padding(10.dp)
            // .clip(CircleShape)
            //  .border(1.dp, Color.Black, CircleShape)
        )
        Text(
            text = "456",
            modifier = Modifier
                .background(Color.Green, RectangleShape)
                .padding(20.dp)
        )
        Text(text = "789", modifier = Modifier.background(Color.Yellow))
        Text(text = "012", modifier = Modifier.background(Color.Blue))
    }
}

@Composable
fun HelloList() {
    LazyColumn(content = {
        item { Item() }
        item { Item() }
        item { Item() }
    })
}

fun onClick() {
    println("点击的是这里")

}

@Composable
fun BoxLayout() {
    Box(
        modifier = Modifier
            .background(Color.Cyan)
            .clickable(onClick = ::onClick)
            .padding(10.dp)
            .size(100.dp, 100.dp)

    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher123),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
        Text(
            text = "123",
            modifier = Modifier
                //.offset(30.dp, 30.dp)
                .background(Color.Red, CircleShape)
                .align(Alignment.BottomEnd),
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

//自定义Row
// ViewGroup onmeasure、onlayout、onDraw ->
//
@Composable
fun ParentLayout(modifier: Modifier, content: @Composable () -> Unit) {
    //1。对布局进行测量
    val measurePolicy = MeasurePolicy { measurables, constraints ->
        //盒子模型
        val placeable = measurables.map { child ->
            //设置child的宽高
            child.measure(constraints = constraints)

        }
        //放置child
        layout(constraints.minWidth, constraints.maxHeight) {
            //placeable.
        }

    }
}

@Composable
fun text() {
    SelectionContainer {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Blue)) {
                    append("Blue")
                }
                append("111")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Bold")
                }
                append("111")
                withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append("HelloHelloHelloHelloHelloHelloHelloHelloHello")
                    }
                }
                append("111dafdsafdsfdsf")
            },
            fontSize = 30.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun clickableText() {
    val annotatedText = buildAnnotatedString {
        append("Click")
        pushStringAnnotation(tag = "URL", annotation = "https://baidu.com")
        withStyle(style = SpanStyle(color = Color.Blue)) {
            append("here")
        }
        //将"URL"注释附加到下面的内容直到调用pop()
        pop()
    }

    ClickableText(text = annotatedText, onClick = { offset ->
        println("clickable is ${offset}")
        val url = annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
        url?.let {
            println(it.first())
        }

    })
}

@Preview
@Composable
fun DefaultPreview() {
    Column {
        HelloList()
        Spacer(
            modifier = Modifier
                .background(Color.Gray)
                .padding(10.dp)
                .fillMaxWidth()
        )
        Column()
        Spacer(
            modifier = Modifier
                .background(Color.Gray)
                .padding(10.dp)
                .fillMaxWidth()
        )
        BoxLayout()
        Spacer(
            modifier = Modifier
                .background(Color.Gray)
                .padding(10.dp)
                .fillMaxWidth()
        )
        text()
        Spacer(
            modifier = Modifier
                .background(Color.Gray)
                .padding(10.dp)
                .fillMaxWidth()
        )
        clickableText()
    }

}