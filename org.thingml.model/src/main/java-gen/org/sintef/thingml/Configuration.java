/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.sintef.thingml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.sintef.thingml.Configuration#getInstances <em>Instances</em>}</li>
 *   <li>{@link org.sintef.thingml.Configuration#getConnectors <em>Connectors</em>}</li>
 *   <li>{@link org.sintef.thingml.Configuration#isFragment <em>Fragment</em>}</li>
 *   <li>{@link org.sintef.thingml.Configuration#getConfigs <em>Configs</em>}</li>
 *   <li>{@link org.sintef.thingml.Configuration#getPropassigns <em>Propassigns</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.sintef.thingml.ThingmlPackage#getConfiguration()
 * @model
 * @generated
 */
public interface Configuration extends AnnotatedElement {
	/**
	 * Returns the value of the '<em><b>Instances</b></em>' containment reference list.
	 * The list contents are of type {@link org.sintef.thingml.Instance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Instances</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Instances</em>' containment reference list.
	 * @see org.sintef.thingml.ThingmlPackage#getConfiguration_Instances()
	 * @model containment="true"
	 * @generated
	 */
	EList<Instance> getInstances();

	/**
	 * Returns the value of the '<em><b>Connectors</b></em>' containment reference list.
	 * The list contents are of type {@link org.sintef.thingml.Connector}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connectors</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connectors</em>' containment reference list.
	 * @see org.sintef.thingml.ThingmlPackage#getConfiguration_Connectors()
	 * @model containment="true"
	 * @generated
	 */
	EList<Connector> getConnectors();

	/**
	 * Returns the value of the '<em><b>Fragment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fragment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fragment</em>' attribute.
	 * @see #setFragment(boolean)
	 * @see org.sintef.thingml.ThingmlPackage#getConfiguration_Fragment()
	 * @model
	 * @generated
	 */
	boolean isFragment();

	/**
	 * Sets the value of the '{@link org.sintef.thingml.Configuration#isFragment <em>Fragment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fragment</em>' attribute.
	 * @see #isFragment()
	 * @generated
	 */
	void setFragment(boolean value);

	/**
	 * Returns the value of the '<em><b>Configs</b></em>' containment reference list.
	 * The list contents are of type {@link org.sintef.thingml.ConfigInclude}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Configs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Configs</em>' containment reference list.
	 * @see org.sintef.thingml.ThingmlPackage#getConfiguration_Configs()
	 * @model containment="true"
	 * @generated
	 */
	EList<ConfigInclude> getConfigs();

	/**
	 * Returns the value of the '<em><b>Propassigns</b></em>' containment reference list.
	 * The list contents are of type {@link org.sintef.thingml.ConfigPropertyAssign}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Propassigns</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Propassigns</em>' containment reference list.
	 * @see org.sintef.thingml.ThingmlPackage#getConfiguration_Propassigns()
	 * @model containment="true"
	 * @generated
	 */
	EList<ConfigPropertyAssign> getPropassigns();

} // Configuration
