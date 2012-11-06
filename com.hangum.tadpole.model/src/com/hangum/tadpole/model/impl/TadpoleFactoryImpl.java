/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.hangum.tadpole.model.impl;

import com.hangum.tadpole.model.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.hangum.tadpole.model.Column;
import com.hangum.tadpole.model.DB;
import com.hangum.tadpole.model.Table;
import com.hangum.tadpole.model.TadpoleFactory;
import com.hangum.tadpole.model.TadpolePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TadpoleFactoryImpl extends EFactoryImpl implements TadpoleFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static TadpoleFactory init() {
		try {
			TadpoleFactory theTadpoleFactory = (TadpoleFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.hangum.tadpole.model.ERDInfo"); 
			if (theTadpoleFactory != null) {
				return theTadpoleFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new TadpoleFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TadpoleFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case TadpolePackage.DB: return createDB();
			case TadpolePackage.TABLE: return createTable();
			case TadpolePackage.COLUMN: return createColumn();
			case TadpolePackage.RELATION: return createRelation();
			case TadpolePackage.VIEW: return createView();
			case TadpolePackage.ERD_INFO: return createERDInfo();
			case TadpolePackage.USER_COMMENT: return createUserComment();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case TadpolePackage.RELATION_KIND:
				return createRelationKindFromString(eDataType, initialValue);
			case TadpolePackage.RECTANGLE:
				return createRectangleFromString(eDataType, initialValue);
			case TadpolePackage.POINT:
				return createPointFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case TadpolePackage.RELATION_KIND:
				return convertRelationKindToString(eDataType, instanceValue);
			case TadpolePackage.RECTANGLE:
				return convertRectangleToString(eDataType, instanceValue);
			case TadpolePackage.POINT:
				return convertPointToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Column createColumn() {
		ColumnImpl column = new ColumnImpl();
		return column;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Relation createRelation() {
		RelationImpl relation = new RelationImpl();
		return relation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public View createView() {
		ViewImpl view = new ViewImpl();
		return view;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ERDInfo createERDInfo() {
		ERDInfoImpl erdInfo = new ERDInfoImpl();
		return erdInfo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UserComment createUserComment() {
		UserCommentImpl userComment = new UserCommentImpl();
		return userComment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RelationKind createRelationKindFromString(EDataType eDataType, String initialValue) {
		RelationKind result = RelationKind.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRelationKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Rectangle createRectangleFromString(EDataType eDataType, String initialValue) {
		if(initialValue == null) {
			return null;
		}
		
//		initialValue.replaceAll("\\s", "");
		String[] values = initialValue.split(",");
		if(values.length != 4) {
			return null;
		}

		Rectangle rect = new Rectangle();
		try {
			rect.setLocation(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
			rect.setSize(Integer.parseInt(values[2]), Integer.parseInt(values[3]));
		} catch(NumberFormatException e) {
			EcorePlugin.INSTANCE.log(e);
			rect = null;
		}

		return rect;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertRectangleToString(EDataType eDataType, Object instanceValue) {
		if(instanceValue == null) {
			return null;
		}

		Rectangle rect = (Rectangle) instanceValue;
		return rect.x+","+rect.y+","+rect.width+","+rect.height;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Point createPointFromString(EDataType eDataType, String initialValue) {
		if(initialValue == null) {
			return null;
		}
		
//		initialValue.replaceAll("\\s", "");
		String[] values = initialValue.split(",");
		if(values.length != 2) {
			return null;
		}

		Point point = new Point();
		try {
			point.setLocation(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
			
		} catch(NumberFormatException e) {
			EcorePlugin.INSTANCE.log(e);
			point = null;
		}

		return point;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertPointToString(EDataType eDataType, Object instanceValue) {
		if(instanceValue == null) {
			return null;
		}

		Point point = (Point) instanceValue;
		return point.x+","+point.y;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Table createTable() {
		TableImpl table = new TableImpl();
		return table;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DB createDB() {
		DBImpl db = new DBImpl();
		return db;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TadpolePackage getTadpolePackage() {
		return (TadpolePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static TadpolePackage getPackage() {
		return TadpolePackage.eINSTANCE;
	}

} //TadpoleFactoryImpl
