/**
 */
package org.eclipselabs.mongo.query.query.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipselabs.mongo.query.query.Database;
import org.eclipselabs.mongo.query.query.QueryPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Database</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipselabs.mongo.query.query.impl.DatabaseImpl#getUrl <em>Url</em>}</li>
 *   <li>{@link org.eclipselabs.mongo.query.query.impl.DatabaseImpl#getPort <em>Port</em>}</li>
 *   <li>{@link org.eclipselabs.mongo.query.query.impl.DatabaseImpl#getDbName <em>Db Name</em>}</li>
 *   <li>{@link org.eclipselabs.mongo.query.query.impl.DatabaseImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DatabaseImpl extends MinimalEObjectImpl.Container implements Database
{
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
   * The default value of the '{@link #getPort() <em>Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPort()
   * @generated
   * @ordered
   */
  protected static final long PORT_EDEFAULT = 0L;

  /**
   * The cached value of the '{@link #getPort() <em>Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPort()
   * @generated
   * @ordered
   */
  protected long port = PORT_EDEFAULT;

  /**
   * The default value of the '{@link #getDbName() <em>Db Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDbName()
   * @generated
   * @ordered
   */
  protected static final String DB_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDbName() <em>Db Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDbName()
   * @generated
   * @ordered
   */
  protected String dbName = DB_NAME_EDEFAULT;

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
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DatabaseImpl()
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
    return QueryPackage.Literals.DATABASE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getUrl()
  {
    return url;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUrl(String newUrl)
  {
    String oldUrl = url;
    url = newUrl;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.DATABASE__URL, oldUrl, url));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public long getPort()
  {
    return port;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPort(long newPort)
  {
    long oldPort = port;
    port = newPort;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.DATABASE__PORT, oldPort, port));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDbName()
  {
    return dbName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDbName(String newDbName)
  {
    String oldDbName = dbName;
    dbName = newDbName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.DATABASE__DB_NAME, oldDbName, dbName));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QueryPackage.DATABASE__NAME, oldName, name));
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
      case QueryPackage.DATABASE__URL:
        return getUrl();
      case QueryPackage.DATABASE__PORT:
        return getPort();
      case QueryPackage.DATABASE__DB_NAME:
        return getDbName();
      case QueryPackage.DATABASE__NAME:
        return getName();
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
      case QueryPackage.DATABASE__URL:
        setUrl((String)newValue);
        return;
      case QueryPackage.DATABASE__PORT:
        setPort((Long)newValue);
        return;
      case QueryPackage.DATABASE__DB_NAME:
        setDbName((String)newValue);
        return;
      case QueryPackage.DATABASE__NAME:
        setName((String)newValue);
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
      case QueryPackage.DATABASE__URL:
        setUrl(URL_EDEFAULT);
        return;
      case QueryPackage.DATABASE__PORT:
        setPort(PORT_EDEFAULT);
        return;
      case QueryPackage.DATABASE__DB_NAME:
        setDbName(DB_NAME_EDEFAULT);
        return;
      case QueryPackage.DATABASE__NAME:
        setName(NAME_EDEFAULT);
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
      case QueryPackage.DATABASE__URL:
        return URL_EDEFAULT == null ? url != null : !URL_EDEFAULT.equals(url);
      case QueryPackage.DATABASE__PORT:
        return port != PORT_EDEFAULT;
      case QueryPackage.DATABASE__DB_NAME:
        return DB_NAME_EDEFAULT == null ? dbName != null : !DB_NAME_EDEFAULT.equals(dbName);
      case QueryPackage.DATABASE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
    result.append(" (url: ");
    result.append(url);
    result.append(", port: ");
    result.append(port);
    result.append(", dbName: ");
    result.append(dbName);
    result.append(", name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //DatabaseImpl
