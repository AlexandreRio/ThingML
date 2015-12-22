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
package org.sintef.thingml;

import org.eclipse.emf.common.util.EList;

import java.util.List;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>State</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.sintef.thingml.State#getOutgoing <em>Outgoing</em>}</li>
 *   <li>{@link org.sintef.thingml.State#getFork <em>Fork</em>}</li>
 *   <li>{@link org.sintef.thingml.State#getExit <em>Exit</em>}</li>
 *   <li>{@link org.sintef.thingml.State#getInternal <em>Internal</em>}</li>
 * </ul>
 *
 * @see org.sintef.thingml.ThingmlPackage#getState()
 * @model
 * @generated
 */
public interface State extends AbstractState {
	/**
	 * Returns the value of the '<em><b>Outgoing</b></em>' containment reference list.
	 * The list contents are of type {@link org.sintef.thingml.Transition}.
	 * It is bidirectional and its opposite is '{@link org.sintef.thingml.Transition#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Outgoing</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Outgoing</em>' containment reference list.
	 * @see org.sintef.thingml.ThingmlPackage#getState_Outgoing()
	 * @see org.sintef.thingml.Transition#getSource
	 * @model opposite="source" containment="true"
	 * @generated
	 */
	EList<Transition> getOutgoing();

	/**
	 * Returns the value of the '<em><b>Fork</b></em>' containment reference list.
	 * The list contents are of type {@link org.sintef.thingml.Fork}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fork</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fork</em>' containment reference list.
	 * @see org.sintef.thingml.ThingmlPackage#getState_Fork()
	 * @model containment="true"
	 * @generated
	 */
	EList<Fork> getFork();

	/**
	 * Returns the value of the '<em><b>Exit</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exit</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exit</em>' containment reference.
	 * @see #setExit(Action)
	 * @see org.sintef.thingml.ThingmlPackage#getState_Exit()
	 * @model containment="true"
	 * @generated
	 */
	Action getExit();

	/**
	 * Sets the value of the '{@link org.sintef.thingml.State#getExit <em>Exit</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exit</em>' containment reference.
	 * @see #getExit()
	 * @generated
	 */
	void setExit(Action value);

	/**
	 * Returns the value of the '<em><b>Internal</b></em>' containment reference list.
	 * The list contents are of type {@link org.sintef.thingml.InternalTransition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Internal</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Internal</em>' containment reference list.
	 * @see org.sintef.thingml.ThingmlPackage#getState_Internal()
	 * @model containment="true"
	 * @generated
	 */
	EList<InternalTransition> getInternal();

    //Derived properties

    /**
     *
     * @return
     * @generated NOT
     */
    List<AbstractState> allStates();

    /**
     *
     * @return
     * @generated NOT
     */
    List<AbstractState> allStatesWithEntry();

    /**
     *
     * @return
     * @generated NOT
     */
    List<State> allStatesWithExit();

    /**
     *
     * @return
     * @generated NOT
     */
    List<AbstractState> allContainingStates();

    /**
     *
     * @return
     * @generated NOT
     */
    List<Property> allProperties();

    /**
     *
     * @return
     * @generated NOT
     */
    List<AbstractState> allValidTargetStates();

    /**
     *
     * @return
     * @generated NOT
     */
    Map<Port, Map<Message, List<Handler>>> allMessageHandlers();

    /**
     *
     * @param p
     * @param m
     * @return
     * @generated NOT
     */
    List<Handler> allHandlers(Port p, Message m);

    /**
     *
     * @param p
     * @param m
     * @return
     * @generated NOT
     */
    boolean canHandle(Port p, Message m);

    /**
     *
     * @return
     * @generated NOT
     */
    boolean hasEmptyHandlers();

    /**
     *
     * @return
     * @generated NOT
     */
    List<Handler> allEmptyHandlers();

    /**
     *
     * @param separator
     * @return
     * @generated NOT
     */
    String qualifiedName(String separator);

} // State
