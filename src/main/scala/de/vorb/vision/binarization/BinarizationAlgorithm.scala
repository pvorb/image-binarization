package de.vorb.vision.binarization

import java.awt.image.BufferedImage

trait BinarizationAlgorithm {
  def name: String
  def binarize(src: BufferedImage, k: Double): BufferedImage
  override def toString = name
}