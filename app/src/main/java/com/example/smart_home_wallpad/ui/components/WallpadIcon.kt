package com.example.smart_home_wallpad.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

enum class WallpadIconType { LIGHT, CURTAIN, CAMERA, PLUG, THERMOMETER, DROPLET, HOME, AUTOMATION, SETTINGS }

@Composable
fun WallpadIcon(
    type: WallpadIconType,
    modifier: Modifier = Modifier.size(24.dp),
    tint: Color = Color(0xFF4D5C68),
) {
    Canvas(modifier) {
        val stroke = Stroke(width = size.minDimension * 0.095f)
        val c = Offset(size.width / 2f, size.height / 2f)
        when (type) {
            WallpadIconType.LIGHT -> {
                drawCircle(tint, radius = size.minDimension * .19f, center = c, style = stroke)
                listOf(Offset(0f, -.37f), Offset(0f, .37f), Offset(-.37f, 0f), Offset(.37f, 0f)).forEach { d ->
                    val start = c + d * size.minDimension
                    val end = c + d * (size.minDimension * .72f)
                    drawLine(tint, start, end, stroke.width)
                }
            }
            WallpadIconType.CAMERA -> {
                drawRoundRect(tint, Offset(size.width * .12f, size.height * .28f), Size(size.width * .58f, size.height * .46f), CornerRadius(size.minDimension * .08f), style = stroke)
                drawCircle(tint, size.minDimension * .12f, c, style = stroke)
                val path = Path().apply { moveTo(size.width * .72f, size.height * .40f); lineTo(size.width * .91f, size.height * .29f); lineTo(size.width * .91f, size.height * .71f); lineTo(size.width * .72f, size.height * .60f); close() }
                drawPath(path, tint, style = stroke)
            }
            WallpadIconType.PLUG -> {
                drawRoundRect(tint, Offset(size.width * .28f, size.height * .28f), Size(size.width * .44f, size.height * .36f), CornerRadius(size.minDimension * .08f), style = stroke)
                drawLine(tint, Offset(size.width * .38f, size.height * .12f), Offset(size.width * .38f, size.height * .28f), stroke.width)
                drawLine(tint, Offset(size.width * .62f, size.height * .12f), Offset(size.width * .62f, size.height * .28f), stroke.width)
                drawLine(tint, Offset(size.width * .50f, size.height * .64f), Offset(size.width * .50f, size.height * .87f), stroke.width)
            }
            WallpadIconType.THERMOMETER -> {
                drawCircle(tint, size.minDimension * .15f, Offset(c.x, size.height * .72f), style = stroke)
                drawLine(tint, Offset(c.x, size.height * .20f), Offset(c.x, size.height * .62f), stroke.width)
                drawLine(tint, Offset(c.x - size.width * .13f, size.height * .20f), Offset(c.x + size.width * .13f, size.height * .20f), stroke.width)
            }
            WallpadIconType.DROPLET -> {
                val path = Path().apply { moveTo(c.x, size.height * .12f); cubicTo(size.width * .17f, size.height * .48f, size.width * .25f, size.height * .78f, c.x, size.height * .86f); cubicTo(size.width * .75f, size.height * .78f, size.width * .83f, size.height * .48f, c.x, size.height * .12f); close() }
                drawPath(path, tint, style = stroke)
            }
            WallpadIconType.CURTAIN -> {
                drawRoundRect(tint, Offset(size.width * .16f, size.height * .18f), Size(size.width * .68f, size.height * .68f), CornerRadius(size.minDimension * .05f), style = stroke)
                drawLine(tint, Offset(c.x, size.height * .20f), Offset(c.x, size.height * .84f), stroke.width)
            }
            WallpadIconType.HOME -> {
                val path = Path().apply { moveTo(size.width * .13f, size.height * .48f); lineTo(c.x, size.height * .15f); lineTo(size.width * .87f, size.height * .48f); lineTo(size.width * .76f, size.height * .48f); lineTo(size.width * .76f, size.height * .84f); lineTo(size.width * .24f, size.height * .84f); lineTo(size.width * .24f, size.height * .48f); close() }
                drawPath(path, tint, style = stroke)
            }
            WallpadIconType.AUTOMATION -> { drawCircle(tint, size.minDimension * .10f, c); drawLine(tint, Offset(c.x, size.height * .12f), Offset(c.x, size.height * .32f), stroke.width); drawLine(tint, Offset(c.x, size.height * .68f), Offset(c.x, size.height * .88f), stroke.width); drawLine(tint, Offset(size.width * .12f, c.y), Offset(size.width * .32f, c.y), stroke.width); drawLine(tint, Offset(size.width * .68f, c.y), Offset(size.width * .88f, c.y), stroke.width) }
            WallpadIconType.SETTINGS -> { drawCircle(tint, size.minDimension * .26f, c, style = stroke); drawCircle(tint, size.minDimension * .08f, c, style = stroke) }
        }
    }
}
