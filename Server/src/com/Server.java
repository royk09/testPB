/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author royk09
 */
public class Server {

    /**
     * @param args the command line arguments
     */
   private static final int port= 49002;

	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = null;
		System.out.println("SERVER START");

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Couldn't listen port - " + port);
			System.exit(-1);
		}

		String depositsFile = "deposits.xml";
		Deposits deposits = new Deposits();
		
		try{
			deposits.deserializeFile();
		} catch (FileNotFoundException e){
			deposits = Deposits.createListDeposites();
			deposits.serializeFile();
			deposits.deserializeFile();
		}
		 

		Socket clientSocket = null;
		int idClient = 1;

		while (true) {
			try {
				System.out.println("Waiting for clients");
				clientSocket = serverSocket.accept();
				System.out.println("Client was connected");

				new ClientThread(clientSocket, idClient+1, deposits).start();
			} catch (IOException e) {
				System.out.println("ERROR accept!!!");
			}
		}
	}
    
}
