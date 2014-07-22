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
 * This code generator targets the Kevoree Framework.
 * Formerly wrapping the Scala code generated from ThingML (by Runze HAO <haoshaochi@gmail.com>),
 * this new compiler now wraps the plain Java code generated from ThingML
 * @author: Brice MORIN <brice.morin@sintef.no>
 */
package org.thingml.kevoreegenerator

import org.thingml.javagenerator.JavaGenerator._
import org.sintef.thingml.constraints.ThingMLHelpers
import org.thingml.model.scalaimpl.ThingMLScalaImpl._
import org.sintef.thingml.resource.thingml.analysis.helper.CharacterEscaper
import scala.collection.JavaConversions._
import scala.io.Source
import scala.actors._
import scala.actors.Actor._
import java.util.{ArrayList, Hashtable}
import java.util.AbstractMap.SimpleEntry
import java.io._
import org.sintef.thingml._

object Context {
  val builder = new StringBuilder()
  
  var thing : Thing = _
  var pack : String = _
  var port_name : String = _
  var file_name : String = _
  var wrapper_name :String =_
  
  

  val keywords = scala.List("abstract","continue","for","new","switch","assert","default","package","synchronized","boolean","do","if","private","this","break","double","implements","protected","throw","byte","else","import","public","throws","case","instanceof","return","transient","catch","extends","int","short","try","char","final","interface","static","void","class","finally","long","volatile","float","native","super","while")
  def protectJavaKeyword(value : String) : String = {
    if(keywords.exists(p => p.equals(value))){
      return "_"+value+"_"
    } 
    else {
      return value
    }
  }
  
  def firstToUpper(value : String) : String = {
    return value.capitalize
  }
  
  def init {
    builder.clear
    thing = null
    pack = null
    port_name = null
    file_name = null
    wrapper_name = null
  }
}

object KevoreeGenerator {
  implicit def kevoreeGeneratorAspect(self: Thing): ThingKevoreeGenerator = ThingKevoreeGenerator(self)
  
  /*
   * 
   */
  def compileAndRun(cfg : Configuration, model: ThingMLModel) {
    new File(System.getProperty("java.io.tmpdir") + "ThingML_temp/").deleteOnExit
    
    val rootDir = System.getProperty("java.io.tmpdir") + "ThingML_temp/" + cfg.getName
    val outputDir = System.getProperty("java.io.tmpdir") + "ThingML_temp/" + cfg.getName + "/src/main/java/org/thingml/generated/kevoree"
    
    val outputDirFile = new File(outputDir)
    outputDirFile.mkdirs
    
    
    cfg.allThings.foreach{case thing=>
        Context.init
        Context.file_name = "K" + Context.firstToUpper(thing.getName())
        //Context.wrapper_name = cfg.getName+"_"+thing.getName()+"_Wrapper"
        val code = compile(thing, "org.thingml.generated", model)
        
        var w = new PrintWriter(new FileWriter(new File(outputDir  + "/" + Context.file_name+".java")));
        System.out.println("code generated at "+outputDir  + "/" + Context.file_name+".java");
        w.println(code._1);
        w.close();
    
        /*w = new PrintWriter(new FileWriter(new File(outputDir + "/"+Context.wrapper_name+".java")));
        System.out.println("code generated at "+outputDir  + "/" + Context.wrapper_name+".java");
        w.println(code._2);
        w.close();*/
    }

    compilePom(cfg)
    compileKevScript(cfg)
    
    /* cfg.allInstances.foreach{case inst =>
     
     }*/
    javax.swing.JOptionPane.showMessageDialog(null, "Kevoree wrappers generated");
  }
  /*
   * 
   */
  def compileKevScript(cfg:Configuration){
    var kevScript:StringBuilder= new StringBuilder()
    kevScript append "tblock\n{\n"
    kevScript append "merge \"mvn:org.kevoree.corelibrary.javase/org.kevoree.library.javase.javaseNode/{kevoree.version}\"\n"
    kevScript append "merge \"mvn:org.kevoree.corelibrary.javase/org.kevoree.library.javase.nanohttp/{kevoree.version}\"\n"
    kevScript append "merge \"mvn:org.kevoree.corelibrary.javase/org.kevoree.library.javase.defaultChannels/{kevoree.version}\"\n"
    kevScript append "addNode node0 : JavaSENode\n"
    kevScript append "addGroup sync: NanoRestGroup \n"
    kevScript append "addToGroup sync* \n"
    kevScript append "updateDictionary sync { port=\"8000\"}@node0\n"
    cfg.allThings.foreach{thing=>
      kevScript append "addComponent "+thing.getName()+"_KevComponent@node0 : "+cfg.getName+"_"+thing.getName()+"_KV2ThingML {}\n"
    }
    cfg.allConnectors.foreach{con=>   
      if(con.getRequired.getSends.size>0 && con.getProvided.getReceives.size>0){
        kevScript append "addChannel c_"+con.hashCode+" : defMSG {}\n"
        kevScript append  "bind "+con.getRequired.getOwner.getName+"_KevComponent."+con.getRequired.getName+"_Transfer@node0 => c_"+con.hashCode+"\n"
        kevScript append  "bind "+con.getProvided.getOwner.getName+"_KevComponent."+con.getProvided.getName+"_rcv@node0 => c_"+con.hashCode+"\n"
      }
      if(con.getRequired.getReceives.size>0 && con.getProvided.getSends.size>0){
        kevScript append "addChannel c_"+con.hashCode+"_re : defMSG {}\n"
        kevScript append  "bind "+con.getRequired.getOwner.getName+"_KevComponent."+con.getRequired.getName+"_rcv@node0 => c_"+con.hashCode+"_re\n"
        kevScript append  "bind "+con.getProvided.getOwner.getName+"_KevComponent."+con.getProvided.getName+"_Transfer@node0 => c_"+con.hashCode+"_re\n"
      }
    }

    kevScript append "\n}"
    val rootDir = System.getProperty("java.io.tmpdir") + "ThingML_temp/" + cfg.getName + "/src/main/kevs"
    val outputDirFile = new File(rootDir)
    outputDirFile.mkdirs
    val w = new PrintWriter(new FileWriter(new File(rootDir+"/"+cfg.getName+".kevscript")));
    w.println(kevScript);
    w.close();
  }
  
  def compilePom(cfg:Configuration){
    //TODO: we should load the already generated POM (from the JASM compiler) and update it with Kevoree information, not to do most of the work twice
    val rootDir = System.getProperty("java.io.tmpdir") + "ThingML_temp/" + cfg.getName
    var pom = Source.fromInputStream(new FileInputStream(rootDir + "/pom.xml"),"utf-8").getLines().mkString("\n")
    val kevoreePlugin = "\n<plugin>\n<groupId>org.kevoree.tools</groupId>\n<artifactId>org.kevoree.tools.mavenplugin</artifactId>\n<version>${kevoree.version}</version>\n<extensions>true</extensions>\n<configuration>\n<nodename>" + cfg.getName + "</nodename><model>src/main/kevs/main.kevs</model>\n</configuration>\n<executions>\n<execution>\n<goals>\n<goal>generate</goal>\n</goals>\n</execution>\n</executions>\n</plugin>\n</plugins>\n"
    pom = pom.replace("</plugins>",kevoreePlugin)

    pom = pom.replace("<!--PROP-->", "<kevoree.version>3.7.1</kevoree.version>\n<!--PROP-->" )

    pom = pom.replace("<!--DEP-->", "<dependency>\n<groupId>org.kevoree</groupId>\n<artifactId>org.kevoree.annotation.api</artifactId>\n<version>${kevoree.version}</version>\n</dependency>\n<!--DEP-->")
    pom = pom.replace("<!--DEP-->", "<dependency>\n<groupId>org.kevoree</groupId>\n<artifactId>org.kevoree.api</artifactId>\n<version>${kevoree.version}</version>\n</dependency>\n<!--DEP-->")

    //println(pom)


    var w = new PrintWriter(new FileWriter(new File(rootDir+"/pom.xml")));
    println(rootDir+"/pom.xml")
    w.println(pom);
    w.close();
  }

  def compile(t: Thing, pack : String, model: ThingMLModel) : Pair[String, String] = {
    Context.pack = pack
    var wrapperBuilder = new StringBuilder()
    
    generateHeader()
    generateHeader(wrapperBuilder, true)
    
    // Generate code for .things which appear in the configuration

    t.generateKevoree()
    (Context.builder.toString, wrapperBuilder.toString)
  }
  
  def generateHeader(builder: StringBuilder = Context.builder, isWrapper : Boolean = false) = {
    builder append "/**\n"
    builder append " * File generated by the ThingML IDE\n"
    builder append " * /!\\Do not edit this file/!\\\n"
    builder append " * In case of a bug in the generated code,\n"
    builder append " * please submit an issue on our GitHub\n"
    builder append " **/\n\n"

    builder append "package " + Context.pack +".kevoree;\n"
    builder append "import " + Context.pack + ".*;\n"
    builder append "import org.kevoree.annotation.*;\n"
    builder append "import org.thingml.java.*;\n"
    builder append "import org.thingml.java.ext.*;\n"

    builder append "\n\n"
  }
}

case class ThingKevoreeGenerator(val self: Thing){

  def generateKevoree(builder: StringBuilder = Context.builder) {
    println(self.getName)
    Context.thing = self

    //TODO: we might want some attributes to be manageable from Kevoree
    //generateDictionary();

    builder append "@ComponentType\n "
    builder append "@Library(name = \"ThingML\")\n "
    builder append "public class K" + Context.firstToUpper(self.getName) +" extends " + Context.firstToUpper(self.getName) + "{//The Kevoree wrapper extends the associated ThingML component\n"

    self.allPorts.filter{p => ! (p.getAnnotations.find{a => a.getName == "internal"}.isDefined)}.filter{p => p.getSends.size>0}
      .foreach{ p=>
      builder append "@Output\n"
      builder append "private org.kevoree.api.Port " + p.getName + "Port;\n"
    }

    builder append "//Constructor (all attributes)\n"
    builder append "public K" + Context.firstToUpper(self.getName) + "(String name"
    self.allPropertiesInDepth.foreach { p =>
      builder append ", final " + p.getType.java_type(p.getCardinality != null) + " " + p.Java_var_name
    }
    builder append ") {\n"
    builder append "super(name"
    self.allPropertiesInDepth.foreach { p =>
      builder append ", " + p.Java_var_name
    }
    builder append ");\n"
    builder append "}\n\n"


    builder append "@Start\n"
    builder append "public void startComponent() {start();\n}\n\n"

    builder append "@Stop\n"
    builder append "public void stopComponent() {stop();\n}\n\n"
 
/*    builder append "@Update\n"
    builder append "public void updateComponent() {System.out.println(\""+Context.file_name+" component update!\");\n"
    builder append "try {\n"
    self.allProperties/*InDepth*/.foreach{case p=>
        if(p.isChangeable){
          builder append p.getType.java_type()+" "+Context.protectJavaKeyword(p.getName)+" = new "+p.getType.java_type()+"((String)this.getDictionary().get(\""+p.getName+"\"));\n"
          builder append "wrapper.getInstance()."+p.scala_var_name+"_$eq("+Context.protectJavaKeyword(p.getName)+");\n"
        }
    }
    builder append "} catch (NullPointerException npe) {\n"
    builder append "System.out.println(\"Warning: no default value set for at least one property\");\n"
    builder append "}\n"
    builder append "}\n\n"
*/

    //forwards outgoing ThingML messages to Kevoree
    builder append "@Override\n"
    builder append "public void send(Event e, Port p) {\n"
    var i = 0
    self.allPorts.filter{p => ! (p.getAnnotations.find{a => a.getName == "internal"}.isDefined)}.filter{p => p.getSends.size > 0}
      .foreach{p =>
        if (i > 0)
          builder append "else "
        builder append "if (p.getName().equals(\"" + p.getName + "\")) {\n"
        builder append p.getName + "Port.send(e);\n"
        builder append "}\n"
        i = i + 1
    }
    builder append "}\n\n"

    //forwards incoming Kevoree messages to ThingML
    self.allPorts.filter{p => ! (p.getAnnotations.find{a => a.getName == "internal"}.isDefined)}.filter{p => p.getReceives.size>0}
      .foreach{ p=>
      builder append "@Input\n"
      builder append "public void " + p.getName + "(Object o) {\n"
      builder append "receive((Event)o, get" + Context.firstToUpper(p.getName) + "_port());\n"
      builder append "}\n\n"
    }
    builder append "}\n"
  }
  
  /*def generateParameters(builder: StringBuilder = Context.builder) {
    builder append self.allPropertiesInDepth.collect{case p=>      
        //"this.kevoreeComponent.getDictionary().get(\"" + p.getName + "\") != null ? new " + p.getType.java_type() + "((String) this.kevoreeComponent.getDictionary().get(\"" + p.getName + "\")) : " + initParameter(p.getType.java_type())
        initParameter(p)
    }.mkString(", ")  
  } */

  /*def initParameter(p : Property):String = {
    p.getType.java_type() match{
      case "Byte" => "this.kevoreeComponent.getDictionary().get(\"" + p.getName + "\") != null ? new " + p.getType.java_type() + "((String) this.kevoreeComponent.getDictionary().get(\"" + p.getName + "\")) : 0x00"
      case "Boolean" => "this.kevoreeComponent.getDictionary().get(\"" + p.getName + "\") != null ? new " + p.getType.java_type() + "((String) this.kevoreeComponent.getDictionary().get(\"" + p.getName + "\")) : false"
      case "Short" => "this.kevoreeComponent.getDictionary().get(\"" + p.getName + "\") != null ? new " + p.getType.java_type() + "((String) this.kevoreeComponent.getDictionary().get(\"" + p.getName + "\")) : 0"
      case "Integer" => "this.kevoreeComponent.getDictionary().get(\"" + p.getName + "\") != null ? new " + p.getType.java_type() + "((String) this.kevoreeComponent.getDictionary().get(\"" + p.getName + "\")) : 0"
      case "Float" => "this.kevoreeComponent.getDictionary().get(\"" + p.getName + "\") != null ? new " + p.getType.java_type() + "((String) this.kevoreeComponent.getDictionary().get(\"" + p.getName + "\")) : 0.0f"
      case "String" => "this.kevoreeComponent.getDictionary().get(\"" + p.getName + "\") != null ? new " + p.getType.java_type() + "((String) this.kevoreeComponent.getDictionary().get(\"" + p.getName + "\")) : \"\""
      case _ => "new " + p.getType.java_type() + "()"
    }
  } */
  
  /*def generateDictionary(builder: StringBuilder = Context.builder){
    if(self.allPropertiesInDepth.size>0)
    {
      builder append "@DictionaryType({\n"   
      builder append self.allProperties/*InDepth*/.collect{case p=>
          val valueBuilder = new StringBuilder()
          p.getInit().generateScala(valueBuilder)          
          "@DictionaryAttribute(name = \""+p.getName+"\"" + (if (valueBuilder.toString == "") "" else { ", defaultValue = \"" + valueBuilder.toString + "\""}) + ", optional = "+p.isChangeable+")"
      }.mkString(",\n")
      builder append "\n})\n"
    }
  } */
}
