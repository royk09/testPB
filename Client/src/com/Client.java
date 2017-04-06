/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author admin
 */
public class Client {

   private static final int port = 49002;
	public static void main(String[] args) throws IOException {

		System.out.println("Client Start");

		Socket fromserver = null;
                String localhost = "localhost";

		try {
			fromserver = new Socket(localhost, port);
		} catch (ConnectException e) {
			System.out.println("Can't connect to server.");
			System.exit(-1);
		}

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		ObjectOutputStream outputStream = new ObjectOutputStream(fromserver.getOutputStream());
		ObjectInputStream inputStream = new ObjectInputStream(fromserver.getInputStream());

		System.out.println("Commands what you can use:\n" + "list\n" + "sum\n" + "count\n"
                + "info account \'account id\'\n" + "info depositor \'depositor\'\n" + "show type \'type\'\n"
                + "show bank \'name\'\n" + "add <bank name> <country> <type> <depositor> <account Id> <amount on deposit> <profitability> \'time constraints\'\n" + "delete \'account id\'\n");

		String clientInput;
		while ((clientInput = input.readLine()) != null) {
			try {

				if (clientInput.equalsIgnoreCase("exit")) {
					break;
				}

				ClientInput serverCommand = new ClientInput(clientInput);
				outputStream.writeObject(serverCommand);

				ClientInput serverAnswer = null;
				try {
					serverAnswer = (ClientInput) inputStream.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					System.out.println("Can't read message from server.");
					continue;
				}

				switch (serverAnswer.getInputType()) {
				case text:
					String messageFromServer = serverAnswer.getClientInputText();
					System.out.println(messageFromServer);
					break;

				case deposit:
					OneDeposit deposit = serverAnswer.getOneDeposit();
					System.out.println("Result of query: one deposit:");
					System.out.println(deposit.toString());
					break;

				case listDeposits:
					Deposits deposits = serverAnswer.getDeposits();
					System.out.println("Result of query: list of deposits (" + deposits.getListDeposites().size() + "):");
					for (OneDeposit depos : deposits.getListDeposites()) {
						System.out.println(depos.toString());
					}
					break;
				default:
					System.out.println("Incorrect Answer From Server");
					break;
				}

			} catch (SocketException e) {
				System.out.println("SocketException.");
			}
		}

		outputStream.close();
		inputStream.close();

		input.close();
		fromserver.close();
	}
    
}
