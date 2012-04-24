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
/*
 * @author Brice MORIN, SINTEF IKT
 */
package org.thingml.utils.comm

import org.thingml.utils.log.Logger

import ch.eth.coap.endpoint.{LocalEndpoint, RemoteEndpoint, Endpoint, LocalResource}
import ch.eth.coap.coap.{Request, PUTRequest}
import java.net.{InetAddress, URI}

trait CoAPThingML {

  private var coapServer : CoAP = _
  def setCoapServer(coapServer : CoAP) {this.coapServer = coapServer}

  /*
   * This will be redefined in concrete ThingML things to instantiate their specific (and generated) COaP servers
   */
  def initialize(port : Int = 61616) {
     new LocalCoAP(this, port)
  }

  /************************************************************************
   * Send and receive operations to allow communication between CoAP and ThingML
   *************************************************************************/
  def sendDataViaCoAP(bytes : Array[Byte], resourceURI : String) {
    val request = new PUTRequest()
    val uri = coapServer match {
      case l : LocalCoAP => new URI("coap://localhost:" + l.port + "/" + resourceURI)
      case r : RemoteCoAP => new URI(r.getURI + "/" + resourceURI)
    }
    request.setURI(uri)
    request.setPayload(bytes)

    //enable response queue in order to use blocking I/O
    //request.enableResponseQueue(true)//Let's just fire-n-forget for now...

    request.execute()
  }
  
  def sendData(bytes : Array[Byte]) {
    Logger.debug("sendData(" + bytes.mkString("[", ", ", "]") + ")")
    val uri = coapServer.resourceMap.get(bytes(4)).getOrElse("")//it should be bytes(3) in the 16-bytes array, but this 18-bytes array includes the START and STOP bytes, hence the index++
    sendDataViaCoAP(bytes, uri)
  }

  def receive(byte : Array[Byte])//This will be refined in the COaP Thing defined in ThingML
}


abstract trait CoAP {
  self : Endpoint =>
  
  var resourceMap = Map[Byte, String]()
  Logger.info("Californium/ThingML server started on " + uriToString)
  Logger.info(" at " + ipInfo)
  Logger.info("  https://github.com/brice-morin/californium  ")
  Logger.info("            http://www.ThingML.org            ")
  
  def uriToString : String
  def coapThingML : CoAPThingML
  
  def ipInfo : String = {
    val addr = InetAddress.getLocalHost()
    return addr.getHostAddress + "(" + addr.getHostName() + ")"
  }
  
}

class LocalCoAP(val coapThingML : CoAPThingML, override val port : Int) extends LocalEndpoint(port) with CoAP {

  coapThingML.setCoapServer(this)

  override def uriToString = "port " + port

  def addResource(resource: ThingMLCoAPLocalResource) {
    super.addResource(resource)
    resourceMap += (resource.code -> resource.getResourcePath)
  }
  
  override def handleRequest(request: Request) {
    Logger.debug("Incoming request: " + request)
    request.log
    super.handleRequest(request)
  }
  
}

class RemoteCoAP(val coapThingML : CoAPThingML, uri : URI) extends RemoteEndpoint(uri) with CoAP {
  self : RemoteEndpoint =>

  coapThingML.setCoapServer(this)
  
  override def uriToString = "URI " + uri
  
  protected[comm] def getURI = uri

  override def handleRequest(request: Request) {
    Logger.debug("Incoming request: " + request)
    request.log
    super.handleRequest(request)
  }  
}