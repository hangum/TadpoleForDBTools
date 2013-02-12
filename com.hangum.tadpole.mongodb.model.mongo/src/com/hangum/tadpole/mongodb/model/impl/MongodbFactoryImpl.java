/**
 */
package com.hangum.tadpole.mongodb.model.impl;

import com.hangum.tadpole.mongodb.model.*;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MongodbFactoryImpl extends EFactoryImpl implements MongodbFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MongodbFactory init() {
		try {
			MongodbFactory theMongodbFactory = (MongodbFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.hangum.tadpole.mongodb.model.ERDInfo"); 
			if (theMongodbFactory != null) {
				return theMongodbFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MongodbFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MongodbFactoryImpl() {
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
			case MongodbPackage.DB: return createDB();
			case MongodbPackage.TABLE: return createTable();
			case MongodbPackage.COLUMN: return createColumn();
			case MongodbPackage.RELATION: return createRelation();
			case MongodbPackage.VIEW: return createView();
			case MongodbPackage.ERD_INFO: return createERDInfo();
			case MongodbPackage.USER_COMMENT: return createUserComment();
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
			case MongodbPackage.RELATION_KIND:
				return createRelationKindFromString(eDataType, initialValue);
			case MongodbPackage.RECTANGLE:
				return createRectangleFromString(eDataType, initialValue);
			case MongodbPackage.POINT:
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
			case MongodbPackage.RELATION_KIND:
				return convertRelationKindToString(eDataType, instanceValue);
			case MongodbPackage.RECTANGLE:
				return convertRectangleToString(eDataType, instanceValue);
			case MongodbPackage.POINT:
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
	public DB createDB() {
		DBImpl db = new DBImpl();
		return db;
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
	public MongodbPackage getMongodbPackage() {
		return (MongodbPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MongodbPackage getPackage() {
		return MongodbPackage.eINSTANCE;
	}

} //MongodbFactoryImpl
