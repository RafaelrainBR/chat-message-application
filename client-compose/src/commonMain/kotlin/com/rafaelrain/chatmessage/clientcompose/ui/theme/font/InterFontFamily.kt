package com.rafaelrain.chatmessage.clientcompose.ui.theme.font

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.rafaelrain.chatmessage.client_compose.generated.resources.Inter_Black
import com.rafaelrain.chatmessage.client_compose.generated.resources.Inter_Bold
import com.rafaelrain.chatmessage.client_compose.generated.resources.Inter_ExtraBold
import com.rafaelrain.chatmessage.client_compose.generated.resources.Inter_ExtraLight
import com.rafaelrain.chatmessage.client_compose.generated.resources.Inter_Light
import com.rafaelrain.chatmessage.client_compose.generated.resources.Inter_Medium
import com.rafaelrain.chatmessage.client_compose.generated.resources.Inter_Regular
import com.rafaelrain.chatmessage.client_compose.generated.resources.Inter_SemiBold
import com.rafaelrain.chatmessage.client_compose.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun InterFontFamily() =
    FontFamily(
        Font(Res.font.Inter_Black, weight = FontWeight.Black, style = FontStyle.Normal),
        Font(Res.font.Inter_Bold, weight = FontWeight.Bold, style = FontStyle.Normal),
        Font(Res.font.Inter_ExtraBold, weight = FontWeight.ExtraBold, style = FontStyle.Normal),
        Font(Res.font.Inter_ExtraLight, weight = FontWeight.ExtraLight, style = FontStyle.Normal),
        Font(Res.font.Inter_Light, weight = FontWeight.Light, style = FontStyle.Normal),
        Font(Res.font.Inter_Medium, weight = FontWeight.Medium, style = FontStyle.Normal),
        Font(Res.font.Inter_Regular, weight = FontWeight.Normal, style = FontStyle.Normal),
        Font(Res.font.Inter_SemiBold, weight = FontWeight.SemiBold, style = FontStyle.Normal),
    )
