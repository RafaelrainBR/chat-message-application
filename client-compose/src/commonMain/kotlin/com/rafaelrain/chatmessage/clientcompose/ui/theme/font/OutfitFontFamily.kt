package com.rafaelrain.chatmessage.clientcompose.ui.theme.font

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.rafaelrain.chatmessage.client_compose.generated.resources.Outfit_Black
import com.rafaelrain.chatmessage.client_compose.generated.resources.Outfit_Bold
import com.rafaelrain.chatmessage.client_compose.generated.resources.Outfit_ExtraBold
import com.rafaelrain.chatmessage.client_compose.generated.resources.Outfit_ExtraLight
import com.rafaelrain.chatmessage.client_compose.generated.resources.Outfit_Light
import com.rafaelrain.chatmessage.client_compose.generated.resources.Outfit_Medium
import com.rafaelrain.chatmessage.client_compose.generated.resources.Outfit_Regular
import com.rafaelrain.chatmessage.client_compose.generated.resources.Outfit_SemiBold
import com.rafaelrain.chatmessage.client_compose.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun OutfitFontFamily() =
    FontFamily(
        Font(Res.font.Outfit_Black, weight = FontWeight.Black, style = FontStyle.Normal),
        Font(Res.font.Outfit_Bold, weight = FontWeight.Bold, style = FontStyle.Normal),
        Font(Res.font.Outfit_ExtraBold, weight = FontWeight.ExtraBold, style = FontStyle.Normal),
        Font(Res.font.Outfit_ExtraLight, weight = FontWeight.ExtraLight, style = FontStyle.Normal),
        Font(Res.font.Outfit_Light, weight = FontWeight.Light, style = FontStyle.Normal),
        Font(Res.font.Outfit_Medium, weight = FontWeight.Medium, style = FontStyle.Normal),
        Font(Res.font.Outfit_Regular, weight = FontWeight.Normal, style = FontStyle.Normal),
        Font(Res.font.Outfit_SemiBold, weight = FontWeight.SemiBold, style = FontStyle.Normal),
    )
