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
package org.thingml.xtext.thingML.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.thingml.xtext.thingML.Expression;
import org.thingml.xtext.thingML.JoinSources;
import org.thingml.xtext.thingML.Message;
import org.thingml.xtext.thingML.Source;
import org.thingml.xtext.thingML.ThingMLPackage;
import org.thingml.xtext.thingML.ViewSource;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Join Sources</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.impl.JoinSourcesImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.JoinSourcesImpl#getSources <em>Sources</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.JoinSourcesImpl#getResultMessage <em>Result Message</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.JoinSourcesImpl#getRules <em>Rules</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.JoinSourcesImpl#getOperators <em>Operators</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class JoinSourcesImpl extends SourceImpl implements JoinSources
{
  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getSources() <em>Sources</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSources()
   * @generated
   * @ordered
   */
  protected EList<Source> sources;

  /**
   * The cached value of the '{@link #getResultMessage() <em>Result Message</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResultMessage()
   * @generated
   * @ordered
   */
  protected Message resultMessage;

  /**
   * The cached value of the '{@link #getRules() <em>Rules</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRules()
   * @generated
   * @ordered
   */
  protected EList<Expression> rules;

  /**
   * The cached value of the '{@link #getOperators() <em>Operators</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOperators()
   * @generated
   * @ordered
   */
  protected EList<ViewSource> operators;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected JoinSourcesImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return ThingMLPackage.Literals.JOIN_SOURCES;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.JOIN_SOURCES__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Source> getSources()
  {
    if (sources == null)
    {
      sources = new EObjectContainmentEList<Source>(Source.class, this, ThingMLPackage.JOIN_SOURCES__SOURCES);
    }
    return sources;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Message getResultMessage()
  {
    if (resultMessage != null && resultMessage.eIsProxy())
    {
      InternalEObject oldResultMessage = (InternalEObject)resultMessage;
      resultMessage = (Message)eResolveProxy(oldResultMessage);
      if (resultMessage != oldResultMessage)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, ThingMLPackage.JOIN_SOURCES__RESULT_MESSAGE, oldResultMessage, resultMessage));
      }
    }
    return resultMessage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Message basicGetResultMessage()
  {
    return resultMessage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setResultMessage(Message newResultMessage)
  {
    Message oldResultMessage = resultMessage;
    resultMessage = newResultMessage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.JOIN_SOURCES__RESULT_MESSAGE, oldResultMessage, resultMessage));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Expression> getRules()
  {
    if (rules == null)
    {
      rules = new EObjectContainmentEList<Expression>(Expression.class, this, ThingMLPackage.JOIN_SOURCES__RULES);
    }
    return rules;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<ViewSource> getOperators()
  {
    if (operators == null)
    {
      operators = new EObjectContainmentEList<ViewSource>(ViewSource.class, this, ThingMLPackage.JOIN_SOURCES__OPERATORS);
    }
    return operators;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ThingMLPackage.JOIN_SOURCES__SOURCES:
        return ((InternalEList<?>)getSources()).basicRemove(otherEnd, msgs);
      case ThingMLPackage.JOIN_SOURCES__RULES:
        return ((InternalEList<?>)getRules()).basicRemove(otherEnd, msgs);
      case ThingMLPackage.JOIN_SOURCES__OPERATORS:
        return ((InternalEList<?>)getOperators()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case ThingMLPackage.JOIN_SOURCES__NAME:
        return getName();
      case ThingMLPackage.JOIN_SOURCES__SOURCES:
        return getSources();
      case ThingMLPackage.JOIN_SOURCES__RESULT_MESSAGE:
        if (resolve) return getResultMessage();
        return basicGetResultMessage();
      case ThingMLPackage.JOIN_SOURCES__RULES:
        return getRules();
      case ThingMLPackage.JOIN_SOURCES__OPERATORS:
        return getOperators();
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
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ThingMLPackage.JOIN_SOURCES__NAME:
        setName((String)newValue);
        return;
      case ThingMLPackage.JOIN_SOURCES__SOURCES:
        getSources().clear();
        getSources().addAll((Collection<? extends Source>)newValue);
        return;
      case ThingMLPackage.JOIN_SOURCES__RESULT_MESSAGE:
        setResultMessage((Message)newValue);
        return;
      case ThingMLPackage.JOIN_SOURCES__RULES:
        getRules().clear();
        getRules().addAll((Collection<? extends Expression>)newValue);
        return;
      case ThingMLPackage.JOIN_SOURCES__OPERATORS:
        getOperators().clear();
        getOperators().addAll((Collection<? extends ViewSource>)newValue);
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
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case ThingMLPackage.JOIN_SOURCES__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ThingMLPackage.JOIN_SOURCES__SOURCES:
        getSources().clear();
        return;
      case ThingMLPackage.JOIN_SOURCES__RESULT_MESSAGE:
        setResultMessage((Message)null);
        return;
      case ThingMLPackage.JOIN_SOURCES__RULES:
        getRules().clear();
        return;
      case ThingMLPackage.JOIN_SOURCES__OPERATORS:
        getOperators().clear();
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
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case ThingMLPackage.JOIN_SOURCES__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ThingMLPackage.JOIN_SOURCES__SOURCES:
        return sources != null && !sources.isEmpty();
      case ThingMLPackage.JOIN_SOURCES__RESULT_MESSAGE:
        return resultMessage != null;
      case ThingMLPackage.JOIN_SOURCES__RULES:
        return rules != null && !rules.isEmpty();
      case ThingMLPackage.JOIN_SOURCES__OPERATORS:
        return operators != null && !operators.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //JoinSourcesImpl
