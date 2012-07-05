
package org.eclipselabs.mongo.query;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class MongoSQLStandaloneSetup extends MongoSQLStandaloneSetupGenerated{

	public static void doSetup() {
		new MongoSQLStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

