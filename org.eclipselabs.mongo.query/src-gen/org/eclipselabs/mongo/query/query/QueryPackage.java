/**
 */
package org.eclipselabs.mongo.query.query;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see org.eclipselabs.mongo.query.query.QueryFactory
 * @model kind="package"
 * @generated
 */
public interface QueryPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "query";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/mongoemf/SQLQuery";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "query";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  QueryPackage eINSTANCE = org.eclipselabs.mongo.query.query.impl.QueryPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.ModelImpl <em>Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.ModelImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getModel()
   * @generated
   */
  int MODEL = 0;

  /**
   * The feature id for the '<em><b>Attrs</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL__ATTRS = 0;

  /**
   * The feature id for the '<em><b>Db</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL__DB = 1;

  /**
   * The feature id for the '<em><b>Where Entry</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL__WHERE_ENTRY = 2;

  /**
   * The number of structural features of the '<em>Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.DatabaseImpl <em>Database</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.DatabaseImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getDatabase()
   * @generated
   */
  int DATABASE = 1;

  /**
   * The feature id for the '<em><b>Url</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATABASE__URL = 0;

  /**
   * The feature id for the '<em><b>Port</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATABASE__PORT = 1;

  /**
   * The feature id for the '<em><b>Db Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATABASE__DB_NAME = 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATABASE__NAME = 3;

  /**
   * The number of structural features of the '<em>Database</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATABASE_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.WhereEntryImpl <em>Where Entry</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.WhereEntryImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getWhereEntry()
   * @generated
   */
  int WHERE_ENTRY = 2;

  /**
   * The number of structural features of the '<em>Where Entry</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WHERE_ENTRY_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.ExpressionWhereEntryImpl <em>Expression Where Entry</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.ExpressionWhereEntryImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getExpressionWhereEntry()
   * @generated
   */
  int EXPRESSION_WHERE_ENTRY = 3;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION_WHERE_ENTRY__NAME = WHERE_ENTRY_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Expression Where Entry</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION_WHERE_ENTRY_FEATURE_COUNT = WHERE_ENTRY_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.SingleExpressionWhereEntryImpl <em>Single Expression Where Entry</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.SingleExpressionWhereEntryImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getSingleExpressionWhereEntry()
   * @generated
   */
  int SINGLE_EXPRESSION_WHERE_ENTRY = 4;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SINGLE_EXPRESSION_WHERE_ENTRY__NAME = EXPRESSION_WHERE_ENTRY__NAME;

  /**
   * The feature id for the '<em><b>Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SINGLE_EXPRESSION_WHERE_ENTRY__OPERATOR = EXPRESSION_WHERE_ENTRY_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Rhs</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SINGLE_EXPRESSION_WHERE_ENTRY__RHS = EXPRESSION_WHERE_ENTRY_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Single Expression Where Entry</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SINGLE_EXPRESSION_WHERE_ENTRY_FEATURE_COUNT = EXPRESSION_WHERE_ENTRY_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.ExpressionImpl <em>Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.ExpressionImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getExpression()
   * @generated
   */
  int EXPRESSION = 5;

  /**
   * The number of structural features of the '<em>Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.ReplacableValueImpl <em>Replacable Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.ReplacableValueImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getReplacableValue()
   * @generated
   */
  int REPLACABLE_VALUE = 6;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPLACABLE_VALUE__VALUE = EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Replacable Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REPLACABLE_VALUE_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.DoubleExpressionImpl <em>Double Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.DoubleExpressionImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getDoubleExpression()
   * @generated
   */
  int DOUBLE_EXPRESSION = 7;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOUBLE_EXPRESSION__VALUE = EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Double Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOUBLE_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.LongExpressionImpl <em>Long Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.LongExpressionImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getLongExpression()
   * @generated
   */
  int LONG_EXPRESSION = 8;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LONG_EXPRESSION__VALUE = EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Long Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LONG_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.StringExpressionImpl <em>String Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.StringExpressionImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getStringExpression()
   * @generated
   */
  int STRING_EXPRESSION = 9;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_EXPRESSION__VALUE = EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>String Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.NullExpressionImpl <em>Null Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.NullExpressionImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getNullExpression()
   * @generated
   */
  int NULL_EXPRESSION = 10;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NULL_EXPRESSION__VALUE = EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Null Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NULL_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.DateExpressionImpl <em>Date Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.DateExpressionImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getDateExpression()
   * @generated
   */
  int DATE_EXPRESSION = 11;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_EXPRESSION__VALUE = EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Date Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.BooleanExpressionImpl <em>Boolean Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.BooleanExpressionImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getBooleanExpression()
   * @generated
   */
  int BOOLEAN_EXPRESSION = 12;

  /**
   * The feature id for the '<em><b>True</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN_EXPRESSION__TRUE = EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Boolean Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN_EXPRESSION_FEATURE_COUNT = EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.MultiExpressionWhereEntryImpl <em>Multi Expression Where Entry</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.MultiExpressionWhereEntryImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getMultiExpressionWhereEntry()
   * @generated
   */
  int MULTI_EXPRESSION_WHERE_ENTRY = 13;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MULTI_EXPRESSION_WHERE_ENTRY__NAME = EXPRESSION_WHERE_ENTRY__NAME;

  /**
   * The feature id for the '<em><b>Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MULTI_EXPRESSION_WHERE_ENTRY__OPERATOR = EXPRESSION_WHERE_ENTRY_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Rhs</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MULTI_EXPRESSION_WHERE_ENTRY__RHS = EXPRESSION_WHERE_ENTRY_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Multi Expression Where Entry</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MULTI_EXPRESSION_WHERE_ENTRY_FEATURE_COUNT = EXPRESSION_WHERE_ENTRY_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.ArrayExpressionImpl <em>Array Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.ArrayExpressionImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getArrayExpression()
   * @generated
   */
  int ARRAY_EXPRESSION = 14;

  /**
   * The number of structural features of the '<em>Array Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_EXPRESSION_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.DoubleArrayExpressionImpl <em>Double Array Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.DoubleArrayExpressionImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getDoubleArrayExpression()
   * @generated
   */
  int DOUBLE_ARRAY_EXPRESSION = 15;

  /**
   * The feature id for the '<em><b>Values</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOUBLE_ARRAY_EXPRESSION__VALUES = ARRAY_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Double Array Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOUBLE_ARRAY_EXPRESSION_FEATURE_COUNT = ARRAY_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.LongArrayExpressionImpl <em>Long Array Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.LongArrayExpressionImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getLongArrayExpression()
   * @generated
   */
  int LONG_ARRAY_EXPRESSION = 16;

  /**
   * The feature id for the '<em><b>Values</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LONG_ARRAY_EXPRESSION__VALUES = ARRAY_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Long Array Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LONG_ARRAY_EXPRESSION_FEATURE_COUNT = ARRAY_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.StringArrayExpressionImpl <em>String Array Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.StringArrayExpressionImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getStringArrayExpression()
   * @generated
   */
  int STRING_ARRAY_EXPRESSION = 17;

  /**
   * The feature id for the '<em><b>Values</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_ARRAY_EXPRESSION__VALUES = ARRAY_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>String Array Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_ARRAY_EXPRESSION_FEATURE_COUNT = ARRAY_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.NullArrayExpressionImpl <em>Null Array Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.NullArrayExpressionImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getNullArrayExpression()
   * @generated
   */
  int NULL_ARRAY_EXPRESSION = 18;

  /**
   * The feature id for the '<em><b>Values</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NULL_ARRAY_EXPRESSION__VALUES = ARRAY_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Null Array Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NULL_ARRAY_EXPRESSION_FEATURE_COUNT = ARRAY_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.DateArrayExpressionImpl <em>Date Array Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.DateArrayExpressionImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getDateArrayExpression()
   * @generated
   */
  int DATE_ARRAY_EXPRESSION = 19;

  /**
   * The feature id for the '<em><b>Values</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_ARRAY_EXPRESSION__VALUES = ARRAY_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Date Array Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATE_ARRAY_EXPRESSION_FEATURE_COUNT = ARRAY_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.BooleanArrayExpressionImpl <em>Boolean Array Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.BooleanArrayExpressionImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getBooleanArrayExpression()
   * @generated
   */
  int BOOLEAN_ARRAY_EXPRESSION = 20;

  /**
   * The feature id for the '<em><b>Values</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN_ARRAY_EXPRESSION__VALUES = ARRAY_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Boolean Array Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN_ARRAY_EXPRESSION_FEATURE_COUNT = ARRAY_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.OrWhereEntryImpl <em>Or Where Entry</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.OrWhereEntryImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getOrWhereEntry()
   * @generated
   */
  int OR_WHERE_ENTRY = 21;

  /**
   * The feature id for the '<em><b>Entries</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OR_WHERE_ENTRY__ENTRIES = WHERE_ENTRY_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Or Where Entry</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OR_WHERE_ENTRY_FEATURE_COUNT = WHERE_ENTRY_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.impl.AndWhereEntryImpl <em>And Where Entry</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.impl.AndWhereEntryImpl
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getAndWhereEntry()
   * @generated
   */
  int AND_WHERE_ENTRY = 22;

  /**
   * The feature id for the '<em><b>Entries</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AND_WHERE_ENTRY__ENTRIES = WHERE_ENTRY_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>And Where Entry</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AND_WHERE_ENTRY_FEATURE_COUNT = WHERE_ENTRY_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.ArrayOperator <em>Array Operator</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.ArrayOperator
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getArrayOperator()
   * @generated
   */
  int ARRAY_OPERATOR = 23;

  /**
   * The meta object id for the '{@link org.eclipselabs.mongo.query.query.Operator <em>Operator</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipselabs.mongo.query.query.Operator
   * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getOperator()
   * @generated
   */
  int OPERATOR = 24;


  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.Model <em>Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model</em>'.
   * @see org.eclipselabs.mongo.query.query.Model
   * @generated
   */
  EClass getModel();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.Model#getAttrs <em>Attrs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Attrs</em>'.
   * @see org.eclipselabs.mongo.query.query.Model#getAttrs()
   * @see #getModel()
   * @generated
   */
  EAttribute getModel_Attrs();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipselabs.mongo.query.query.Model#getDb <em>Db</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Db</em>'.
   * @see org.eclipselabs.mongo.query.query.Model#getDb()
   * @see #getModel()
   * @generated
   */
  EReference getModel_Db();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipselabs.mongo.query.query.Model#getWhereEntry <em>Where Entry</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Where Entry</em>'.
   * @see org.eclipselabs.mongo.query.query.Model#getWhereEntry()
   * @see #getModel()
   * @generated
   */
  EReference getModel_WhereEntry();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.Database <em>Database</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Database</em>'.
   * @see org.eclipselabs.mongo.query.query.Database
   * @generated
   */
  EClass getDatabase();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.Database#getUrl <em>Url</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Url</em>'.
   * @see org.eclipselabs.mongo.query.query.Database#getUrl()
   * @see #getDatabase()
   * @generated
   */
  EAttribute getDatabase_Url();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.Database#getPort <em>Port</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Port</em>'.
   * @see org.eclipselabs.mongo.query.query.Database#getPort()
   * @see #getDatabase()
   * @generated
   */
  EAttribute getDatabase_Port();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.Database#getDbName <em>Db Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Db Name</em>'.
   * @see org.eclipselabs.mongo.query.query.Database#getDbName()
   * @see #getDatabase()
   * @generated
   */
  EAttribute getDatabase_DbName();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.Database#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipselabs.mongo.query.query.Database#getName()
   * @see #getDatabase()
   * @generated
   */
  EAttribute getDatabase_Name();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.WhereEntry <em>Where Entry</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Where Entry</em>'.
   * @see org.eclipselabs.mongo.query.query.WhereEntry
   * @generated
   */
  EClass getWhereEntry();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.ExpressionWhereEntry <em>Expression Where Entry</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Expression Where Entry</em>'.
   * @see org.eclipselabs.mongo.query.query.ExpressionWhereEntry
   * @generated
   */
  EClass getExpressionWhereEntry();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.ExpressionWhereEntry#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipselabs.mongo.query.query.ExpressionWhereEntry#getName()
   * @see #getExpressionWhereEntry()
   * @generated
   */
  EAttribute getExpressionWhereEntry_Name();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.SingleExpressionWhereEntry <em>Single Expression Where Entry</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Single Expression Where Entry</em>'.
   * @see org.eclipselabs.mongo.query.query.SingleExpressionWhereEntry
   * @generated
   */
  EClass getSingleExpressionWhereEntry();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.SingleExpressionWhereEntry#getOperator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Operator</em>'.
   * @see org.eclipselabs.mongo.query.query.SingleExpressionWhereEntry#getOperator()
   * @see #getSingleExpressionWhereEntry()
   * @generated
   */
  EAttribute getSingleExpressionWhereEntry_Operator();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipselabs.mongo.query.query.SingleExpressionWhereEntry#getRhs <em>Rhs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Rhs</em>'.
   * @see org.eclipselabs.mongo.query.query.SingleExpressionWhereEntry#getRhs()
   * @see #getSingleExpressionWhereEntry()
   * @generated
   */
  EReference getSingleExpressionWhereEntry_Rhs();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.Expression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Expression</em>'.
   * @see org.eclipselabs.mongo.query.query.Expression
   * @generated
   */
  EClass getExpression();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.ReplacableValue <em>Replacable Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Replacable Value</em>'.
   * @see org.eclipselabs.mongo.query.query.ReplacableValue
   * @generated
   */
  EClass getReplacableValue();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.ReplacableValue#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipselabs.mongo.query.query.ReplacableValue#getValue()
   * @see #getReplacableValue()
   * @generated
   */
  EAttribute getReplacableValue_Value();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.DoubleExpression <em>Double Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Double Expression</em>'.
   * @see org.eclipselabs.mongo.query.query.DoubleExpression
   * @generated
   */
  EClass getDoubleExpression();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.DoubleExpression#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipselabs.mongo.query.query.DoubleExpression#getValue()
   * @see #getDoubleExpression()
   * @generated
   */
  EAttribute getDoubleExpression_Value();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.LongExpression <em>Long Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Long Expression</em>'.
   * @see org.eclipselabs.mongo.query.query.LongExpression
   * @generated
   */
  EClass getLongExpression();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.LongExpression#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipselabs.mongo.query.query.LongExpression#getValue()
   * @see #getLongExpression()
   * @generated
   */
  EAttribute getLongExpression_Value();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.StringExpression <em>String Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>String Expression</em>'.
   * @see org.eclipselabs.mongo.query.query.StringExpression
   * @generated
   */
  EClass getStringExpression();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.StringExpression#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipselabs.mongo.query.query.StringExpression#getValue()
   * @see #getStringExpression()
   * @generated
   */
  EAttribute getStringExpression_Value();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.NullExpression <em>Null Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Null Expression</em>'.
   * @see org.eclipselabs.mongo.query.query.NullExpression
   * @generated
   */
  EClass getNullExpression();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.NullExpression#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipselabs.mongo.query.query.NullExpression#getValue()
   * @see #getNullExpression()
   * @generated
   */
  EAttribute getNullExpression_Value();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.DateExpression <em>Date Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Date Expression</em>'.
   * @see org.eclipselabs.mongo.query.query.DateExpression
   * @generated
   */
  EClass getDateExpression();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.DateExpression#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipselabs.mongo.query.query.DateExpression#getValue()
   * @see #getDateExpression()
   * @generated
   */
  EAttribute getDateExpression_Value();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.BooleanExpression <em>Boolean Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Boolean Expression</em>'.
   * @see org.eclipselabs.mongo.query.query.BooleanExpression
   * @generated
   */
  EClass getBooleanExpression();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.BooleanExpression#getTrue <em>True</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>True</em>'.
   * @see org.eclipselabs.mongo.query.query.BooleanExpression#getTrue()
   * @see #getBooleanExpression()
   * @generated
   */
  EAttribute getBooleanExpression_True();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.MultiExpressionWhereEntry <em>Multi Expression Where Entry</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Multi Expression Where Entry</em>'.
   * @see org.eclipselabs.mongo.query.query.MultiExpressionWhereEntry
   * @generated
   */
  EClass getMultiExpressionWhereEntry();

  /**
   * Returns the meta object for the attribute '{@link org.eclipselabs.mongo.query.query.MultiExpressionWhereEntry#getOperator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Operator</em>'.
   * @see org.eclipselabs.mongo.query.query.MultiExpressionWhereEntry#getOperator()
   * @see #getMultiExpressionWhereEntry()
   * @generated
   */
  EAttribute getMultiExpressionWhereEntry_Operator();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipselabs.mongo.query.query.MultiExpressionWhereEntry#getRhs <em>Rhs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Rhs</em>'.
   * @see org.eclipselabs.mongo.query.query.MultiExpressionWhereEntry#getRhs()
   * @see #getMultiExpressionWhereEntry()
   * @generated
   */
  EReference getMultiExpressionWhereEntry_Rhs();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.ArrayExpression <em>Array Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Array Expression</em>'.
   * @see org.eclipselabs.mongo.query.query.ArrayExpression
   * @generated
   */
  EClass getArrayExpression();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.DoubleArrayExpression <em>Double Array Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Double Array Expression</em>'.
   * @see org.eclipselabs.mongo.query.query.DoubleArrayExpression
   * @generated
   */
  EClass getDoubleArrayExpression();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipselabs.mongo.query.query.DoubleArrayExpression#getValues <em>Values</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Values</em>'.
   * @see org.eclipselabs.mongo.query.query.DoubleArrayExpression#getValues()
   * @see #getDoubleArrayExpression()
   * @generated
   */
  EAttribute getDoubleArrayExpression_Values();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.LongArrayExpression <em>Long Array Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Long Array Expression</em>'.
   * @see org.eclipselabs.mongo.query.query.LongArrayExpression
   * @generated
   */
  EClass getLongArrayExpression();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipselabs.mongo.query.query.LongArrayExpression#getValues <em>Values</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Values</em>'.
   * @see org.eclipselabs.mongo.query.query.LongArrayExpression#getValues()
   * @see #getLongArrayExpression()
   * @generated
   */
  EAttribute getLongArrayExpression_Values();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.StringArrayExpression <em>String Array Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>String Array Expression</em>'.
   * @see org.eclipselabs.mongo.query.query.StringArrayExpression
   * @generated
   */
  EClass getStringArrayExpression();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipselabs.mongo.query.query.StringArrayExpression#getValues <em>Values</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Values</em>'.
   * @see org.eclipselabs.mongo.query.query.StringArrayExpression#getValues()
   * @see #getStringArrayExpression()
   * @generated
   */
  EAttribute getStringArrayExpression_Values();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.NullArrayExpression <em>Null Array Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Null Array Expression</em>'.
   * @see org.eclipselabs.mongo.query.query.NullArrayExpression
   * @generated
   */
  EClass getNullArrayExpression();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipselabs.mongo.query.query.NullArrayExpression#getValues <em>Values</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Values</em>'.
   * @see org.eclipselabs.mongo.query.query.NullArrayExpression#getValues()
   * @see #getNullArrayExpression()
   * @generated
   */
  EAttribute getNullArrayExpression_Values();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.DateArrayExpression <em>Date Array Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Date Array Expression</em>'.
   * @see org.eclipselabs.mongo.query.query.DateArrayExpression
   * @generated
   */
  EClass getDateArrayExpression();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipselabs.mongo.query.query.DateArrayExpression#getValues <em>Values</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Values</em>'.
   * @see org.eclipselabs.mongo.query.query.DateArrayExpression#getValues()
   * @see #getDateArrayExpression()
   * @generated
   */
  EAttribute getDateArrayExpression_Values();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.BooleanArrayExpression <em>Boolean Array Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Boolean Array Expression</em>'.
   * @see org.eclipselabs.mongo.query.query.BooleanArrayExpression
   * @generated
   */
  EClass getBooleanArrayExpression();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipselabs.mongo.query.query.BooleanArrayExpression#getValues <em>Values</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Values</em>'.
   * @see org.eclipselabs.mongo.query.query.BooleanArrayExpression#getValues()
   * @see #getBooleanArrayExpression()
   * @generated
   */
  EAttribute getBooleanArrayExpression_Values();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.OrWhereEntry <em>Or Where Entry</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Or Where Entry</em>'.
   * @see org.eclipselabs.mongo.query.query.OrWhereEntry
   * @generated
   */
  EClass getOrWhereEntry();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipselabs.mongo.query.query.OrWhereEntry#getEntries <em>Entries</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Entries</em>'.
   * @see org.eclipselabs.mongo.query.query.OrWhereEntry#getEntries()
   * @see #getOrWhereEntry()
   * @generated
   */
  EReference getOrWhereEntry_Entries();

  /**
   * Returns the meta object for class '{@link org.eclipselabs.mongo.query.query.AndWhereEntry <em>And Where Entry</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>And Where Entry</em>'.
   * @see org.eclipselabs.mongo.query.query.AndWhereEntry
   * @generated
   */
  EClass getAndWhereEntry();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipselabs.mongo.query.query.AndWhereEntry#getEntries <em>Entries</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Entries</em>'.
   * @see org.eclipselabs.mongo.query.query.AndWhereEntry#getEntries()
   * @see #getAndWhereEntry()
   * @generated
   */
  EReference getAndWhereEntry_Entries();

  /**
   * Returns the meta object for enum '{@link org.eclipselabs.mongo.query.query.ArrayOperator <em>Array Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Array Operator</em>'.
   * @see org.eclipselabs.mongo.query.query.ArrayOperator
   * @generated
   */
  EEnum getArrayOperator();

  /**
   * Returns the meta object for enum '{@link org.eclipselabs.mongo.query.query.Operator <em>Operator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Operator</em>'.
   * @see org.eclipselabs.mongo.query.query.Operator
   * @generated
   */
  EEnum getOperator();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  QueryFactory getQueryFactory();

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
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.ModelImpl <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.ModelImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getModel()
     * @generated
     */
    EClass MODEL = eINSTANCE.getModel();

    /**
     * The meta object literal for the '<em><b>Attrs</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MODEL__ATTRS = eINSTANCE.getModel_Attrs();

    /**
     * The meta object literal for the '<em><b>Db</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL__DB = eINSTANCE.getModel_Db();

    /**
     * The meta object literal for the '<em><b>Where Entry</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL__WHERE_ENTRY = eINSTANCE.getModel_WhereEntry();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.DatabaseImpl <em>Database</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.DatabaseImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getDatabase()
     * @generated
     */
    EClass DATABASE = eINSTANCE.getDatabase();

    /**
     * The meta object literal for the '<em><b>Url</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATABASE__URL = eINSTANCE.getDatabase_Url();

    /**
     * The meta object literal for the '<em><b>Port</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATABASE__PORT = eINSTANCE.getDatabase_Port();

    /**
     * The meta object literal for the '<em><b>Db Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATABASE__DB_NAME = eINSTANCE.getDatabase_DbName();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATABASE__NAME = eINSTANCE.getDatabase_Name();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.WhereEntryImpl <em>Where Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.WhereEntryImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getWhereEntry()
     * @generated
     */
    EClass WHERE_ENTRY = eINSTANCE.getWhereEntry();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.ExpressionWhereEntryImpl <em>Expression Where Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.ExpressionWhereEntryImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getExpressionWhereEntry()
     * @generated
     */
    EClass EXPRESSION_WHERE_ENTRY = eINSTANCE.getExpressionWhereEntry();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute EXPRESSION_WHERE_ENTRY__NAME = eINSTANCE.getExpressionWhereEntry_Name();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.SingleExpressionWhereEntryImpl <em>Single Expression Where Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.SingleExpressionWhereEntryImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getSingleExpressionWhereEntry()
     * @generated
     */
    EClass SINGLE_EXPRESSION_WHERE_ENTRY = eINSTANCE.getSingleExpressionWhereEntry();

    /**
     * The meta object literal for the '<em><b>Operator</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SINGLE_EXPRESSION_WHERE_ENTRY__OPERATOR = eINSTANCE.getSingleExpressionWhereEntry_Operator();

    /**
     * The meta object literal for the '<em><b>Rhs</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SINGLE_EXPRESSION_WHERE_ENTRY__RHS = eINSTANCE.getSingleExpressionWhereEntry_Rhs();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.ExpressionImpl <em>Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.ExpressionImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getExpression()
     * @generated
     */
    EClass EXPRESSION = eINSTANCE.getExpression();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.ReplacableValueImpl <em>Replacable Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.ReplacableValueImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getReplacableValue()
     * @generated
     */
    EClass REPLACABLE_VALUE = eINSTANCE.getReplacableValue();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REPLACABLE_VALUE__VALUE = eINSTANCE.getReplacableValue_Value();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.DoubleExpressionImpl <em>Double Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.DoubleExpressionImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getDoubleExpression()
     * @generated
     */
    EClass DOUBLE_EXPRESSION = eINSTANCE.getDoubleExpression();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DOUBLE_EXPRESSION__VALUE = eINSTANCE.getDoubleExpression_Value();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.LongExpressionImpl <em>Long Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.LongExpressionImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getLongExpression()
     * @generated
     */
    EClass LONG_EXPRESSION = eINSTANCE.getLongExpression();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LONG_EXPRESSION__VALUE = eINSTANCE.getLongExpression_Value();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.StringExpressionImpl <em>String Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.StringExpressionImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getStringExpression()
     * @generated
     */
    EClass STRING_EXPRESSION = eINSTANCE.getStringExpression();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_EXPRESSION__VALUE = eINSTANCE.getStringExpression_Value();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.NullExpressionImpl <em>Null Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.NullExpressionImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getNullExpression()
     * @generated
     */
    EClass NULL_EXPRESSION = eINSTANCE.getNullExpression();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute NULL_EXPRESSION__VALUE = eINSTANCE.getNullExpression_Value();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.DateExpressionImpl <em>Date Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.DateExpressionImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getDateExpression()
     * @generated
     */
    EClass DATE_EXPRESSION = eINSTANCE.getDateExpression();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATE_EXPRESSION__VALUE = eINSTANCE.getDateExpression_Value();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.BooleanExpressionImpl <em>Boolean Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.BooleanExpressionImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getBooleanExpression()
     * @generated
     */
    EClass BOOLEAN_EXPRESSION = eINSTANCE.getBooleanExpression();

    /**
     * The meta object literal for the '<em><b>True</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BOOLEAN_EXPRESSION__TRUE = eINSTANCE.getBooleanExpression_True();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.MultiExpressionWhereEntryImpl <em>Multi Expression Where Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.MultiExpressionWhereEntryImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getMultiExpressionWhereEntry()
     * @generated
     */
    EClass MULTI_EXPRESSION_WHERE_ENTRY = eINSTANCE.getMultiExpressionWhereEntry();

    /**
     * The meta object literal for the '<em><b>Operator</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MULTI_EXPRESSION_WHERE_ENTRY__OPERATOR = eINSTANCE.getMultiExpressionWhereEntry_Operator();

    /**
     * The meta object literal for the '<em><b>Rhs</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MULTI_EXPRESSION_WHERE_ENTRY__RHS = eINSTANCE.getMultiExpressionWhereEntry_Rhs();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.ArrayExpressionImpl <em>Array Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.ArrayExpressionImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getArrayExpression()
     * @generated
     */
    EClass ARRAY_EXPRESSION = eINSTANCE.getArrayExpression();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.DoubleArrayExpressionImpl <em>Double Array Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.DoubleArrayExpressionImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getDoubleArrayExpression()
     * @generated
     */
    EClass DOUBLE_ARRAY_EXPRESSION = eINSTANCE.getDoubleArrayExpression();

    /**
     * The meta object literal for the '<em><b>Values</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DOUBLE_ARRAY_EXPRESSION__VALUES = eINSTANCE.getDoubleArrayExpression_Values();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.LongArrayExpressionImpl <em>Long Array Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.LongArrayExpressionImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getLongArrayExpression()
     * @generated
     */
    EClass LONG_ARRAY_EXPRESSION = eINSTANCE.getLongArrayExpression();

    /**
     * The meta object literal for the '<em><b>Values</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LONG_ARRAY_EXPRESSION__VALUES = eINSTANCE.getLongArrayExpression_Values();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.StringArrayExpressionImpl <em>String Array Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.StringArrayExpressionImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getStringArrayExpression()
     * @generated
     */
    EClass STRING_ARRAY_EXPRESSION = eINSTANCE.getStringArrayExpression();

    /**
     * The meta object literal for the '<em><b>Values</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_ARRAY_EXPRESSION__VALUES = eINSTANCE.getStringArrayExpression_Values();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.NullArrayExpressionImpl <em>Null Array Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.NullArrayExpressionImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getNullArrayExpression()
     * @generated
     */
    EClass NULL_ARRAY_EXPRESSION = eINSTANCE.getNullArrayExpression();

    /**
     * The meta object literal for the '<em><b>Values</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute NULL_ARRAY_EXPRESSION__VALUES = eINSTANCE.getNullArrayExpression_Values();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.DateArrayExpressionImpl <em>Date Array Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.DateArrayExpressionImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getDateArrayExpression()
     * @generated
     */
    EClass DATE_ARRAY_EXPRESSION = eINSTANCE.getDateArrayExpression();

    /**
     * The meta object literal for the '<em><b>Values</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATE_ARRAY_EXPRESSION__VALUES = eINSTANCE.getDateArrayExpression_Values();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.BooleanArrayExpressionImpl <em>Boolean Array Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.BooleanArrayExpressionImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getBooleanArrayExpression()
     * @generated
     */
    EClass BOOLEAN_ARRAY_EXPRESSION = eINSTANCE.getBooleanArrayExpression();

    /**
     * The meta object literal for the '<em><b>Values</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BOOLEAN_ARRAY_EXPRESSION__VALUES = eINSTANCE.getBooleanArrayExpression_Values();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.OrWhereEntryImpl <em>Or Where Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.OrWhereEntryImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getOrWhereEntry()
     * @generated
     */
    EClass OR_WHERE_ENTRY = eINSTANCE.getOrWhereEntry();

    /**
     * The meta object literal for the '<em><b>Entries</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OR_WHERE_ENTRY__ENTRIES = eINSTANCE.getOrWhereEntry_Entries();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.impl.AndWhereEntryImpl <em>And Where Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.impl.AndWhereEntryImpl
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getAndWhereEntry()
     * @generated
     */
    EClass AND_WHERE_ENTRY = eINSTANCE.getAndWhereEntry();

    /**
     * The meta object literal for the '<em><b>Entries</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference AND_WHERE_ENTRY__ENTRIES = eINSTANCE.getAndWhereEntry_Entries();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.ArrayOperator <em>Array Operator</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.ArrayOperator
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getArrayOperator()
     * @generated
     */
    EEnum ARRAY_OPERATOR = eINSTANCE.getArrayOperator();

    /**
     * The meta object literal for the '{@link org.eclipselabs.mongo.query.query.Operator <em>Operator</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipselabs.mongo.query.query.Operator
     * @see org.eclipselabs.mongo.query.query.impl.QueryPackageImpl#getOperator()
     * @generated
     */
    EEnum OPERATOR = eINSTANCE.getOperator();

  }

} //QueryPackage
