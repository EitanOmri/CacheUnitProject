package com.hit.services;

import java.io.IOException;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;

public class CacheUnitService<T> extends Object {
	private IAlgoCache<Long, DataModel<String>> algo;
	private IDao<Long, DataModel<String>> dao;
	private CacheUnit<String> cacheUnit;
	private static int totalRequest = 0;
	private static int totalDataModels = 0;
	private int Capcity = 300;
	
	public CacheUnitService() {
		algo = new LRUAlgoCacheImpl<>(Capcity);
		dao = new DaoFileImpl<>("src/main/resources/CacheUnitFile.txt");
		cacheUnit = new CacheUnit<>(algo, dao);
	}
	public void shutdown() {
		for(int i=0;i<1000;i++) {
			DataModel<String> dm=algo.getElement((long)(i+1));
			if(dm!=null)
				dao.save(dm);				
		}
			
		
	}
	@SuppressWarnings("unchecked")
	public boolean update(DataModel<T>[] dataModels) {
		totalRequest++;
		totalDataModels += dataModels.length;
		Long[] ids = new Long[dataModels.length];
		for (int i = 0; i < dataModels.length; i++) {
			ids[i] = dataModels[i].getDataModelId();
		}
		try {
			DataModel<T>[] tempDataModels = (DataModel<T>[]) cacheUnit.getDataModels(ids);
			for (int i = 0; i < dataModels.length; i++)
				tempDataModels[i].setContent(dataModels[i].getContent());
			return true;

		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean delete(DataModel<T>[] dataModels) {
		totalRequest++;
		totalDataModels += dataModels.length;
		java.lang.Long[] ids = new java.lang.Long[dataModels.length];
		int i = 0;
		for (DataModel<T> dm : dataModels) {
			ids[i++] = dm.getDataModelId();
		}
		try {
			DataModel<T>[] tempDataModels = (DataModel<T>[]) cacheUnit.getDataModels(ids);
			for (DataModel<T> dm : tempDataModels) {
				dm.setContent(null);
				return true;
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public DataModel<T>[] get(DataModel<T>[] dataModels) {
		totalRequest++;
		totalDataModels += dataModels.length;
		java.lang.Long[] ids = new java.lang.Long[dataModels.length];
		int i = 0;
		for (DataModel<T> dm : dataModels) {
			ids[i++] = dm.getDataModelId();
		}
		try {
			return (DataModel<T>[]) cacheUnit.getDataModels(ids);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getStatistics() {
		String algoName;
		if(algo.getClass().toString().toUpperCase().contains("LRU"))
			algoName="LRU";
		else
			if(algo.getClass().toString().toUpperCase().contains("MRU"))
				algoName="MRU";
			else
				if(algo.getClass().toString().toUpperCase().contains("RANDOM"))
					algoName="RANDOM";
				else
					algoName="Second Chance";
		
		return "Capacity: " + Capcity + "\nAlgorithm: "
				+ algoName+ "\nTotal number of requests: "
				+ totalRequest + "\nTotal number of DataModels (GET/DELETE/UPDATE requests): " + totalDataModels + "\nTotal number of DataModel swaps (from Cache to Disk): "
				+ this.cacheUnit.getTotalSwaps();
	}

}
