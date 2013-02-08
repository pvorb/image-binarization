package de.vorb.vision.binarization

import java.awt.image.BufferedImage
import java.awt.color.ColorSpace
import java.awt.Color
import java.awt.image.ColorConvertOp
import java.awt.image.ColorModel

case class Pixel(x: Int, y: Int) {
  require(x >= 0 && y >= 0, "No negative coordinates.")
}
case class Window(seq: Seq[Int])

object Sauvola extends BinarizationAlgorithm {
  def name = "Sauvola"

  val R = 128

  def binarize(src: BufferedImage, k: Double, radius: Int): BufferedImage = {
    //require(src.get,
    //  "Source image must be grayscale")
    require(k >= 0.2 && k <= 0.5, "k must be in interval [0.2, 0.5]")

    val in = new BufferedImage(src.getWidth, src.getHeight,
      BufferedImage.TYPE_BYTE_GRAY)

    src.getType match {
      case BufferedImage.TYPE_BYTE_GRAY => src
      case _ =>
        val transformation =
          new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB),
            ColorSpace.getInstance(ColorSpace.CS_GRAY), null).filter(src, in)
    }

    val out = new BufferedImage(src.getWidth, src.getHeight,
      BufferedImage.TYPE_BYTE_BINARY)

    for {
      x <- 0 until src.getWidth
      y <- 0 until src.getHeight
      g = binarize(in, Pixel(x, y), k, radius)
    } {
      out.setRGB(x, y, g)
    }

    out
  }

  private def binarize(src: BufferedImage, px: Pixel, k: Double, radius: Int): Int = {
    val w = window(src, px, radius)
    val c = color(src, px)
    if (intensity(c) <= threshold(w, k)) 0
    else 0xFFFFFF
  }

  private def window(src: BufferedImage, px: Pixel, radius: Int): Window = Window {
    val res = new Array[Int]((radius * 2 + 1) * (radius * 2 + 1))

    val xMin = 0 max (px.x - radius)
    val xMax = (px.x + radius) min (src.getWidth - 1)
    val yMin = 0 max (px.y - radius)
    val yMax = (px.y + radius) min (src.getHeight - 1)

    var i = 0
    for {
      x <- xMin until xMax
      y <- yMin until yMax
    } {
      res(i) = intensity(new Color(src.getRGB(x, y)))
      i += 1
    }
    res
  }

  private def color(src: BufferedImage, px: Pixel) =
    new Color(src.getRGB(px.x, px.y))

  private def intensity(c: Color): Int =
    c.getRed max c.getGreen max c.getBlue

  private def mean(w: Window): Double =
    w.seq.sum.toDouble / w.seq.size.toDouble

  private def variance(w: Window): Double = {
    val m = mean(w)
    var tmp = 0.0
    for {
      a <- w.seq
      b = m - a.toDouble
    } tmp += b * b

    tmp / w.seq.size
  }

  private def standardDeviation(w: Window): Double =
    math.sqrt(variance(w))

  private def threshold(w: Window, k: Double): Double =
    mean(w) * (1.0 + k * (standardDeviation(w) / R - 1.0))
}