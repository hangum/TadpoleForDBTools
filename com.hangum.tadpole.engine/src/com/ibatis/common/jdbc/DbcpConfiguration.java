/*
 *  Copyright 2004 Clinton Begin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.ibatis.common.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;

/**
 * Wrapper class to simplify use of DBCP
 */
public class DbcpConfiguration {

  private static final Probe PROBE = ProbeFactory.getProbe();
  private static final String ADD_DRIVER_PROPS_PREFIX = "Driver.";
  private static final int ADD_DRIVER_PROPS_PREFIX_LENGTH = ADD_DRIVER_PROPS_PREFIX.length();
  private DataSource dataSource;

  /**
   * Constructor to supply a map of properties
   *
   * @param properties - the map of configuration properties
   */
  public DbcpConfiguration(Map properties) {
    try {

      dataSource = legacyDbcpConfiguration(properties);
      if (dataSource == null) {
        dataSource = newDbcpConfiguration(properties);
      }

    } catch (Exception e) {
      throw new RuntimeException("Error initializing DbcpDataSourceFactory.  Cause: " + e, e);
    }
  }

  /**
   * Getter for DataSource
   *
   * @return The DataSource
   */
  public DataSource getDataSource() {
    return dataSource;
  }

  private BasicDataSource newDbcpConfiguration(Map map) {
    BasicDataSource basicDataSource = new BasicDataSource();
    Iterator props = map.keySet().iterator();
    while (props.hasNext()) {
      String propertyName = (String) props.next();
      if (propertyName.startsWith(ADD_DRIVER_PROPS_PREFIX)) {
        String value = (String) map.get(propertyName);
        basicDataSource.addConnectionProperty(propertyName.substring(ADD_DRIVER_PROPS_PREFIX_LENGTH), value);
      } else if (PROBE.hasWritableProperty(basicDataSource, propertyName)) {
        String value = (String) map.get(propertyName);
        Object convertedValue = convertValue(basicDataSource, propertyName, value);
        PROBE.setObject(basicDataSource, propertyName, convertedValue);
      }
    }
    return basicDataSource;
  }

  private Object convertValue(Object object, String propertyName, String value) {
    Object convertedValue = value;
    Class targetType = PROBE.getPropertyTypeForSetter(object, propertyName);
    if (targetType == Integer.class || targetType == int.class) {
      convertedValue = Integer.valueOf(value);
    } else if (targetType == Long.class || targetType == long.class) {
      convertedValue = Long.valueOf(value);
    } else if (targetType == Boolean.class || targetType == boolean.class) {
      convertedValue = Boolean.valueOf(value);
    }
    return convertedValue;
  }

  private BasicDataSource legacyDbcpConfiguration(Map map) {
    BasicDataSource basicDataSource = null;
    if (map.containsKey("JDBC.Driver")) {
      basicDataSource = new BasicDataSource();
      String driver = (String) map.get("JDBC.Driver");
      String url = (String) map.get("JDBC.ConnectionURL");
      String username = (String) map.get("JDBC.Username");
      String password = (String) map.get("JDBC.Password");
      
      String validationQuery = (String) map.get("Pool.ValidationQuery");
      String maxActive = (String) map.get("Pool.MaximumActiveConnections");
      String maxIdle = (String) map.get("Pool.MaximumIdleConnections");
      String maxWait = (String) map.get("Pool.MaximumWait");

      basicDataSource.setUrl(url);
      basicDataSource.setDriverClassName(driver);
      basicDataSource.setUsername(username);
      basicDataSource.setPassword(password);
      
      if (notEmpty(validationQuery)) {
        basicDataSource.setValidationQuery(validationQuery);
      }

      if (notEmpty(maxActive)) {
        basicDataSource.setMaxActive(Integer.parseInt(maxActive));
      }

      if (notEmpty(maxIdle)) {
        basicDataSource.setMaxIdle(Integer.parseInt(maxIdle));
      }

      if (notEmpty(maxWait)) {
        basicDataSource.setMaxWait(Integer.parseInt(maxWait));
      }
      
      //
      //
      // tdb setting initialize start
      String initialSize = (String) map.get("initialSize");
      String testOnBorrow = (String) map.get("testOnBorrow");
      String testOnReturn = (String) map.get("testOnReturn");
      String testWhileIdle = (String) map.get("testWhileIdle");
      String timeBetweenEvictionRunsMillis = (String) map.get("timeBetweenEvictionRunsMillis");
      String minEvictableIdleTimeMillis = (String) map.get("minEvictableIdleTimeMillis");
      String numTestsPerEvictionRun = (String) map.get("numTestsPerEvictionRun");
      String removeAbandonedTimeout = (String) map.get("removeAbandonedTimeout");
      String removeAbandoned = (String) map.get("removeAbandoned");
      String logAbandoned = (String) map.get("logAbandoned");
      String minIdle = (String) map.get("minIdle");
      
      
      if (notEmpty(initialSize)) {
        basicDataSource.setInitialSize(Integer.parseInt(initialSize));
      }
      
      if (notEmpty(testOnBorrow)) {
          basicDataSource.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
      }
      if (notEmpty(testOnReturn)) {
          basicDataSource.setTestOnReturn(Boolean.parseBoolean(testOnReturn));
      }
      if (notEmpty(testWhileIdle)) {
          basicDataSource.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));
      }
      if (notEmpty(timeBetweenEvictionRunsMillis)) {
          basicDataSource.setTimeBetweenEvictionRunsMillis(Integer.parseInt(timeBetweenEvictionRunsMillis));
      }
      if (notEmpty(minEvictableIdleTimeMillis)) {
          basicDataSource.setMinEvictableIdleTimeMillis(Integer.parseInt(minEvictableIdleTimeMillis));
      }
      if (notEmpty(numTestsPerEvictionRun)) {
          basicDataSource.setNumTestsPerEvictionRun(Integer.parseInt(numTestsPerEvictionRun));
      }
      if (notEmpty(removeAbandonedTimeout)) {
          basicDataSource.setRemoveAbandonedTimeout(Integer.parseInt(removeAbandonedTimeout));
      }
      if (notEmpty(removeAbandoned)) {
          basicDataSource.setRemoveAbandoned(Boolean.parseBoolean(removeAbandoned));
      }
      if (notEmpty(logAbandoned)) {
          basicDataSource.setLogAbandoned(Boolean.parseBoolean(logAbandoned));
      }
      if (notEmpty(minIdle)) {
          basicDataSource.setMinIdle(Integer.parseInt(minIdle));
      }
      
      // tdb setting initialize end
      //
      //
      
      // add tdb properties - hangum
      List<String> listConnectionInitialize = new ArrayList<String>();
      String initSQL0 = (String) map.get("tdb.connectionInitSqls-0");
      if (notEmpty(initSQL0) && !"TDB.SPECIAL.INITIALSTRING.0".equals(initSQL0)) listConnectionInitialize.add(initSQL0);

      String initSQL1 = (String) map.get("tdb.connectionInitSqls-1");
      if (notEmpty(initSQL1) && !"TDB.SPECIAL.INITIALSTRING.1".equals(initSQL1)) listConnectionInitialize.add(initSQL1);

      if(!listConnectionInitialize.isEmpty()) {
    	  basicDataSource.setConnectionInitSqls(listConnectionInitialize);;
      }
      // add tdb properties - hangum

      Iterator props = map.keySet().iterator();
      while (props.hasNext()) {
        String propertyName = (String) props.next();
        if (propertyName.startsWith(ADD_DRIVER_PROPS_PREFIX)) {
          String value = (String) map.get(propertyName);
          basicDataSource.addConnectionProperty(propertyName.substring(ADD_DRIVER_PROPS_PREFIX_LENGTH), value);
        }
      }
    }
    return basicDataSource;
  }

  private boolean notEmpty(String s) {
    return s != null && s.length() > 0;
  }

}
