/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
/**
 */
package com.hangum.tadpole.rdb.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ERD Info</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.hangum.tadpole.rdb.model.ERDInfo#isAutoLayout <em>Auto Layout</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.ERDInfo#getVersion <em>Version</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.ERDInfo#getStyle <em>Style</em>}</li>
 * </ul>
 *
 * @see com.hangum.tadpole.rdb.model.RdbPackage#getERDInfo()
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
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getERDInfo_AutoLayout()
	 * @model
	 * @generated
	 */
	boolean isAutoLayout();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.ERDInfo#isAutoLayout <em>Auto Layout</em>}' attribute.
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
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getERDInfo_Version()
	 * @model default="0.8.1(2012.04.13)"
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.ERDInfo#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

	/**
	 * Returns the value of the '<em><b>Style</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Style</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Style</em>' reference.
	 * @see #setStyle(Style)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getERDInfo_Style()
	 * @model required="true"
	 * @generated
	 */
	Style getStyle();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.ERDInfo#getStyle <em>Style</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Style</em>' reference.
	 * @see #getStyle()
	 * @generated
	 */
	void setStyle(Style value);

} // ERDInfo
