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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.hangum.tadpole.mongodb.model.MongodbFactory
 * @model kind="package"
 * @generated
 */
public interface MongodbPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "mongodb";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://com.hangum.tadpole.mongodb.model.ERDInfo";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "mongodb";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MongodbPackage eINSTANCE = com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.hangum.tadpole.mongodb.model.impl.ERDInfoImpl <em>ERD Info</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.hangum.tadpole.mongodb.model.impl.ERDInfoImpl
	 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getERDInfo()
	 * @generated
	 */
	int ERD_INFO = 5;

	/**
	 * The feature id for the '<em><b>Auto Layout</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERD_INFO__AUTO_LAYOUT = 0;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERD_INFO__VERSION = 1;

	/**
	 * The feature id for the '<em><b>Auto Layout type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERD_INFO__AUTO_LAYOUT_TYPE = 2;

	/**
	 * The number of structural features of the '<em>ERD Info</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERD_INFO_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link com.hangum.tadpole.mongodb.model.impl.DBImpl <em>DB</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.hangum.tadpole.mongodb.model.impl.DBImpl
	 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getDB()
	 * @generated
	 */
	int DB = 0;

	/**
	 * The feature id for the '<em><b>Auto Layout</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB__AUTO_LAYOUT = ERD_INFO__AUTO_LAYOUT;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB__VERSION = ERD_INFO__VERSION;

	/**
	 * The feature id for the '<em><b>Auto Layout type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB__AUTO_LAYOUT_TYPE = ERD_INFO__AUTO_LAYOUT_TYPE;

	/**
	 * The feature id for the '<em><b>Db Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB__DB_TYPE = ERD_INFO_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Tables</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB__TABLES = ERD_INFO_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB__KEY = ERD_INFO_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB__URL = ERD_INFO_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB__ID = ERD_INFO_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Sid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB__SID = ERD_INFO_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB__REFERENCES = ERD_INFO_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB__COMMENT = ERD_INFO_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>DB</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DB_FEATURE_COUNT = ERD_INFO_FEATURE_COUNT + 8;

	/**
	 * The meta object id for the '{@link com.hangum.tadpole.mongodb.model.impl.TableImpl <em>Table</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.hangum.tadpole.mongodb.model.impl.TableImpl
	 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getTable()
	 * @generated
	 */
	int TABLE = 1;

	/**
	 * The feature id for the '<em><b>Columns</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__COLUMNS = 0;

	/**
	 * The feature id for the '<em><b>Db</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__DB = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__NAME = 2;

	/**
	 * The feature id for the '<em><b>Constraints</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__CONSTRAINTS = 3;

	/**
	 * The feature id for the '<em><b>Incoming Links</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__INCOMING_LINKS = 4;

	/**
	 * The feature id for the '<em><b>Outgoing Links</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__OUTGOING_LINKS = 5;

	/**
	 * The feature id for the '<em><b>Logical Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__LOGICAL_NAME = 6;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__COMMENT = 7;

	/**
	 * The feature id for the '<em><b>User Comment Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__USER_COMMENT_REFERENCE = 8;

	/**
	 * The number of structural features of the '<em>Table</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE_FEATURE_COUNT = 9;

	/**
	 * The meta object id for the '{@link com.hangum.tadpole.mongodb.model.impl.ColumnImpl <em>Column</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.hangum.tadpole.mongodb.model.impl.ColumnImpl
	 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getColumn()
	 * @generated
	 */
	int COLUMN = 2;

	/**
	 * The feature id for the '<em><b>Field</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__FIELD = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__TYPE = 1;

	/**
	 * The feature id for the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__DEFAULT = 2;

	/**
	 * The feature id for the '<em><b>Table</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__TABLE = 3;

	/**
	 * The feature id for the '<em><b>Logical Field</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__LOGICAL_FIELD = 4;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__KEY = 5;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__COMMENT = 6;

	/**
	 * The feature id for the '<em><b>Sub Doc</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__SUB_DOC = 7;

	/**
	 * The number of structural features of the '<em>Column</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN_FEATURE_COUNT = 8;

	/**
	 * The meta object id for the '{@link com.hangum.tadpole.mongodb.model.impl.RelationImpl <em>Relation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.hangum.tadpole.mongodb.model.impl.RelationImpl
	 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getRelation()
	 * @generated
	 */
	int RELATION = 3;

	/**
	 * The feature id for the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELATION__SOURCE = 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELATION__TARGET = 1;

	/**
	 * The feature id for the '<em><b>Source kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELATION__SOURCE_KIND = 2;

	/**
	 * The feature id for the '<em><b>Target kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELATION__TARGET_KIND = 3;

	/**
	 * The feature id for the '<em><b>Column name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELATION__COLUMN_NAME = 4;

	/**
	 * The feature id for the '<em><b>Referenced column name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELATION__REFERENCED_COLUMN_NAME = 5;

	/**
	 * The feature id for the '<em><b>Bendpoint</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELATION__BENDPOINT = 6;

	/**
	 * The feature id for the '<em><b>Db</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELATION__DB = 7;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELATION__COMMENT = 8;

	/**
	 * The feature id for the '<em><b>Constraint name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELATION__CONSTRAINT_NAME = 9;

	/**
	 * The number of structural features of the '<em>Relation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELATION_FEATURE_COUNT = 10;

	/**
	 * The meta object id for the '{@link com.hangum.tadpole.mongodb.model.impl.ViewImpl <em>View</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.hangum.tadpole.mongodb.model.impl.ViewImpl
	 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getView()
	 * @generated
	 */
	int VIEW = 4;

	/**
	 * The feature id for the '<em><b>Columns</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__COLUMNS = TABLE__COLUMNS;

	/**
	 * The feature id for the '<em><b>Db</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__DB = TABLE__DB;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__NAME = TABLE__NAME;

	/**
	 * The feature id for the '<em><b>Constraints</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__CONSTRAINTS = TABLE__CONSTRAINTS;

	/**
	 * The feature id for the '<em><b>Incoming Links</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__INCOMING_LINKS = TABLE__INCOMING_LINKS;

	/**
	 * The feature id for the '<em><b>Outgoing Links</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__OUTGOING_LINKS = TABLE__OUTGOING_LINKS;

	/**
	 * The feature id for the '<em><b>Logical Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__LOGICAL_NAME = TABLE__LOGICAL_NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__COMMENT = TABLE__COMMENT;

	/**
	 * The feature id for the '<em><b>User Comment Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__USER_COMMENT_REFERENCE = TABLE__USER_COMMENT_REFERENCE;

	/**
	 * The feature id for the '<em><b>Table Name</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW__TABLE_NAME = TABLE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>View</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_FEATURE_COUNT = TABLE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.hangum.tadpole.mongodb.model.impl.UserCommentImpl <em>User Comment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.hangum.tadpole.mongodb.model.impl.UserCommentImpl
	 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getUserComment()
	 * @generated
	 */
	int USER_COMMENT = 6;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_COMMENT__COMMENT = 0;

	/**
	 * The number of structural features of the '<em>User Comment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_COMMENT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.hangum.tadpole.mongodb.model.RelationKind <em>Relation Kind</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.hangum.tadpole.mongodb.model.RelationKind
	 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getRelationKind()
	 * @generated
	 */
	int RELATION_KIND = 7;

	/**
	 * The meta object id for the '<em>Rectangle</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.draw2d.geometry.Rectangle
	 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getRectangle()
	 * @generated
	 */
	int RECTANGLE = 8;

	/**
	 * The meta object id for the '<em>Point</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.draw2d.geometry.Point
	 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getPoint()
	 * @generated
	 */
	int POINT = 9;


	/**
	 * Returns the meta object for class '{@link com.hangum.tadpole.mongodb.model.DB <em>DB</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>DB</em>'.
	 * @see com.hangum.tadpole.mongodb.model.DB
	 * @generated
	 */
	EClass getDB();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.DB#getDbType <em>Db Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Db Type</em>'.
	 * @see com.hangum.tadpole.mongodb.model.DB#getDbType()
	 * @see #getDB()
	 * @generated
	 */
	EAttribute getDB_DbType();

	/**
	 * Returns the meta object for the containment reference list '{@link com.hangum.tadpole.mongodb.model.DB#getTables <em>Tables</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Tables</em>'.
	 * @see com.hangum.tadpole.mongodb.model.DB#getTables()
	 * @see #getDB()
	 * @generated
	 */
	EReference getDB_Tables();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.DB#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see com.hangum.tadpole.mongodb.model.DB#getKey()
	 * @see #getDB()
	 * @generated
	 */
	EAttribute getDB_Key();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.DB#getUrl <em>Url</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Url</em>'.
	 * @see com.hangum.tadpole.mongodb.model.DB#getUrl()
	 * @see #getDB()
	 * @generated
	 */
	EAttribute getDB_Url();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.DB#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see com.hangum.tadpole.mongodb.model.DB#getId()
	 * @see #getDB()
	 * @generated
	 */
	EAttribute getDB_Id();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.DB#getSid <em>Sid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sid</em>'.
	 * @see com.hangum.tadpole.mongodb.model.DB#getSid()
	 * @see #getDB()
	 * @generated
	 */
	EAttribute getDB_Sid();

	/**
	 * Returns the meta object for the containment reference list '{@link com.hangum.tadpole.mongodb.model.DB#getReferences <em>References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>References</em>'.
	 * @see com.hangum.tadpole.mongodb.model.DB#getReferences()
	 * @see #getDB()
	 * @generated
	 */
	EReference getDB_References();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.DB#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see com.hangum.tadpole.mongodb.model.DB#getComment()
	 * @see #getDB()
	 * @generated
	 */
	EAttribute getDB_Comment();

	/**
	 * Returns the meta object for class '{@link com.hangum.tadpole.mongodb.model.Table <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Table</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Table
	 * @generated
	 */
	EClass getTable();

	/**
	 * Returns the meta object for the containment reference list '{@link com.hangum.tadpole.mongodb.model.Table#getColumns <em>Columns</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Columns</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Table#getColumns()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_Columns();

	/**
	 * Returns the meta object for the container reference '{@link com.hangum.tadpole.mongodb.model.Table#getDb <em>Db</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Db</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Table#getDb()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_Db();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Table#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Table#getName()
	 * @see #getTable()
	 * @generated
	 */
	EAttribute getTable_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Table#getConstraints <em>Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Constraints</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Table#getConstraints()
	 * @see #getTable()
	 * @generated
	 */
	EAttribute getTable_Constraints();

	/**
	 * Returns the meta object for the reference list '{@link com.hangum.tadpole.mongodb.model.Table#getIncomingLinks <em>Incoming Links</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Incoming Links</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Table#getIncomingLinks()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_IncomingLinks();

	/**
	 * Returns the meta object for the reference list '{@link com.hangum.tadpole.mongodb.model.Table#getOutgoingLinks <em>Outgoing Links</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Outgoing Links</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Table#getOutgoingLinks()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_OutgoingLinks();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Table#getLogicalName <em>Logical Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Logical Name</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Table#getLogicalName()
	 * @see #getTable()
	 * @generated
	 */
	EAttribute getTable_LogicalName();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Table#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Table#getComment()
	 * @see #getTable()
	 * @generated
	 */
	EAttribute getTable_Comment();

	/**
	 * Returns the meta object for the reference '{@link com.hangum.tadpole.mongodb.model.Table#getUserCommentReference <em>User Comment Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>User Comment Reference</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Table#getUserCommentReference()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_UserCommentReference();

	/**
	 * Returns the meta object for class '{@link com.hangum.tadpole.mongodb.model.Column <em>Column</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Column</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Column
	 * @generated
	 */
	EClass getColumn();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Column#getField <em>Field</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Field</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Column#getField()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_Field();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Column#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Column#getType()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_Type();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Column#getDefault <em>Default</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Column#getDefault()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_Default();

	/**
	 * Returns the meta object for the container reference '{@link com.hangum.tadpole.mongodb.model.Column#getTable <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Table</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Column#getTable()
	 * @see #getColumn()
	 * @generated
	 */
	EReference getColumn_Table();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Column#getLogicalField <em>Logical Field</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Logical Field</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Column#getLogicalField()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_LogicalField();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Column#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Column#getKey()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_Key();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Column#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Column#getComment()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_Comment();

	/**
	 * Returns the meta object for the containment reference list '{@link com.hangum.tadpole.mongodb.model.Column#getSubDoc <em>Sub Doc</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sub Doc</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Column#getSubDoc()
	 * @see #getColumn()
	 * @generated
	 */
	EReference getColumn_SubDoc();

	/**
	 * Returns the meta object for class '{@link com.hangum.tadpole.mongodb.model.Relation <em>Relation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Relation</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Relation
	 * @generated
	 */
	EClass getRelation();

	/**
	 * Returns the meta object for the reference '{@link com.hangum.tadpole.mongodb.model.Relation#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Relation#getSource()
	 * @see #getRelation()
	 * @generated
	 */
	EReference getRelation_Source();

	/**
	 * Returns the meta object for the reference '{@link com.hangum.tadpole.mongodb.model.Relation#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Relation#getTarget()
	 * @see #getRelation()
	 * @generated
	 */
	EReference getRelation_Target();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Relation#getSource_kind <em>Source kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source kind</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Relation#getSource_kind()
	 * @see #getRelation()
	 * @generated
	 */
	EAttribute getRelation_Source_kind();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Relation#getTarget_kind <em>Target kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target kind</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Relation#getTarget_kind()
	 * @see #getRelation()
	 * @generated
	 */
	EAttribute getRelation_Target_kind();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Relation#getColumn_name <em>Column name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Column name</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Relation#getColumn_name()
	 * @see #getRelation()
	 * @generated
	 */
	EAttribute getRelation_Column_name();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Relation#getReferenced_column_name <em>Referenced column name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Referenced column name</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Relation#getReferenced_column_name()
	 * @see #getRelation()
	 * @generated
	 */
	EAttribute getRelation_Referenced_column_name();

	/**
	 * Returns the meta object for the attribute list '{@link com.hangum.tadpole.mongodb.model.Relation#getBendpoint <em>Bendpoint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Bendpoint</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Relation#getBendpoint()
	 * @see #getRelation()
	 * @generated
	 */
	EAttribute getRelation_Bendpoint();

	/**
	 * Returns the meta object for the container reference '{@link com.hangum.tadpole.mongodb.model.Relation#getDb <em>Db</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Db</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Relation#getDb()
	 * @see #getRelation()
	 * @generated
	 */
	EReference getRelation_Db();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Relation#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Relation#getComment()
	 * @see #getRelation()
	 * @generated
	 */
	EAttribute getRelation_Comment();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.Relation#getConstraint_name <em>Constraint name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Constraint name</em>'.
	 * @see com.hangum.tadpole.mongodb.model.Relation#getConstraint_name()
	 * @see #getRelation()
	 * @generated
	 */
	EAttribute getRelation_Constraint_name();

	/**
	 * Returns the meta object for class '{@link com.hangum.tadpole.mongodb.model.View <em>View</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>View</em>'.
	 * @see com.hangum.tadpole.mongodb.model.View
	 * @generated
	 */
	EClass getView();

	/**
	 * Returns the meta object for the reference '{@link com.hangum.tadpole.mongodb.model.View#getTableName <em>Table Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Table Name</em>'.
	 * @see com.hangum.tadpole.mongodb.model.View#getTableName()
	 * @see #getView()
	 * @generated
	 */
	EReference getView_TableName();

	/**
	 * Returns the meta object for class '{@link com.hangum.tadpole.mongodb.model.ERDInfo <em>ERD Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ERD Info</em>'.
	 * @see com.hangum.tadpole.mongodb.model.ERDInfo
	 * @generated
	 */
	EClass getERDInfo();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.ERDInfo#isAutoLayout <em>Auto Layout</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Auto Layout</em>'.
	 * @see com.hangum.tadpole.mongodb.model.ERDInfo#isAutoLayout()
	 * @see #getERDInfo()
	 * @generated
	 */
	EAttribute getERDInfo_AutoLayout();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.ERDInfo#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see com.hangum.tadpole.mongodb.model.ERDInfo#getVersion()
	 * @see #getERDInfo()
	 * @generated
	 */
	EAttribute getERDInfo_Version();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.ERDInfo#getAutoLayout_type <em>Auto Layout type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Auto Layout type</em>'.
	 * @see com.hangum.tadpole.mongodb.model.ERDInfo#getAutoLayout_type()
	 * @see #getERDInfo()
	 * @generated
	 */
	EAttribute getERDInfo_AutoLayout_type();

	/**
	 * Returns the meta object for class '{@link com.hangum.tadpole.mongodb.model.UserComment <em>User Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>User Comment</em>'.
	 * @see com.hangum.tadpole.mongodb.model.UserComment
	 * @generated
	 */
	EClass getUserComment();

	/**
	 * Returns the meta object for the attribute '{@link com.hangum.tadpole.mongodb.model.UserComment#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see com.hangum.tadpole.mongodb.model.UserComment#getComment()
	 * @see #getUserComment()
	 * @generated
	 */
	EAttribute getUserComment_Comment();

	/**
	 * Returns the meta object for enum '{@link com.hangum.tadpole.mongodb.model.RelationKind <em>Relation Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Relation Kind</em>'.
	 * @see com.hangum.tadpole.mongodb.model.RelationKind
	 * @generated
	 */
	EEnum getRelationKind();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.draw2d.geometry.Rectangle <em>Rectangle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Rectangle</em>'.
	 * @see org.eclipse.draw2d.geometry.Rectangle
	 * @model instanceClass="org.eclipse.draw2d.geometry.Rectangle"
	 * @generated
	 */
	EDataType getRectangle();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.draw2d.geometry.Point <em>Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Point</em>'.
	 * @see org.eclipse.draw2d.geometry.Point
	 * @model instanceClass="org.eclipse.draw2d.geometry.Point"
	 * @generated
	 */
	EDataType getPoint();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MongodbFactory getMongodbFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.hangum.tadpole.mongodb.model.impl.DBImpl <em>DB</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.hangum.tadpole.mongodb.model.impl.DBImpl
		 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getDB()
		 * @generated
		 */
		EClass DB = eINSTANCE.getDB();

		/**
		 * The meta object literal for the '<em><b>Db Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DB__DB_TYPE = eINSTANCE.getDB_DbType();

		/**
		 * The meta object literal for the '<em><b>Tables</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DB__TABLES = eINSTANCE.getDB_Tables();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DB__KEY = eINSTANCE.getDB_Key();

		/**
		 * The meta object literal for the '<em><b>Url</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DB__URL = eINSTANCE.getDB_Url();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DB__ID = eINSTANCE.getDB_Id();

		/**
		 * The meta object literal for the '<em><b>Sid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DB__SID = eINSTANCE.getDB_Sid();

		/**
		 * The meta object literal for the '<em><b>References</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DB__REFERENCES = eINSTANCE.getDB_References();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DB__COMMENT = eINSTANCE.getDB_Comment();

		/**
		 * The meta object literal for the '{@link com.hangum.tadpole.mongodb.model.impl.TableImpl <em>Table</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.hangum.tadpole.mongodb.model.impl.TableImpl
		 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getTable()
		 * @generated
		 */
		EClass TABLE = eINSTANCE.getTable();

		/**
		 * The meta object literal for the '<em><b>Columns</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__COLUMNS = eINSTANCE.getTable_Columns();

		/**
		 * The meta object literal for the '<em><b>Db</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__DB = eINSTANCE.getTable_Db();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TABLE__NAME = eINSTANCE.getTable_Name();

		/**
		 * The meta object literal for the '<em><b>Constraints</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TABLE__CONSTRAINTS = eINSTANCE.getTable_Constraints();

		/**
		 * The meta object literal for the '<em><b>Incoming Links</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__INCOMING_LINKS = eINSTANCE.getTable_IncomingLinks();

		/**
		 * The meta object literal for the '<em><b>Outgoing Links</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__OUTGOING_LINKS = eINSTANCE.getTable_OutgoingLinks();

		/**
		 * The meta object literal for the '<em><b>Logical Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TABLE__LOGICAL_NAME = eINSTANCE.getTable_LogicalName();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TABLE__COMMENT = eINSTANCE.getTable_Comment();

		/**
		 * The meta object literal for the '<em><b>User Comment Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__USER_COMMENT_REFERENCE = eINSTANCE.getTable_UserCommentReference();

		/**
		 * The meta object literal for the '{@link com.hangum.tadpole.mongodb.model.impl.ColumnImpl <em>Column</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.hangum.tadpole.mongodb.model.impl.ColumnImpl
		 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getColumn()
		 * @generated
		 */
		EClass COLUMN = eINSTANCE.getColumn();

		/**
		 * The meta object literal for the '<em><b>Field</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__FIELD = eINSTANCE.getColumn_Field();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__TYPE = eINSTANCE.getColumn_Type();

		/**
		 * The meta object literal for the '<em><b>Default</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__DEFAULT = eINSTANCE.getColumn_Default();

		/**
		 * The meta object literal for the '<em><b>Table</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COLUMN__TABLE = eINSTANCE.getColumn_Table();

		/**
		 * The meta object literal for the '<em><b>Logical Field</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__LOGICAL_FIELD = eINSTANCE.getColumn_LogicalField();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__KEY = eINSTANCE.getColumn_Key();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__COMMENT = eINSTANCE.getColumn_Comment();

		/**
		 * The meta object literal for the '<em><b>Sub Doc</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COLUMN__SUB_DOC = eINSTANCE.getColumn_SubDoc();

		/**
		 * The meta object literal for the '{@link com.hangum.tadpole.mongodb.model.impl.RelationImpl <em>Relation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.hangum.tadpole.mongodb.model.impl.RelationImpl
		 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getRelation()
		 * @generated
		 */
		EClass RELATION = eINSTANCE.getRelation();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RELATION__SOURCE = eINSTANCE.getRelation_Source();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RELATION__TARGET = eINSTANCE.getRelation_Target();

		/**
		 * The meta object literal for the '<em><b>Source kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RELATION__SOURCE_KIND = eINSTANCE.getRelation_Source_kind();

		/**
		 * The meta object literal for the '<em><b>Target kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RELATION__TARGET_KIND = eINSTANCE.getRelation_Target_kind();

		/**
		 * The meta object literal for the '<em><b>Column name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RELATION__COLUMN_NAME = eINSTANCE.getRelation_Column_name();

		/**
		 * The meta object literal for the '<em><b>Referenced column name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RELATION__REFERENCED_COLUMN_NAME = eINSTANCE.getRelation_Referenced_column_name();

		/**
		 * The meta object literal for the '<em><b>Bendpoint</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RELATION__BENDPOINT = eINSTANCE.getRelation_Bendpoint();

		/**
		 * The meta object literal for the '<em><b>Db</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RELATION__DB = eINSTANCE.getRelation_Db();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RELATION__COMMENT = eINSTANCE.getRelation_Comment();

		/**
		 * The meta object literal for the '<em><b>Constraint name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RELATION__CONSTRAINT_NAME = eINSTANCE.getRelation_Constraint_name();

		/**
		 * The meta object literal for the '{@link com.hangum.tadpole.mongodb.model.impl.ViewImpl <em>View</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.hangum.tadpole.mongodb.model.impl.ViewImpl
		 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getView()
		 * @generated
		 */
		EClass VIEW = eINSTANCE.getView();

		/**
		 * The meta object literal for the '<em><b>Table Name</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIEW__TABLE_NAME = eINSTANCE.getView_TableName();

		/**
		 * The meta object literal for the '{@link com.hangum.tadpole.mongodb.model.impl.ERDInfoImpl <em>ERD Info</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.hangum.tadpole.mongodb.model.impl.ERDInfoImpl
		 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getERDInfo()
		 * @generated
		 */
		EClass ERD_INFO = eINSTANCE.getERDInfo();

		/**
		 * The meta object literal for the '<em><b>Auto Layout</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ERD_INFO__AUTO_LAYOUT = eINSTANCE.getERDInfo_AutoLayout();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ERD_INFO__VERSION = eINSTANCE.getERDInfo_Version();

		/**
		 * The meta object literal for the '<em><b>Auto Layout type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ERD_INFO__AUTO_LAYOUT_TYPE = eINSTANCE.getERDInfo_AutoLayout_type();

		/**
		 * The meta object literal for the '{@link com.hangum.tadpole.mongodb.model.impl.UserCommentImpl <em>User Comment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.hangum.tadpole.mongodb.model.impl.UserCommentImpl
		 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getUserComment()
		 * @generated
		 */
		EClass USER_COMMENT = eINSTANCE.getUserComment();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_COMMENT__COMMENT = eINSTANCE.getUserComment_Comment();

		/**
		 * The meta object literal for the '{@link com.hangum.tadpole.mongodb.model.RelationKind <em>Relation Kind</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.hangum.tadpole.mongodb.model.RelationKind
		 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getRelationKind()
		 * @generated
		 */
		EEnum RELATION_KIND = eINSTANCE.getRelationKind();

		/**
		 * The meta object literal for the '<em>Rectangle</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.draw2d.geometry.Rectangle
		 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getRectangle()
		 * @generated
		 */
		EDataType RECTANGLE = eINSTANCE.getRectangle();

		/**
		 * The meta object literal for the '<em>Point</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.draw2d.geometry.Point
		 * @see com.hangum.tadpole.mongodb.model.impl.MongodbPackageImpl#getPoint()
		 * @generated
		 */
		EDataType POINT = eINSTANCE.getPoint();

	}

} //MongodbPackage
