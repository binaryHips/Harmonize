package com.example.harmonizer

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.harmonizer.ui.theme.HarmonizerTheme
import kotlin.math.round


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarmonizePage(modifier: Modifier = Modifier, chosenPhoto: PhotoItem? = null) {

    val navController = (LocalActivity.current as MainActivity).navController
    val client = (LocalActivity.current as MainActivity).client

    var currentMode by remember { mutableIntStateOf(0) }
    var currentModeOptions = listOf(
        stringResource(R.string.harmonize_mode_option_automatic),
        stringResource(R.string.harmonize_mode_option_choose_template),
        stringResource(R.string.harmonize_mode_option_choose_template_angle)
    )

    val templateOptions = listOf("i", "V", "L", "I", "T", "Y", "X")
    val (currentTemplate, onTemplateSelected) = remember { mutableStateOf(templateOptions[0]) }

    var anglePosition by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // image selection column
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {



            }

            // options column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                //modifier = Modifier.background(Color(0, 0, 0, 30))
                ) {

                SingleChoiceSegmentedButtonRow {
                    currentModeOptions.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = currentModeOptions.size
                            ),
                            onClick = { currentMode = index },
                            selected = index == currentMode,
                            label = { Text(label) }
                        )
                    }
                }

                // template selector
                if (currentMode >= 1){
                    Row(modifier.selectableGroup()) {
                        templateOptions.forEach { text ->
                            Column(
                                Modifier
                                    .width(32.dp)
                                    .height(56.dp)
                                    .selectable(
                                        selected = (text == currentTemplate),
                                        onClick = { onTemplateSelected(text) },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 1.dp),
                                //verticalAlignment = Alignment.CenterVertically
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                RadioButton(
                                    selected = (text == currentTemplate),
                                    onClick = null // null recommended for accessibility with screen readers
                                )
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 1.dp)
                                )
                            }
                        }
                    }
                } else Spacer(modifier = Modifier.height(56.dp))

                if (currentMode >= 2){
                    Column (modifier = Modifier.height(96.dp)){
                        Slider(
                            value = anglePosition,
                            onValueChange = { anglePosition = round(it) },
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.secondary,
                                activeTrackColor = MaterialTheme.colorScheme.secondary,
                                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                            ),
                            steps = 361,
                            valueRange = 0f..360f
                        )
                        Text(text = "${anglePosition.toInt()}°")
                    }
                } else Spacer(modifier = Modifier.height(96.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {  },
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    Text(stringResource(R.string.harmonize_send), fontSize = 18.sp)
                }
            }
        }
    }
}

fun onChooseButtonClicked(){

}

fun onNewPhotoButtonClicked(){
}

@Preview(showBackground = false)
@Composable
fun HarmonizePreview() {
    HarmonizerTheme {
        HarmonizePage()
    }
}
