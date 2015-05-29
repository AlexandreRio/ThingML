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
package org.thingml.compilers.cep;

import org.sintef.thingml.*;
import org.thingml.compilers.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ludovic
 */
public class JSCepCompiler extends CepCompiler {
    @Override
    public void generateStream(Stream stream, StringBuilder builder, Context ctx) {
        if( (stream.getInputs().size() > 1 && stream.getOutput().getParameters().size() == 0) ||
                stream.getInputs().size() == 1) {
            builder.append("var " + stream.qname("_") + " = Rx.Observable.fromEvent(this.eventEmitterForStream" + ", '" + stream.qname("_") + "')");

            if (stream.isFinalStream()) {
                builder.append(".subscribe(\n\t" +
                        "function(x) {\n\t\t" +
                        "var json = JSON.parse(x);\n\t\t" +
                        "console.log(\"Hack!! \"");

                for (Parameter p : stream.getInputs().get(0).getMessage().getParameters()) {
                    builder.append(" + \"" + p.getName() + "= \" + json." + p.getName() + "+ \"; \"");
                }

                builder.append(");\n");
                ctx.getCompiler().getActionCompiler().generate(stream.getOutput(), builder, ctx);

                builder.append("});\n");
            }
        } else if(stream.getInputs().size() == 2 && stream.getOutput().getParameters().size() > 0) {
            builder.append("function wait1() { return Rx.Observable.timer(50); }\n");
            for(ReceiveMessage rm : stream.getInputs()) {
                builder.append("var " + stream.qname("_") + "_" + rm.getMessage().getName() + " = Rx.Observable.fromEvent(this.eventEmitterForStream" + ", '" + stream.qname("_") + "_" + rm.getMessage().getName() + "');\n");
            }
            String nameSTream1 = stream.qname("_") + "_" + stream.getInputs().get(1).getMessage().getName(),
                    nameSTream2 = stream.qname("_") + "_" + stream.getInputs().get(0).getMessage().getName();
            builder.append("var " + stream.qname("_") + " = " + nameSTream1 + ".join(" + nameSTream2 + ",wait1,wait1,\n" +
                            "\tfunction(m1,m2) {\n" + //fixme
                                "\t\tvar m1J = JSON.parse(m1);\n" + //fixme
                                "\t\tvar m2J = JSON.parse(m2);\n"); //fixme

            List<Expression> parameters = new ArrayList<>();
            String returnString = "'{ ";
            int i = 0;
            for(Expression p : stream.getOutput().getParameters()) {

                String name = stream.getOutput().getMessage().getParameters().get(i).getName();
                builder.append("\t\tvar " + name + " = ");
                ctx.getCompiler().getActionCompiler().generate(TransformExpression.copyExpression(p),builder,ctx);
                builder.append(";\n");
                if(i==0)
                    returnString += "\"" + name + "\": ' + " + name + " + '";
                else
                    returnString += ", \"" + name + "\" : ' + " + name + " + '";

                ExternExpression externExpression = ThingmlFactory.eINSTANCE.createExternExpression();
                externExpression.setExpression("json." + name);
                parameters.add(externExpression);
                i++;
            }
            returnString += "}';";
            builder.append("\t\treturn " + returnString + "\n");
            builder.append("\t}).subscribe(\n\t" +
                                 "\t\tfunction(x) {\n" +
                                    "\t\t\tvar json = JSON.parse(x);\n");
            builder.append("\t\t\t");

            stream.getOutput().getParameters().clear();
            stream.getOutput().getParameters().addAll(parameters);
            ctx.getCompiler().getActionCompiler().generate(stream.getOutput(),builder,ctx);


            builder.append("\t});\n");
        } else {
            throw new UnsupportedOperationException("Incorrect number of inputs or parameters for stream " + stream.getName());
        }

    }
}
