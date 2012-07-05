/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.hangum.tadpole.model.impl;

import java.util.Collection;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import com.hangum.tadpole.model.Column;
import com.hangum.tadpole.model.DB;
import com.hangum.tadpole.model.Relation;
import com.hangum.tadpole.model.Table;
import com.hangum.tadpole.model.TadpolePackage;
import com.hangum.tadpole.model.UserComment;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.hangum.tadpole.model.impl.TableImpl#getColumns <em>Columns</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.TableImpl#getDb <em>Db</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.TableImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.TableImpl#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.TableImpl#getIncomingLinks <em>Incoming Links</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.TableImpl#getOutgoingLinks <em>Outgoing Links</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.TableImpl#getLogicalName <em>Logical Name</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.TableImpl#getCommant <em>Commant</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.TableImpl#getUserCommentReference <em>User Comment Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TableImpl extends EObjectImpl implements Table {
	/**
	 * The cached value of the '{@link #getColumns() <em>Columns</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumns()
	 * @generated
	 * @ordered
	 */
	protected EList<Column> columns;

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
	 * The default value of the '{@link #getConstraints() <em>Constraints</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected static final Rectangle CONSTRAINTS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getConstraints() <em>Constraints</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected Rectangle constraints = CONSTRAINTS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getIncomingLinks() <em>Incoming Links</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIncomingLinks()
	 * @generated
	 * @ordered
	 */
	protected EList<Relation> incomingLinks;

	/**
	 * The cached value of the '{@link #getOutgoingLinks() <em>Outgoing Links</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutgoingLinks()
	 * @generated
	 * @ordered
	 */
	protected EList<Relation> outgoingLinks;

	/**
	 * The default value of the '{@link #getLogicalName() <em>Logical Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLogicalName()
	 * @generated
	 * @ordered
	 */
	protected static final String LOGICAL_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLogicalName() <em>Logical Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLogicalName()
	 * @generated
	 * @ordered
	 */
	protected String logicalName = LOGICAL_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getCommant() <em>Commant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommant()
	 * @generated
	 * @ordered
	 */
	protected static final String COMMANT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCommant() <em>Commant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommant()
	 * @generated
	 * @ordered
	 */
	protected String commant = COMMANT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getUserCommentReference() <em>User Comment Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUserCommentReference()
	 * @generated
	 * @ordered
	 */
	protected UserComment userCommentReference;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TableImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TadpolePackage.Literals.TABLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Column> getColumns() {
		if (columns == null) {
			columns = new EObjectContainmentWithInverseEList<Column>(Column.class, this, TadpolePackage.TABLE__COLUMNS, TadpolePackage.COLUMN__TABLE);
		}
		return columns;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DB getDb() {
		if (eContainerFeatureID() != TadpolePackage.TABLE__DB) return null;
		return (DB)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDb(DB newDb, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newDb, TadpolePackage.TABLE__DB, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDb(DB newDb) {
		if (newDb != eInternalContainer() || (eContainerFeatureID() != TadpolePackage.TABLE__DB && newDb != null)) {
			if (EcoreUtil.isAncestor(this, newDb))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newDb != null)
				msgs = ((InternalEObject)newDb).eInverseAdd(this, TadpolePackage.DB__TABLES, DB.class, msgs);
			msgs = basicSetDb(newDb, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.TABLE__DB, newDb, newDb));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.TABLE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rectangle getConstraints() {
		return constraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConstraints(Rectangle newConstraints) {
		Rectangle oldConstraints = constraints;
		constraints = newConstraints;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.TABLE__CONSTRAINTS, oldConstraints, constraints));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Relation> getIncomingLinks() {
		if (incomingLinks == null) {
			incomingLinks = new EObjectWithInverseResolvingEList<Relation>(Relation.class, this, TadpolePackage.TABLE__INCOMING_LINKS, TadpolePackage.RELATION__TARGET);
		}
		return incomingLinks;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Relation> getOutgoingLinks() {
		if (outgoingLinks == null) {
			outgoingLinks = new EObjectWithInverseResolvingEList<Relation>(Relation.class, this, TadpolePackage.TABLE__OUTGOING_LINKS, TadpolePackage.RELATION__SOURCE);
		}
		return outgoingLinks;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLogicalName() {
		return logicalName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLogicalName(String newLogicalName) {
		String oldLogicalName = logicalName;
		logicalName = newLogicalName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.TABLE__LOGICAL_NAME, oldLogicalName, logicalName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCommant() {
		return commant;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCommant(String newCommant) {
		String oldCommant = commant;
		commant = newCommant;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.TABLE__COMMANT, oldCommant, commant));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UserComment getUserCommentReference() {
		if (userCommentReference != null && userCommentReference.eIsProxy()) {
			InternalEObject oldUserCommentReference = (InternalEObject)userCommentReference;
			userCommentReference = (UserComment)eResolveProxy(oldUserCommentReference);
			if (userCommentReference != oldUserCommentReference) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TadpolePackage.TABLE__USER_COMMENT_REFERENCE, oldUserCommentReference, userCommentReference));
			}
		}
		return userCommentReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UserComment basicGetUserCommentReference() {
		return userCommentReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUserCommentReference(UserComment newUserCommentReference) {
		UserComment oldUserCommentReference = userCommentReference;
		userCommentReference = newUserCommentReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.TABLE__USER_COMMENT_REFERENCE, oldUserCommentReference, userCommentReference));
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
			case TadpolePackage.TABLE__COLUMNS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getColumns()).basicAdd(otherEnd, msgs);
			case TadpolePackage.TABLE__DB:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetDb((DB)otherEnd, msgs);
			case TadpolePackage.TABLE__INCOMING_LINKS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getIncomingLinks()).basicAdd(otherEnd, msgs);
			case TadpolePackage.TABLE__OUTGOING_LINKS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getOutgoingLinks()).basicAdd(otherEnd, msgs);
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
			case TadpolePackage.TABLE__COLUMNS:
				return ((InternalEList<?>)getColumns()).basicRemove(otherEnd, msgs);
			case TadpolePackage.TABLE__DB:
				return basicSetDb(null, msgs);
			case TadpolePackage.TABLE__INCOMING_LINKS:
				return ((InternalEList<?>)getIncomingLinks()).basicRemove(otherEnd, msgs);
			case TadpolePackage.TABLE__OUTGOING_LINKS:
				return ((InternalEList<?>)getOutgoingLinks()).basicRemove(otherEnd, msgs);
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
			case TadpolePackage.TABLE__DB:
				return eInternalContainer().eInverseRemove(this, TadpolePackage.DB__TABLES, DB.class, msgs);
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
			case TadpolePackage.TABLE__COLUMNS:
				return getColumns();
			case TadpolePackage.TABLE__DB:
				return getDb();
			case TadpolePackage.TABLE__NAME:
				return getName();
			case TadpolePackage.TABLE__CONSTRAINTS:
				return getConstraints();
			case TadpolePackage.TABLE__INCOMING_LINKS:
				return getIncomingLinks();
			case TadpolePackage.TABLE__OUTGOING_LINKS:
				return getOutgoingLinks();
			case TadpolePackage.TABLE__LOGICAL_NAME:
				return getLogicalName();
			case TadpolePackage.TABLE__COMMANT:
				return getCommant();
			case TadpolePackage.TABLE__USER_COMMENT_REFERENCE:
				if (resolve) return getUserCommentReference();
				return basicGetUserCommentReference();
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
			case TadpolePackage.TABLE__COLUMNS:
				getColumns().clear();
				getColumns().addAll((Collection<? extends Column>)newValue);
				return;
			case TadpolePackage.TABLE__DB:
				setDb((DB)newValue);
				return;
			case TadpolePackage.TABLE__NAME:
				setName((String)newValue);
				return;
			case TadpolePackage.TABLE__CONSTRAINTS:
				setConstraints((Rectangle)newValue);
				return;
			case TadpolePackage.TABLE__INCOMING_LINKS:
				getIncomingLinks().clear();
				getIncomingLinks().addAll((Collection<? extends Relation>)newValue);
				return;
			case TadpolePackage.TABLE__OUTGOING_LINKS:
				getOutgoingLinks().clear();
				getOutgoingLinks().addAll((Collection<? extends Relation>)newValue);
				return;
			case TadpolePackage.TABLE__LOGICAL_NAME:
				setLogicalName((String)newValue);
				return;
			case TadpolePackage.TABLE__COMMANT:
				setCommant((String)newValue);
				return;
			case TadpolePackage.TABLE__USER_COMMENT_REFERENCE:
				setUserCommentReference((UserComment)newValue);
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
			case TadpolePackage.TABLE__COLUMNS:
				getColumns().clear();
				return;
			case TadpolePackage.TABLE__DB:
				setDb((DB)null);
				return;
			case TadpolePackage.TABLE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case TadpolePackage.TABLE__CONSTRAINTS:
				setConstraints(CONSTRAINTS_EDEFAULT);
				return;
			case TadpolePackage.TABLE__INCOMING_LINKS:
				getIncomingLinks().clear();
				return;
			case TadpolePackage.TABLE__OUTGOING_LINKS:
				getOutgoingLinks().clear();
				return;
			case TadpolePackage.TABLE__LOGICAL_NAME:
				setLogicalName(LOGICAL_NAME_EDEFAULT);
				return;
			case TadpolePackage.TABLE__COMMANT:
				setCommant(COMMANT_EDEFAULT);
				return;
			case TadpolePackage.TABLE__USER_COMMENT_REFERENCE:
				setUserCommentReference((UserComment)null);
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
			case TadpolePackage.TABLE__COLUMNS:
				return columns != null && !columns.isEmpty();
			case TadpolePackage.TABLE__DB:
				return getDb() != null;
			case TadpolePackage.TABLE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case TadpolePackage.TABLE__CONSTRAINTS:
				return CONSTRAINTS_EDEFAULT == null ? constraints != null : !CONSTRAINTS_EDEFAULT.equals(constraints);
			case TadpolePackage.TABLE__INCOMING_LINKS:
				return incomingLinks != null && !incomingLinks.isEmpty();
			case TadpolePackage.TABLE__OUTGOING_LINKS:
				return outgoingLinks != null && !outgoingLinks.isEmpty();
			case TadpolePackage.TABLE__LOGICAL_NAME:
				return LOGICAL_NAME_EDEFAULT == null ? logicalName != null : !LOGICAL_NAME_EDEFAULT.equals(logicalName);
			case TadpolePackage.TABLE__COMMANT:
				return COMMANT_EDEFAULT == null ? commant != null : !COMMANT_EDEFAULT.equals(commant);
			case TadpolePackage.TABLE__USER_COMMENT_REFERENCE:
				return userCommentReference != null;
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
		result.append(" (name: ");
		result.append(name);
		result.append(", constraints: ");
		result.append(constraints);
		result.append(", logicalName: ");
		result.append(logicalName);
		result.append(", commant: ");
		result.append(commant);
		result.append(')');
		return result.toString();
	}

} //TableImpl
