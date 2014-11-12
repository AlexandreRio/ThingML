/**
 * Copyright (C) 2014 SINTEF <franck.fleurey@sintef.no>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * This code generator extend the Java generated code with MQTT connectors
 * @author: Brice MORIN <brice.morin@sintef.no>
 */
package org.thingml.javagenerator.extension

import java.io._
import java.util.Hashtable

import org.sintef.thingml._
import org.thingml.javagenerator.extension.WebSocketGenerator._

import scala.collection.JavaConversions._
import scala.io.Source

object WebSocketGenerator {
  implicit def wsGeneratorAspect(self: Thing): ThingWSGenerator = ThingWSGenerator(self)

  implicit def wsGeneratorAspect(self: Type) = self match {
    case _ => TypeWSGenerator(self)
  }

  implicit def wsGeneratorAspect(self: Configuration): ConfigurationWSGenerator = ConfigurationWSGenerator(self)

  def compileAndRun(cfg: Configuration, model: ThingMLModel, doingTests: Boolean = false) {
    //ConfigurationImpl.MergedConfigurationCache.clearCache();

    //doingTests should be ignored, it is only used when calling from org.thingml.cmd
    var tmpFolder = System.getProperty("java.io.tmpdir") + "/ThingML_temp/"
    if (doingTests) {
      tmpFolder = "tmp/ThingML_Java/"
    }
    new File(tmpFolder).deleteOnExit

    val code = compile(cfg, "org.thingml.generated.websocket", model)
    val rootDir = tmpFolder + cfg.getName

    val outputDir = cfg.getAnnotations.filter(a => a.getName == "java_folder").headOption match {
      case Some(a) => tmpFolder + cfg.getName + a.getValue + "/java/org/thingml/generated"
      case None => tmpFolder + cfg.getName + "/src/main/java/org/thingml/generated"
    }

    println("outputDir: " + outputDir)

    val outputDirFile = new File(outputDir)
    outputDirFile.mkdirs

    val mqttDir = new File(outputDirFile, "websocket")
    mqttDir.mkdirs()

    code.foreach { case (file, code) =>
      println("Dumping " + file);
      val w = new PrintWriter(new FileWriter(new File(outputDir + "/" + file)));
      w.println(code.toString);
      w.close();
    }

    var pom = Source.fromInputStream(new FileInputStream(rootDir + "/pom.xml"), "utf-8").getLines().mkString("\n")
    pom = pom.replace("<!--CONFIGURATIONNAME-->", cfg.getName())
    pom = pom.replace("<!--DEP-->", "<dependency>\n\t<groupId>com.eclipsesource.minimal-json</groupId>\n\t<artifactId>minimal-json</artifactId>\n\t<version>0.9.1</version>\n</dependency>\n<dependency>\n<groupId>org.java-websocket</groupId>\n<artifactId>Java-WebSocket</artifactId>\n<version>1.3.0</version>\n</dependency>\n<!--DEP-->")
    //pom = pom.replace("<!--REPO-->", "\n<!--REPO-->");
    val w = new PrintWriter(new FileWriter(new File(rootDir + "/pom.xml")));
    w.println(pom);
    w.close();
    if (!doingTests) {
      javax.swing.JOptionPane.showMessageDialog(null, "$>cd " + rootDir + "\n$>mvn clean package exec:java -Dexec.mainClass=org.thingml.generated.websocket.Main");
    }
    /*
     * GENERATE SOME DOCUMENTATION
     */

    if (!doingTests) {
      new Thread(new Runnable {
        override def run(): Unit = compileGeneratedCode(rootDir)
      }).start()
    }
  }

  def isWindows(): Boolean = {
    var os = System.getProperty("os.name").toLowerCase();
    return (os.indexOf("win") >= 0);
  }

  def compileGeneratedCode(rootDir: String) = {
    val runtime = Runtime.getRuntime().exec((if (isWindows) "cmd /c start " else "") + "mvn clean package exec:java -Dexec.mainClass=org.thingml.generated.websocket.Main", null, new File(rootDir));

    val in = new BufferedReader(new InputStreamReader(runtime.getInputStream()));
    val out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(runtime.getOutputStream())), true);

    var line: String = in.readLine()
    while (line != null) {
      println(line);
      line = in.readLine()
    }
    runtime.waitFor();
    in.close();
    out.close();
    runtime.destroy();
  }

  def compile(t: Configuration, pack: String, model: ThingMLModel): java.util.Map[String, StringBuilder] = {
    //ConfigurationImpl.MergedConfigurationCache.clearCache();

    Context.init
    Context.pack = pack

    var mainBuilder = Context.getBuilder("websocket/Main.java")
    Context.pack = "org.thingml.generated.websocket"
    generateHeader(mainBuilder, true)

    t.generateJavaMain(mainBuilder)
    t.generateJava()
    return Context.builder
  }

  def generateHeader(builder: StringBuilder, isMain: Boolean = false) = {
    builder append "/**\n"
    builder append " * File generated by the ThingML IDE\n"
    builder append " * In case of a bug in the generated code,\n"
    builder append " * please submit an issue on our GitHub\n"
    builder append " **/\n\n"

    builder append "package " + Context.pack + ";\n\n"

    if(isMain)
      builder append "import java.io.IOException;\n"

    if(!isMain)
      builder append "import com.eclipsesource.json.JsonObject;\n\n"

    builder append "import org.java_websocket.WebSocket;\n"
    builder append "import org.java_websocket.handshake.ClientHandshake;\n"
    builder append "import org.java_websocket.server.WebSocketServer;\n\n"
    builder append "import org.thingml.generated.*;\n"
    builder append "import org.thingml.generated.api.*;\n\n"

    builder append "import java.net.InetSocketAddress;\n"
    builder append "import java.net.UnknownHostException;\n"
    builder append "import java.nio.channels.NotYetConnectedException;\n"
    builder append "import java.text.SimpleDateFormat;\n"
    builder append "import java.util.Collection;\n"
    builder append "import java.util.Date;\n"
  }
}

class ThingMLWSGenerator(self: ThingMLElement) {
  def generateJava() {
    // Implemented in the sub-classes
  }
}

case class ConfigurationWSGenerator(val self: Configuration) extends ThingMLJavaGenerator(self) {

  override def generateJava() {
    self.allInstances().foreach { thing =>
      thing.getType.generateJava()
    }
  }

  def generateJavaMain(builder: StringBuilder) {
    builder append "public class Main {\n"
    builder append "public static void main(String args[]) {\n"

    builder append "//Calling standalone ThingML Main\n"
    builder append "org.thingml.generated.Main.main(null);\n\n"

    builder append "//Instantiate and link per instance and per port WebSocket wrappers\n"
    var socket = 9000;
    self.allInstances.foreach { i =>
      i.getType.allPorts().foreach { p =>
        if (p.isDefined("public", "true")) {
          builder append "System.out.println(\"[WebSocket_" + p.getName + "] Server starting on port " + socket + "\");"
          builder append "final WebSocket_" + i.getType.getName + "_" + p.getName + " " + i.getName + "_" + p.getName + "_ws = new WebSocket_" + i.getType.getName + "_" + p.getName + "(" + socket + ", org.thingml.generated.Main." + i.getType.getName + "_" + i.getName + ");\n"
          builder append i.getName + "_" + p.getName + "_ws.start();\n"
          builder append "org.thingml.generated.Main." + i.getType.getName + "_" + i.getName + ".registerOn" + Context.firstToUpper(p.getName()) + "(" + i.getName + "_" + p.getName + "_ws);\n\n"
          socket = socket + 1
        }
      }
    }

    builder append "Runtime.getRuntime().addShutdownHook(new Thread() {\n"
    builder append "public void run() {\n"
    builder append "System.out.println(\"Terminating websockets...\");\n"
    builder append "try {\n"
    self.allInstances.foreach { i =>
      i.getType.allPorts().foreach { p =>
        if (p.isDefined("public", "true")) {
          builder append i.getName + "_" + p.getName + "_ws.stop();\n"
        }
      }
    }
    builder append "} catch (IOException e) {\n"
    builder append "e.printStackTrace();\n"
    builder append "} catch (InterruptedException e) {\n"
    builder append "e.printStackTrace();\n"
    builder append "}\n"
    builder append "System.out.println(\"websockets terminated. RIP!\");\n"
    builder append "}\n"
    builder append "});\n\n"

    builder append "}\n"
    builder append "}\n"
  }
}

case class ThingWSGenerator(val self: Thing) extends ThingMLJavaGenerator(self) {

  override def generateJava() {

    self.allPorts.filter{p => p.isDefined("public", "true")}.foreach{ p =>
      val builder = Context.getBuilder("websocket/WebSocket_" + Context.firstToUpper(self.getName) + "_" + p.getName + ".java")
      Context.thing = self
      Context.pack = "org.thingml.generated.websocket"
      generateHeader(builder)

      builder append "public class WebSocket_" + Context.firstToUpper(self.getName) + "_" + p.getName
      builder append " extends WebSocketServer"
      if (p.getSends.size() > 0)
        builder append " implements I" + self.getName + "_" + p.getName + "Client"
      builder append " {\n\n"


      builder append "SimpleDateFormat dateFormat = new SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss.SSS\");\n"
      builder append Context.firstToUpper(self.getName) + " " + self.getName + ";\n"
      builder append "String deviceId = \"" + self.getName + "\";\n"

      builder append "//Constructor\n"
      builder append "public WebSocket_" + Context.firstToUpper(self.getName) + "_" + p.getName + "(int port, " + Context.firstToUpper(self.getName) + " thing) {\n"
      builder append "super(new InetSocketAddress(port));\n"
      builder append self.getName + " = thing;\n"
      builder append "}\n\n"

      builder append "public void sendToAll(String text) {\n"
      builder append "Collection<WebSocket> con = connections();\n"
      builder append "synchronized (con) {\n"
      builder append "for (WebSocket c : con) {\n"
      builder append "try {\n"
      builder append "c.send(text);\n"
      builder append "} catch (NotYetConnectedException ignored) {}\n"
      builder append "}\n"
      builder append "}\n"
      builder append "}\n\n"

      builder append "@Override\n"
      builder append "public void onOpen(WebSocket ws, ClientHandshake ch) {\n"
      builder append "System.out.println(\"[WebSocket_" + p.getName + "] Open Client: \" + ws.getRemoteSocketAddress().getAddress().getHostAddress());\n"
      builder append "}\n\n"

      builder append "@Override\n"
      builder append "public void onClose(WebSocket ws, int i, String string, boolean bln) {\n"
      builder append "System.out.println(\"[WebSocket_" + p.getName + "] Close Client: \" + ws.getRemoteSocketAddress().getAddress().getHostAddress());\n"
      builder append "}\n\n"

      builder append "@Override\n"
      builder append "public void onError(WebSocket ws, Exception excptn) {\n"
      builder append "System.out.println(\"[WebSocket_" + p.getName + "] Error ws = \" + ws + \" exception = \" + excptn.getMessage());\n"
      builder append "excptn.printStackTrace();\n"
      builder append "}\n\n"

      builder append "@Override\n"
      builder append "public void onMessage(WebSocket ws, String string) {"
      builder append "System.out.println(\"[WebSocket_" + p.getName + "] Message from \" + ws.getRemoteSocketAddress().getAddress().getHostAddress() + \" Data = \" + string);\n"

      builder append "JsonObject json = JsonObject.readFrom(string);\n"
      builder append "if (deviceId.equals(json.get(\"deviceId\").asString())) {\n"
      var i = 0;
      p.getReceives.foreach{m =>
        if (i>0)
          builder append "else "
        builder append "if (\"" + p.getName + "." + m.getName + "\".equals(json.get(\"sensorId\").asString())) {\n"
        builder append self.getName + "." + m.getName + "_via_" + p.getName + "("
        builder append m.getParameters.collect { case pa => "(" + pa.getType.java_type() + ") json.get(\"" + Context.protectJavaKeyword(pa.getName) + "\").as" + (if (pa.getType.java_type() == "short") "Int" else Context.firstToUpper(pa.getType.java_type())) + "()"}.mkString(", ")
        builder append ");\n"
        builder append "}\n"
        i = i + 1
      }
      builder append "}\n"

      builder append "}\n\n"

      p.getSends.foreach { m =>
        builder append "protected String " + p.getName + "_" + m.getName + "toJSON("
        builder append m.getParameters.collect { case pa => pa.getType.java_type(pa.getCardinality != null) + " " + Context.protectJavaKeyword(pa.getName)}.mkString(", ")
        builder append "){\n"
        builder append "final Date date = new Date();\n"
        builder append "final StringBuilder builder = new StringBuilder();\n"
        builder append "builder.append(\"{\");\n"
        builder append "builder.append(\"\\\"deviceId\\\":\\\"\" + deviceId + \"\\\",\");\n"
        builder append "builder.append(\"\\\"sensorId\\\":\\\"" + p.getName + "." + m.getName  + "\\\",\");\n"
        builder append "builder.append(\"\\\"observationTime\\\":\\\"\" + dateFormat.format(date) + \"\\\",\");\n"
        builder append "builder.append(\"\\\"observations\\\":[\");\n"
        m.getParameters.foreach { pa =>
          builder append "builder.append(\"{\\\"" + Context.protectJavaKeyword(pa.getName) + "\\\":\\\"\" + " + Context.protectJavaKeyword(pa.getName) + "+ \"\\\"}\");\n"
        }
        builder append "builder.append(\"]}\");\n"
        builder append "return builder.toString();\n"
        builder append "}\n\n"

        builder append "@Override\n"
        builder append "public void " + m.getName + "_from_" + p.getName + "("
        builder append m.getParameters.collect { case pa => pa.getType.java_type(pa.getCardinality != null) + " " + Context.protectJavaKeyword(pa.getName)}.mkString(", ")
        builder append ") {\n"
        builder append "sendToAll(" + p.getName + "_" + m.getName + "toJSON(" + m.getParameters.collect { case pa => Context.protectJavaKeyword(pa.getName)}.mkString(", ") + "));\n"
        builder append "}\n\n"
      }

      builder append "}"
    }
  }
}

case class TypeWSGenerator(val self: Type) extends ThingMLJavaGenerator(self) {
  def java_type(isArray: Boolean = false): String = {
    if (self == null) {
      return "void"
    } else if (self.isInstanceOf[Enumeration]) {
      return Context.firstToUpper(self.getName) + "_ENUM"
    }
    else {
      var res: String = self.getAnnotations.filter {
        a => a.getName == "java_type"
      }.headOption match {
        case Some(a) =>
          a.asInstanceOf[PlatformAnnotation].getValue
        case None =>
          "Object"
      }
      if (isArray) {
        res = res + "[]"
      }
      return res
    }
  }
}