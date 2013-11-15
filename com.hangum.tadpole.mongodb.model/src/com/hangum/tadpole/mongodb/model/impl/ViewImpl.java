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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.hangum.tadpole.mongodb.model.MongodbPackage;
import com.hangum.tadpole.mongodb.model.Table;
import com.hangum.tadpole.mongodb.model.View;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>View</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link com.hangum.tadpole.mongodb.model.impl.ViewImpl#getTableName <em>
 * Table Name</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ViewImpl extends TableImpl implements View {
	/**
	 * The cached value of the '{@link #getTableName() <em>Table Name</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTableName()
	 * @generated
	 * @ordered
	 */
	protected Table tableName;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ViewImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MongodbPackage.Literals.VIEW;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Table getTableName() {
		if (tableName != null && tableName.eIsProxy()) {
			InternalEObject oldTableName = (InternalEObject) tableName;
			tableName = (Table) eResolveProxy(oldTableName);
			if (tableName != oldTableName) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MongodbPackage.VIEW__TABLE_NAME, oldTableName, tableName));
			}
		}
		return tableName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Table basicGetTableName() {
		return tableName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTableName(Table newTableName) {
		Table oldTableName = tableName;
		tableName = newTableName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MongodbPackage.VIEW__TABLE_NAME, oldTableName, tableName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case MongodbPackage.VIEW__TABLE_NAME:
			if (resolve)
				return getTableName();
			return basicGetTableName();
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
		case MongodbPackage.VIEW__TABLE_NAME:
			setTableName((Table) newValue);
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
		case MongodbPackage.VIEW__TABLE_NAME:
			setTableName((Table) null);
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
		case MongodbPackage.VIEW__TABLE_NAME:
			return tableName != null;
		}
		return super.eIsSet(featureID);
	}

} // ViewImpl
