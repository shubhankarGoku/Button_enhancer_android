package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.LinearProgressIndicator

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CounterApp()
        }
    }
}

@Composable
fun CounterApp() {
// remember saveable prevents counter to reset during screen change.
// adding conditions here
    var count by rememberSaveable { mutableStateOf(0) }
    var name by rememberSaveable { mutableStateOf("") }
    var sliderValue by rememberSaveable { mutableStateOf(0f) }
    var isEnabled by rememberSaveable { mutableStateOf(false) }

    val message = when {
        count == 0 -> "Start Counting!"
        count < 5 -> "Keep Going!"
        count < 10 -> "Awesome!"
        else -> "Maximum Reached!"
    }

    val textColor = when {
        count < 5 -> Color.Magenta
        count < 10 -> Color.Blue
        else -> Color.Red
    }

    //this will give effect to counter value
    val animatedSize by animateFloatAsState(
        targetValue = if (count > 5) 70f else 50f,
        label = "text_size_animation"
    )
// "Column" arranges all items inside layout vertically one below the other
    //fill max size in column lets buttons take full screensize rather than min size
    //padding required for space between all items
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
// if vertical alignment not done then this will move all items to top
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            fontSize = 20.sp
        )

        Text(
            text = "Counter Value",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        //this is for the name input in text field
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Your Name") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        //this shows the name
        Text(
            text = "Hello $name",
            fontSize = 22.sp
        )
        // when counter number changes, animation effect
        AnimatedContent(
            //number changes smoothly hence count
            targetState = count,
            label = "counter_animation"
        ) { targetCount ->

            // here the fontSize was earlier 50.sp, but now it is animatedSize.sp
            // which added the animation effect
            Text(
                text = targetCount.toString(),
                fontSize = animatedSize.sp,
                color = textColor
            )
        }

        // adding a slider
        Slider(
            value = sliderValue,
            onValueChange = {
                sliderValue = it
                count = it.toInt()
            },
            valueRange = 0f..10f
        )

        // this shows the slider text below the slider
        Text(
            text = "Slider Value: ${sliderValue.toInt()}",
            fontSize = 18.sp
        )

        //progress to be shown as a Line when counter is changed
        LinearProgressIndicator(
        progress = { count / 10f },
        modifier = Modifier,
        color = ProgressIndicatorDefaults.linearColor,
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )

        Spacer(modifier = Modifier.height(20.dp))

        //-- added a switch button which will enable or disable the buttons
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text("Enable Counter")

            Spacer(modifier = Modifier.width(10.dp))

            Switch(
                checked = isEnabled,
                onCheckedChange = {
                    isEnabled = it
                }
            )
        }
// -- end
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Button(
                onClick = { count++ },
                enabled = isEnabled
            ) {
                Text("Increase")
            }

            Button(
                onClick = {
                    if (count > 0) count--
                },
                enabled = isEnabled
            ) {
                Text("Decrease")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { count = 0 },
            enabled = isEnabled
        ) {
            Text("Reset")
        }
    }
}