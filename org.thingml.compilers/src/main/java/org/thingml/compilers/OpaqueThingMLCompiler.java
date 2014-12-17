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
import org.thingml.compilers.actions.ActionCompiler;
import org.thingml.compilers.api.ApiCompiler;
import org.thingml.compilers.build.BuildCompiler;
import org.thingml.compilers.main.MainGenerator;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by ffl on 24.11.14.
 */
public abstract class OpaqueThingMLCompiler extends ThingMLCompiler {

    public OpaqueThingMLCompiler(ActionCompiler actionCompiler, ApiCompiler apiCompiler, MainGenerator mainCompiler, BuildCompiler buildCompiler) {
        super(actionCompiler, apiCompiler, mainCompiler, buildCompiler);
    }
    PrintStream m, e;

    private void println(String msg) {
        if (m!=null)
            m.println(msg);
        else
            System.out.println(msg);
    }

    private void erroln(String msg) {
        if (e!=null)
            e.println(msg);
        else
            System.err.println(msg);
    }

    @Override
    public boolean compile(Configuration cfg) {
        final OutputStream stream = getMessageStream();
        if (stream != null) {
            m = new PrintStream(stream);
            e = new PrintStream(stream);
        }

        try {
            println("Running "+getName()+" compiler on configuration " + cfg.getName());
            do_call_compiler(cfg);
            println("Compilation complete.");
        }
        catch (Error err) {
            erroln("Compilation error:" + err.getMessage());
            return false;
        }
        return true;
    }

    public abstract void do_call_compiler(Configuration cfg);

}
