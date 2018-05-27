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
package com.hangum.tadpole.mongodb.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DB</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.hangum.tadpole.mongodb.model.DB#getDbType <em>Db Type</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.DB#getTables <em>Tables</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.DB#getKey <em>Key</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.DB#getUrl <em>Url</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.DB#getId <em>Id</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.DB#getSid <em>Sid</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.DB#getReferences <em>References</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.DB#getComment <em>Comment</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getDB()
 * @model
 * @generated
 */
public interface DB extends ERDInfo {
	/**
	 * Returns the value of the '<em><b>Db Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Db Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Db Type</em>' attribute.
	 * @see #setDbType(String)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getDB_DbType()
	 * @model
	 * @generated
	 */
	String getDbType();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.DB#getDbType <em>Db Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Db Type</em>' attribute.
	 * @see #getDbType()
	 * @generated
	 */
	void setDbType(String value);

	/**
	 * Returns the value of the '<em><b>Tables</b></em>' containment reference list.
	 * The list contents are of type {@link com.hangum.tadpole.mongodb.model.Table}.
	 * It is bidirectional and its opposite is '{@link com.hangum.tadpole.mongodb.model.Table#getDb <em>Db</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tables</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tables</em>' containment reference list.
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getDB_Tables()
	 * @see com.hangum.tadpole.mongodb.model.Table#getDb
	 * @model opposite="db" containment="true"
	 * @generated
	 */
	EList<Table> getTables();

	/**
	 * Returns the value of the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key</em>' attribute.
	 * @see #setKey(String)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getDB_Key()
	 * @model
	 * @generated
	 */
	String getKey();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.DB#getKey <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key</em>' attribute.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(String value);

	/**
	 * Returns the value of the '<em><b>Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Url</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Url</em>' attribute.
	 * @see #setUrl(String)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getDB_Url()
	 * @model
	 * @generated
	 */
	String getUrl();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.DB#getUrl <em>Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Url</em>' attribute.
	 * @see #getUrl()
	 * @generated
	 */
	void setUrl(String value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getDB_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.DB#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Sid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sid</em>' attribute.
	 * @see #setSid(String)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getDB_Sid()
	 * @model
	 * @generated
	 */
	String getSid();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.DB#getSid <em>Sid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sid</em>' attribute.
	 * @see #getSid()
	 * @generated
	 */
	void setSid(String value);

	/**
	 * Returns the value of the '<em><b>References</b></em>' containment reference list.
	 * The list contents are of type {@link com.hangum.tadpole.mongodb.model.Relation}.
	 * It is bidirectional and its opposite is '{@link com.hangum.tadpole.mongodb.model.Relation#getDb <em>Db</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>References</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>References</em>' containment reference list.
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getDB_References()
	 * @see com.hangum.tadpole.mongodb.model.Relation#getDb
	 * @model opposite="db" containment="true"
	 * @generated
	 */
	EList<Relation> getReferences();

	/**
	 * Returns the value of the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Comment</em>' attribute.
	 * @see #setComment(String)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getDB_Comment()
	 * @model
	 * @generated
	 */
	String getComment();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.DB#getComment <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Comment</em>' attribute.
	 * @see #getComment()
	 * @generated
	 */
	void setComment(String value);

} // DB
