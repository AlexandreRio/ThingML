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

/**
 * User: ffouquet
 * Date: 29/06/11
 * Time: 15:25
 */

import javax.swing.text.Segment
import jsyntaxpane.{Token => JTOK}
import jsyntaxpane.TokenType
import resource.thingml.IThingmlTextToken
import resource.thingml.mopp.{ThingmlLexer, ThingmlAntlrScanner}
import scala.collection.JavaConversions._
;


class ThingMLJSyntaxLexerWrapper extends jsyntaxpane.Lexer {


  override def parse(sgmnt: Segment, i: Int, list: java.util.List[JTOK]) {
    list.clear()
    val lexer = new ThingmlLexer()
    val tokens = new ThingmlAntlrScanner(lexer)
    tokens.setText(sgmnt.toString)

    var token = tokens.getNextToken
    while (token != null) {
      val newtype = getType(token)
      val newtok = new JTOK(newtype, token.getOffset, token.getLength);
      token = tokens.getNextToken
      list.add(newtok)
    }

  }


  def getType(tok: IThingmlTextToken): TokenType = {
    tok.getName match {

      case "SL_COMMENT"=> TokenType.COMMENT
      case "ML_COMMENT"=> TokenType.COMMENT




      /*
case k: Keyword => TokenType.KEYWORD
case i: Identifier => TokenType.IDENTIFIER
case d: Delimiter => TokenType.DELIMITER
case c: Comment => TokenType.COMMENT
case c: MLComment => TokenType.COMMENT
case e: KError => TokenType.ERROR
case slit: StringLit => TokenType.STRING
case i: KIncomplet => TokenType.ERROR
      */
      case _ @ name => {
        println("tokName="+tok.getName)
        //println(tok.getText)
        //println(tok.getClass.getName)

       // if(lexer.getTokenNames.contains(name)){
       //    TokenType.KEYWORD
       // } else {
           TokenType.DEFAULT
        //}

      }
    }
  }

}