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
package org.thingml.compilers.c.arduino;

import org.sintef.thingml.*;
import org.thingml.compilers.Context;
import org.thingml.compilers.c.CCompilerContext;
import org.thingml.compilers.thing.ThingCepCompiler;
import org.thingml.compilers.thing.ThingCepSourceDeclaration;
import org.thingml.compilers.thing.ThingCepViewCompiler;

import java.util.ArrayList;
import java.util.List;

public class ArduinoThingCepCompiler extends ThingCepCompiler {
    public ArduinoThingCepCompiler(ThingCepViewCompiler cepViewCompiler, ThingCepSourceDeclaration sourceDeclaration) {
        super(cepViewCompiler, sourceDeclaration);
    }

    public static void generateSubscription(Stream stream, StringBuilder builder, Context context, String paramName, Message outPut) {
    }

    /**
     * We generate a buffer for Join and Merge operations or if the source has a
     * Length or Window specified
     *
     * @param thing
     * @return
     */
    public static List<Stream> getStreamWithBuffer(Thing thing) {
        List<Stream> ret = new ArrayList<>();
        for (Stream s : thing.getStreams()) {
            Source source = s.getInput();
            if (source instanceof SourceComposition) {
                ret.add(s);
            } else {
                for (ViewSource vs : source.getOperators()) {
                    if (vs instanceof LengthWindow) {
                        ret.add(s);
                    } else if (vs instanceof TimeWindow) {
                        ret.add(s);
                    }
                }
            }
        }
        return ret;
    }

    public static List<Message> getMessageFromStream(Stream stream) {
        List<Message> ret = new ArrayList<>();
        Source source = stream.getInput();

        if (source instanceof SimpleSource) {
            ret.add(((SimpleSource) source).getMessage().getMessage());
        } else if (source instanceof MergeSources) {
            for (Source s : ((MergeSources) source).getSources()) {
                if (s instanceof SimpleSource) {
                    ret.add(((SimpleSource) s).getMessage().getMessage());
                }
            }
        } else if (source instanceof JoinSources) {
            for (Source s : ((JoinSources) source).getSources()) {
                if (s instanceof SimpleSource) {
                    ret.add(((SimpleSource) s).getMessage().getMessage());
                }
            }

        }
        return ret;
    }

    public static void generateCEPLib(Thing thing, StringBuilder builder, CCompilerContext ctx) {
        for (Stream s : ArduinoThingCepCompiler.getStreamWithBuffer(thing)) {
            //debug, restrict to one stream
            if (s.getName().equals("filteredJoin1")) {
                String cepTemplate = ctx.getCEPLibTemplateClass();

                String constants = "";
                String methodsSignatures = "";
                String attributesSignatures = "";
                for (Message msg : ArduinoThingCepCompiler.getMessageFromStream(s)) {
                    System.out.println("Msg: " + msg.getName());
                    String constantTemplate = ctx.getCEPLibTemplateConstants();
                    constantTemplate = constantTemplate.replace("/*MESSAGE_NAME_UPPER*/", msg.getName().toUpperCase());
                    constants += constantTemplate;

                    String methodsTemplate = ctx.getCEPLibTemplateMethodsSignatures();
                    methodsTemplate = methodsTemplate.replace("/*MESSAGE_NAME*/", msg.getName().toUpperCase());
                    methodsSignatures += methodsTemplate;

                    String attributesTemplate = ctx.getCEPLibTemplateAttributesSignatures();
                    attributesTemplate = attributesTemplate.replace("/*MESSAGE_NAME*/", msg.getName());
                    attributesTemplate = attributesTemplate.replace("/*MESSAGE_NAME_UPPER*/", msg.getName().toUpperCase());
                    attributesSignatures += attributesTemplate;
                }
                cepTemplate = cepTemplate.replace("/*STREAM_NAME*/", s.getName());
                cepTemplate = cepTemplate.replace("/*METHOD_SIGNATURES*/", methodsSignatures);
                cepTemplate = cepTemplate.replace("/*ATTRIBUTES_SIGNATURES*/", attributesSignatures);
                cepTemplate = cepTemplate.replace("/*STREAM_CONSTANTS*/", constants);

                builder.append(cepTemplate);
            }
        }
    }

    @Override
    public void generateStream(Stream stream, StringBuilder builder, Context ctx) {
        sourceDeclaration.generate(stream, stream.getInput(), builder, ctx);
        if (stream.getInput() instanceof SimpleSource) {
            SimpleSource simpleSource = (SimpleSource) stream.getInput();
            String paramName = simpleSource.getMessage().getName();
            generateSubscription(stream, builder, ctx, paramName, simpleSource.getMessage().getMessage());
        } else if (stream.getInput() instanceof SourceComposition){
            Message outPut = ((SourceComposition)stream.getInput()).getResultMessage();
            generateSubscription(stream, builder, ctx, outPut.getName(), outPut);
        }
    }
}
