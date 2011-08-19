/**
 * <copyright>
 * </copyright>
 *
 * 
 */
package org.sintef.thingml.resource.thingml.grammar;

/**
 * The abstract super class for all elements of a grammar. This class provides
 * methods to traverse the grammar rules.
 */
public abstract class ThingmlSyntaxElement {
	
	private ThingmlSyntaxElement[] children;
	private ThingmlSyntaxElement parent;
	private org.sintef.thingml.resource.thingml.grammar.ThingmlCardinality cardinality;
	
	public ThingmlSyntaxElement(org.sintef.thingml.resource.thingml.grammar.ThingmlCardinality cardinality, ThingmlSyntaxElement[] children) {
		this.cardinality = cardinality;
		this.children = children;
		if (this.children != null) {
			for (ThingmlSyntaxElement child : this.children) {
				child.setParent(this);
			}
		}
	}
	
	public void setParent(ThingmlSyntaxElement parent) {
		assert this.parent == null;
		this.parent = parent;
	}
	
	public ThingmlSyntaxElement[] getChildren() {
		if (children == null) {
			return new ThingmlSyntaxElement[0];
		}
		return children;
	}
	
	public org.eclipse.emf.ecore.EClass getMetaclass() {
		return parent.getMetaclass();
	}
	
	public org.sintef.thingml.resource.thingml.grammar.ThingmlCardinality getCardinality() {
		return cardinality;
	}
	
}
