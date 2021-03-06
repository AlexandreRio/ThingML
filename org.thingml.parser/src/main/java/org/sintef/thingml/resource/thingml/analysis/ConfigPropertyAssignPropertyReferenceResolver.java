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
package org.sintef.thingml.resource.thingml.analysis;

import java.util.ArrayList;

import org.sintef.thingml.Property;
import org.sintef.thingml.Thing;
import org.sintef.thingml.constraints.ThingMLHelpers;

public class ConfigPropertyAssignPropertyReferenceResolver implements org.sintef.thingml.resource.thingml.IThingmlReferenceResolver<org.sintef.thingml.ConfigPropertyAssign, org.sintef.thingml.Property> {
	
	private org.sintef.thingml.resource.thingml.analysis.ThingmlDefaultResolverDelegate<org.sintef.thingml.ConfigPropertyAssign, org.sintef.thingml.Property> delegate = new org.sintef.thingml.resource.thingml.analysis.ThingmlDefaultResolverDelegate<org.sintef.thingml.ConfigPropertyAssign, org.sintef.thingml.Property>();
	
	public void resolve(String identifier, org.sintef.thingml.ConfigPropertyAssign container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final org.sintef.thingml.resource.thingml.IThingmlReferenceResolveResult<org.sintef.thingml.Property> result) {
		Thing s = container.getInstance().getInstance().getType();
		/*
		if (s == null) {
			s = ThingMLHelpers.findContainingInstance(container).getType();
		}
		*/
		if (s != null) {
			ArrayList<Property> ps = ThingMLHelpers.findProperty(s, identifier, resolveFuzzy);
			for(Property p : ps) {
				result.addMapping(p.getName(), p);
			}
		}
		if (!result.wasResolved())
			result.setErrorMessage("Cannot resolve property " + identifier);
	}
	
	public String deResolve(org.sintef.thingml.Property element, org.sintef.thingml.ConfigPropertyAssign container, org.eclipse.emf.ecore.EReference reference) {
		return delegate.deResolve(element, container, reference);
	}
	
	public void setOptions(java.util.Map<?,?> options) {
		// save options in a field or leave method empty if this resolver does not depend
		// on any option
	}
	
}
