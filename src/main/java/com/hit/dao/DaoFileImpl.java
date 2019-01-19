package com.hit.dao;

import com.hit.dm.DataModel;

import java.io.*;
import java.util.HashMap;

public class DaoFileImpl<T> extends Object implements IDao<Long, DataModel<T>> {
	private String filePath;
	private HashMap<Long, DataModel<T>> data;

	@SuppressWarnings("unchecked")
	public DaoFileImpl(String filePath) {
		this.filePath = filePath;
		try { 
			//check if file have pre data
			FileInputStream fis = new FileInputStream(filePath);
			ObjectInputStream in = new ObjectInputStream(fis);
			data = (HashMap<Long, DataModel<T>>) in.readObject();
			in.close();
			if(data.size()!=1000) {// create data in file
			data = new HashMap<>();
			for (int i = 0; i < 1000; i++)
				data.put((long) (i + 1), new DataModel<T>((long) (i + 1), null));
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
			out.writeObject(data);
			out.close();
			}
		} catch (EOFException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void delete(DataModel<T> t) {
		if (find(t.getDataModelId()) != null) { // only in case where the obj exist
			try {
				FileInputStream fis = new FileInputStream(filePath);
				ObjectInputStream in = new ObjectInputStream(fis);
				data = (HashMap<Long, DataModel<T>>) in.readObject();
				in.close();
				data.remove(t.getDataModelId());
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
				out.writeObject(data);
				out.close();
			} catch (EOFException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public DataModel<T> find(Long id) {
		try {

			Reader reader = new FileReader(filePath);
			if (reader.read() == -1) { // file is empty
				reader.close();
				return null;

			} else {
				reader.close();
				FileInputStream fis = new FileInputStream(filePath);
				ObjectInputStream in = new ObjectInputStream(fis);
				data = (HashMap<Long, DataModel<T>>) in.readObject();
				in.close();
				fis.close();
			}
			if (data.containsKey(id)) // O(1)
				return data.get(id);
			return null;
		} catch (EOFException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void save(DataModel<T> t) {
		try {

			Reader reader = new FileReader(filePath);
			if (reader.read() == -1) // file is empty
				data = new HashMap<>();
			else {
				FileInputStream fis = new FileInputStream(filePath);
				ObjectInputStream in = new ObjectInputStream(fis);
				data = (HashMap<Long, DataModel<T>>) in.readObject();
				in.close();
				fis.close();
			}
			reader.close();
			data.replace(t.getDataModelId(), (DataModel<T>) t);
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
			out.writeObject(data);
			out.close();
		} catch (EOFException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

	}
}
