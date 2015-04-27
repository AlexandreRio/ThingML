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
package org.thingml.compilers;

import org.sintef.thingml.*;
import org.sintef.thingml.constraints.ThingMLHelpers;
import org.thingml.cgenerator.CGenerator;
import org.thingml.compilers.actions.ActionCompiler;
import org.thingml.compilers.actions.JSActionCompiler;
import org.thingml.compilers.api.ApiCompiler;
import org.thingml.compilers.api.JavaScriptApiCompiler;
import org.thingml.compilers.behavior.BehaviorCompiler;
import org.thingml.compilers.behavior.JSBehaviorCompiler;
import org.thingml.compilers.build.BuildCompiler;
import org.thingml.compilers.build.JSBuildCompiler;
import org.thingml.compilers.connectors.ConnectorCompiler;
import org.thingml.compilers.connectors.JS2Kevoree;
import org.thingml.compilers.main.JSMainGenerator;
import org.thingml.compilers.main.MainGenerator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ffl on 25.11.14.
 */
public class JavaScriptCompiler extends OpaqueThingMLCompiler {

    {
        Map<String, ConnectorCompiler> connectorCompilerMap = new HashMap<>();
        connectorCompilerMap.put("kevoree-js", new JS2Kevoree());
        addConnectorCompilers(connectorCompilerMap);
    }

    public JavaScriptCompiler() {
        super(new JSActionCompiler(), new JavaScriptApiCompiler(), new JSMainGenerator(), new JSBuildCompiler(), new JSBehaviorCompiler());
    }

    public JavaScriptCompiler(ActionCompiler actionCompiler, ApiCompiler apiCompiler, MainGenerator mainCompiler, BuildCompiler buildCompiler, BehaviorCompiler behaviorCompiler) {
        super(actionCompiler, apiCompiler, mainCompiler, buildCompiler, behaviorCompiler);
    }

    @Override
    public ThingMLCompiler clone() {
        return new JavaScriptCompiler();
    }

    @Override
    public String getPlatform() {
        return "javascript";
    }

    @Override
    public String getName() {
        return "Javascript for NodeJS";
    }

    public String getDescription() {
        return "Generates Javascript code for the NodeJS platform.";
    }

    @Override
    public void do_call_compiler(Configuration cfg, String... options) {
        ctx.setThisRef("_this.");
        new File(ctx.getOutputDir() + "/" + cfg.getName()).mkdirs();
        ctx.setCurrentConfiguration(cfg);
        compile(cfg, ThingMLHelpers.findContainingModel(cfg), true, ctx);
        ctx.getCompiler().getBuildCompiler().generate(cfg, ctx);
        ctx.dump();
    }

    private void compile(Configuration t, ThingMLModel model, boolean isNode, Context ctx) {
        ctx.copy(this.getClass().getClassLoader().getResourceAsStream("javascript/lib/state-factory.js"), t.getName(), "state-factory.js");
        ctx.copy(this.getClass().getClassLoader().getResourceAsStream("javascript/lib/Connector.js"), t.getName(), "Connector.js");

        for(Type ty : model.allUsedSimpleTypes()) {
            if (ty instanceof Enumeration) {
                Enumeration e = (Enumeration) ty;
                ctx.addProperty("hasEnum", "true");
                StringBuilder builder = ctx.getBuilder(ctx.getCurrentConfiguration().getName() + "/enums.js"); //FIXME: this code should be integrated into the compilation framework
                builder.append("// Definition of Enumeration  " + e.getName() + "\n");
                builder.append("var " + e.getName() + "_ENUM = {\n");
                int i = 0;
                for(EnumerationLiteral l : e.getLiterals()) {
                    if (i > 0)
                        builder.append(",\n");
                    builder.append(l.getName().toUpperCase() + ": \"" + l.getName() + "\"");
                    i++;
                }
                builder.append("}\n");
                builder.append("exports." + e.getName() + "_ENUM = " + e.getName() + "_ENUM;\n");
            }
        }
        for(Thing thing : t.allThings()) {
            ctx.getCompiler().getApiCompiler().generateComponent(thing, ctx);
            ctx.getCompiler().getApiCompiler().generatePublicAPI(thing, ctx);
        }
        ctx.getCompiler().getMainCompiler().generate(t, model, ctx);
    }
}
