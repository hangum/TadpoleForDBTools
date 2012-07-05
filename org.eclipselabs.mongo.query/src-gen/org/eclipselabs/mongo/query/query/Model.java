/**
 */
package org.eclipselabs.mongo.query.query;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipselabs.mongo.query.query.Model#getAttrs <em>Attrs</em>}</li>
 *   <li>{@link org.eclipselabs.mongo.query.query.Model#getDb <em>Db</em>}</li>
 *   <li>{@link org.eclipselabs.mongo.query.query.Model#getWhereEntry <em>Where Entry</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipselabs.mongo.query.query.QueryPackage#getModel()
 * @model
 * @generated
 */
public interface Model extends EObject
{
  /**
   * Returns the value of the '<em><b>Attrs</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Attrs</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Attrs</em>' attribute.
   * @see #setAttrs(String)
   * @see org.eclipselabs.mongo.query.query.QueryPackage#getModel_Attrs()
   * @model
   * @generated
   */
  String getAttrs();

  /**
   * Sets the value of the '{@link org.eclipselabs.mongo.query.query.Model#getAttrs <em>Attrs</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Attrs</em>' attribute.
   * @see #getAttrs()
   * @generated
   */
  void setAttrs(String value);

  /**
   * Returns the value of the '<em><b>Db</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Db</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Db</em>' containment reference.
   * @see #setDb(Database)
   * @see org.eclipselabs.mongo.query.query.QueryPackage#getModel_Db()
   * @model containment="true"
   * @generated
   */
  Database getDb();

  /**
   * Sets the value of the '{@link org.eclipselabs.mongo.query.query.Model#getDb <em>Db</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Db</em>' containment reference.
   * @see #getDb()
   * @generated
   */
  void setDb(Database value);

  /**
   * Returns the value of the '<em><b>Where Entry</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Where Entry</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Where Entry</em>' containment reference.
   * @see #setWhereEntry(WhereEntry)
   * @see org.eclipselabs.mongo.query.query.QueryPackage#getModel_WhereEntry()
   * @model containment="true"
   * @generated
   */
  WhereEntry getWhereEntry();

  /**
   * Sets the value of the '{@link org.eclipselabs.mongo.query.query.Model#getWhereEntry <em>Where Entry</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Where Entry</em>' containment reference.
   * @see #getWhereEntry()
   * @generated
   */
  void setWhereEntry(WhereEntry value);

} // Model
