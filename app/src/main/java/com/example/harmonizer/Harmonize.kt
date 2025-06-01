package com.example.harmonizer

import android.provider.ContactsContract.Contacts.Photo
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.harmonizer.ui.theme.HarmonizerTheme
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarmonizePage(modifier: Modifier = Modifier, chosenPhoto: PhotoItem? = null) {

    val navController = (LocalActivity.current as MainActivity).navController
    val gallery = (LocalActivity.current as MainActivity).gallery
    val client = (LocalActivity.current as MainActivity).client

    var selectedImage: PhotoItem? by remember { mutableStateOf(null)}

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            val alreadyHas = gallery.getFromUriOrNull(uri)
            if (alreadyHas != null){
                selectedImage = alreadyHas
            } else {
                selectedImage = PhotoItem(
                    0,
                    uri,
                    "",
                    ""
                )
                //gallery.addImage(selectedImage!!)
            }
            Log.d("HARMONIZE", "Selected image")
        } else {
            Log.d("HARMONIZE", "No media selected")
        }
    }

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

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            // image selection column
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (selectedImage != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImage!!.uri),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()

                            .clip(RoundedCornerShape(36.dp))
                            .aspectRatio(1f)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.image),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                Row() {
                    Button(
                        onClick = {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        modifier = Modifier.padding(bottom = 32.dp)
                    ) {
                        Text(stringResource(R.string.select_image), fontSize = 18.sp)
                    }
                    Button(
                        onClick = {  },
                        modifier = Modifier.padding(bottom = 32.dp)
                    ) {
                        Text(stringResource(R.string.new_photo), fontSize = 18.sp)
                    }
                }


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
                            label = { Text(label, fontSize = 12.sp) }
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

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        if (selectedImage != null) {
                            client.requestImageHarmonization(
                                selectedImage as PhotoItem,
                                if (currentMode >= 1) currentTemplate else null,
                                if (currentMode >= 2) anglePosition else null,
                                )
                        }
                    },
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    Text(stringResource(R.string.harmonize_send), fontSize = 18.sp)
                }
            }
        }
    }
}


@Preview(showBackground = false)
@Composable
fun HarmonizePreview() {
    HarmonizerTheme {
        HarmonizePage()
    }
}
