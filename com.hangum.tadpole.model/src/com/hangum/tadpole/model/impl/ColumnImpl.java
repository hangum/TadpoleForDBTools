/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.hangum.tadpole.model.impl;

import com.hangum.tadpole.model.Column;
import com.hangum.tadpole.model.Table;
import com.hangum.tadpole.model.TadpolePackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Column</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.hangum.tadpole.model.impl.ColumnImpl#getField <em>Field</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.ColumnImpl#getType <em>Type</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.ColumnImpl#getNull <em>Null</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.ColumnImpl#getDefault <em>Default</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.ColumnImpl#getExtra <em>Extra</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.ColumnImpl#getTable <em>Table</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.ColumnImpl#getLogicalField <em>Logical Field</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.ColumnImpl#getKey <em>Key</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.ColumnImpl#getComment <em>Comment</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ColumnImpl extends EObjectImpl implements Column {
	/**
	 * The default value of the '{@link #getField() <em>Field</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getField()
	 * @generated
	 * @ordered
	 */
	protected static final String FIELD_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getField() <em>Field</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getField()
	 * @generated
	 * @ordered
	 */
	protected String field = FIELD_EDEFAULT;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_EDEFAULT = null;

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
	 * The default value of the '{@link #getNull() <em>Null</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNull()
	 * @generated
	 * @ordered
	 */
	protected static final String NULL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNull() <em>Null</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNull()
	 * @generated
	 * @ordered
	 */
	protected String null_ = NULL_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefault() <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefault()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefault() <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefault()
	 * @generated
	 * @ordered
	 */
	protected String default_ = DEFAULT_EDEFAULT;

	/**
	 * The default value of the '{@link #getExtra() <em>Extra</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtra()
	 * @generated
	 * @ordered
	 */
	protected static final String EXTRA_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExtra() <em>Extra</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtra()
	 * @generated
	 * @ordered
	 */
	protected String extra = EXTRA_EDEFAULT;

	/**
	 * The default value of the '{@link #getLogicalField() <em>Logical Field</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLogicalField()
	 * @generated
	 * @ordered
	 */
	protected static final String LOGICAL_FIELD_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLogicalField() <em>Logical Field</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLogicalField()
	 * @generated
	 * @ordered
	 */
	protected String logicalField = LOGICAL_FIELD_EDEFAULT;

	/**
	 * The default value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected static final String KEY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected String key = KEY_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ColumnImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TadpolePackage.Literals.COLUMN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getField() {
		return field;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setField(String newField) {
		String oldField = field;
		field = newField;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.COLUMN__FIELD, oldField, field));
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
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.COLUMN__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNull() {
		return null_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNull(String newNull) {
		String oldNull = null_;
		null_ = newNull;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.COLUMN__NULL, oldNull, null_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefault() {
		return default_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefault(String newDefault) {
		String oldDefault = default_;
		default_ = newDefault;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.COLUMN__DEFAULT, oldDefault, default_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExtra() {
		return extra;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExtra(String newExtra) {
		String oldExtra = extra;
		extra = newExtra;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.COLUMN__EXTRA, oldExtra, extra));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Table getTable() {
		if (eContainerFeatureID() != TadpolePackage.COLUMN__TABLE) return null;
		return (Table)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTable(Table newTable, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newTable, TadpolePackage.COLUMN__TABLE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTable(Table newTable) {
		if (newTable != eInternalContainer() || (eContainerFeatureID() != TadpolePackage.COLUMN__TABLE && newTable != null)) {
			if (EcoreUtil.isAncestor(this, newTable))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newTable != null)
				msgs = ((InternalEObject)newTable).eInverseAdd(this, TadpolePackage.TABLE__COLUMNS, Table.class, msgs);
			msgs = basicSetTable(newTable, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.COLUMN__TABLE, newTable, newTable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLogicalField() {
		return logicalField;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLogicalField(String newLogicalField) {
		String oldLogicalField = logicalField;
		logicalField = newLogicalField;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.COLUMN__LOGICAL_FIELD, oldLogicalField, logicalField));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getKey() {
		return key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKey(String newKey) {
		String oldKey = key;
		key = newKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.COLUMN__KEY, oldKey, key));
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
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.COLUMN__COMMENT, oldComment, comment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TadpolePackage.COLUMN__TABLE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetTable((Table)otherEnd, msgs);
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
			case TadpolePackage.COLUMN__TABLE:
				return basicSetTable(null, msgs);
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
			case TadpolePackage.COLUMN__TABLE:
				return eInternalContainer().eInverseRemove(this, TadpolePackage.TABLE__COLUMNS, Table.class, msgs);
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
			case TadpolePackage.COLUMN__FIELD:
				return getField();
			case TadpolePackage.COLUMN__TYPE:
				return getType();
			case TadpolePackage.COLUMN__NULL:
				return getNull();
			case TadpolePackage.COLUMN__DEFAULT:
				return getDefault();
			case TadpolePackage.COLUMN__EXTRA:
				return getExtra();
			case TadpolePackage.COLUMN__TABLE:
				return getTable();
			case TadpolePackage.COLUMN__LOGICAL_FIELD:
				return getLogicalField();
			case TadpolePackage.COLUMN__KEY:
				return getKey();
			case TadpolePackage.COLUMN__COMMENT:
				return getComment();
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
			case TadpolePackage.COLUMN__FIELD:
				setField((String)newValue);
				return;
			case TadpolePackage.COLUMN__TYPE:
				setType((String)newValue);
				return;
			case TadpolePackage.COLUMN__NULL:
				setNull((String)newValue);
				return;
			case TadpolePackage.COLUMN__DEFAULT:
				setDefault((String)newValue);
				return;
			case TadpolePackage.COLUMN__EXTRA:
				setExtra((String)newValue);
				return;
			case TadpolePackage.COLUMN__TABLE:
				setTable((Table)newValue);
				return;
			case TadpolePackage.COLUMN__LOGICAL_FIELD:
				setLogicalField((String)newValue);
				return;
			case TadpolePackage.COLUMN__KEY:
				setKey((String)newValue);
				return;
			case TadpolePackage.COLUMN__COMMENT:
				setComment((String)newValue);
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
			case TadpolePackage.COLUMN__FIELD:
				setField(FIELD_EDEFAULT);
				return;
			case TadpolePackage.COLUMN__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case TadpolePackage.COLUMN__NULL:
				setNull(NULL_EDEFAULT);
				return;
			case TadpolePackage.COLUMN__DEFAULT:
				setDefault(DEFAULT_EDEFAULT);
				return;
			case TadpolePackage.COLUMN__EXTRA:
				setExtra(EXTRA_EDEFAULT);
				return;
			case TadpolePackage.COLUMN__TABLE:
				setTable((Table)null);
				return;
			case TadpolePackage.COLUMN__LOGICAL_FIELD:
				setLogicalField(LOGICAL_FIELD_EDEFAULT);
				return;
			case TadpolePackage.COLUMN__KEY:
				setKey(KEY_EDEFAULT);
				return;
			case TadpolePackage.COLUMN__COMMENT:
				setComment(COMMENT_EDEFAULT);
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
			case TadpolePackage.COLUMN__FIELD:
				return FIELD_EDEFAULT == null ? field != null : !FIELD_EDEFAULT.equals(field);
			case TadpolePackage.COLUMN__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			case TadpolePackage.COLUMN__NULL:
				return NULL_EDEFAULT == null ? null_ != null : !NULL_EDEFAULT.equals(null_);
			case TadpolePackage.COLUMN__DEFAULT:
				return DEFAULT_EDEFAULT == null ? default_ != null : !DEFAULT_EDEFAULT.equals(default_);
			case TadpolePackage.COLUMN__EXTRA:
				return EXTRA_EDEFAULT == null ? extra != null : !EXTRA_EDEFAULT.equals(extra);
			case TadpolePackage.COLUMN__TABLE:
				return getTable() != null;
			case TadpolePackage.COLUMN__LOGICAL_FIELD:
				return LOGICAL_FIELD_EDEFAULT == null ? logicalField != null : !LOGICAL_FIELD_EDEFAULT.equals(logicalField);
			case TadpolePackage.COLUMN__KEY:
				return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
			case TadpolePackage.COLUMN__COMMENT:
				return COMMENT_EDEFAULT == null ? comment != null : !COMMENT_EDEFAULT.equals(comment);
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
		result.append(" (field: ");
		result.append(field);
		result.append(", type: ");
		result.append(type);
		result.append(", null: ");
		result.append(null_);
		result.append(", default: ");
		result.append(default_);
		result.append(", extra: ");
		result.append(extra);
		result.append(", logicalField: ");
		result.append(logicalField);
		result.append(", key: ");
		result.append(key);
		result.append(", comment: ");
		result.append(comment);
		result.append(')');
		return result.toString();
	}

} //ColumnImpl
