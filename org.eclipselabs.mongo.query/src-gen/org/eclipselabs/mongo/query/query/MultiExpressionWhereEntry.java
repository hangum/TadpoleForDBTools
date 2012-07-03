/**
 */
package org.eclipselabs.mongo.query.query;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multi Expression Where Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipselabs.mongo.query.query.MultiExpressionWhereEntry#getOperator <em>Operator</em>}</li>
 *   <li>{@link org.eclipselabs.mongo.query.query.MultiExpressionWhereEntry#getRhs <em>Rhs</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipselabs.mongo.query.query.QueryPackage#getMultiExpressionWhereEntry()
 * @model
 * @generated
 */
public interface MultiExpressionWhereEntry extends ExpressionWhereEntry
{
  /**
   * Returns the value of the '<em><b>Operator</b></em>' attribute.
   * The literals are from the enumeration {@link org.eclipselabs.mongo.query.query.ArrayOperator}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Operator</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Operator</em>' attribute.
   * @see org.eclipselabs.mongo.query.query.ArrayOperator
   * @see #setOperator(ArrayOperator)
   * @see org.eclipselabs.mongo.query.query.QueryPackage#getMultiExpressionWhereEntry_Operator()
   * @model
   * @generated
   */
  ArrayOperator getOperator();

  /**
   * Sets the value of the '{@link org.eclipselabs.mongo.query.query.MultiExpressionWhereEntry#getOperator <em>Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Operator</em>' attribute.
   * @see org.eclipselabs.mongo.query.query.ArrayOperator
   * @see #getOperator()
   * @generated
   */
  void setOperator(ArrayOperator value);

  /**
   * Returns the value of the '<em><b>Rhs</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Rhs</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Rhs</em>' containment reference.
   * @see #setRhs(ArrayExpression)
   * @see org.eclipselabs.mongo.query.query.QueryPackage#getMultiExpressionWhereEntry_Rhs()
   * @model containment="true"
   * @generated
   */
  ArrayExpression getRhs();

  /**
   * Sets the value of the '{@link org.eclipselabs.mongo.query.query.MultiExpressionWhereEntry#getRhs <em>Rhs</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Rhs</em>' containment reference.
   * @see #getRhs()
   * @generated
   */
  void setRhs(ArrayExpression value);

} // MultiExpressionWhereEntry
