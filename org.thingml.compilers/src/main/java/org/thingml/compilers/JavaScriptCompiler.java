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

import org.sintef.thingml.Configuration;
import org.sintef.thingml.constraints.ThingMLHelpers;
import org.thingml.cgenerator.CGenerator;
import org.thingml.compilers.actions.ActionCompiler;
import org.thingml.compilers.actions.JSActionCompiler;
import org.thingml.compilers.api.ApiCompiler;
import org.thingml.compilers.api.JavaScriptApiCompiler;
import org.thingml.compilers.build.BuildCompiler;
import org.thingml.compilers.build.JSBuildCompiler;
import org.thingml.compilers.main.MainGenerator;

/**
 * Created by ffl on 25.11.14.
 */
public class JavaScriptCompiler extends OpaqueThingMLCompiler {

    public JavaScriptCompiler() {
        super(new JSActionCompiler(), new JavaScriptApiCompiler(), new MainGenerator(), new JSBuildCompiler());
    }

    public JavaScriptCompiler(ActionCompiler actionCompiler, ApiCompiler apiCompiler, MainGenerator mainCompiler, BuildCompiler buildCompiler) {
        super(actionCompiler, apiCompiler, mainCompiler, buildCompiler);
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
    public void do_call_compiler(Configuration cfg) {
        Context ctx = new Context(this);
        org.thingml.jsgenerator.JavaScriptGenerator.compileAndRun(cfg, ThingMLHelpers.findContainingModel(cfg), false, getOutputDirectory(), ctx);
    }
}
