package com.hit.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.hit.services.CacheUnitController;

public class Server extends Object implements Observer {

	private ServerSocket listenSocket;
	private Boolean serverUp;
	private final Executor executor;
	private CacheUnitController<String> cuc;

	public Server() {
		serverUp = false;
		listenSocket = null;
		executor = Executors.newCachedThreadPool();
		cuc = new CacheUnitController<String>();
		try {
			this.listenSocket = new ServerSocket(12345);
		} catch (IOException e) {
			System.err.println("An exception occurred while creating the listen socket: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void start() {
		try {

			while (serverUp) {
				final Socket clientSocket = this.listenSocket.accept();
				this.executor.execute(new HandleRequest<String>(clientSocket, cuc));
			}
		} catch (

		Exception e) {
			((ExecutorService) executor).shutdown();
			System.out.println("tiered of waiting for connection");
		} finally {
			try {
				if (listenSocket != null)
					listenSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				((ExecutorService) executor).shutdown();
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg.equals("start")) // 'start' or 'stop'
			serverUp = true;
		else {
			if(serverUp)
				this.cuc.shutdown();
			serverUp = false;
		}
			start();
	}

}
