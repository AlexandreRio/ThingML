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
package org.thingml.compilers.checker.genericRules;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.sintef.thingml.*;
import org.thingml.compilers.checker.Checker;
import org.thingml.compilers.checker.Rule;

/**
 *
 * @author sintef
 */
public class VariableUsage extends Rule {

    public VariableUsage() {
        super();
    }

    @Override
    public Checker.InfoType getHighestLevel() {
        return Checker.InfoType.NOTICE;
    }

    @Override
    public String getName() {
        return "Messages Usage";
    }

    @Override
    public String getDescription() {
        return "Check variables and properties.";
    }

    private void check(Variable va, Expression e, Thing t, Checker checker, EObject o) {
        if (va instanceof Property) {
            Property p = (Property) va;
            if (!p.isChangeable()) {
                checker.addGenericError("Property " + va.getName() + " of Thing " + t.getName() + " is read-only and cannot be re-assigned.", o);
            }
        }
        if (va.getType() == null)//parsing probably still ongoing...
            return;
        Type expected = va.getType().getBroadType();
        Type actual = checker.typeChecker.computeTypeOf(e);
        if (actual != null) { //FIXME: improve type checker so that it does not return null (some actions are not yet implemented in the type checker)
            if (actual.getName().equals("ERROR_TYPE")) {
                checker.addGenericError("Property " + va.getName() + " of Thing " + t.getName() + " is assigned with an erroneous value/expression. Expected " + expected.getBroadType().getName() + ", assigned with " + actual.getBroadType().getName(), o);
            } else if (actual.getName().equals("ANY_TYPE")) {
                checker.addGenericWarning("Property " + va.getName() + " of Thing " + t.getName() + " is assigned with a value/expression which cannot be typed. Expected " + expected.getBroadType().getName() + ", assigned with " + actual.getBroadType().getName(), o);
            } else if (!actual.isA(expected)) {
                checker.addGenericWarning("Property " + va.getName() + " of Thing " + t.getName() + " is assigned with an erroneous value/expression. Expected " + expected.getBroadType().getName() + ", assigned with " + actual.getBroadType().getName(), o);
            }
        }
    }

    @Override
    public void check(Configuration cfg, Checker checker) {
        for(Thing t : cfg.allThings()) {
            for(Action a : t.allAction(VariableAssignment.class)) {
                //FIXME @Brice see testIfElse
                if(a instanceof VariableAssignment) {
                    VariableAssignment va = (VariableAssignment)a;
                    check(va.getProperty(), va.getExpression(), t, checker, va);
                }
            }
            for(Action a : t.allAction(LocalVariable.class)) {
                //FIXME @Brice see testIfElse
                if(a instanceof LocalVariable) {
                    LocalVariable lv = (LocalVariable) a;
                    check(lv, lv.getInit(), t, checker, lv);
                }
            }
        }
    }
    
}
