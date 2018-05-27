/**
 */
package com.hangum.tadpole.rdb.model.impl;

import com.hangum.tadpole.rdb.model.DB;
import com.hangum.tadpole.rdb.model.RdbPackage;
import com.hangum.tadpole.rdb.model.Style;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Style</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.StyleImpl#getScale <em>Scale</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.StyleImpl#getTableTitle <em>Table Title</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.StyleImpl#getColumnPrimaryKey <em>Column Primary Key</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.StyleImpl#getColumnName <em>Column Name</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.StyleImpl#getColumnComment <em>Column Comment</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.StyleImpl#getColumnType <em>Column Type</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.StyleImpl#getColumnNullCheck <em>Column Null Check</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.StyleImpl#getGrid <em>Grid</em>}</li>
 *   <li>{@link com.hangum.tadpole.rdb.model.impl.StyleImpl#getDb <em>Db</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StyleImpl extends EObjectImpl implements Style {
	/**
	 * The default value of the '{@link #getScale() <em>Scale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScale()
	 * @generated
	 * @ordered
	 */
	protected static final Double SCALE_EDEFAULT = new Double(100.0);

	/**
	 * The cached value of the '{@link #getScale() <em>Scale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScale()
	 * @generated
	 * @ordered
	 */
	protected Double scale = SCALE_EDEFAULT;

	/**
	 * The default value of the '{@link #getTableTitle() <em>Table Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTableTitle()
	 * @generated
	 * @ordered
	 */
	protected static final String TABLE_TITLE_EDEFAULT = "nameComment";

	/**
	 * The cached value of the '{@link #getTableTitle() <em>Table Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTableTitle()
	 * @generated
	 * @ordered
	 */
	protected String tableTitle = TABLE_TITLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getColumnPrimaryKey() <em>Column Primary Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumnPrimaryKey()
	 * @generated
	 * @ordered
	 */
	protected static final String COLUMN_PRIMARY_KEY_EDEFAULT = "YES";

	/**
	 * The cached value of the '{@link #getColumnPrimaryKey() <em>Column Primary Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumnPrimaryKey()
	 * @generated
	 * @ordered
	 */
	protected String columnPrimaryKey = COLUMN_PRIMARY_KEY_EDEFAULT;

	/**
	 * The default value of the '{@link #getColumnName() <em>Column Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumnName()
	 * @generated
	 * @ordered
	 */
	protected static final String COLUMN_NAME_EDEFAULT = "YES";

	/**
	 * The cached value of the '{@link #getColumnName() <em>Column Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumnName()
	 * @generated
	 * @ordered
	 */
	protected String columnName = COLUMN_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getColumnComment() <em>Column Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumnComment()
	 * @generated
	 * @ordered
	 */
	protected static final String COLUMN_COMMENT_EDEFAULT = "YES";

	/**
	 * The cached value of the '{@link #getColumnComment() <em>Column Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumnComment()
	 * @generated
	 * @ordered
	 */
	protected String columnComment = COLUMN_COMMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getColumnType() <em>Column Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumnType()
	 * @generated
	 * @ordered
	 */
	protected static final String COLUMN_TYPE_EDEFAULT = "YES";

	/**
	 * The cached value of the '{@link #getColumnType() <em>Column Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumnType()
	 * @generated
	 * @ordered
	 */
	protected String columnType = COLUMN_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getColumnNullCheck() <em>Column Null Check</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumnNullCheck()
	 * @generated
	 * @ordered
	 */
	protected static final String COLUMN_NULL_CHECK_EDEFAULT = "YES";

	/**
	 * The cached value of the '{@link #getColumnNullCheck() <em>Column Null Check</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumnNullCheck()
	 * @generated
	 * @ordered
	 */
	protected String columnNullCheck = COLUMN_NULL_CHECK_EDEFAULT;

	/**
	 * The default value of the '{@link #getGrid() <em>Grid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGrid()
	 * @generated
	 * @ordered
	 */
	protected static final String GRID_EDEFAULT = "YES";

	/**
	 * The cached value of the '{@link #getGrid() <em>Grid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGrid()
	 * @generated
	 * @ordered
	 */
	protected String grid = GRID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StyleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RdbPackage.Literals.STYLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Double getScale() {
		return scale;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScale(Double newScale) {
		Double oldScale = scale;
		scale = newScale;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.STYLE__SCALE, oldScale, scale));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTableTitle() {
		return tableTitle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTableTitle(String newTableTitle) {
		String oldTableTitle = tableTitle;
		tableTitle = newTableTitle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.STYLE__TABLE_TITLE, oldTableTitle, tableTitle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getColumnPrimaryKey() {
		return columnPrimaryKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setColumnPrimaryKey(String newColumnPrimaryKey) {
		String oldColumnPrimaryKey = columnPrimaryKey;
		columnPrimaryKey = newColumnPrimaryKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.STYLE__COLUMN_PRIMARY_KEY, oldColumnPrimaryKey, columnPrimaryKey));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setColumnName(String newColumnName) {
		String oldColumnName = columnName;
		columnName = newColumnName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.STYLE__COLUMN_NAME, oldColumnName, columnName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getColumnComment() {
		return columnComment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setColumnComment(String newColumnComment) {
		String oldColumnComment = columnComment;
		columnComment = newColumnComment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.STYLE__COLUMN_COMMENT, oldColumnComment, columnComment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getColumnType() {
		return columnType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setColumnType(String newColumnType) {
		String oldColumnType = columnType;
		columnType = newColumnType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.STYLE__COLUMN_TYPE, oldColumnType, columnType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getColumnNullCheck() {
		return columnNullCheck;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setColumnNullCheck(String newColumnNullCheck) {
		String oldColumnNullCheck = columnNullCheck;
		columnNullCheck = newColumnNullCheck;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.STYLE__COLUMN_NULL_CHECK, oldColumnNullCheck, columnNullCheck));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getGrid() {
		return grid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGrid(String newGrid) {
		String oldGrid = grid;
		grid = newGrid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.STYLE__GRID, oldGrid, grid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DB getDb() {
		if (eContainerFeatureID() != RdbPackage.STYLE__DB) return null;
		return (DB)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDb(DB newDb, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newDb, RdbPackage.STYLE__DB, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDb(DB newDb) {
		if (newDb != eInternalContainer() || (eContainerFeatureID() != RdbPackage.STYLE__DB && newDb != null)) {
			if (EcoreUtil.isAncestor(this, newDb))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newDb != null)
				msgs = ((InternalEObject)newDb).eInverseAdd(this, RdbPackage.DB__STYLED_REFERENCE, DB.class, msgs);
			msgs = basicSetDb(newDb, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RdbPackage.STYLE__DB, newDb, newDb));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RdbPackage.STYLE__DB:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetDb((DB)otherEnd, msgs);
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
			case RdbPackage.STYLE__DB:
				return basicSetDb(null, msgs);
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
			case RdbPackage.STYLE__DB:
				return eInternalContainer().eInverseRemove(this, RdbPackage.DB__STYLED_REFERENCE, DB.class, msgs);
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
			case RdbPackage.STYLE__SCALE:
				return getScale();
			case RdbPackage.STYLE__TABLE_TITLE:
				return getTableTitle();
			case RdbPackage.STYLE__COLUMN_PRIMARY_KEY:
				return getColumnPrimaryKey();
			case RdbPackage.STYLE__COLUMN_NAME:
				return getColumnName();
			case RdbPackage.STYLE__COLUMN_COMMENT:
				return getColumnComment();
			case RdbPackage.STYLE__COLUMN_TYPE:
				return getColumnType();
			case RdbPackage.STYLE__COLUMN_NULL_CHECK:
				return getColumnNullCheck();
			case RdbPackage.STYLE__GRID:
				return getGrid();
			case RdbPackage.STYLE__DB:
				return getDb();
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
			case RdbPackage.STYLE__SCALE:
				setScale((Double)newValue);
				return;
			case RdbPackage.STYLE__TABLE_TITLE:
				setTableTitle((String)newValue);
				return;
			case RdbPackage.STYLE__COLUMN_PRIMARY_KEY:
				setColumnPrimaryKey((String)newValue);
				return;
			case RdbPackage.STYLE__COLUMN_NAME:
				setColumnName((String)newValue);
				return;
			case RdbPackage.STYLE__COLUMN_COMMENT:
				setColumnComment((String)newValue);
				return;
			case RdbPackage.STYLE__COLUMN_TYPE:
				setColumnType((String)newValue);
				return;
			case RdbPackage.STYLE__COLUMN_NULL_CHECK:
				setColumnNullCheck((String)newValue);
				return;
			case RdbPackage.STYLE__GRID:
				setGrid((String)newValue);
				return;
			case RdbPackage.STYLE__DB:
				setDb((DB)newValue);
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
			case RdbPackage.STYLE__SCALE:
				setScale(SCALE_EDEFAULT);
				return;
			case RdbPackage.STYLE__TABLE_TITLE:
				setTableTitle(TABLE_TITLE_EDEFAULT);
				return;
			case RdbPackage.STYLE__COLUMN_PRIMARY_KEY:
				setColumnPrimaryKey(COLUMN_PRIMARY_KEY_EDEFAULT);
				return;
			case RdbPackage.STYLE__COLUMN_NAME:
				setColumnName(COLUMN_NAME_EDEFAULT);
				return;
			case RdbPackage.STYLE__COLUMN_COMMENT:
				setColumnComment(COLUMN_COMMENT_EDEFAULT);
				return;
			case RdbPackage.STYLE__COLUMN_TYPE:
				setColumnType(COLUMN_TYPE_EDEFAULT);
				return;
			case RdbPackage.STYLE__COLUMN_NULL_CHECK:
				setColumnNullCheck(COLUMN_NULL_CHECK_EDEFAULT);
				return;
			case RdbPackage.STYLE__GRID:
				setGrid(GRID_EDEFAULT);
				return;
			case RdbPackage.STYLE__DB:
				setDb((DB)null);
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
			case RdbPackage.STYLE__SCALE:
				return SCALE_EDEFAULT == null ? scale != null : !SCALE_EDEFAULT.equals(scale);
			case RdbPackage.STYLE__TABLE_TITLE:
				return TABLE_TITLE_EDEFAULT == null ? tableTitle != null : !TABLE_TITLE_EDEFAULT.equals(tableTitle);
			case RdbPackage.STYLE__COLUMN_PRIMARY_KEY:
				return COLUMN_PRIMARY_KEY_EDEFAULT == null ? columnPrimaryKey != null : !COLUMN_PRIMARY_KEY_EDEFAULT.equals(columnPrimaryKey);
			case RdbPackage.STYLE__COLUMN_NAME:
				return COLUMN_NAME_EDEFAULT == null ? columnName != null : !COLUMN_NAME_EDEFAULT.equals(columnName);
			case RdbPackage.STYLE__COLUMN_COMMENT:
				return COLUMN_COMMENT_EDEFAULT == null ? columnComment != null : !COLUMN_COMMENT_EDEFAULT.equals(columnComment);
			case RdbPackage.STYLE__COLUMN_TYPE:
				return COLUMN_TYPE_EDEFAULT == null ? columnType != null : !COLUMN_TYPE_EDEFAULT.equals(columnType);
			case RdbPackage.STYLE__COLUMN_NULL_CHECK:
				return COLUMN_NULL_CHECK_EDEFAULT == null ? columnNullCheck != null : !COLUMN_NULL_CHECK_EDEFAULT.equals(columnNullCheck);
			case RdbPackage.STYLE__GRID:
				return GRID_EDEFAULT == null ? grid != null : !GRID_EDEFAULT.equals(grid);
			case RdbPackage.STYLE__DB:
				return getDb() != null;
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
		result.append(" (scale: ");
		result.append(scale);
		result.append(", tableTitle: ");
		result.append(tableTitle);
		result.append(", columnPrimaryKey: ");
		result.append(columnPrimaryKey);
		result.append(", columnName: ");
		result.append(columnName);
		result.append(", columnComment: ");
		result.append(columnComment);
		result.append(", columnType: ");
		result.append(columnType);
		result.append(", columnNullCheck: ");
		result.append(columnNullCheck);
		result.append(", grid: ");
		result.append(grid);
		result.append(')');
		return result.toString();
	}

} //StyleImpl
