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
package com.hangum.tadpole.rdb.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.hangum.tadpole.rdb.model.ERDInfo;
import com.hangum.tadpole.rdb.model.RdbPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>ERD Info</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link com.hangum.tadpole.rdb.model.impl.ERDInfoImpl#isAutoLayout <em>
 * Auto Layout</em>}</li>
 * <li>{@link com.hangum.tadpole.rdb.model.impl.ERDInfoImpl#getVersion <em>
 * Version</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ERDInfoImpl extends EObjectImpl implements ERDInfo {
	/**
	 * The default value of the '{@link #isAutoLayout() <em>Auto Layout</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isAutoLayout()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AUTO_LAYOUT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAutoLayout() <em>Auto Layout</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isAutoLayout()
	 * @generated
	 * @ordered
	 */
	protected boolean autoLayout = AUTO_LAYOUT_EDEFAULT;

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = "0.8.1(2012.04.13)";

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ERDInfoImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RdbPackage.Literals.ERD_INFO;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isAutoLayout() {
		return autoLayout;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAutoLayout(boolean newAutoLayout) {
		boolean oldAutoLayout = autoLayout;
		autoLayout = newAutoLayout;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.ERD_INFO__AUTO_LAYOUT, oldAutoLayout, autoLayout));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.ERD_INFO__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case RdbPackage.ERD_INFO__AUTO_LAYOUT:
			return isAutoLayout();
		case RdbPackage.ERD_INFO__VERSION:
			return getVersion();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case RdbPackage.ERD_INFO__AUTO_LAYOUT:
			setAutoLayout((Boolean) newValue);
			return;
		case RdbPackage.ERD_INFO__VERSION:
			setVersion((String) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case RdbPackage.ERD_INFO__AUTO_LAYOUT:
			setAutoLayout(AUTO_LAYOUT_EDEFAULT);
			return;
		case RdbPackage.ERD_INFO__VERSION:
			setVersion(VERSION_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case RdbPackage.ERD_INFO__AUTO_LAYOUT:
			return autoLayout != AUTO_LAYOUT_EDEFAULT;
		case RdbPackage.ERD_INFO__VERSION:
			return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (autoLayout: ");
		result.append(autoLayout);
		result.append(", version: ");
		result.append(version);
		result.append(')');
		return result.toString();
	}

} // ERDInfoImpl
