package com.wj.kotlin.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.wj.kotlin.R
import com.wj.kotlin.mvvm.FirstMvvmActivity
import kotlin.properties.Delegates

class FirstComposeActivity : ComponentActivity() {

    val id: Int by Delegates.notNull<Int>()
//    val day: Int by Delegates.observable(0,
//        { property: KProperty<*>, oldValue: Int, newValue: Int ->
//            println()
//        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //???ComponentActivity???????????????
        setContent {
            //TextFieldComposable()
            //MutableStateComposable()
            NavigatorComposable()
        }
    }
}

//val context = LocalContext.current
@Composable
fun NavigatorComposable() {
    Button(onClick = {
        println("onClick")
        // val nav = rememberNavController()
    } ) {
        Text(text = "?????????????????????")
    }
}

@Composable
fun NavigatorClickComposable() {
    println(" onClick ")
    val navController = rememberNavController()

}

@Composable //?????????Compose?????????
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


@ExperimentalFoundationApi
@Composable
fun LazyColumnComposable(messages: List<String>) {
    LazyColumn(
        content = {
            items(3) { Item() }
            stickyHeader {
                Text("header", color = Color.Yellow)
            }
            items(messages) {
                Message(msg = it)
            }
        },
        contentPadding = PaddingValues(50.dp, 50.dp),
        verticalArrangement = Arrangement.spacedBy(50.0.dp)
    )

//    LazyVerticalGrid(cells = GridCells.Fixed(3)) {
//        items(mutableListOf("a", "b", "c")) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_launcher123),
//                contentDescription = "12"
//            )
//        }
//    }

//    LazyRow({}
//        content = { items(4) { Text(text = "${it},123") } },
//        horizontalArrangement = Arrangement.SpaceEvenly,
//        modifier = Modifier.fillMaxWidth()
//    )
}

@Composable
fun Message(msg: String) {
    Text(
        text = msg,
        fontSize = 30.sp,
        color = Color.Red,
        modifier = Modifier
            .background(Color.Yellow)
            .fillMaxWidth()
    )
}

fun onClick() {
    println("??????????????????")

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

//?????????Row
// ViewGroup onmeasure???onlayout???onDraw ->
//
@Composable
fun ParentLayout(modifier: Modifier, content: @Composable () -> Unit) {
    //1????????????????????????
    val measurePolicy = MeasurePolicy { measurables, constraints ->
        //????????????
        val placeable = measurables.map { child ->
            //??????child?????????
            child.measure(constraints = constraints)

        }
        //??????child
        layout(constraints.minWidth, constraints.maxHeight) {
            //placeable.
        }

    }
}

@Composable
fun TextComposable() {
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
        //???"URL"??????????????????????????????????????????pop()
        pop()
    }

    ClickableText(text = annotatedText, onClick = { offset ->
        println("clickable is ${offset}")
        // url????????????[Range(item=https://baidu.com, start=5, end=9, tag=URL)]
        val url = annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
        println("url ${url}")
        //????????????????????????
        url?.let {
            println(it.firstOrNull()?.let {
                println("${it.item}, ${it.start},${it.end}, ${it.tag}")
            })
        }
    })
}

@Composable
fun TextFieldComposable() {
    /**
     * ???????????????????????????????????????????????????mutableStateOf???????????????
     * ????????????????????????????????????????????????????????????
     */
    var text by remember { mutableStateOf("Hello") }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            println("${it}")
        },
        label = { Text("title") },
        placeholder = { Text("hint") },
        leadingIcon = { GreetingImage() },
        //trailingIcon = { BoxLayout()}
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            autoCorrect = true,
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Search
        ),
        visualTransformation = PasswordVisualTransformation('*')
    )
    var text1 by remember {
        mutableStateOf("Hello\nWorld")
    }
    BasicTextField(value = text1, onValueChange = {})
}

@Composable
fun CanvasComposable() {

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(color = Color.Red, RectangleShape)
    ) {
        // inset(20.0f, 0.0f) {
        drawCircle(
            color = Color.Blue,
            radius = size.height / 2,
            center = Offset(size.height / 2, size.height / 2)
        )
        //}
        withTransform({
            // translate(0.0f, 0.0f)
            // rotate(degrees = 90f)
            scale(0.5f, 0.5f)
        }) {
            drawRect(
                color = Color.Yellow,
                topLeft = Offset(size.height, 0.0f),
                size = Size(size.height * 2, size.height)
            )
        }

        // rotate(degrees = 20.0f) {
        drawLine(
            start = Offset(size.height * 3, size.height / 2),
            end = Offset(size.width, size.height / 2),
            color = Color.Blue,
            strokeWidth = 20.0f
        )
        //  }
    }
}

data class City(val name: String, val country: String)

val CitySaver = run {
    val nameKey = "Name"
    val countryKey = "Country"
    mapSaver(
        //?????????????????????map??????????????????????????????
        save = { mapOf(nameKey to it.name, countryKey to it.country) },
        //?????????????????????map???????????????????????????????????????????????????
        restore = { City(it[nameKey] as String, it[countryKey] as String) })
}

val CityListSaver = listSaver<City, Any>(
    save = { listOf(it.name, it.country) },
    restore = { City(it[0] as String, it[1] as String) }
)

@Composable
fun MutableStateComposable() {
    var name = "??????"
    var mutableName by remember {
        mutableStateOf("123")
    }
    println(mutableName)

    val mutableName1 = remember {
        mutableStateOf(name)
    }

    val (mutableName2, setValue) = remember {
        mutableStateOf(name)
    }

    val selectedCity by rememberSaveable(stateSaver = CitySaver) {
        mutableStateOf(City("??????", "China"))
    }

    val selectedCity1 by rememberSaveable(stateSaver = CityListSaver) {
        mutableStateOf(City("??????", "China"))
    }

    println(selectedCity)
    println(selectedCity1)







    println(mutableName2::class.javaObjectType)
    println(setValue)
    OutlinedTextField(value = "", onValueChange = {
        mutableName = it
    }, label = { Text(mutableName) })
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(backgroundColor = 0xffffff, showBackground = true)
@Composable
fun DefaultPreview() {
    Column {
        //LazyColumnComposable(mutableListOf("1241324", "234324"))
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
//        Spacer(
//            modifier = Modifier
//                .background(Color.Gray)
//                .padding(10.dp)
//                .fillMaxWidth()
//        )
//        TextComposable()
//        Spacer(
//            modifier = Modifier
//                .background(Color.Gray)
//                .padding(10.dp)
//                .fillMaxWidth()
//        )
//        clickableText()
        Spacer(
            modifier = Modifier
                .background(Color.Gray)
                .padding(10.dp)
                .fillMaxWidth()
        )
        TextFieldComposable()
        Spacer(
            modifier = Modifier
                .background(Color.Gray)
                .padding(10.dp)
                .fillMaxWidth()
        )
        CanvasComposable()
        Spacer(
            modifier = Modifier
                .background(Color.Gray)
                .padding(10.dp)
                .fillMaxWidth()
        )
        MutableStateComposable()

    }

}