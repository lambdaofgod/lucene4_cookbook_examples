import ammonite.ops._
import java.io.File

val toplevelPath = Path("/" +
  pwd.segments
    .dropRight(1)
    .mkString("/"))

val datadir = toplevelPath / 'data

// Default jar path, override at your leisure
private val jarPath = toplevelPath / "target"

def loadProjectDependencies = {
  val jars = ls! jarPath
  jars.foreach(interp.load.cp)
}

def loadData(fileName: String) = {
  lazy val dataPath = toplevelPath / 'data / fileName
  new File(dataPath.toString)
}
