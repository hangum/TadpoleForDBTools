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

import com.hangum.tadpole.rdb.model.DB;
import com.hangum.tadpole.rdb.model.RdbPackage;
import com.hangum.tadpole.rdb.model.Relation;
import com.hangum.tadpole.rdb.model.RelationKind;
import com.hangum.tadpole.rdb.model.Table;

import java.util.Collection;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Relation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.RelationImpl#getType <em>Type</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.RelationImpl#getSource <em>Source</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.RelationImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.RelationImpl#getSource_kind <em>Source kind</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.RelationImpl#getTarget_kind <em>Target kind</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.RelationImpl#getColumn_name <em>Column name</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.RelationImpl#getReferenced_column_name <em>Referenced column name</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.RelationImpl#getBendpoint <em>Bendpoint</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.RelationImpl#getDb <em>Db</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.RelationImpl#getComment <em>Comment</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.RelationImpl#getConstraint_name <em>Constraint name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RelationImpl extends EObjectImpl implements Relation {
	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_EDEFAULT = "RELATION";

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected String type = TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSource() <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSource()
	 * @generated
	 * @ordered
	 */
	protected Table source;

	/**
	 * The cached value of the '{@link #getTarget() <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
	protected Table target;

	/**
	 * The default value of the '{@link #getSource_kind() <em>Source kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSource_kind()
	 * @generated
	 * @ordered
	 */
	protected static final RelationKind SOURCE_KIND_EDEFAULT = RelationKind.ONLY_ONE;

	/**
	 * The cached value of the '{@link #getSource_kind() <em>Source kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSource_kind()
	 * @generated
	 * @ordered
	 */
	protected RelationKind source_kind = SOURCE_KIND_EDEFAULT;

	/**
	 * The default value of the '{@link #getTarget_kind() <em>Target kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTarget_kind()
	 * @generated
	 * @ordered
	 */
	protected static final RelationKind TARGET_KIND_EDEFAULT = RelationKind.ONLY_ONE;

	/**
	 * The cached value of the '{@link #getTarget_kind() <em>Target kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTarget_kind()
	 * @generated
	 * @ordered
	 */
	protected RelationKind target_kind = TARGET_KIND_EDEFAULT;

	/**
	 * The default value of the '{@link #getColumn_name() <em>Column name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumn_name()
	 * @generated
	 * @ordered
	 */
	protected static final String COLUMN_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getColumn_name() <em>Column name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumn_name()
	 * @generated
	 * @ordered
	 */
	protected String column_name = COLUMN_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getReferenced_column_name() <em>Referenced column name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenced_column_name()
	 * @generated
	 * @ordered
	 */
	protected static final String REFERENCED_COLUMN_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getReferenced_column_name() <em>Referenced column name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenced_column_name()
	 * @generated
	 * @ordered
	 */
	protected String referenced_column_name = REFERENCED_COLUMN_NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getBendpoint() <em>Bendpoint</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBendpoint()
	 * @generated
	 * @ordered
	 */
	protected EList<Point> bendpoint;

	/**
	 * The default value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
	protected static final String COMMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
	protected String comment = COMMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getConstraint_name() <em>Constraint name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraint_name()
	 * @generated
	 * @ordered
	 */
	protected static final String CONSTRAINT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getConstraint_name() <em>Constraint name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraint_name()
	 * @generated
	 * @ordered
	 */
	protected String constraint_name = CONSTRAINT_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RelationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RdbPackage.Literals.RELATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(String newType) {
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.RELATION__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Table getSource() {
		if (source != null && source.eIsProxy()) {
			InternalEObject oldSource = (InternalEObject)source;
			source = (Table)eResolveProxy(oldSource);
			if (source != oldSource) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RdbPackage.RELATION__SOURCE, oldSource, source));
			}
		}
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Table basicGetSource() {
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSource(Table newSource, NotificationChain msgs) {
		Table oldSource = source;
		source = newSource;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RdbPackage.RELATION__SOURCE, oldSource, newSource);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSource(Table newSource) {
		if (newSource != source) {
			NotificationChain msgs = null;
			if (source != null)
				msgs = ((InternalEObject)source).eInverseRemove(this, RdbPackage.TABLE__OUTGOING_LINKS, Table.class, msgs);
			if (newSource != null)
				msgs = ((InternalEObject)newSource).eInverseAdd(this, RdbPackage.TABLE__OUTGOING_LINKS, Table.class, msgs);
			msgs = basicSetSource(newSource, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.RELATION__SOURCE, newSource, newSource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Table getTarget() {
		if (target != null && target.eIsProxy()) {
			InternalEObject oldTarget = (InternalEObject)target;
			target = (Table)eResolveProxy(oldTarget);
			if (target != oldTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RdbPackage.RELATION__TARGET, oldTarget, target));
			}
		}
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Table basicGetTarget() {
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTarget(Table newTarget, NotificationChain msgs) {
		Table oldTarget = target;
		target = newTarget;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RdbPackage.RELATION__TARGET, oldTarget, newTarget);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTarget(Table newTarget) {
		if (newTarget != target) {
			NotificationChain msgs = null;
			if (target != null)
				msgs = ((InternalEObject)target).eInverseRemove(this, RdbPackage.TABLE__INCOMING_LINKS, Table.class, msgs);
			if (newTarget != null)
				msgs = ((InternalEObject)newTarget).eInverseAdd(this, RdbPackage.TABLE__INCOMING_LINKS, Table.class, msgs);
			msgs = basicSetTarget(newTarget, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.RELATION__TARGET, newTarget, newTarget));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RelationKind getSource_kind() {
		return source_kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSource_kind(RelationKind newSource_kind) {
		RelationKind oldSource_kind = source_kind;
		source_kind = newSource_kind == null ? SOURCE_KIND_EDEFAULT : newSource_kind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.RELATION__SOURCE_KIND, oldSource_kind, source_kind));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RelationKind getTarget_kind() {
		return target_kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTarget_kind(RelationKind newTarget_kind) {
		RelationKind oldTarget_kind = target_kind;
		target_kind = newTarget_kind == null ? TARGET_KIND_EDEFAULT : newTarget_kind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.RELATION__TARGET_KIND, oldTarget_kind, target_kind));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getColumn_name() {
		return column_name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setColumn_name(String newColumn_name) {
		String oldColumn_name = column_name;
		column_name = newColumn_name;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.RELATION__COLUMN_NAME, oldColumn_name, column_name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getReferenced_column_name() {
		return referenced_column_name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReferenced_column_name(String newReferenced_column_name) {
		String oldReferenced_column_name = referenced_column_name;
		referenced_column_name = newReferenced_column_name;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.RELATION__REFERENCED_COLUMN_NAME, oldReferenced_column_name, referenced_column_name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Point> getBendpoint() {
		if (bendpoint == null) {
			bendpoint = new EDataTypeUniqueEList<Point>(Point.class, this, RdbPackage.RELATION__BENDPOINT);
		}
		return bendpoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DB getDb() {
		if (eContainerFeatureID() != RdbPackage.RELATION__DB) return null;
		return (DB)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDb(DB newDb, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newDb, RdbPackage.RELATION__DB, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDb(DB newDb) {
		if (newDb != eInternalContainer() || (eContainerFeatureID() != RdbPackage.RELATION__DB && newDb != null)) {
			if (EcoreUtil.isAncestor(this, newDb))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newDb != null)
				msgs = ((InternalEObject)newDb).eInverseAdd(this, RdbPackage.DB__REFERENCES, DB.class, msgs);
			msgs = basicSetDb(newDb, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.RELATION__DB, newDb, newDb));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComment(String newComment) {
		String oldComment = comment;
		comment = newComment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.RELATION__COMMENT, oldComment, comment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getConstraint_name() {
		return constraint_name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConstraint_name(String newConstraint_name) {
		String oldConstraint_name = constraint_name;
		constraint_name = newConstraint_name;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.RELATION__CONSTRAINT_NAME, oldConstraint_name, constraint_name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RdbPackage.RELATION__SOURCE:
				if (source != null)
					msgs = ((InternalEObject)source).eInverseRemove(this, RdbPackage.TABLE__OUTGOING_LINKS, Table.class, msgs);
				return basicSetSource((Table)otherEnd, msgs);
			case RdbPackage.RELATION__TARGET:
				if (target != null)
					msgs = ((InternalEObject)target).eInverseRemove(this, RdbPackage.TABLE__INCOMING_LINKS, Table.class, msgs);
				return basicSetTarget((Table)otherEnd, msgs);
			case RdbPackage.RELATION__DB:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetDb((DB)otherEnd, msgs);
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
			case RdbPackage.RELATION__SOURCE:
				return basicSetSource(null, msgs);
			case RdbPackage.RELATION__TARGET:
				return basicSetTarget(null, msgs);
			case RdbPackage.RELATION__DB:
				return basicSetDb(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case RdbPackage.RELATION__DB:
				return eInternalContainer().eInverseRemove(this, RdbPackage.DB__REFERENCES, DB.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RdbPackage.RELATION__TYPE:
				return getType();
			case RdbPackage.RELATION__SOURCE:
				if (resolve) return getSource();
				return basicGetSource();
			case RdbPackage.RELATION__TARGET:
				if (resolve) return getTarget();
				return basicGetTarget();
			case RdbPackage.RELATION__SOURCE_KIND:
				return getSource_kind();
			case RdbPackage.RELATION__TARGET_KIND:
				return getTarget_kind();
			case RdbPackage.RELATION__COLUMN_NAME:
				return getColumn_name();
			case RdbPackage.RELATION__REFERENCED_COLUMN_NAME:
				return getReferenced_column_name();
			case RdbPackage.RELATION__BENDPOINT:
				return getBendpoint();
			case RdbPackage.RELATION__DB:
				return getDb();
			case RdbPackage.RELATION__COMMENT:
				return getComment();
			case RdbPackage.RELATION__CONSTRAINT_NAME:
				return getConstraint_name();
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
			case RdbPackage.RELATION__TYPE:
				setType((String)newValue);
				return;
			case RdbPackage.RELATION__SOURCE:
				setSource((Table)newValue);
				return;
			case RdbPackage.RELATION__TARGET:
				setTarget((Table)newValue);
				return;
			case RdbPackage.RELATION__SOURCE_KIND:
				setSource_kind((RelationKind)newValue);
				return;
			case RdbPackage.RELATION__TARGET_KIND:
				setTarget_kind((RelationKind)newValue);
				return;
			case RdbPackage.RELATION__COLUMN_NAME:
				setColumn_name((String)newValue);
				return;
			case RdbPackage.RELATION__REFERENCED_COLUMN_NAME:
				setReferenced_column_name((String)newValue);
				return;
			case RdbPackage.RELATION__BENDPOINT:
				getBendpoint().clear();
				getBendpoint().addAll((Collection<? extends Point>)newValue);
				return;
			case RdbPackage.RELATION__DB:
				setDb((DB)newValue);
				return;
			case RdbPackage.RELATION__COMMENT:
				setComment((String)newValue);
				return;
			case RdbPackage.RELATION__CONSTRAINT_NAME:
				setConstraint_name((String)newValue);
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
			case RdbPackage.RELATION__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case RdbPackage.RELATION__SOURCE:
				setSource((Table)null);
				return;
			case RdbPackage.RELATION__TARGET:
				setTarget((Table)null);
				return;
			case RdbPackage.RELATION__SOURCE_KIND:
				setSource_kind(SOURCE_KIND_EDEFAULT);
				return;
			case RdbPackage.RELATION__TARGET_KIND:
				setTarget_kind(TARGET_KIND_EDEFAULT);
				return;
			case RdbPackage.RELATION__COLUMN_NAME:
				setColumn_name(COLUMN_NAME_EDEFAULT);
				return;
			case RdbPackage.RELATION__REFERENCED_COLUMN_NAME:
				setReferenced_column_name(REFERENCED_COLUMN_NAME_EDEFAULT);
				return;
			case RdbPackage.RELATION__BENDPOINT:
				getBendpoint().clear();
				return;
			case RdbPackage.RELATION__DB:
				setDb((DB)null);
				return;
			case RdbPackage.RELATION__COMMENT:
				setComment(COMMENT_EDEFAULT);
				return;
			case RdbPackage.RELATION__CONSTRAINT_NAME:
				setConstraint_name(CONSTRAINT_NAME_EDEFAULT);
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
			case RdbPackage.RELATION__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			case RdbPackage.RELATION__SOURCE:
				return source != null;
			case RdbPackage.RELATION__TARGET:
				return target != null;
			case RdbPackage.RELATION__SOURCE_KIND:
				return source_kind != SOURCE_KIND_EDEFAULT;
			case RdbPackage.RELATION__TARGET_KIND:
				return target_kind != TARGET_KIND_EDEFAULT;
			case RdbPackage.RELATION__COLUMN_NAME:
				return COLUMN_NAME_EDEFAULT == null ? column_name != null : !COLUMN_NAME_EDEFAULT.equals(column_name);
			case RdbPackage.RELATION__REFERENCED_COLUMN_NAME:
				return REFERENCED_COLUMN_NAME_EDEFAULT == null ? referenced_column_name != null : !REFERENCED_COLUMN_NAME_EDEFAULT.equals(referenced_column_name);
			case RdbPackage.RELATION__BENDPOINT:
				return bendpoint != null && !bendpoint.isEmpty();
			case RdbPackage.RELATION__DB:
				return getDb() != null;
			case RdbPackage.RELATION__COMMENT:
				return COMMENT_EDEFAULT == null ? comment != null : !COMMENT_EDEFAULT.equals(comment);
			case RdbPackage.RELATION__CONSTRAINT_NAME:
				return CONSTRAINT_NAME_EDEFAULT == null ? constraint_name != null : !CONSTRAINT_NAME_EDEFAULT.equals(constraint_name);
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
		result.append(" (type: ");
		result.append(type);
		result.append(", source_kind: ");
		result.append(source_kind);
		result.append(", target_kind: ");
		result.append(target_kind);
		result.append(", column_name: ");
		result.append(column_name);
		result.append(", referenced_column_name: ");
		result.append(referenced_column_name);
		result.append(", bendpoint: ");
		result.append(bendpoint);
		result.append(", comment: ");
		result.append(comment);
		result.append(", constraint_name: ");
		result.append(constraint_name);
		result.append(')');
		return result.toString();
	}

} //RelationImpl
