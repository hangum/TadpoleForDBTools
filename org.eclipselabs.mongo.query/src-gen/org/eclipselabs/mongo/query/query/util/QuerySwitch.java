/**
 */
package org.eclipselabs.mongo.query.query.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipselabs.mongo.query.query.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipselabs.mongo.query.query.QueryPackage
 * @generated
 */
public class QuerySwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static QueryPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QuerySwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = QueryPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @parameter ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case QueryPackage.MODEL:
      {
        Model model = (Model)theEObject;
        T result = caseModel(model);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.DATABASE:
      {
        Database database = (Database)theEObject;
        T result = caseDatabase(database);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.WHERE_ENTRY:
      {
        WhereEntry whereEntry = (WhereEntry)theEObject;
        T result = caseWhereEntry(whereEntry);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.EXPRESSION_WHERE_ENTRY:
      {
        ExpressionWhereEntry expressionWhereEntry = (ExpressionWhereEntry)theEObject;
        T result = caseExpressionWhereEntry(expressionWhereEntry);
        if (result == null) result = caseWhereEntry(expressionWhereEntry);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.SINGLE_EXPRESSION_WHERE_ENTRY:
      {
        SingleExpressionWhereEntry singleExpressionWhereEntry = (SingleExpressionWhereEntry)theEObject;
        T result = caseSingleExpressionWhereEntry(singleExpressionWhereEntry);
        if (result == null) result = caseExpressionWhereEntry(singleExpressionWhereEntry);
        if (result == null) result = caseWhereEntry(singleExpressionWhereEntry);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.EXPRESSION:
      {
        Expression expression = (Expression)theEObject;
        T result = caseExpression(expression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.REPLACABLE_VALUE:
      {
        ReplacableValue replacableValue = (ReplacableValue)theEObject;
        T result = caseReplacableValue(replacableValue);
        if (result == null) result = caseExpression(replacableValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.DOUBLE_EXPRESSION:
      {
        DoubleExpression doubleExpression = (DoubleExpression)theEObject;
        T result = caseDoubleExpression(doubleExpression);
        if (result == null) result = caseExpression(doubleExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.LONG_EXPRESSION:
      {
        LongExpression longExpression = (LongExpression)theEObject;
        T result = caseLongExpression(longExpression);
        if (result == null) result = caseExpression(longExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.STRING_EXPRESSION:
      {
        StringExpression stringExpression = (StringExpression)theEObject;
        T result = caseStringExpression(stringExpression);
        if (result == null) result = caseExpression(stringExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.NULL_EXPRESSION:
      {
        NullExpression nullExpression = (NullExpression)theEObject;
        T result = caseNullExpression(nullExpression);
        if (result == null) result = caseExpression(nullExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.DATE_EXPRESSION:
      {
        DateExpression dateExpression = (DateExpression)theEObject;
        T result = caseDateExpression(dateExpression);
        if (result == null) result = caseExpression(dateExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.BOOLEAN_EXPRESSION:
      {
        BooleanExpression booleanExpression = (BooleanExpression)theEObject;
        T result = caseBooleanExpression(booleanExpression);
        if (result == null) result = caseExpression(booleanExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.MULTI_EXPRESSION_WHERE_ENTRY:
      {
        MultiExpressionWhereEntry multiExpressionWhereEntry = (MultiExpressionWhereEntry)theEObject;
        T result = caseMultiExpressionWhereEntry(multiExpressionWhereEntry);
        if (result == null) result = caseExpressionWhereEntry(multiExpressionWhereEntry);
        if (result == null) result = caseWhereEntry(multiExpressionWhereEntry);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.ARRAY_EXPRESSION:
      {
        ArrayExpression arrayExpression = (ArrayExpression)theEObject;
        T result = caseArrayExpression(arrayExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.DOUBLE_ARRAY_EXPRESSION:
      {
        DoubleArrayExpression doubleArrayExpression = (DoubleArrayExpression)theEObject;
        T result = caseDoubleArrayExpression(doubleArrayExpression);
        if (result == null) result = caseArrayExpression(doubleArrayExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.LONG_ARRAY_EXPRESSION:
      {
        LongArrayExpression longArrayExpression = (LongArrayExpression)theEObject;
        T result = caseLongArrayExpression(longArrayExpression);
        if (result == null) result = caseArrayExpression(longArrayExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.STRING_ARRAY_EXPRESSION:
      {
        StringArrayExpression stringArrayExpression = (StringArrayExpression)theEObject;
        T result = caseStringArrayExpression(stringArrayExpression);
        if (result == null) result = caseArrayExpression(stringArrayExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.NULL_ARRAY_EXPRESSION:
      {
        NullArrayExpression nullArrayExpression = (NullArrayExpression)theEObject;
        T result = caseNullArrayExpression(nullArrayExpression);
        if (result == null) result = caseArrayExpression(nullArrayExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.DATE_ARRAY_EXPRESSION:
      {
        DateArrayExpression dateArrayExpression = (DateArrayExpression)theEObject;
        T result = caseDateArrayExpression(dateArrayExpression);
        if (result == null) result = caseArrayExpression(dateArrayExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.BOOLEAN_ARRAY_EXPRESSION:
      {
        BooleanArrayExpression booleanArrayExpression = (BooleanArrayExpression)theEObject;
        T result = caseBooleanArrayExpression(booleanArrayExpression);
        if (result == null) result = caseArrayExpression(booleanArrayExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.OR_WHERE_ENTRY:
      {
        OrWhereEntry orWhereEntry = (OrWhereEntry)theEObject;
        T result = caseOrWhereEntry(orWhereEntry);
        if (result == null) result = caseWhereEntry(orWhereEntry);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case QueryPackage.AND_WHERE_ENTRY:
      {
        AndWhereEntry andWhereEntry = (AndWhereEntry)theEObject;
        T result = caseAndWhereEntry(andWhereEntry);
        if (result == null) result = caseWhereEntry(andWhereEntry);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseModel(Model object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Database</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Database</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDatabase(Database object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Where Entry</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Where Entry</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseWhereEntry(WhereEntry object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Expression Where Entry</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Expression Where Entry</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseExpressionWhereEntry(ExpressionWhereEntry object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Single Expression Where Entry</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Single Expression Where Entry</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSingleExpressionWhereEntry(SingleExpressionWhereEntry object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseExpression(Expression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Replacable Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Replacable Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseReplacableValue(ReplacableValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Double Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Double Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDoubleExpression(DoubleExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Long Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Long Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLongExpression(LongExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>String Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>String Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStringExpression(StringExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Null Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Null Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNullExpression(NullExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Date Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Date Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDateExpression(DateExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Boolean Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Boolean Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBooleanExpression(BooleanExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Multi Expression Where Entry</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Multi Expression Where Entry</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMultiExpressionWhereEntry(MultiExpressionWhereEntry object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Array Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Array Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseArrayExpression(ArrayExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Double Array Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Double Array Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDoubleArrayExpression(DoubleArrayExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Long Array Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Long Array Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLongArrayExpression(LongArrayExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>String Array Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>String Array Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStringArrayExpression(StringArrayExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Null Array Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Null Array Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNullArrayExpression(NullArrayExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Date Array Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Date Array Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDateArrayExpression(DateArrayExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Boolean Array Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Boolean Array Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBooleanArrayExpression(BooleanArrayExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Or Where Entry</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Or Where Entry</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOrWhereEntry(OrWhereEntry object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>And Where Entry</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>And Where Entry</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAndWhereEntry(AndWhereEntry object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //QuerySwitch
