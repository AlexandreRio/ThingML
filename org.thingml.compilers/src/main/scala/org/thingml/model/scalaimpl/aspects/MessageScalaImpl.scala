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
package org.thingml.model.scalaimpl.aspects

import org.sintef.thingml._
import org.thingml.model.scalaimpl.ThingMLScalaImpl._
import scala.collection.JavaConversions._
import java.util.ArrayList
import org.sintef.thingml.constraints.ThingMLHelpers
import java.lang.Boolean

/**
 * Created by IntelliJ IDEA.
 * User: ffl
 * Date: 04.07.11
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */

case class MessageScalaImpl (self : Message) {
  def getCode : Int = {
    self.getAnnotations.filter {
      a => a.getName == "code"
    }.headOption match {
      case Some(a) => {
          return a.asInstanceOf[PlatformAnnotation].getValue.toInt
        }
      case None => {
          return -1
        }
    }
  }
  
  def getSenMLunits : List[String] = {
    self.getAnnotations.filter {
      a => a.getName == "senML_unit"
    }.headOption match {
      case Some(a) => {
          return a.asInstanceOf[PlatformAnnotation].getValue.split(",").toList.map(_.trim)
        }
      case None => {
          return Nil
        }
    }
  }
  
  def getHTTPurls : Set[String] = {
    self.getAnnotations.filter {
      a => a.getName == "http"
    }.headOption match {
      case Some(a) => {
          return a.asInstanceOf[PlatformAnnotation].getValue.split(",").toList.map(_.trim).toSet
        }
      case None => {
          return Set()
        }
    }
  }
  
}