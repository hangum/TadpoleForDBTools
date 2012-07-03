/**
 */
package org.eclipselabs.mongo.query.query.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipselabs.mongo.query.query.Database;
import org.eclipselabs.mongo.query.query.Model;
import org.eclipselabs.mongo.query.query.QueryPackage;
import org.eclipselabs.mongo.query.query.WhereEntry;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipselabs.mongo.query.query.impl.ModelImpl#getAttrs <em>Attrs</em>}</li>
 *   <li>{@link org.eclipselabs.mongo.query.query.impl.ModelImpl#getDb <em>Db</em>}</li>
 *   <li>{@link org.eclipselabs.mongo.query.query.impl.ModelImpl#getWhereEntry <em>Where Entry</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelImpl extends MinimalEObjectImpl.Container implements Model
{
  /**
   * The default value of the '{@link #getAttrs() <em>Attrs</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAttrs()
   * @generated
   * @ordered
   */
  protected static final String ATTRS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getAttrs() <em>Attrs</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAttrs()
   * @generated
   * @ordered
   */
  protected String attrs = ATTRS_EDEFAULT;

  /**
   * The cached value of the '{@link #getDb() <em>Db</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDb()
   * @generated
   * @ordered
   */
  protected Database db;

  /**
   * The cached value of the '{@link #getWhereEntry() <em>Where Entry</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWhereEntry()
   * @generated
   * @ordered
   */
  protected WhereEntry whereEntry;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ModelImpl()
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
    return QueryPackage.Literals.MODEL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getAttrs()
  {
    return attrs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAttrs(String newAttrs)
  {
    String oldAttrs = attrs;
    attrs = newAttrs;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.MODEL__ATTRS, oldAttrs, attrs));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Database getDb()
  {
    return db;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetDb(Database newDb, NotificationChain msgs)
  {
    Database oldDb = db;
    db = newDb;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QueryPackage.MODEL__DB, oldDb, newDb);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDb(Database newDb)
  {
    if (newDb != db)
    {
      NotificationChain msgs = null;
      if (db != null)
        msgs = ((InternalEObject)db).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QueryPackage.MODEL__DB, null, msgs);
      if (newDb != null)
        msgs = ((InternalEObject)newDb).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QueryPackage.MODEL__DB, null, msgs);
      msgs = basicSetDb(newDb, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.MODEL__DB, newDb, newDb));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WhereEntry getWhereEntry()
  {
    return whereEntry;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetWhereEntry(WhereEntry newWhereEntry, NotificationChain msgs)
  {
    WhereEntry oldWhereEntry = whereEntry;
    whereEntry = newWhereEntry;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QueryPackage.MODEL__WHERE_ENTRY, oldWhereEntry, newWhereEntry);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setWhereEntry(WhereEntry newWhereEntry)
  {
    if (newWhereEntry != whereEntry)
    {
      NotificationChain msgs = null;
      if (whereEntry != null)
        msgs = ((InternalEObject)whereEntry).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QueryPackage.MODEL__WHERE_ENTRY, null, msgs);
      if (newWhereEntry != null)
        msgs = ((InternalEObject)newWhereEntry).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QueryPackage.MODEL__WHERE_ENTRY, null, msgs);
      msgs = basicSetWhereEntry(newWhereEntry, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.MODEL__WHERE_ENTRY, newWhereEntry, newWhereEntry));
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
      case QueryPackage.MODEL__DB:
        return basicSetDb(null, msgs);
      case QueryPackage.MODEL__WHERE_ENTRY:
        return basicSetWhereEntry(null, msgs);
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
      case QueryPackage.MODEL__ATTRS:
        return getAttrs();
      case QueryPackage.MODEL__DB:
        return getDb();
      case QueryPackage.MODEL__WHERE_ENTRY:
        return getWhereEntry();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case QueryPackage.MODEL__ATTRS:
        setAttrs((String)newValue);
        return;
      case QueryPackage.MODEL__DB:
        setDb((Database)newValue);
        return;
      case QueryPackage.MODEL__WHERE_ENTRY:
        setWhereEntry((WhereEntry)newValue);
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
      case QueryPackage.MODEL__ATTRS:
        setAttrs(ATTRS_EDEFAULT);
        return;
      case QueryPackage.MODEL__DB:
        setDb((Database)null);
        return;
      case QueryPackage.MODEL__WHERE_ENTRY:
        setWhereEntry((WhereEntry)null);
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
      case QueryPackage.MODEL__ATTRS:
        return ATTRS_EDEFAULT == null ? attrs != null : !ATTRS_EDEFAULT.equals(attrs);
      case QueryPackage.MODEL__DB:
        return db != null;
      case QueryPackage.MODEL__WHERE_ENTRY:
        return whereEntry != null;
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
    result.append(" (attrs: ");
    result.append(attrs);
    result.append(')');
    return result.toString();
  }

} //ModelImpl
