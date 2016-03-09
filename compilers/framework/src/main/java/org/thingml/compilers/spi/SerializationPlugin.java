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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.compilers.spi;

import java.util.List;
import java.util.Set;
import org.sintef.thingml.Configuration;
import org.sintef.thingml.Message;
import org.thingml.compilers.Context;
import org.thingml.compilers.checker.Checker;
import org.thingml.compilers.checker.Rule;

/**
 *
 * @author sintef
 */
public abstract class SerializationPlugin extends Rule  {
    
    public Context context;
    public Configuration configuration;
    
    public SerializationPlugin (Context ctx, Configuration cfg) {
        context = ctx;
        configuration = cfg;
    }
    
    /* ------------ Plugin Body (Mandatory) ------------ */
    
    /* Methods: generateSerialization
     * 
     * Parameters:
     *      - builder : StringBuilder in which the generated 
     *                  code will be put.
     *      - bufferName : name of the buffer that will contain
     *                    the serialization.
     *                    the size of buffer.
     *      - m : Model of the message to be serialized
     * 
     * Results:
     *      builder will contain code declaring a buffer named
     *      bufferName of the require size. The buffer will 
     *      contain a serialization of message m. The size of
     *      the buffer is returned by the method.
     * 
     * Note: All paramters annotated as ignored must be ignored.
    */
    public abstract int generateSerialization(StringBuilder builder, String bufferName, Message m);
    
    /* Methods: generateParserBody
     * 
     * Parameters:
     *      - builder : StringBuilder in which the generated 
     *                  code will be put.
     *      - bufferName : name of the buffer that contains
     *                    the raw information to be parsed
     *      - bufferSizeName : name of the variable that contains
     *                    the size of buffer.
     *      - messages : Model of the messages to be parseded
     *      - sender : name of the variable describing the externalport
     * 
     * Results:
     *      builder will contain code parsing buffer, and sending
     *      a ThingML message to connected ports.
     * 
     * Note: All paramters annotated as ignored must be ignored.
    */
    public abstract void generateParserBody(StringBuilder builder, String bufferName, String bufferSizeName, Set<Message> messages, String sender);
    
    /* ------------ Plugin Info (Mandatory) ------------ */
    
    /*
     * In case of overlaping protocol support the 
     * choice of plugin will be specified with the
     * annotation @plugin "plugiID"
    */
    public abstract String getPluginID();
    
    public abstract String getTargetedLanguage();
    
    
    
    
    /* ------------ Rule Checkng (Optional) ------------
     * Should be overriden if the plugin need to perform
     * some specific checking.
    */
    
    public Checker.InfoType getHighestLevel() {
        return Checker.InfoType.NOTICE;
    }

    
    public String getName() {
        return this.getPluginID() + " plugin's rules";
    }

    
    public String getDescription() {
        return "Check that " + this.getPluginID() + " plugin can be used.";
    }

    
    public void check(Configuration cfg, Checker checker) {
        
    }
}