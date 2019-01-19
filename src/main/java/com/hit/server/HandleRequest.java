package com.hit.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.hit.services.CacheUnitController;
import com.hit.dm.*;

public class HandleRequest<T> extends Object implements Runnable {

	private Socket socket;
	private CacheUnitController<T> controller;

	public HandleRequest(Socket s, CacheUnitController<T> controller) {
		this.controller = controller;
		this.socket = s;

	}

	
	public void run() {
		try {

			ObjectOutputStream writerToClient = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream scan_fromClient = new ObjectInputStream(socket.getInputStream());

			String request = (String) scan_fromClient.readObject();
			Type ref = new TypeToken<Request<DataModel<T>[]>>() {
			}.getType();
			Request<DataModel<T>[]> req = new Gson().fromJson(request, ref);
			HashMap<String, String> answer = new HashMap<String, String>();
			switch (req.getHeaders().get("action").toString().toLowerCase()) {
			case "update":
				answer.put("action", "update");
				answer.put("Succeeded", this.controller.update((DataModel<T>[]) req.getBody()) ? "true" : "false");
				writerToClient.writeObject(answer);
				writerToClient.flush();
				break;
			case "get":
				DataModel<T>[] res= this.controller.get((DataModel<T>[]) req.getBody()); // the Client dosen't support "datamodel"
				answer.put("action", "get");
				answer.put("Succeeded", "true");
				//
				String results="[\n";
				for (DataModel<T> dm: res)
					results=results+ "\t{dataModelId: "+ dm.getDataModelId() +", content: "+dm.getContent()+"},\n";
				results=results+"\n]";
	
					answer.put("results", results);
				//
				writerToClient.writeObject(answer);
				writerToClient.flush();
				break;
			case "delete":
				answer.put("action", "delete");
				answer.put("Succeeded", this.controller.delete((DataModel<T>[]) req.getBody()) ? "true" : "false");
				writerToClient.writeObject(answer);
				writerToClient.flush();
				break;
			case "statistics":
				answer.put("action", "statistics");
				answer.put("Succeeded", "true");
				answer.put("statistics", this.controller.getStatistics());
				writerToClient.writeObject(answer);
				writerToClient.flush();
				break;
			}
			scan_fromClient.close();
			writerToClient.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
