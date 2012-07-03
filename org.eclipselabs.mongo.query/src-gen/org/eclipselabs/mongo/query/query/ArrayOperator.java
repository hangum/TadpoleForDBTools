/**
 */
package org.eclipselabs.mongo.query.query;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Array Operator</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipselabs.mongo.query.query.QueryPackage#getArrayOperator()
 * @model
 * @generated
 */
public enum ArrayOperator implements Enumerator
{
  /**
   * The '<em><b>Mongo all</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MONGO_ALL_VALUE
   * @generated
   * @ordered
   */
  MONGO_ALL(0, "mongo_all", "$all"),

  /**
   * The '<em><b>Mongo in</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MONGO_IN_VALUE
   * @generated
   * @ordered
   */
  MONGO_IN(1, "mongo_in", "$in"),

  /**
   * The '<em><b>Sql in</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SQL_IN_VALUE
   * @generated
   * @ordered
   */
  SQL_IN(2, "sql_in", "in"),

  /**
   * The '<em><b>Mongo nin</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MONGO_NIN_VALUE
   * @generated
   * @ordered
   */
  MONGO_NIN(3, "mongo_nin", "$nin"),

  /**
   * The '<em><b>Sql not In</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SQL_NOT_IN_VALUE
   * @generated
   * @ordered
   */
  SQL_NOT_IN(4, "sql_notIn", "not in");

  /**
   * The '<em><b>Mongo all</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Mongo all</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #MONGO_ALL
   * @model name="mongo_all" literal="$all"
   * @generated
   * @ordered
   */
  public static final int MONGO_ALL_VALUE = 0;

  /**
   * The '<em><b>Mongo in</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Mongo in</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #MONGO_IN
   * @model name="mongo_in" literal="$in"
   * @generated
   * @ordered
   */
  public static final int MONGO_IN_VALUE = 1;

  /**
   * The '<em><b>Sql in</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Sql in</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #SQL_IN
   * @model name="sql_in" literal="in"
   * @generated
   * @ordered
   */
  public static final int SQL_IN_VALUE = 2;

  /**
   * The '<em><b>Mongo nin</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Mongo nin</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #MONGO_NIN
   * @model name="mongo_nin" literal="$nin"
   * @generated
   * @ordered
   */
  public static final int MONGO_NIN_VALUE = 3;

  /**
   * The '<em><b>Sql not In</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Sql not In</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #SQL_NOT_IN
   * @model name="sql_notIn" literal="not in"
   * @generated
   * @ordered
   */
  public static final int SQL_NOT_IN_VALUE = 4;

  /**
   * An array of all the '<em><b>Array Operator</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final ArrayOperator[] VALUES_ARRAY =
    new ArrayOperator[]
    {
      MONGO_ALL,
      MONGO_IN,
      SQL_IN,
      MONGO_NIN,
      SQL_NOT_IN,
    };

  /**
   * A public read-only list of all the '<em><b>Array Operator</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<ArrayOperator> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Array Operator</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ArrayOperator get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      ArrayOperator result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Array Operator</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ArrayOperator getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      ArrayOperator result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Array Operator</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ArrayOperator get(int value)
  {
    switch (value)
    {
      case MONGO_ALL_VALUE: return MONGO_ALL;
      case MONGO_IN_VALUE: return MONGO_IN;
      case SQL_IN_VALUE: return SQL_IN;
      case MONGO_NIN_VALUE: return MONGO_NIN;
      case SQL_NOT_IN_VALUE: return SQL_NOT_IN;
    }
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final int value;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String name;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String literal;

  /**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private ArrayOperator(int value, String name, String literal)
  {
    this.value = value;
    this.name = name;
    this.literal = literal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLiteral()
  {
    return literal;
  }

  /**
   * Returns the literal value of the enumerator, which is its string representation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    return literal;
  }
  
} //ArrayOperator
