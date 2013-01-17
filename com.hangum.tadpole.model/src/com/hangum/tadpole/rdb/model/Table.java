/**
 */
package com.hangum.tadpole.rdb.model;

import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.hangum.tadpole.rdb.model.Table#getColumns <em>Columns</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Table#getDb <em>Db</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Table#getName <em>Name</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Table#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Table#getIncomingLinks <em>Incoming Links</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Table#getOutgoingLinks <em>Outgoing Links</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Table#getLogicalName <em>Logical Name</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Table#getCommant <em>Commant</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Table#getUserCommentReference <em>User Comment Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.hangum.tadpole.rdb.model.RdbPackage#getTable()
 * @model
 * @generated
 */
public interface Table extends EObject {
	/**
	 * Returns the value of the '<em><b>Columns</b></em>' containment reference list.
	 * The list contents are of type {@link com.hangum.tadpole.rdb.model.Column}.
	 * It is bidirectional and its opposite is '{@link com.hangum.tadpole.rdb.model.Column#getTable <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Columns</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Columns</em>' containment reference list.
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getTable_Columns()
	 * @see com.hangum.tadpole.rdb.model.Column#getTable
	 * @model opposite="table" containment="true" required="true"
	 * @generated
	 */
	EList<Column> getColumns();

	/**
	 * Returns the value of the '<em><b>Db</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.hangum.tadpole.rdb.model.DB#getTables <em>Tables</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Db</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Db</em>' container reference.
	 * @see #setDb(DB)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getTable_Db()
	 * @see com.hangum.tadpole.rdb.model.DB#getTables
	 * @model opposite="tables" required="true" transient="false"
	 * @generated
	 */
	DB getDb();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Table#getDb <em>Db</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Db</em>' container reference.
	 * @see #getDb()
	 * @generated
	 */
	void setDb(DB value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getTable_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Table#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraints</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraints</em>' attribute.
	 * @see #setConstraints(Rectangle)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getTable_Constraints()
	 * @model dataType="com.hangum.tadpole.rdb.model.Rectangle"
	 * @generated
	 */
	Rectangle getConstraints();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Table#getConstraints <em>Constraints</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constraints</em>' attribute.
	 * @see #getConstraints()
	 * @generated
	 */
	void setConstraints(Rectangle value);

	/**
	 * Returns the value of the '<em><b>Incoming Links</b></em>' reference list.
	 * The list contents are of type {@link com.hangum.tadpole.rdb.model.Relation}.
	 * It is bidirectional and its opposite is '{@link com.hangum.tadpole.rdb.model.Relation#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Incoming Links</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Incoming Links</em>' reference list.
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getTable_IncomingLinks()
	 * @see com.hangum.tadpole.rdb.model.Relation#getTarget
	 * @model opposite="target"
	 * @generated
	 */
	EList<Relation> getIncomingLinks();

	/**
	 * Returns the value of the '<em><b>Outgoing Links</b></em>' reference list.
	 * The list contents are of type {@link com.hangum.tadpole.rdb.model.Relation}.
	 * It is bidirectional and its opposite is '{@link com.hangum.tadpole.rdb.model.Relation#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Outgoing Links</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Outgoing Links</em>' reference list.
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getTable_OutgoingLinks()
	 * @see com.hangum.tadpole.rdb.model.Relation#getSource
	 * @model opposite="source"
	 * @generated
	 */
	EList<Relation> getOutgoingLinks();

	/**
	 * Returns the value of the '<em><b>Logical Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Logical Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Logical Name</em>' attribute.
	 * @see #setLogicalName(String)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getTable_LogicalName()
	 * @model
	 * @generated
	 */
	String getLogicalName();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Table#getLogicalName <em>Logical Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Logical Name</em>' attribute.
	 * @see #getLogicalName()
	 * @generated
	 */
	void setLogicalName(String value);

	/**
	 * Returns the value of the '<em><b>Commant</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Commant</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Commant</em>' attribute.
	 * @see #setCommant(String)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getTable_Commant()
	 * @model
	 * @generated
	 */
	String getCommant();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Table#getCommant <em>Commant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Commant</em>' attribute.
	 * @see #getCommant()
	 * @generated
	 */
	void setCommant(String value);

	/**
	 * Returns the value of the '<em><b>User Comment Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>User Comment Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>User Comment Reference</em>' reference.
	 * @see #setUserCommentReference(UserComment)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getTable_UserCommentReference()
	 * @model
	 * @generated
	 */
	UserComment getUserCommentReference();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Table#getUserCommentReference <em>User Comment Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>User Comment Reference</em>' reference.
	 * @see #getUserCommentReference()
	 * @generated
	 */
	void setUserCommentReference(UserComment value);

} // Table
