package com.hit.memory;

import java.io.IOException;

import com.hit.algorithm.IAlgoCache;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;

public class CacheUnit<T> extends Object {

	private IAlgoCache<java.lang.Long, DataModel<T>> algo;
	private IDao<Long, DataModel<T>> dao;
	private int swapCounter;

	public CacheUnit(IAlgoCache<Long, DataModel<T>> algo, IDao<Long, DataModel<T>> dao) {
		this.algo = algo;
		this.dao = dao;
		swapCounter = 0;
	}

	@SuppressWarnings("unchecked")
	public DataModel<T>[] getDataModels(Long[] ids) throws ClassNotFoundException, IOException {

		DataModel<T>[] results = new DataModel[ids.length];

		int i = 0;
		for (Long id : ids) {
			if (algo.getElement(id) == null) {
				DataModel<T> dataToRemoveFromCache = algo.putElement(id, dao.find(id));
				if (dataToRemoveFromCache != null) { // if cache full
					dao.save(dataToRemoveFromCache);
					swapCounter++;
				}
			}
			results[i++] = algo.getElement(id);
		}
		return results;
	}

	public int getTotalSwaps() {
		return swapCounter;
	}

}
