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
package com.hangum.tadpole.mongodb.model.impl;

import com.hangum.tadpole.mongodb.model.ERDInfo;
import com.hangum.tadpole.mongodb.model.MongodbPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>ERD Info</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.hangum.tadpole.mongodb.model.impl.ERDInfoImpl#isAutoLayout <em>Auto Layout</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.impl.ERDInfoImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.impl.ERDInfoImpl#getAutoLayout_type <em>Auto Layout type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ERDInfoImpl extends EObjectImpl implements ERDInfo {
	/**
	 * The default value of the '{@link #isAutoLayout() <em>Auto Layout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAutoLayout()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AUTO_LAYOUT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAutoLayout() <em>Auto Layout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAutoLayout()
	 * @generated
	 * @ordered
	 */
	protected boolean autoLayout = AUTO_LAYOUT_EDEFAULT;

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = "0.8.1(2012.04.13)";

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getAutoLayout_type() <em>Auto Layout type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAutoLayout_type()
	 * @generated
	 * @ordered
	 */
	protected static final String AUTO_LAYOUT_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAutoLayout_type() <em>Auto Layout type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAutoLayout_type()
	 * @generated
	 * @ordered
	 */
	protected String autoLayout_type = AUTO_LAYOUT_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ERDInfoImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MongodbPackage.Literals.ERD_INFO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAutoLayout() {
		return autoLayout;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAutoLayout(boolean newAutoLayout) {
		boolean oldAutoLayout = autoLayout;
		autoLayout = newAutoLayout;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MongodbPackage.ERD_INFO__AUTO_LAYOUT, oldAutoLayout, autoLayout));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MongodbPackage.ERD_INFO__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAutoLayout_type() {
		return autoLayout_type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAutoLayout_type(String newAutoLayout_type) {
		String oldAutoLayout_type = autoLayout_type;
		autoLayout_type = newAutoLayout_type;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MongodbPackage.ERD_INFO__AUTO_LAYOUT_TYPE, oldAutoLayout_type, autoLayout_type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MongodbPackage.ERD_INFO__AUTO_LAYOUT:
				return isAutoLayout();
			case MongodbPackage.ERD_INFO__VERSION:
				return getVersion();
			case MongodbPackage.ERD_INFO__AUTO_LAYOUT_TYPE:
				return getAutoLayout_type();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MongodbPackage.ERD_INFO__AUTO_LAYOUT:
				setAutoLayout((Boolean)newValue);
				return;
			case MongodbPackage.ERD_INFO__VERSION:
				setVersion((String)newValue);
				return;
			case MongodbPackage.ERD_INFO__AUTO_LAYOUT_TYPE:
				setAutoLayout_type((String)newValue);
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
			case MongodbPackage.ERD_INFO__AUTO_LAYOUT:
				setAutoLayout(AUTO_LAYOUT_EDEFAULT);
				return;
			case MongodbPackage.ERD_INFO__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case MongodbPackage.ERD_INFO__AUTO_LAYOUT_TYPE:
				setAutoLayout_type(AUTO_LAYOUT_TYPE_EDEFAULT);
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
			case MongodbPackage.ERD_INFO__AUTO_LAYOUT:
				return autoLayout != AUTO_LAYOUT_EDEFAULT;
			case MongodbPackage.ERD_INFO__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
			case MongodbPackage.ERD_INFO__AUTO_LAYOUT_TYPE:
				return AUTO_LAYOUT_TYPE_EDEFAULT == null ? autoLayout_type != null : !AUTO_LAYOUT_TYPE_EDEFAULT.equals(autoLayout_type);
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
		result.append(" (autoLayout: ");
		result.append(autoLayout);
		result.append(", version: ");
		result.append(version);
		result.append(", autoLayout_type: ");
		result.append(autoLayout_type);
		result.append(')');
		return result.toString();
	}

} //ERDInfoImpl
