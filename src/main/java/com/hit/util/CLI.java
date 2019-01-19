package com.hit.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Scanner;

public class CLI extends Observable implements Runnable {
	private Scanner in;
	private PrintWriter out;

	public CLI(InputStream in, OutputStream out) {
		this.in = new Scanner(in);
		this.out = new PrintWriter(out, true);
	}

	public void write(String string) {
		if (string.toLowerCase().equals("start"))
			out.printf("%s\n", "starting server.....\n");
		else if (string.toLowerCase().equals("stop"))
			out.printf("%s\n", "shutdown server\n");
		else
			out.printf("%s\n", "Not a valid command\n");
	}

	@Override
	public void run() {
		String input = null;
		while (true) {
			out.printf("%s\n", "Please enter command (start/stop)");
			input = in.next();
			write(input);
			if (input.equals("stop") || input.equals("start")) {
				setChanged(); // for notifyObserver
				notifyObservers(input);// update all the observers
			}
		}
	}
}