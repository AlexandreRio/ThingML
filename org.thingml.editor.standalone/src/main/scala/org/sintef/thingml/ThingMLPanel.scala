/**
 * Copyright (C) 2011 SINTEF <franck.fleurey@sintef.no>
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
package org.sintef.thingml

import java.awt.{Color, BorderLayout}
import javax.swing.{JToolBar, JScrollPane, JEditorPane, JPanel}
import actors.DaemonActor
import javax.swing.event.{DocumentEvent, DocumentListener}
import jsyntaxpane.components.Markers
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.common.util.URI
import java.io.ByteArrayInputStream
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import scala.collection.JavaConversions._
import javax.swing.text.{Utilities, JTextComponent}
import org.eclipse.emf.ecore.util.EcoreUtil

/**
 * User: ffouquet
 * Date: 29/06/11
 * Time: 15:58
 */

class ThingMLPanel extends JPanel {

  this.setLayout(new BorderLayout())
  jsyntaxpane.DefaultSyntaxKit.initKit();
  jsyntaxpane.DefaultSyntaxKit.registerContentType("text/thingml", classOf[ThingMLJSyntaxKit].getName());
  var codeEditor = new JEditorPane();
  var scrPane = new JScrollPane(codeEditor);
  codeEditor.setContentType("text/thingml; charset=UTF-8");

  //codeEditor.setBackground(Color.LIGHT_GRAY)

  var editorKit = codeEditor.getEditorKit
  var toolPane = new JToolBar
  editorKit.asInstanceOf[ThingMLJSyntaxKit].addToolBarActions(codeEditor, toolPane)


  add(scrPane, BorderLayout.CENTER)
  add(toolPane, BorderLayout.NORTH)


  def getIndex(line: Int, column: Int): Int = {
    val lineStart = codeEditor.getDocument.getDefaultRootElement.getElement( line -1 ).getStartOffset
    lineStart + column
  }
  def getNextIndex(offset : Int) = {
    if(codeEditor.getDocument.getEndPosition.getOffset > (offset + 1)){
       offset + 1
    } else {
      0
    }


  }

  def loadText(content : String){
     codeEditor.setText(content)
  }



  object notificationSeamless extends DaemonActor {
    start()
    var checkNeeded = false

    private def updateMarkers(content: String) {

      try {
        val ressource = new org.sintef.thingml.resource.thingml.mopp.ThingmlResource(URI.createURI(org.sintef.thingml.ThingmlPackage.eNS_URI))
        val stream = new ByteArrayInputStream(codeEditor.getText.getBytes);
        ressource.load(stream, new java.util.HashMap());
        //EcoreUtil.resolveAll(ressource)
        Markers.removeMarkers(codeEditor)
        ressource.getErrors.foreach {
          error =>
            val marker = new Markers.SimpleMarker(new Color(255,0,0,100), error.getMessage)
            val offset =  getIndex(error.getLine, error.getColumn)
            Markers.markText(codeEditor,offset ,getNextIndex(offset), marker)
        }
        ressource.getWarnings.foreach {
          error =>
            val marker = new Markers.SimpleMarker(new Color(255,155,0,100), error.getMessage)
            val offset =  getIndex(error.getLine, error.getColumn)
            Markers.markText(codeEditor,offset ,getNextIndex(offset), marker)
        }
      } catch {
        case _@e => {
          e.printStackTrace()
        }
      }

    }

    def act() {
      loop {
        reactWithin(500) {
          case scala.actors.TIMEOUT => if (checkNeeded) {

            if (codeEditor.getDocument.getLength > 1) {
              updateMarkers(codeEditor.getDocument.getText(0, codeEditor.getDocument.getLength - 1));
            }

            checkNeeded = false
          }
          case _ => checkNeeded = true
        }
      }
    }
  }

  codeEditor.getDocument.addDocumentListener(new DocumentListener() {
    def removeUpdate(e: DocumentEvent) {
      notificationSeamless ! "checkNeeded"
      //updateMarkers(e.getDocument.getText(0, e.getDocument.getLength - 1))
    }

    def insertUpdate(e: DocumentEvent) {
      notificationSeamless ! "checkNeeded"
      //updateMarkers(e.getDocument.getText(0, e.getDocument.getLength - 1))
    }

    def changedUpdate(e: DocumentEvent) {
      notificationSeamless ! "checkNeeded"
      //updateMarkers(e.getDocument.getText(0, e.getDocument.getLength - 1))
    }
  })

}