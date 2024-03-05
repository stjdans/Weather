package com.example.weathers.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable

class BitmapGenerator {
    companion object {
        fun generateBitmapWithImageAndText(context: Context, id: Int, text1: String, text2: String): Bitmap {

            val imageDrawable: Drawable = context.resources.getDrawable(id, null)
            val width = 116
            val height = 146
            val imageW = 96
            val imageH = 96
            // 비트맵 생성
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val backgroundColor = Color.WHITE
            val alpha = (255 / 1.5).toInt()
            // 배경색 설정
            canvas.drawColor(backgroundColor)

            // 알파 값 설정
            bitmap.setHasAlpha(true) // 알파 채널을 사용하도록 설정
            val colorAlpha = Color.alpha(backgroundColor) // 배경색의 알파 값을 가져옴
            val finalAlpha: Int = alpha * colorAlpha / 255 // 원하는 알파 값과 배경색의 알파 값의 조합
            bitmap.eraseColor(Color.argb(finalAlpha, Color.red(backgroundColor), Color.green(backgroundColor), Color.blue(backgroundColor)))

            // 이미지 그리기
            val imageBounds = Rect((width - imageW) / 2, 0, imageW + (width - imageW) / 2, imageH) // 이미지의 사이즈를 비트맵 크기로 설정
            imageDrawable.bounds = imageBounds
            imageDrawable.draw(canvas)

            // 텍스트 그리기
            val paint = Paint()
            paint.color = Color.BLACK
            paint.textSize = 20f
            paint.isAntiAlias = true
            paint.textAlign = Paint.Align.CENTER

            // 텍스트를 비트맵 하단에 그리기
            var x = (canvas.width / 2).toFloat()
            var y = (canvas.height - 25).toFloat() // 텍스트 아래 여백 조절
            canvas.drawText(text1, x, y, paint)

            x = (canvas.width / 2).toFloat()
            y = (canvas.height - 5).toFloat() // 텍스트 아래 여백 조절
            canvas.drawText(text2, x, y, paint)

            return bitmap
        }
    }

}