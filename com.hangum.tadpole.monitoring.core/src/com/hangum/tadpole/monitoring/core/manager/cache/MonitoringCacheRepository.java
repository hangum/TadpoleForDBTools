package com.hangum.tadpole.monitoring.core.manager.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hangum.tadpole.engine.query.dao.system.monitoring.MonitoringResultDAO;

/**
 * monitoring cache repository
 * 
 * @author hangum
 *
 */
public class MonitoringCacheRepository {
	private static MonitoringCacheRepository instance = null;
	
	/**
	 * temporary cache pattern map
	 * 
	 * key is user Email
	 * value is monitoring data
	 */
	private Map<String, List<MonitoringResultDAO>> temporaryCacheMap = new HashMap<>();//CacheBuilder.newBuilder().maximumSize(2000).build();
	private MonitoringCacheRepository() {}

	public static MonitoringCacheRepository getInstance() {
		if(instance == null) {
			instance = new MonitoringCacheRepository();
		}
		return instance;
	}
	

	public void put(String key, List<MonitoringResultDAO> value) {
		temporaryCacheMap.put(key, value);
	}
	
	public List<MonitoringResultDAO> get(String key) {
		return temporaryCacheMap.get(key);
	}
}
