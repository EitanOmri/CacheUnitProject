package com.hit.memory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.EOFException;
import java.io.IOException;

import org.junit.Test;

import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;

public class CacheUnitTest {

	private com.hit.algorithm.IAlgoCache<java.lang.Long, DataModel<String>> algo;
	private IDao<Long, DataModel<String>> dao;
	private CacheUnit<String> cacheUnit;

	@Test
	public void test() {
		algo = new LRUAlgoCacheImpl<>(5);
		dao = new DaoFileImpl<>("src/main/resources/CacheUnitTestFile.txt");
		cacheUnit = new CacheUnit<>(algo, dao);
		DataModel<String> dataModel1 = new DataModel<>((long) 1, "a");
		DataModel<String> dataModel2 = new DataModel<>((long) 2, "b");
		DataModel<String> dataModel3 = new DataModel<>((long) 3, "c");
		DataModel<String> dataModel4 = new DataModel<>((long) 4, "d");
		DataModel<String> dataModel5 = new DataModel<>((long) 5, "e");
		DataModel<String> dataModel6 = new DataModel<>((long) 6, "f");
		dao.save(dataModel1);
		dao.save(dataModel2);
		dao.save(dataModel3);
		dao.save(dataModel4);
		dao.save(dataModel5);
		dao.save(dataModel6);

		DataModel<String> dm = dao.find((long) 1);
		assertEquals(dm, dataModel1);

		dao.delete(dataModel1);
		assertNull(dao.find((long) 1));
		dao.save(dataModel1);
		Long[] arr = { (long) 1, (long) 2, (long) 3, (long) 4, (long) 5, (long) 6 };

		try {
			DataModel<String>[] results = cacheUnit.getDataModels(arr);
			assertEquals(6, results.length);

		} catch (EOFException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		assertEquals(dataModel2, algo.getElement((long) 2));
		assertEquals(dataModel3, algo.getElement((long) 3));
		assertEquals(dataModel4, algo.getElement((long) 4));
		assertEquals(dataModel5, algo.getElement((long) 5));
		assertEquals(dataModel6, algo.getElement((long) 6));
		assertNull(algo.getElement((long) 1));
		algo.removeElement((long) 4);
		Long[] arr1 = { (long) 1, (long) 2, (long) 3, (long) 5, (long) 6 };
		try {
			DataModel<String>[] results = cacheUnit.getDataModels(arr1);
			assertEquals(5, results.length);
		} catch (EOFException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		assertEquals(dataModel2, algo.getElement((long) 2));
		assertEquals(dataModel3, algo.getElement((long) 3));
		assertEquals(dataModel5, algo.getElement((long) 5));
		assertEquals(dataModel6, algo.getElement((long) 6));

	}

}
