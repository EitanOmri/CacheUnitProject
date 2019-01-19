package com.hit.services;

import com.hit.dm.DataModel;

public class CacheUnitController<T> extends Object {

	private CacheUnitService<T> cacheUnitService;

	public CacheUnitController() {
		this.cacheUnitService = new CacheUnitService<>();
	}

	public boolean update(DataModel<T>[] dataModels) {
		return this.cacheUnitService.update(dataModels);
	}

	public boolean delete(DataModel<T>[] dataModels) {
		return this.cacheUnitService.delete(dataModels);
	}

	public DataModel<T>[] get(DataModel<T>[] dataModels) {
		return this.cacheUnitService.get(dataModels);
	}

	public String getStatistics() {
		return this.cacheUnitService.getStatistics();
	}
	public void shutdown() {
		this.cacheUnitService.shutdown();
	}

}
