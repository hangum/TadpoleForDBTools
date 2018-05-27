/**
 */
package com.hangum.tadpole.rdb.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Style</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.hangum.tadpole.rdb.model.Style#getScale <em>Scale</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Style#getTableTitle <em>Table Title</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Style#getColumnPrimaryKey <em>Column Primary Key</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Style#getColumnName <em>Column Name</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Style#getColumnComment <em>Column Comment</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Style#getColumnType <em>Column Type</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Style#getColumnNullCheck <em>Column Null Check</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Style#getGrid <em>Grid</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.Style#getDb <em>Db</em>}</li>
 * </ul>
 *
 * @see com.hangum.tadpole.rdb.model.RdbPackage#getStyle()
 * @model
 * @generated
 */
public interface Style extends EObject {
	/**
	 * Returns the value of the '<em><b>Scale</b></em>' attribute.
	 * The default value is <code>"100"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scale</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scale</em>' attribute.
	 * @see #setScale(Double)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getStyle_Scale()
	 * @model default="100"
	 * @generated
	 */
	Double getScale();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Style#getScale <em>Scale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scale</em>' attribute.
	 * @see #getScale()
	 * @generated
	 */
	void setScale(Double value);

	/**
	 * Returns the value of the '<em><b>Table Title</b></em>' attribute.
	 * The default value is <code>"nameComment"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Table Title</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Table Title</em>' attribute.
	 * @see #setTableTitle(String)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getStyle_TableTitle()
	 * @model default="nameComment"
	 * @generated
	 */
	String getTableTitle();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Style#getTableTitle <em>Table Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Table Title</em>' attribute.
	 * @see #getTableTitle()
	 * @generated
	 */
	void setTableTitle(String value);

	/**
	 * Returns the value of the '<em><b>Column Primary Key</b></em>' attribute.
	 * The default value is <code>"YES"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Column Primary Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Column Primary Key</em>' attribute.
	 * @see #setColumnPrimaryKey(String)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getStyle_ColumnPrimaryKey()
	 * @model default="YES"
	 * @generated
	 */
	String getColumnPrimaryKey();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Style#getColumnPrimaryKey <em>Column Primary Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Column Primary Key</em>' attribute.
	 * @see #getColumnPrimaryKey()
	 * @generated
	 */
	void setColumnPrimaryKey(String value);

	/**
	 * Returns the value of the '<em><b>Column Name</b></em>' attribute.
	 * The default value is <code>"YES"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Column Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Column Name</em>' attribute.
	 * @see #setColumnName(String)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getStyle_ColumnName()
	 * @model default="YES"
	 * @generated
	 */
	String getColumnName();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Style#getColumnName <em>Column Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Column Name</em>' attribute.
	 * @see #getColumnName()
	 * @generated
	 */
	void setColumnName(String value);

	/**
	 * Returns the value of the '<em><b>Column Comment</b></em>' attribute.
	 * The default value is <code>"YES"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Column Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Column Comment</em>' attribute.
	 * @see #setColumnComment(String)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getStyle_ColumnComment()
	 * @model default="YES"
	 * @generated
	 */
	String getColumnComment();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Style#getColumnComment <em>Column Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Column Comment</em>' attribute.
	 * @see #getColumnComment()
	 * @generated
	 */
	void setColumnComment(String value);

	/**
	 * Returns the value of the '<em><b>Column Type</b></em>' attribute.
	 * The default value is <code>"YES"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Column Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Column Type</em>' attribute.
	 * @see #setColumnType(String)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getStyle_ColumnType()
	 * @model default="YES"
	 * @generated
	 */
	String getColumnType();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Style#getColumnType <em>Column Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Column Type</em>' attribute.
	 * @see #getColumnType()
	 * @generated
	 */
	void setColumnType(String value);

	/**
	 * Returns the value of the '<em><b>Column Null Check</b></em>' attribute.
	 * The default value is <code>"YES"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Column Null Check</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Column Null Check</em>' attribute.
	 * @see #setColumnNullCheck(String)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getStyle_ColumnNullCheck()
	 * @model default="YES"
	 * @generated
	 */
	String getColumnNullCheck();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Style#getColumnNullCheck <em>Column Null Check</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Column Null Check</em>' attribute.
	 * @see #getColumnNullCheck()
	 * @generated
	 */
	void setColumnNullCheck(String value);

	/**
	 * Returns the value of the '<em><b>Grid</b></em>' attribute.
	 * The default value is <code>"YES"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Grid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Grid</em>' attribute.
	 * @see #setGrid(String)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getStyle_Grid()
	 * @model default="YES"
	 * @generated
	 */
	String getGrid();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Style#getGrid <em>Grid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Grid</em>' attribute.
	 * @see #getGrid()
	 * @generated
	 */
	void setGrid(String value);

	/**
	 * Returns the value of the '<em><b>Db</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.hangum.tadpole.rdb.model.DB#getStyledReference <em>Styled Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Db</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Db</em>' container reference.
	 * @see #setDb(DB)
	 * @see com.hangum.tadpole.rdb.model.RdbPackage#getStyle_Db()
	 * @see com.hangum.tadpole.rdb.model.DB#getStyledReference
	 * @model opposite="styledReference" required="true" transient="false"
	 * @generated
	 */
	DB getDb();

	/**
	 * Sets the value of the '{@link com.hangum.tadpole.rdb.model.Style#getDb <em>Db</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Db</em>' container reference.
	 * @see #getDb()
	 * @generated
	 */
	void setDb(DB value);

} // Style
