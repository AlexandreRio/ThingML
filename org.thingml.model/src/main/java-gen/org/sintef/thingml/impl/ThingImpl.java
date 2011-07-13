/**
 * Copyright (C) 2011 SINTEF <franck.fleurey@sintef.no>
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
/**
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
package org.sintef.thingml.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.sintef.thingml.Port;
import org.sintef.thingml.Property;
import org.sintef.thingml.PropertyAssign;
import org.sintef.thingml.StateMachine;
import org.sintef.thingml.Thing;
import org.sintef.thingml.ThingmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Thing</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.sintef.thingml.impl.ThingImpl#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.sintef.thingml.impl.ThingImpl#isFragment <em>Fragment</em>}</li>
 *   <li>{@link org.sintef.thingml.impl.ThingImpl#getPorts <em>Ports</em>}</li>
 *   <li>{@link org.sintef.thingml.impl.ThingImpl#getBehaviour <em>Behaviour</em>}</li>
 *   <li>{@link org.sintef.thingml.impl.ThingImpl#getIncludes <em>Includes</em>}</li>
 *   <li>{@link org.sintef.thingml.impl.ThingImpl#getAssign <em>Assign</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ThingImpl extends TypeImpl implements Thing {
	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EList<Property> properties;

	/**
	 * The default value of the '{@link #isFragment() <em>Fragment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFragment()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FRAGMENT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFragment() <em>Fragment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFragment()
	 * @generated
	 * @ordered
	 */
	protected boolean fragment = FRAGMENT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPorts() <em>Ports</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<Port> ports;

	/**
	 * The cached value of the '{@link #getBehaviour() <em>Behaviour</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBehaviour()
	 * @generated
	 * @ordered
	 */
	protected EList<StateMachine> behaviour;

	/**
	 * The cached value of the '{@link #getIncludes() <em>Includes</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIncludes()
	 * @generated
	 * @ordered
	 */
	protected EList<Thing> includes;

	/**
	 * The cached value of the '{@link #getAssign() <em>Assign</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssign()
	 * @generated
	 * @ordered
	 */
	protected EList<PropertyAssign> assign;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ThingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ThingmlPackage.Literals.THING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Property> getProperties() {
		if (properties == null) {
			properties = new EObjectContainmentEList<Property>(Property.class, this, ThingmlPackage.THING__PROPERTIES);
		}
		return properties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFragment() {
		return fragment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFragment(boolean newFragment) {
		boolean oldFragment = fragment;
		fragment = newFragment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ThingmlPackage.THING__FRAGMENT, oldFragment, fragment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Port> getPorts() {
		if (ports == null) {
			ports = new EObjectContainmentWithInverseEList<Port>(Port.class, this, ThingmlPackage.THING__PORTS, ThingmlPackage.PORT__OWNER);
		}
		return ports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StateMachine> getBehaviour() {
		if (behaviour == null) {
			behaviour = new EObjectContainmentEList<StateMachine>(StateMachine.class, this, ThingmlPackage.THING__BEHAVIOUR);
		}
		return behaviour;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Thing> getIncludes() {
		if (includes == null) {
			includes = new EObjectResolvingEList<Thing>(Thing.class, this, ThingmlPackage.THING__INCLUDES);
		}
		return includes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PropertyAssign> getAssign() {
		if (assign == null) {
			assign = new EObjectContainmentEList<PropertyAssign>(PropertyAssign.class, this, ThingmlPackage.THING__ASSIGN);
		}
		return assign;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ThingmlPackage.THING__PORTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getPorts()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ThingmlPackage.THING__PROPERTIES:
				return ((InternalEList<?>)getProperties()).basicRemove(otherEnd, msgs);
			case ThingmlPackage.THING__PORTS:
				return ((InternalEList<?>)getPorts()).basicRemove(otherEnd, msgs);
			case ThingmlPackage.THING__BEHAVIOUR:
				return ((InternalEList<?>)getBehaviour()).basicRemove(otherEnd, msgs);
			case ThingmlPackage.THING__ASSIGN:
				return ((InternalEList<?>)getAssign()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ThingmlPackage.THING__PROPERTIES:
				return getProperties();
			case ThingmlPackage.THING__FRAGMENT:
				return isFragment();
			case ThingmlPackage.THING__PORTS:
				return getPorts();
			case ThingmlPackage.THING__BEHAVIOUR:
				return getBehaviour();
			case ThingmlPackage.THING__INCLUDES:
				return getIncludes();
			case ThingmlPackage.THING__ASSIGN:
				return getAssign();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ThingmlPackage.THING__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection<? extends Property>)newValue);
				return;
			case ThingmlPackage.THING__FRAGMENT:
				setFragment((Boolean)newValue);
				return;
			case ThingmlPackage.THING__PORTS:
				getPorts().clear();
				getPorts().addAll((Collection<? extends Port>)newValue);
				return;
			case ThingmlPackage.THING__BEHAVIOUR:
				getBehaviour().clear();
				getBehaviour().addAll((Collection<? extends StateMachine>)newValue);
				return;
			case ThingmlPackage.THING__INCLUDES:
				getIncludes().clear();
				getIncludes().addAll((Collection<? extends Thing>)newValue);
				return;
			case ThingmlPackage.THING__ASSIGN:
				getAssign().clear();
				getAssign().addAll((Collection<? extends PropertyAssign>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ThingmlPackage.THING__PROPERTIES:
				getProperties().clear();
				return;
			case ThingmlPackage.THING__FRAGMENT:
				setFragment(FRAGMENT_EDEFAULT);
				return;
			case ThingmlPackage.THING__PORTS:
				getPorts().clear();
				return;
			case ThingmlPackage.THING__BEHAVIOUR:
				getBehaviour().clear();
				return;
			case ThingmlPackage.THING__INCLUDES:
				getIncludes().clear();
				return;
			case ThingmlPackage.THING__ASSIGN:
				getAssign().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ThingmlPackage.THING__PROPERTIES:
				return properties != null && !properties.isEmpty();
			case ThingmlPackage.THING__FRAGMENT:
				return fragment != FRAGMENT_EDEFAULT;
			case ThingmlPackage.THING__PORTS:
				return ports != null && !ports.isEmpty();
			case ThingmlPackage.THING__BEHAVIOUR:
				return behaviour != null && !behaviour.isEmpty();
			case ThingmlPackage.THING__INCLUDES:
				return includes != null && !includes.isEmpty();
			case ThingmlPackage.THING__ASSIGN:
				return assign != null && !assign.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (fragment: ");
		result.append(fragment);
		result.append(')');
		return result.toString();
	}

} //ThingImpl
