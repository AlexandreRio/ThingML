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
package org.thingml.compilers.c.arduino.plugin;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.sintef.thingml.Configuration;
import org.sintef.thingml.ExternalConnector;
import org.sintef.thingml.Message;
import org.sintef.thingml.Parameter;
import org.sintef.thingml.Port;
import org.sintef.thingml.Protocol;
import org.sintef.thingml.Thing;
import org.thingml.compilers.c.CCompilerContext;
import org.thingml.compilers.c.CMessageSerializer;
import org.thingml.compilers.c.CNetworkLibraryGenerator;
import org.thingml.compilers.c.plugin.CByteArraySerializer;
import org.thingml.compilers.c.plugin.CMSPSerializer;

/**
 *
 * @author sintef
 */
public class NoBufSerial extends CNetworkLibraryGenerator {

    CMessageSerializer ser;
        
    HWSerial Serial0 = new HWSerial();
    HWSerial Serial1 = new HWSerial();
    HWSerial Serial2 = new HWSerial();
    HWSerial Serial3 = new HWSerial();
    
    public NoBufSerial(Configuration cfg, CCompilerContext ctx) {
        super(cfg, ctx);
        this.ser = new CByteArraySerializer(ctx, cfg);
    }
    public NoBufSerial(Configuration cfg, CCompilerContext ctx, Set<ExternalConnector> ExternalConnectors) {
        super(cfg, ctx, ExternalConnectors);
        this.ser = new CByteArraySerializer(ctx, cfg);
    }

    @Override
    public void generateNetworkLibrary() {
        CCompilerContext ctx = (CCompilerContext) this.ctx;
        
        Protocol prot;
        for(ExternalConnector eco : this.getExternalConnectors()) {
            if((eco.getProtocol().getName().compareToIgnoreCase("Serial0") == 0) 
                    || (eco.getProtocol().getName().compareToIgnoreCase("Serial") == 0)) {
                Serial0.protocol = eco.getProtocol();
                Serial0.ecos.add(eco);
                eco.setName(eco.getProtocol().getName());
            
            } else if (eco.getProtocol().getName().compareToIgnoreCase("Serial1") == 0) {
                Serial1.protocol = eco.getProtocol();
                Serial1.ecos.add(eco);
                eco.setName(eco.getProtocol().getName());
            } else if (eco.getProtocol().getName().compareToIgnoreCase("Serial2") == 0) {
                Serial2.protocol = eco.getProtocol();
                Serial2.ecos.add(eco);
                eco.setName(eco.getProtocol().getName());
            } else if (eco.getProtocol().getName().compareToIgnoreCase("Serial3") == 0) {
                Serial3.protocol = eco.getProtocol();
                Serial3.ecos.add(eco);
                eco.setName(eco.getProtocol().getName());
            }
            
        }
        
        Serial0.generateNetworkLibrary(ctx);
        Serial1.generateNetworkLibrary(ctx);
        Serial2.generateNetworkLibrary(ctx);
        Serial3.generateNetworkLibrary(ctx);
    }
    
    @Override
    public void generateMessageForwarders(StringBuilder builder, StringBuilder headerbuilder) {
        CCompilerContext ctx = (CCompilerContext) this.ctx;
        for (ExternalConnector eco : this.getExternalConnectors()) {
            //if (eco.hasAnnotation("c_external_send")) {
            Thing t = eco.getInst().getInstance().getType();
            Port p = eco.getPort();
            CMessageSerializer Aser;
            if(eco.getProtocol().isDefined("serializer", "MSP")) {
                Aser = new CMSPSerializer(ctx, cfg);
            } else if (eco.getProtocol().isDefined("serializer", "msgpack")) {
                Aser = new ArduinoMessagePackSerializer(ctx, cfg);
            } else {
                Aser = new CByteArraySerializer(ctx, cfg);
            }
            
            for (Message m : p.getSends()) {
                Set<String> ignoreList = new HashSet<String>();

                builder.append("// Forwarding of messages " + eco.getName() + "::" + t.getName() + "::" + p.getName() + "::" + m.getName() + "\n");
                builder.append("void forward_" + eco.getName() + "_" + ctx.getSenderName(t, p, m));
                ctx.appendFormalParameters(t, builder, m);
                builder.append("{\n");
                
                
                
                int i = Aser.generateMessageSerialzer(eco, m, builder, "forward_buf", new LinkedList<Parameter>());
                //ctx.generateSerializationForForwarder(m, builder, ctx.getHandlerCode(cfg, m), ignoreList);

                builder.append("\n//Forwarding with specified function \n");
                builder.append(eco.getName() + "_forwardMessage(forward_buf, " + i + ");\n");
                
        //builder.append(eco.annotation("c_external_send").iterator().next() + "(forward_buf, " + (ctx.getMessageSerializationSize(m) - 2) + ");\n");
                builder.append("}\n\n");
            }
                
        }
    }
    
    
    private class HWSerial {
        Set<ExternalConnector> ecos;
        Protocol protocol;
        String port;
        String header;
        String tail;
        char escapeChar;
        char[] charToEscape;
        boolean escape;
        int baudrate;
        CMessageSerializer ser;
        
        HWSerial() {
            ecos = new HashSet<>();
        }
        
        void readAnnotations() {
            port = protocol.getName();
            escape = false;
            baudrate = 115200;
            String toEscape = "";
            if(protocol.hasAnnotation("serial_start_byte")) {
                Integer i = Integer.parseInt(protocol.annotation("serial_start_byte").get(0));
                header ="" + ((char) i.intValue());
                System.out.println("header: " + i);
                System.out.println("header: " + header);
            }
            if(protocol.hasAnnotation("serial_header")) {
                header = protocol.annotation("serial_header").get(0);
            }
            if(protocol.hasAnnotation("serial_stop_byte")) {
                Integer i = Integer.parseInt(protocol.annotation("serial_stop_byte").get(0));
                tail = "" + ((char) i.intValue());
                System.out.println("footer: " + i);
                System.out.println("footer: " + tail);
            }
            if(protocol.hasAnnotation("serial_footer")) {
                tail = protocol.annotation("serial_footer").get(0);
            }
            if(protocol.hasAnnotation("serial_escape_byte")) {
                Integer i = Integer.parseInt(protocol.annotation("serial_escape_byte").get(0));
                escapeChar = (char) i.intValue();
                escape = true;
            }
            if(protocol.hasAnnotation("serial_escape_char")) {
                escapeChar = protocol.annotation("serial_escape_byte").get(0).charAt(0);
                escape = true;
            }
            if(protocol.hasAnnotation("serial_baudrate")) {
                Integer i = Integer.parseInt(protocol.annotation("serial_baudrate").get(0));
                baudrate = i.intValue();
            }
            if(escape) {
                if(header != null)
                    toEscape += header.charAt(0);
                if(tail != null)
                    toEscape += tail.charAt(0);
                toEscape += escapeChar;
                charToEscape = toEscape.toCharArray();
                System.out.println("toEscape: " + toEscape);
            }
        }
        
        void generateNetworkLibrary(CCompilerContext ctx) {
            if(!ecos.isEmpty()) {
                readAnnotations();
                String ctemplate = ctx.getNetworkLibNoBufSerialTemplate();
                //Processing TODO

                String portName = port;
                for(ExternalConnector eco : ecos) {
                    if(eco.hasAnnotation("port_name")) {
                        portName = eco.annotation("port_name").iterator().next();
                    }

                    eco.setName(portName);
                }
                StringBuilder listenerState = new StringBuilder();
                StringBuilder parserImpl = new StringBuilder();
                StringBuilder tailHandling = new StringBuilder();
                StringBuilder readerImpl = new StringBuilder();

                readerImpl.append("bool /*PORT_NAME*/_read_header() {\n");
                readerImpl.append(" bool /*PORT_NAME*/_res = true;\n");
                if(header != null) {
                    ctemplate = ctemplate.replace("/*WRITE_HEADER*/", port + ".write(\"" + header + "\");\n");
                    readerImpl.append(" uint8_t /*PORT_NAME*/_buf;\n");
                    for(char c : header.toCharArray()) {
                        readerImpl.append(" /*PORT_NAME*/_res &= ((" + port + ".readBytes(&/*PORT_NAME*/_buf, 1) == 1) && (/*PORT_NAME*/_buf == " + (int) c + "));\n");
                    }
                }
                readerImpl.append(" return /*PORT_NAME*/_res;\n");
                readerImpl.append("}\n");
                ctemplate = ctemplate.replace("/*BAUDRATE*/", ""+baudrate);
                
                if(tail != null) {
                    ctemplate = ctemplate.replace("/*WRITE_FOOTER*/", port + ".write(\"" + tail + "\");\n");
                    listenerState.append("#define /*PORT_NAME*/_LISTENER_STATE_READ_TAIL 2");
                     ctemplate = ctemplate.replace("/*TRANSITION_TO_READ_FOOTER*/", "/*PORT_NAME*/_serialListenerState = /*PORT_NAME*/_LISTENER_STATE_READ_TAIL;\n");

                    readerImpl.append("bool /*PORT_NAME*/_read_footer() {\n");
                    readerImpl.append(" bool /*PORT_NAME*/_res = true;\n");
                    readerImpl.append(" uint8_t /*PORT_NAME*/_buf;\n");
                    for(char c : tail.toCharArray()) {
                        readerImpl.append(" /*PORT_NAME*/_res &= ((" + port + ".readBytes(&/*PORT_NAME*/_buf, 1) == 1) && (/*PORT_NAME*/_buf == " + (int) c + "));\n");
                    }
                    readerImpl.append(" return /*PORT_NAME*/_res;\n");
                    readerImpl.append("}\n");

                    tailHandling.append("case /*PORT_NAME*/_LISTENER_STATE_READ_TAIL:\n" +
                    "        if(/*PORT_NAME*/_read_footer()) {\n" +
                    "              /*PORT_NAME*/_serialListenerState = /*PORT_NAME*/_LISTENER_STATE_IDLE;\n" +
                    "            } else {\n" +
                    "              /*PORT_NAME*/_serialListenerState = /*PORT_NAME*/_LISTENER_STATE_ERROR;\n" +
                    "            }\n" +
                    "      break;");
                }

                if(escape) {
                    StringBuilder escapeImpl = new StringBuilder();
                    escapeImpl.append("if(");
                    boolean isFirst = true;
                    for(char c : charToEscape) {
                        if(isFirst)
                            isFirst = false;
                        else
                            escapeImpl.append(" || ");
                        escapeImpl.append("(msg[i] == " + (int) c + ")");
                    }
                    escapeImpl.append(") {\n");
                    escapeImpl.append(port + ".write(" + (int) escapeChar + ");\n");
                    escapeImpl.append("}\n");
                    ctemplate = ctemplate.replace("/*WRITE_ESCAPE*/", escapeImpl);
                }

                // -------------------- Parser -------------------- 
                Set<Message> messages = new HashSet<Message>();
                int maxSize = 0;
                int minSize = 0;
                for(ExternalConnector eco : ecos) {
                    for(Message msg : eco.getPort().getReceives()) {
                        if(!ctx.containsMessage(messages, msg)) {
                            messages.add(msg);
                            if(ctx.getMessageSerializationSize(msg) > maxSize) {
                                maxSize = ctx.getMessageSerializationSize(msg);
                            }
                        }
                    }
                }
                if(protocol.isDefined("serializer", "msgpack")) {
                    ArduinoMessagePackSerializer Aser = new ArduinoMessagePackSerializer(ctx, cfg);
                    Aser.generateMessageParser(port, port, parserImpl, messages, maxSize);
                } else {
                    if(protocol.isDefined("serializer", "MSP")) {
                        CMSPSerializer Aser = new CMSPSerializer(ctx, cfg);
                        //Aser.generateMessageParser(ecos.iterator().next(), readerImpl);
                        Aser.generateMessageParser(portName, messages, readerImpl);
                    }
                    
                    parserImpl.append("bool /*PORT_NAME*/_parse() {\n");
                    
                    parserImpl.append("uint8_t msgbuf[" + maxSize + "];\n");
                    parserImpl.append("uint8_t bytebuf;\n");
                    parserImpl.append("uint8_t index = 0;\n");
                    if(protocol.isDefined("serializer", "MSP")) {
                         parserImpl.append("" + port + ".readBytes(&bytebuf, 1);\n");
                         parserImpl.append("" + port + ".readBytes(&bytebuf, 1);\n");
                         parserImpl.append("msgbuf[0] = 1;\n");
                         parserImpl.append("index++;\n");
                         parserImpl.append("index += " + port + ".readBytes(&msgbuf[1], 1);\n");
                    } else {
                        parserImpl.append("index += " + port + ".readBytes(&msgbuf[index], 2);\n");
                    }
                    parserImpl.append("uint16_t msgID = (msgbuf[0] << 8) + msgbuf[1];\n");
                    parserImpl.append("if(index != 2) return false;\n");
                    parserImpl.append("switch(msgID) {\n");
                    for(Message m : messages) {
                        parserImpl.append("case ");
                        parserImpl.append(ctx.getHandlerCode(cfg ,m));
                        parserImpl.append(":\n");
                        parserImpl.append("while(index < " + (ctx.getMessageSerializationSize(m)-2) + ") {\n");
                        parserImpl.append("if(" + port + ".readBytes(&bytebuf, 1) == 0) {\n");
                        parserImpl.append("return false;\n}\n");

                        if(escape) {
                            parserImpl.append("if(bytebuf != " + (int) escapeChar + ") {\n");
                        }
                        parserImpl.append("msgbuf[index] = bytebuf;\n");
                        parserImpl.append("index++;\n");

                        if(escape) {
                            parserImpl.append("} else {\n");
                            parserImpl.append("if(" + port + ".readBytes(&bytebuf, 1) == 0) {\n");
                            parserImpl.append("return false;\n}\n");
                            parserImpl.append("msgbuf[index] = bytebuf;\n");
                            parserImpl.append("index++;\n");
                            parserImpl.append("}\n");
                        }
                        parserImpl.append("}\n");
                        parserImpl.append("break;\n");
                    }
                    parserImpl.append("}\n");
                    
                    if(protocol.isDefined("serializer", "MSP")) {
                        parserImpl.append("/*PORT_NAME*/_parser(msgbuf, index, /*PORT_NAME*/_instance.listener_id);\n");
                        parserImpl.append(port + ".readBytes(&bytebuf, 1);\n");
                    } else {
                        parserImpl.append("externalMessageEnqueue(msgbuf, index, /*PORT_NAME*/_instance.listener_id);\n");
                    }

                    parserImpl.append("}\n");
                }
                // ------------------ End Parser ------------------ 


                ctemplate = ctemplate.replace("/*LISTENER_STATE*/", listenerState);
                ctemplate = ctemplate.replace("/*PARSER_IMPLEMENTATION*/", parserImpl);
                ctemplate = ctemplate.replace("/*READER_IMPLEMENTATION*/", readerImpl);
                ctemplate = ctemplate.replace("/*OTHER_CASES*/", tailHandling);
                ctemplate = ctemplate.replace("/*PORT_NAME*/", port);

                ctx.addToInitCode("\n" + port + "_instance.listener_id = add_instance(&" + port + "_instance);\n");
                ctx.addToInitCode(port + "_setup();\n");
                ctx.addToPollCode(port + "_read();\n");

                ctx.getBuilder(port + ".c").append(ctemplate);
            }
        }
    }
}
