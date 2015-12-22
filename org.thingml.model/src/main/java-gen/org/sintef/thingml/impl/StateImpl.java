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
package org.sintef.thingml.impl;

import java.util.Collection;
import java.util.*;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.sintef.thingml.Action;
import org.sintef.thingml.Fork;
import org.sintef.thingml.InternalTransition;
import org.sintef.thingml.Property;
import org.sintef.thingml.State;
import org.sintef.thingml.ThingmlPackage;
import org.sintef.thingml.Transition;
import org.sintef.thingml.*;
import org.sintef.thingml.constraints.ThingMLHelpers;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>State</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.sintef.thingml.impl.StateImpl#getOutgoing <em>Outgoing</em>}</li>
 *   <li>{@link org.sintef.thingml.impl.StateImpl#getFork <em>Fork</em>}</li>
 *   <li>{@link org.sintef.thingml.impl.StateImpl#getExit <em>Exit</em>}</li>
 *   <li>{@link org.sintef.thingml.impl.StateImpl#getInternal <em>Internal</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StateImpl extends AbstractStateImpl implements State {
	/**
	 * The cached value of the '{@link #getOutgoing() <em>Outgoing</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutgoing()
	 * @generated
	 * @ordered
	 */
	protected EList<Transition> outgoing;

	/**
	 * The cached value of the '{@link #getFork() <em>Fork</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFork()
	 * @generated
	 * @ordered
	 */
	protected EList<Fork> fork;

	/**
	 * The cached value of the '{@link #getExit() <em>Exit</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExit()
	 * @generated
	 * @ordered
	 */
	protected Action exit;

	/**
	 * The cached value of the '{@link #getInternal() <em>Internal</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInternal()
	 * @generated
	 * @ordered
	 */
	protected EList<InternalTransition> internal;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StateImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ThingmlPackage.Literals.STATE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Transition> getOutgoing() {
		if (outgoing == null) {
			outgoing = new EObjectContainmentWithInverseEList<Transition>(Transition.class, this, ThingmlPackage.STATE__OUTGOING, ThingmlPackage.TRANSITION__SOURCE);
		}
		return outgoing;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Fork> getFork() {
		if (fork == null) {
			fork = new EObjectContainmentEList<Fork>(Fork.class, this, ThingmlPackage.STATE__FORK);
		}
		return fork;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Action getExit() {
		return exit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetExit(Action newExit, NotificationChain msgs) {
		Action oldExit = exit;
		exit = newExit;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingmlPackage.STATE__EXIT, oldExit, newExit);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExit(Action newExit) {
		if (newExit != exit) {
			NotificationChain msgs = null;
			if (exit != null)
				msgs = ((InternalEObject)exit).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingmlPackage.STATE__EXIT, null, msgs);
			if (newExit != null)
				msgs = ((InternalEObject)newExit).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingmlPackage.STATE__EXIT, null, msgs);
			msgs = basicSetExit(newExit, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ThingmlPackage.STATE__EXIT, newExit, newExit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InternalTransition> getInternal() {
		if (internal == null) {
			internal = new EObjectContainmentEList<InternalTransition>(InternalTransition.class, this, ThingmlPackage.STATE__INTERNAL);
		}
		return internal;
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
			case ThingmlPackage.STATE__OUTGOING:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getOutgoing()).basicAdd(otherEnd, msgs);
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
			case ThingmlPackage.STATE__OUTGOING:
				return ((InternalEList<?>)getOutgoing()).basicRemove(otherEnd, msgs);
			case ThingmlPackage.STATE__FORK:
				return ((InternalEList<?>)getFork()).basicRemove(otherEnd, msgs);
			case ThingmlPackage.STATE__EXIT:
				return basicSetExit(null, msgs);
			case ThingmlPackage.STATE__INTERNAL:
				return ((InternalEList<?>)getInternal()).basicRemove(otherEnd, msgs);
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
			case ThingmlPackage.STATE__OUTGOING:
				return getOutgoing();
			case ThingmlPackage.STATE__FORK:
				return getFork();
			case ThingmlPackage.STATE__EXIT:
				return getExit();
			case ThingmlPackage.STATE__INTERNAL:
				return getInternal();
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
			case ThingmlPackage.STATE__OUTGOING:
				getOutgoing().clear();
				getOutgoing().addAll((Collection<? extends Transition>)newValue);
				return;
			case ThingmlPackage.STATE__FORK:
				getFork().clear();
				getFork().addAll((Collection<? extends Fork>)newValue);
				return;
			case ThingmlPackage.STATE__EXIT:
				setExit((Action)newValue);
				return;
			case ThingmlPackage.STATE__INTERNAL:
				getInternal().clear();
				getInternal().addAll((Collection<? extends InternalTransition>)newValue);
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
			case ThingmlPackage.STATE__OUTGOING:
				getOutgoing().clear();
				return;
			case ThingmlPackage.STATE__FORK:
				getFork().clear();
				return;
			case ThingmlPackage.STATE__EXIT:
				setExit((Action)null);
				return;
			case ThingmlPackage.STATE__INTERNAL:
				getInternal().clear();
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
			case ThingmlPackage.STATE__OUTGOING:
				return outgoing != null && !outgoing.isEmpty();
			case ThingmlPackage.STATE__FORK:
				return fork != null && !fork.isEmpty();
			case ThingmlPackage.STATE__EXIT:
				return exit != null;
			case ThingmlPackage.STATE__INTERNAL:
				return internal != null && !internal.isEmpty();
		}
		return super.eIsSet(featureID);
	}


    /**
     *
     * @return
     * @generated NOT
     */
    public List<AbstractState> allStates() {
        if (this instanceof CompositeState) {
            return ((CompositeState)this).allContainedStates();
        } else {
            return Collections.singletonList((AbstractState)this);
        }
    }

    /**
     *
     * @return
     * @generated NOT
     */
    public List<AbstractState> allStatesWithEntry() {
        final List<AbstractState> result = new ArrayList<AbstractState>();
        for(AbstractState s : allStates()) {
            if (s.getEntry() != null)
                result.add(s);
        }
        return result;
    }

    /**
     *
     * @return
     * @generated NOT
     */
    public List<State> allStatesWithExit() {
        final List<State> result = new ArrayList<State>();
        for(AbstractState s : allStates()) {
            if (s instanceof State && ((State)s).getExit() != null)
                result.add((State)s);
        }
        return result;
    }

    /**
     *
     * @return
     * @generated NOT
     */
    public List<AbstractState> allContainingStates() {
        return ThingMLHelpers.allContainingStates(this);
    }

    /**
     *
     * @return
     * @generated NOT
     */
    public List<Property> allProperties() {
        return ThingMLHelpers.allProperties(this);
    }

    /**
     *
     * @return
     * @generated NOT
     */
    public List<AbstractState> allValidTargetStates() {
        return ThingMLHelpers.allValidTargetStates(this);
    }

    /**
     *
     * @param p
     * @param m
     * @return
     * @generated NOT
     */
    public List<Handler> allHandlers(Port p, Message m) {
        Map<Port, Map<Message, List<Handler>>> handlers = allMessageHandlers();
        if (!handlers.containsKey(p) || !handlers.get(p).containsKey(m))
            return new ArrayList<Handler>();
        else
            return handlers.get(p).get(m);
    }


    /**
     *
     * @return
     * @generated NOT
     */
    public Map<Port, Map<Message, List<Handler>>> allMessageHandlers() {
        Map<Port, Map<Message, List<Handler>>> result = new HashMap<Port, Map<Message, List<Handler>>>();
        for(AbstractState s : allStates()) {
        	if (s instanceof State) {
            //println("Processisng state " + s.getName)
            List<Handler> handlers = new ArrayList<Handler>();
            for(Transition t : ((State)s).getOutgoing()){
                handlers.add(t);
            }
            for(InternalTransition i : ((State)s).getInternal()) {
                handlers.add(i);
            }
            for(Handler t : handlers){
                //println("  Processisng handler " + t + " Event = " + t.getEvent)
                for(Event e : t.getEvent()){
                    if (e instanceof ReceiveMessage) {
                        ReceiveMessage rm = (ReceiveMessage)e;
                        Map<Message, List<Handler>> phdlrs = result.get(rm.getPort());
                        if (phdlrs == null) {
                            phdlrs = new HashMap<Message, List<Handler>>();
                            result.put(rm.getPort(), phdlrs);
                        }
                        List<Handler> hdlrs = phdlrs.get(rm.getMessage());
                        if (hdlrs == null) {
                            hdlrs = new ArrayList<Handler>();
                            phdlrs.put(rm.getMessage(), hdlrs);
                        }
                        hdlrs.add(t);
                    }
                }
            }
        }
        }
        return result;
    }

    /**
     *
     * @param p
     * @param m
     * @return
     * @generated NOT
     */
    public boolean canHandle(Port p, Message m) {
        Map<Port, Map<Message, List<Handler>>> handlers = allMessageHandlers();
        if (!handlers.containsKey(p))
            return false;
        else
            return handlers.get(p).containsKey(m);
    }

    /**
     *
     * @return
     * @generated NOT
     */
    public boolean hasEmptyHandlers() {
        return !allEmptyHandlers().isEmpty();
    }

    /**
     *
     * @return
     * @generated NOT
     */
    public List<Handler> allEmptyHandlers() {
        final List<Handler> result = new ArrayList<Handler>();
        for(AbstractState s : allStates()){
        	if (s instanceof State) {
            List<Handler> handlers = new ArrayList<Handler>();
            for(Transition t : ((State)s).getOutgoing()){
                handlers.add(t);
            }
            for(InternalTransition i : ((State)s).getInternal()) {
                handlers.add(i);
            }
            for(Handler t : handlers) {
                if (t.getEvent().isEmpty()) {
                    result.add(t);
                }
            }
        }
        }
        return result;
    }

    /**
     *
     * @param separator
     * @return
     * @generated NOT
     */
    public String qualifiedName(String separator) {
        if (eContainer() instanceof State) {
            return ((State)eContainer()).qualifiedName(separator) + separator + getName();
        } else if (eContainer() instanceof Region) {
            return ((Region)eContainer()).qualifiedName(separator) + separator + getName();
        } else {
            return getName();
        }
    }

} //StateImpl
