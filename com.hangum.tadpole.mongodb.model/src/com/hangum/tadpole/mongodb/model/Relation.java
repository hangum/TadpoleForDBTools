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

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Relation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.hangum.tadpole.mongodb.model.Relation#getSource <em>Source</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.Relation#getTarget <em>Target</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.Relation#getSource_kind <em>Source kind</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.Relation#getTarget_kind <em>Target kind</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.Relation#getColumn_name <em>Column name</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.Relation#getReferenced_column_name <em>Referenced column name</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.Relation#getBendpoint <em>Bendpoint</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.Relation#getDb <em>Db</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.Relation#getComment <em>Comment</em>}</li>
 *   <li>{@link com.hangum.tadpole.mongodb.model.Relation#getConstraint_name <em>Constraint name</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getRelation()
 * @model
 * @generated
 */
public interface Relation extends EObject {
	/**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.hangum.tadpole.mongodb.model.Table#getOutgoingLinks <em>Outgoing Links</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(Table)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getRelation_Source()
	 * @see com.hangum.tadpole.mongodb.model.Table#getOutgoingLinks
	 * @model opposite="outgoingLinks"
	 * @generated
	 */
	Table getSource();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.Relation#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(Table value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.hangum.tadpole.mongodb.model.Table#getIncomingLinks <em>Incoming Links</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(Table)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getRelation_Target()
	 * @see com.hangum.tadpole.mongodb.model.Table#getIncomingLinks
	 * @model opposite="incomingLinks"
	 * @generated
	 */
	Table getTarget();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.Relation#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(Table value);

	/**
	 * Returns the value of the '<em><b>Source kind</b></em>' attribute.
	 * The literals are from the enumeration {@link com.hangum.tadpole.mongodb.model.RelationKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source kind</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source kind</em>' attribute.
	 * @see com.hangum.tadpole.mongodb.model.RelationKind
	 * @see #setSource_kind(RelationKind)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getRelation_Source_kind()
	 * @model
	 * @generated
	 */
	RelationKind getSource_kind();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.Relation#getSource_kind <em>Source kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source kind</em>' attribute.
	 * @see com.hangum.tadpole.mongodb.model.RelationKind
	 * @see #getSource_kind()
	 * @generated
	 */
	void setSource_kind(RelationKind value);

	/**
	 * Returns the value of the '<em><b>Target kind</b></em>' attribute.
	 * The literals are from the enumeration {@link com.hangum.tadpole.mongodb.model.RelationKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target kind</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target kind</em>' attribute.
	 * @see com.hangum.tadpole.mongodb.model.RelationKind
	 * @see #setTarget_kind(RelationKind)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getRelation_Target_kind()
	 * @model
	 * @generated
	 */
	RelationKind getTarget_kind();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.Relation#getTarget_kind <em>Target kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target kind</em>' attribute.
	 * @see com.hangum.tadpole.mongodb.model.RelationKind
	 * @see #getTarget_kind()
	 * @generated
	 */
	void setTarget_kind(RelationKind value);

	/**
	 * Returns the value of the '<em><b>Column name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Column name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Column name</em>' attribute.
	 * @see #setColumn_name(String)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getRelation_Column_name()
	 * @model
	 * @generated
	 */
	String getColumn_name();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.Relation#getColumn_name <em>Column name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Column name</em>' attribute.
	 * @see #getColumn_name()
	 * @generated
	 */
	void setColumn_name(String value);

	/**
	 * Returns the value of the '<em><b>Referenced column name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Referenced column name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Referenced column name</em>' attribute.
	 * @see #setReferenced_column_name(String)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getRelation_Referenced_column_name()
	 * @model
	 * @generated
	 */
	String getReferenced_column_name();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.Relation#getReferenced_column_name <em>Referenced column name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referenced column name</em>' attribute.
	 * @see #getReferenced_column_name()
	 * @generated
	 */
	void setReferenced_column_name(String value);

	/**
	 * Returns the value of the '<em><b>Bendpoint</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.draw2d.geometry.Point}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bendpoint</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bendpoint</em>' attribute list.
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getRelation_Bendpoint()
	 * @model dataType="com.hangum.tadpole.mongodb.model.Point"
	 * @generated
	 */
	EList<Point> getBendpoint();

	/**
	 * Returns the value of the '<em><b>Db</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.hangum.tadpole.mongodb.model.DB#getReferences <em>References</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Db</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Db</em>' container reference.
	 * @see #setDb(DB)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getRelation_Db()
	 * @see com.hangum.tadpole.mongodb.model.DB#getReferences
	 * @model opposite="references" required="true" transient="false"
	 * @generated
	 */
	DB getDb();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.Relation#getDb <em>Db</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Db</em>' container reference.
	 * @see #getDb()
	 * @generated
	 */
	void setDb(DB value);

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
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getRelation_Comment()
	 * @model
	 * @generated
	 */
	String getComment();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.Relation#getComment <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Comment</em>' attribute.
	 * @see #getComment()
	 * @generated
	 */
	void setComment(String value);

	/**
	 * Returns the value of the '<em><b>Constraint name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraint name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraint name</em>' attribute.
	 * @see #setConstraint_name(String)
	 * @see com.hangum.tadpole.mongodb.model.MongodbPackage#getRelation_Constraint_name()
	 * @model
	 * @generated
	 */
	String getConstraint_name();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.mongodb.model.Relation#getConstraint_name <em>Constraint name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constraint name</em>' attribute.
	 * @see #getConstraint_name()
	 * @generated
	 */
	void setConstraint_name(String value);

} // Relation
