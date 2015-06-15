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

import java.io.File;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ffl on 23.11.14.
 */
public abstract class ThingMLCompiler {

    protected Context ctx = new Context(this);

    private ActionCompiler actionCompiler;
    private ApiCompiler apiCompiler;
    private MainGenerator mainCompiler;
    private BuildCompiler buildCompiler;
    private BehaviorCompiler behaviorCompiler;

    //we might need several connector compilers has different ports might use different connectors
    private Map<String, ConnectorCompiler> connectorCompilers = new HashMap<String, ConnectorCompiler>();

    public ThingMLCompiler() {
        this.actionCompiler = new ActionCompiler();
        this.apiCompiler = new ApiCompiler();
        this.mainCompiler = new MainGenerator();
        this.buildCompiler = new BuildCompiler();
        this.behaviorCompiler = new BehaviorCompiler();
        connectorCompilers.put("default", new ConnectorCompiler());
    }

    public ThingMLCompiler(ActionCompiler actionCompiler, ApiCompiler apiCompiler, MainGenerator mainCompiler, BuildCompiler buildCompiler, BehaviorCompiler behaviorCompiler) {
        this.actionCompiler = actionCompiler;
        this.apiCompiler = apiCompiler;
        this.mainCompiler = mainCompiler;
        this.buildCompiler = buildCompiler;
        this.behaviorCompiler = behaviorCompiler;
    }

    public abstract ThingMLCompiler clone();

    /**************************************************************
     * META-DATA about this particular compiler
     **************************************************************/
    public abstract String getPlatform();
    public abstract String getName();
    public abstract String getDescription();

    /**************************************************************
     * Parameters common to all compilers
     **************************************************************/

    /**************************************************************
     * Entry point of the compiler
     **************************************************************/
    public abstract boolean compile(Configuration cfg, String... options);

    public boolean compileConnector(String connector, Configuration cfg, String... options) {
        ctx.setCurrentConfiguration(cfg);
        final ConnectorCompiler cc = connectorCompilers.get(connector);
        if (cc != null) {
            cc.generateLib(ctx, cfg, options);
            ctx.writeGeneratedCodeToFiles();
            return true;
        }
        return false;
    }

    public ActionCompiler getActionCompiler() {
        return actionCompiler;
    }

    public ApiCompiler getApiCompiler() {
        return apiCompiler;
    }

    public MainGenerator getMainCompiler() {
        return mainCompiler;
    }

    public BuildCompiler getBuildCompiler() {
        return buildCompiler;
    }

    public BehaviorCompiler getBehaviorCompiler() {return behaviorCompiler; }

    public void addConnectorCompilers(Map<String, ConnectorCompiler> connectorCompilers) {
        this.connectorCompilers.putAll(connectorCompilers);
    }

    public Map<String, ConnectorCompiler> getConnectorCompilers() {
        return Collections.unmodifiableMap(connectorCompilers);
    }
    
    private OutputStream messageStream = System.out;
    private OutputStream errorStream = System.err;

    public OutputStream getErrorStream() {
        return errorStream;
    }

    public void setErrorStream(OutputStream errorStream) {
        this.errorStream = errorStream;
    }

    public OutputStream getMessageStream() {
        return messageStream;
    }

    public void setMessageStream(OutputStream messageStream) {
        this.messageStream = messageStream;
    }
    
   private File outputDirectory = null;

    public void setOutputDirectory(File outDir) {
        outDir.mkdirs();
        if (!outDir.exists()) throw new Error("ERROR: The output directory does not exist (" + outDir.getAbsolutePath() + ").");
        if (!outDir.isDirectory()) throw new Error("ERROR: The output directory has to be a directory (" + outDir.getAbsolutePath() + ").");
        if (!outDir.canWrite()) throw new Error("ERROR: The output directory is not writable (" + outDir.getAbsolutePath() + ").");
        outputDirectory = outDir;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    
}
