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
/**
 * generated by Xtext 2.9.1
 */
package org.thingml.xtext.thingML;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Merge Sources</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.MergeSources#getName <em>Name</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.MergeSources#getSources <em>Sources</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.MergeSources#getResultMessage <em>Result Message</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.MergeSources#getOperators <em>Operators</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getMergeSources()
 * @model
 * @generated
 */
public interface MergeSources extends Source, ReferencedElmt
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getMergeSources_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.MergeSources#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Sources</b></em>' containment reference list.
   * The list contents are of type {@link org.thingml.xtext.thingML.Source}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sources</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sources</em>' containment reference list.
   * @see org.thingml.xtext.thingML.ThingMLPackage#getMergeSources_Sources()
   * @model containment="true"
   * @generated
   */
  EList<Source> getSources();

  /**
   * Returns the value of the '<em><b>Result Message</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Result Message</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Result Message</em>' reference.
   * @see #setResultMessage(Message)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getMergeSources_ResultMessage()
   * @model
   * @generated
   */
  Message getResultMessage();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.MergeSources#getResultMessage <em>Result Message</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Result Message</em>' reference.
   * @see #getResultMessage()
   * @generated
   */
  void setResultMessage(Message value);

  /**
   * Returns the value of the '<em><b>Operators</b></em>' containment reference list.
   * The list contents are of type {@link org.thingml.xtext.thingML.ViewSource}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Operators</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Operators</em>' containment reference list.
   * @see org.thingml.xtext.thingML.ThingMLPackage#getMergeSources_Operators()
   * @model containment="true"
   * @generated
   */
  EList<ViewSource> getOperators();

} // MergeSources
