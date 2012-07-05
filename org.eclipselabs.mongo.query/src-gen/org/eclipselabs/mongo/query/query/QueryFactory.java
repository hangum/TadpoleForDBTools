/**
 */
package org.eclipselabs.mongo.query.query;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipselabs.mongo.query.query.QueryPackage
 * @generated
 */
public interface QueryFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  QueryFactory eINSTANCE = org.eclipselabs.mongo.query.query.impl.QueryFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Model</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Model</em>'.
   * @generated
   */
  Model createModel();

  /**
   * Returns a new object of class '<em>Database</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Database</em>'.
   * @generated
   */
  Database createDatabase();

  /**
   * Returns a new object of class '<em>Where Entry</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Where Entry</em>'.
   * @generated
   */
  WhereEntry createWhereEntry();

  /**
   * Returns a new object of class '<em>Expression Where Entry</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Expression Where Entry</em>'.
   * @generated
   */
  ExpressionWhereEntry createExpressionWhereEntry();

  /**
   * Returns a new object of class '<em>Single Expression Where Entry</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Single Expression Where Entry</em>'.
   * @generated
   */
  SingleExpressionWhereEntry createSingleExpressionWhereEntry();

  /**
   * Returns a new object of class '<em>Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Expression</em>'.
   * @generated
   */
  Expression createExpression();

  /**
   * Returns a new object of class '<em>Replacable Value</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Replacable Value</em>'.
   * @generated
   */
  ReplacableValue createReplacableValue();

  /**
   * Returns a new object of class '<em>Double Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Double Expression</em>'.
   * @generated
   */
  DoubleExpression createDoubleExpression();

  /**
   * Returns a new object of class '<em>Long Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Long Expression</em>'.
   * @generated
   */
  LongExpression createLongExpression();

  /**
   * Returns a new object of class '<em>String Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>String Expression</em>'.
   * @generated
   */
  StringExpression createStringExpression();

  /**
   * Returns a new object of class '<em>Null Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Null Expression</em>'.
   * @generated
   */
  NullExpression createNullExpression();

  /**
   * Returns a new object of class '<em>Date Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Date Expression</em>'.
   * @generated
   */
  DateExpression createDateExpression();

  /**
   * Returns a new object of class '<em>Boolean Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Boolean Expression</em>'.
   * @generated
   */
  BooleanExpression createBooleanExpression();

  /**
   * Returns a new object of class '<em>Multi Expression Where Entry</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Multi Expression Where Entry</em>'.
   * @generated
   */
  MultiExpressionWhereEntry createMultiExpressionWhereEntry();

  /**
   * Returns a new object of class '<em>Array Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Array Expression</em>'.
   * @generated
   */
  ArrayExpression createArrayExpression();

  /**
   * Returns a new object of class '<em>Double Array Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Double Array Expression</em>'.
   * @generated
   */
  DoubleArrayExpression createDoubleArrayExpression();

  /**
   * Returns a new object of class '<em>Long Array Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Long Array Expression</em>'.
   * @generated
   */
  LongArrayExpression createLongArrayExpression();

  /**
   * Returns a new object of class '<em>String Array Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>String Array Expression</em>'.
   * @generated
   */
  StringArrayExpression createStringArrayExpression();

  /**
   * Returns a new object of class '<em>Null Array Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Null Array Expression</em>'.
   * @generated
   */
  NullArrayExpression createNullArrayExpression();

  /**
   * Returns a new object of class '<em>Date Array Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Date Array Expression</em>'.
   * @generated
   */
  DateArrayExpression createDateArrayExpression();

  /**
   * Returns a new object of class '<em>Boolean Array Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Boolean Array Expression</em>'.
   * @generated
   */
  BooleanArrayExpression createBooleanArrayExpression();

  /**
   * Returns a new object of class '<em>Or Where Entry</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Or Where Entry</em>'.
   * @generated
   */
  OrWhereEntry createOrWhereEntry();

  /**
   * Returns a new object of class '<em>And Where Entry</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>And Where Entry</em>'.
   * @generated
   */
  AndWhereEntry createAndWhereEntry();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  QueryPackage getQueryPackage();

} //QueryFactory
