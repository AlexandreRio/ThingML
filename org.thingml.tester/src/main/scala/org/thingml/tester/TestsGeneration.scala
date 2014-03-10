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
package org.thingml.tester
import scala.io.Source
import junit.framework.Assert
import java.lang.AssertionError
import java.io._
import java.util._

object TestsGeneration {
	def main(args: Array[String]) {
		var p: Process = Runtime.getRuntime().exec("mvn clean install",null,new File((new File(System.getProperty("user.dir"))).getParentFile(),"org.thingml.cmd"))
		var line = ""
		var in: BufferedReader = new BufferedReader(
		new InputStreamReader(p.getInputStream()) )
		
		var result : BufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/test/java/tests/results.html")))
		result.write("<!DOCTYPE html>\n"+
"<html>\n"+
"	<head>\n"+
"		<meta charset=\"utf-8\" />\n"+
"		<title>ThingML tests results</title>\n"+
"		<style>\n"+
"		table\n"+
"		{\n"+
"			border-collapse: collapse;\n"+
"		}\n"+
"		td, th \n"+
"		{\n"+
"			border: 1px solid black;\n"+
"		}\n"+
"		.green\n"+
"		{\n"+
"			background: lightgreen\n"+
"		}\n"+
"		.red\n"+
"		{\n"+
"			background: red\n"+
"		}\n"+
"		</style>\n"+
"	</head>\n"+
"	<body>\n"+
"		<Table>\n"+
"	<tr>\n"+
"		<th>Test name</th>\n"+
"		<th>Compiler</th>\n"+
"		<th>Result</th>\n"+
"	</tr>\n")
		result.close();
		while ({line = in.readLine(); line!= null}) {
			System.out.println(line)
		}
		in.close();
		p = Runtime.getRuntime().exec("python ../org.thingml.tests/src/main/thingml/tests/Tester/genTests.py")
		in = new BufferedReader(
		new InputStreamReader(p.getInputStream()) )
		while ({line = in.readLine(); line!= null}) {
			System.out.println(line)
		}
		in.close();
    }
}