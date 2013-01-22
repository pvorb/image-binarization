package de.vorb.vision.binarization

import java.awt.Dimension
import java.io.File
import scala.swing.{ BoxPanel, Button, ComboBox, FileChooser, FlowPanel, Label, MainFrame, Orientation, SimpleSwingApplication }
import scala.swing.event.ButtonClicked
import javax.imageio.ImageIO
import javax.swing.{ JOptionPane, UIManager }
import javax.swing.filechooser.FileFilter
import scala.swing.Dialog
import javax.swing.ImageIcon
import scala.swing.FormattedTextField
import java.text.Format
import scala.swing.TextField
import javax.swing.InputVerifier
import javax.swing.JComponent
import javax.swing.JTextField
import scala.swing.Component
import java.awt.Color
import java.io.IOException
import java.awt.image.BufferedImage
import scala.swing.CheckBox
import java.awt.Desktop

object GUI extends SimpleSwingApplication {
  try {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
  } catch { case _: Throwable => }

  def top = new MainFrame {
    title = "Image binarization"
    try {
      iconImage = ImageIO.read(getClass.getResource("/logo.png"))
    } catch { case _: Throwable => }

    val algorithmSelector = new ComboBox[BinarizationAlgorithm](Sauvola :: Nil)
    val coefficient = new TextField(3) {
      text = "0.5"
      val invalid = new Color(0xFFFFAAAA)
      val valid = new Color(0xFFAAFFAA)
      tooltip = "a decimal number between 0.2 and 0.5"
      inputVerifier = (c: Component) => c match {
        case c: TextField => try {
          val k = c.text.toDouble
          if (k >= 0.2 && k <= 0.5) {
            c.background = valid
            true
          } else {
            c.background = invalid
            false
          }
        } catch {
          case _ =>
            c.background = invalid
            false
        }
        case _ => false
      }
    }

    val openResult = new CheckBox {
      selected = true
    }

    var src: Option[File] = None
    var ext: Option[String] = None

    val supportedFileFormats: Set[Option[String]] =
      Set(Some("png"), Some("jpg"), Some("jpeg"), Some("gif"))

    def warnNoInputFile() =
      JOptionPane.showMessageDialog(self,
        "Please choose a source image first",
        "No source file specified",
        JOptionPane.WARNING_MESSAGE)

    contents = new BoxPanel(Orientation.Vertical) {
      contents += new FlowPanel {
        contents += new Button("Select source image ...") {
          reactions += {
            case ButtonClicked(b) =>
              val fc = new FileChooser {
                title = "Choose source image"
                fileFilter = new FileFilter {
                  def accept(f: File) = {
                    ext = {
                      val name = f.getName.split("\\.")
                      Some(name(name.size - 1).toLowerCase)
                    }

                    f.isDirectory || (f.canRead &&
                      supportedFileFormats.contains(ext))
                  }

                  def getDescription = "image files"
                }
              }

              if (fc.showOpenDialog(b) == FileChooser.Result.Approve)
                src = Some(fc.selectedFile)
          }
        }
      }

      contents += new FlowPanel {
        contents += new Label("Algorithm")
        contents += algorithmSelector

        contents += new Label("Coefficient")
        contents += coefficient
      }

      def binarize(src: Option[File], after: BufferedImage => Unit) = {
        src match {
          case None => warnNoInputFile()
          case Some(file) => try {
            val in = ImageIO.read(file)
            val alg = algorithmSelector.selection.item
            val k =
              if (coefficient.inputVerifier(coefficient))
                coefficient.text.toDouble
              else
                throw new IllegalArgumentException("k invalid")
            val out = alg.binarize(in, 0.2)
            after(out)
          } catch {
            case e: IOException =>
              JOptionPane.showMessageDialog(self,
                "Could not read the source image",
                "Invalid file format",
                JOptionPane.ERROR_MESSAGE)
            case e: IllegalArgumentException =>
              JOptionPane.showMessageDialog(self,
                "Coefficient must be between 0.2 and 0.5",
                "Invalid coefficient",
                JOptionPane.WARNING_MESSAGE)
          }
        }
      }

      contents += new FlowPanel {
        contents += new Button("Preview") {
          reactions += {
            case ButtonClicked(b) =>
              binarize(src, (out: BufferedImage) =>
                new Dialog(top) {
                  title = "Preview"
                  contents = new Label {
                    icon = new ImageIcon(out)
                  }
                  modal = false

                  centerOnScreen()
                  visible = true
                })
          }
        }

        contents += new Button("Save as ...") {
          reactions += {
            case ButtonClicked(b) =>

              val fc = new FileChooser {
                title = "Save result"
                fileFilter = new FileFilter {
                  def accept(f: File) = f.canWrite
                  def getDescription = "PNG images"
                }
              }

              if (fc.showSaveDialog(b) == FileChooser.Result.Approve) {
                val destination =
                  if (!fc.selectedFile.getName().endsWith(".png"))
                    new File(fc.selectedFile.getAbsolutePath + ".png")
                  else
                    fc.selectedFile

                binarize(src, (out: BufferedImage) => {
                  try {
                    ImageIO.write(out, "PNG", destination)

                    if (openResult.selected)
                      try {
                        Desktop.getDesktop.open(destination)
                      } catch {
                        case _ =>
                          JOptionPane.showMessageDialog(self,
                            "The result cannot be opened.",
                            "Missing permissions",
                            JOptionPane.WARNING_MESSAGE)
                      }
                  } catch {
                    case _ => JOptionPane.showMessageDialog(self,
                      "The result cannot be saved to the selected location.",
                      "Missing permissions",
                      JOptionPane.ERROR_MESSAGE)
                  }
                })
              }
          }
        }

        contents += new Label("Open result")
        contents += openResult
      }
    }

    resizable = false
    centerOnScreen()
  }
}