/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
/**
 */
package com.hangum.tadpole.mongodb.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ERD Info</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.hangum.tadpole.mongodb.model.ERDInfo#isAutoLayout <em>Auto Layout</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.ERDInfo#getVersion <em>Version</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.ERDInfo#getAutoLayout_type <em>Auto Layout type</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getERDInfo()
 * @model
 * @generated
 */
public interface ERDInfo extends EObject {
	/**
	 * Returns the value of the '<em><b>Auto Layout</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Auto Layout</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Auto Layout</em>' attribute.
	 * @see #setAutoLayout(boolean)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getERDInfo_AutoLayout()
	 * @model
	 * @generated
	 */
	boolean isAutoLayout();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.ERDInfo#isAutoLayout <em>Auto Layout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Auto Layout</em>' attribute.
	 * @see #isAutoLayout()
	 * @generated
	 */
	void setAutoLayout(boolean value);

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * The default value is <code>"0.8.1(2012.04.13)"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(String)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getERDInfo_Version()
	 * @model default="0.8.1(2012.04.13)"
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.ERDInfo#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

	/**
	 * Returns the value of the '<em><b>Auto Layout type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Auto Layout type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Auto Layout type</em>' attribute.
	 * @see #setAutoLayout_type(String)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getERDInfo_AutoLayout_type()
	 * @model
	 * @generated
	 */
	String getAutoLayout_type();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.ERDInfo#getAutoLayout_type <em>Auto Layout type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Auto Layout type</em>' attribute.
	 * @see #getAutoLayout_type()
	 * @generated
	 */
	void setAutoLayout_type(String value);

} // ERDInfo
