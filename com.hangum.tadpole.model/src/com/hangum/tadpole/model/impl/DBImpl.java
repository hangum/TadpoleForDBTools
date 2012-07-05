/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.hangum.tadpole.model.impl;

import com.hangum.tadpole.model.DB;
import com.hangum.tadpole.model.Relation;
import com.hangum.tadpole.model.Table;
import com.hangum.tadpole.model.TadpolePackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DB</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.hangum.tadpole.model.impl.DBImpl#getDbType <em>Db Type</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.DBImpl#getTables <em>Tables</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.DBImpl#getKey <em>Key</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.DBImpl#getUrl <em>Url</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.DBImpl#getId <em>Id</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.DBImpl#getSid <em>Sid</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.DBImpl#getReferences <em>References</em>}</li>
 *   <li>{@link com.hangum.tadpole.model.impl.DBImpl#getComment <em>Comment</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DBImpl extends ERDInfoImpl implements DB {
	/**
	 * The default value of the '{@link #getDbType() <em>Db Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDbType()
	 * @generated
	 * @ordered
	 */
	protected static final String DB_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDbType() <em>Db Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDbType()
	 * @generated
	 * @ordered
	 */
	protected String dbType = DB_TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTables() <em>Tables</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTables()
	 * @generated
	 * @ordered
	 */
	protected EList<Table> tables;

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
	 * The default value of the '{@link #getUrl() <em>Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUrl()
	 * @generated
	 * @ordered
	 */
	protected static final String URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUrl() <em>Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUrl()
	 * @generated
	 * @ordered
	 */
	protected String url = URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getSid() <em>Sid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSid()
	 * @generated
	 * @ordered
	 */
	protected static final String SID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSid() <em>Sid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSid()
	 * @generated
	 * @ordered
	 */
	protected String sid = SID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReferences() <em>References</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferences()
	 * @generated
	 * @ordered
	 */
	protected EList<Relation> references;

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
	protected DBImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TadpolePackage.Literals.DB;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDbType() {
		return dbType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDbType(String newDbType) {
		String oldDbType = dbType;
		dbType = newDbType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.DB__DB_TYPE, oldDbType, dbType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Table> getTables() {
		if (tables == null) {
			tables = new EObjectContainmentWithInverseEList<Table>(Table.class, this, TadpolePackage.DB__TABLES, TadpolePackage.TABLE__DB);
		}
		return tables;
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
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.DB__KEY, oldKey, key));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUrl(String newUrl) {
		String oldUrl = url;
		url = newUrl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.DB__URL, oldUrl, url));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.DB__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSid() {
		return sid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSid(String newSid) {
		String oldSid = sid;
		sid = newSid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.DB__SID, oldSid, sid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Relation> getReferences() {
		if (references == null) {
			references = new EObjectContainmentWithInverseEList<Relation>(Relation.class, this, TadpolePackage.DB__REFERENCES, TadpolePackage.RELATION__DB);
		}
		return references;
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
			eNotify(new ENotificationImpl(this, Notification.SET, TadpolePackage.DB__COMMENT, oldComment, comment));
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
			case TadpolePackage.DB__TABLES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getTables()).basicAdd(otherEnd, msgs);
			case TadpolePackage.DB__REFERENCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getReferences()).basicAdd(otherEnd, msgs);
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
			case TadpolePackage.DB__TABLES:
				return ((InternalEList<?>)getTables()).basicRemove(otherEnd, msgs);
			case TadpolePackage.DB__REFERENCES:
				return ((InternalEList<?>)getReferences()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TadpolePackage.DB__DB_TYPE:
				return getDbType();
			case TadpolePackage.DB__TABLES:
				return getTables();
			case TadpolePackage.DB__KEY:
				return getKey();
			case TadpolePackage.DB__URL:
				return getUrl();
			case TadpolePackage.DB__ID:
				return getId();
			case TadpolePackage.DB__SID:
				return getSid();
			case TadpolePackage.DB__REFERENCES:
				return getReferences();
			case TadpolePackage.DB__COMMENT:
				return getComment();
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
			case TadpolePackage.DB__DB_TYPE:
				setDbType((String)newValue);
				return;
			case TadpolePackage.DB__TABLES:
				getTables().clear();
				getTables().addAll((Collection<? extends Table>)newValue);
				return;
			case TadpolePackage.DB__KEY:
				setKey((String)newValue);
				return;
			case TadpolePackage.DB__URL:
				setUrl((String)newValue);
				return;
			case TadpolePackage.DB__ID:
				setId((String)newValue);
				return;
			case TadpolePackage.DB__SID:
				setSid((String)newValue);
				return;
			case TadpolePackage.DB__REFERENCES:
				getReferences().clear();
				getReferences().addAll((Collection<? extends Relation>)newValue);
				return;
			case TadpolePackage.DB__COMMENT:
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
			case TadpolePackage.DB__DB_TYPE:
				setDbType(DB_TYPE_EDEFAULT);
				return;
			case TadpolePackage.DB__TABLES:
				getTables().clear();
				return;
			case TadpolePackage.DB__KEY:
				setKey(KEY_EDEFAULT);
				return;
			case TadpolePackage.DB__URL:
				setUrl(URL_EDEFAULT);
				return;
			case TadpolePackage.DB__ID:
				setId(ID_EDEFAULT);
				return;
			case TadpolePackage.DB__SID:
				setSid(SID_EDEFAULT);
				return;
			case TadpolePackage.DB__REFERENCES:
				getReferences().clear();
				return;
			case TadpolePackage.DB__COMMENT:
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
			case TadpolePackage.DB__DB_TYPE:
				return DB_TYPE_EDEFAULT == null ? dbType != null : !DB_TYPE_EDEFAULT.equals(dbType);
			case TadpolePackage.DB__TABLES:
				return tables != null && !tables.isEmpty();
			case TadpolePackage.DB__KEY:
				return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
			case TadpolePackage.DB__URL:
				return URL_EDEFAULT == null ? url != null : !URL_EDEFAULT.equals(url);
			case TadpolePackage.DB__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case TadpolePackage.DB__SID:
				return SID_EDEFAULT == null ? sid != null : !SID_EDEFAULT.equals(sid);
			case TadpolePackage.DB__REFERENCES:
				return references != null && !references.isEmpty();
			case TadpolePackage.DB__COMMENT:
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
		result.append(" (dbType: ");
		result.append(dbType);
		result.append(", key: ");
		result.append(key);
		result.append(", url: ");
		result.append(url);
		result.append(", id: ");
		result.append(id);
		result.append(", sid: ");
		result.append(sid);
		result.append(", comment: ");
		result.append(comment);
		result.append(')');
		return result.toString();
	}

} //DBImpl
