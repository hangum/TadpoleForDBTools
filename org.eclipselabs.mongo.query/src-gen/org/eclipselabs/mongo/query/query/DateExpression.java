/**
 */
package org.eclipselabs.mongo.query.query;

import java.util.Date;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Date Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipselabs.mongo.query.query.DateExpression#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipselabs.mongo.query.query.QueryPackage#getDateExpression()
 * @model
 * @generated
 */
public interface DateExpression extends Expression
{
  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(Date)
   * @see org.eclipselabs.mongo.query.query.QueryPackage#getDateExpression_Value()
   * @model
   * @generated
   */
  Date getValue();

  /**
   * Sets the value of the '{@link org.eclipselabs.mongo.query.query.DateExpression#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(Date value);

} // DateExpression
