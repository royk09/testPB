/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.OneDeposit.Type;

/**
 *
 * @author royk09
 */
public class ClientThread extends Thread {

    protected Socket socket;
    protected int clientId;
    protected Deposits deposits;

    public ClientThread(Socket socket, int clientId, Deposits deposits) {
        this.socket = socket;
        this.clientId = clientId;
        this.deposits = deposits;
    }

    public void run() {

        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;

        try {
            inputStream = new ObjectInputStream(this.socket.getInputStream());
            outputStream = new ObjectOutputStream(this.socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
                            try {
                ClientInput clientInput = (ClientInput) inputStream.readObject();
                System.out.println(clientInput.getClass());

                if (clientInput.getInputType() == ClientInput.InputType.text) {
                    String clientInputText = clientInput.getClientInputText().trim().replaceAll("\\s+", " ");

                    if (clientInputText.isEmpty()) {
                        outputStream.writeObject(new ClientInput(""));
                        continue;
                    }

                    String[] splitClientText = clientInputText.split("\\s");

                    switch (splitClientText[0]) {
                        case "exit":
                            socket.close();
                            return;

                        case "list":
                            outputStream.reset();
                            outputStream.writeObject(new ClientInput(deposits));
                            break;

                        case "sum":
                            double sumAmountDeposits = 0;
                            for (OneDeposit oneDeposit : deposits.getListDeposites()) {
                                sumAmountDeposits += oneDeposit.getAmountDeposit();
                            }
                            outputStream.writeObject(new ClientInput("Sum deposits: " + sumAmountDeposits));
                            break;

//                        case "types":
//                            StringBuilder resultTypes = new StringBuilder();
//                            for (Deposit.Type depType : Deposit.Type.values()) {
//                                resultTypes.append(depType.name() + '\n');
//                            }
//
//                            outputStream.writeObject(new ClientInput("Types:\n" + resultTypes.toString()));
//                            break;
                        case "count":
                            outputStream.writeObject(
                                    new ClientInput("Deposits count: " + deposits.getListDeposites().size()));
                            break;

                        case "add":
                            String strIncorrectAddCommand = "Incorrect command. Correct command: "
                                    + "add <bank name> <country> <type> <depositor> <account Id> <amount on deposit> <profitability> <time constraints>";

                            if (splitClientText.length == 9) {

                                String bankName = splitClientText[1].trim();
                                String country = splitClientText[2].trim();

                                String strType = splitClientText[3].trim();
                                OneDeposit.Type type;
                                try {
                                    type = OneDeposit.Type.valueOf(strType);
                                } catch (IllegalArgumentException e) {
                                    StringBuilder possibleDepositTypes = new StringBuilder();
                                    for (Type possibleType : OneDeposit.Type.class.getEnumConstants()) {
                                        possibleDepositTypes.append(possibleType.toString() + '\n');
                                    }
                                    outputStream.writeObject(new ClientInput(
                                            strIncorrectAddCommand + '\n' + "ERROR in type."));
                                    break;
                                }

                                String depositor = splitClientText[4].trim();
                                Pattern pattern = Pattern.compile("\\s");
                                Matcher matcher = pattern.matcher(depositor);
                                if (matcher.find()) {
                                    outputStream.writeObject(new ClientInput(strIncorrectAddCommand + '\n'
                                            + "ERROR in depositor."));
                                    break;
                                }

                                String strAccountId = splitClientText[5].trim();
                                int accountId;
                                try {
                                    accountId = Integer.parseInt(strAccountId);
                                } catch (NumberFormatException e) {
                                    outputStream.writeObject(
                                            new ClientInput(strIncorrectAddCommand + '\n' + "ERROR in account Id."));
                                    break;
                                }
                                if (accountId < 0) {
                                    outputStream.writeObject(new ClientInput(strIncorrectAddCommand + '\n'
                                            + "ERROR in account Id"));
                                    break;
                                }

                                String strAmountOfDeposit = splitClientText[6].trim();
                                double amountOfDeposit;
                                try {
                                    amountOfDeposit = Double.parseDouble(strAmountOfDeposit);
                                } catch (NumberFormatException e) {
                                    outputStream.writeObject(new ClientInput(strIncorrectAddCommand + '\n'
                                            + "ERROR in amount on deposit."));
                                    break;
                                }
                                if (amountOfDeposit <= 0) {
                                    outputStream.writeObject(
                                            new ClientInput(strIncorrectAddCommand + '\n' + "ERROR in amount on deposit."
                                                    + '\n' + "Must be > 0."));
                                    break;
                                }

                                String strProfitability = splitClientText[7].trim();
                                double profitability;
                                try {
                                    profitability = Double.parseDouble(strProfitability);
                                } catch (NumberFormatException e) {
                                    outputStream.writeObject(
                                            new ClientInput(strIncorrectAddCommand + '\n' + "ERROR in profitability."));
                                    break;
                                }
                                if (profitability <= 0) {
                                    outputStream.writeObject(new ClientInput(strIncorrectAddCommand + '\n'
                                            + "ERROR in profitability." + '\n' + "Must be > 0."));
                                    break;
                                }

                                String strTimeConstraints = splitClientText[8].trim();
                                int timeConstraints = 0;

                                try {
                                    timeConstraints = Integer.parseInt(strTimeConstraints);
                                } catch (NumberFormatException e) {
                                    outputStream.writeObject(new ClientInput(strIncorrectAddCommand + '\n'
                                            + "ERROR in time constraints."));
                                    break;
                                }

                                boolean isSameDepositExist = false;
                                for (OneDeposit deposit : deposits.getListDeposites()) {
                                    if (deposit.getAccountId() == accountId) {
                                        isSameDepositExist = true;
                                        break;
                                    }
                                }

                                if (isSameDepositExist) {
                                    outputStream.writeObject(new ClientInput(
                                            "Deposit with input account id is already exist(" + accountId + ")"));
                                    break;
                                }

                                OneDeposit deposit = new OneDeposit(bankName, country, type, depositor, accountId, amountOfDeposit,
                                        profitability, timeConstraints);

                                deposits.addDeposit(deposit);

                                outputStream.writeObject(new ClientInput("OK. Deposit ADD!!"));
                            } else {
                                outputStream.writeObject(new ClientInput(strIncorrectAddCommand));
                            }

                            break;

                        case "info":

                            if (splitClientText.length == 3) {

                                switch (splitClientText[1]) {

                                    case "account":
                                        int accountId;
                                        try {
                                            accountId = Integer.parseInt(splitClientText[2]);
                                        } catch (NumberFormatException e) {
                                            outputStream.writeObject(new ClientInput("account id must be integer."));
                                            break;
                                        }

                                        OneDeposit depositByAccountId = getOneDepositByAccountId(accountId);

                                        if (depositByAccountId != null) {
                                            outputStream.writeObject(new ClientInput(depositByAccountId));
                                        } else {
                                            outputStream.writeObject(
                                                    new ClientInput("Can't find deposit with specified account id."));
                                        }
                                        break;

                                    case "depositor":
                                        String depositor = splitClientText[2];

                                        Deposits deposits = getMultDepositsByDepositorName(depositor);

                                        if (deposits.getListDeposites().size() > 0) {
                                            outputStream.reset();
                                            outputStream.writeObject(new ClientInput(deposits));
                                        } else {
                                            outputStream.writeObject(
                                                    new ClientInput("No such depositor."));
                                        }

                                        break;
                                }
                            } else {
                                outputStream.writeObject(new ClientInput(
                                        "Incorrect command. Correct example: \'info depositor Sidorov A.A.\'"));
                            }

                            break;

                        case "show":

                            if (splitClientText.length == 3) {

                                switch (splitClientText[1]) {

                                    case "type":
                                        String depositType = splitClientText[2];
                                        Deposits deposits1 = getMultDepositsByDepositType(
                                                OneDeposit.Type.valueOf(depositType));
                                        if (deposits1.getListDeposites().size() > 0) {
                                            outputStream.reset();
                                            outputStream.writeObject(new ClientInput(deposits1));
                                        } else {
                                            outputStream.writeObject(new ClientInput("No deposits with such type."));
                                        }

                                        break;

                                    case "bank":
                                        String bankTitle = splitClientText[2];
                                        Deposits deposits2 = getMultDepositsByBankTitle(bankTitle);
                                        if (deposits2.getListDeposites().size() > 0) {
                                            outputStream.reset();
                                            outputStream.writeObject(new ClientInput(deposits2));
                                        } else {
                                            outputStream.writeObject(
                                                    new ClientInput("No deposits with such bank title."));
                                        }

                                        break;
                                }
                            } else {
                                outputStream.writeObject(new ClientInput(
                                        "Incorrect command."));
                            }

                            break;

                        case "delete":
                            if (splitClientText.length == 2) {
                                int accountId;
                                try {
                                    accountId = Integer.parseInt(splitClientText[1]);
                                } catch (NumberFormatException e) {
                                    outputStream.writeObject(new ClientInput("account id must be integer!!!."));
                                    break;
                                }

                                boolean deleteResult = deleteOneDepositByAccountID(accountId);
                                if (deleteResult) {
                                    outputStream.writeObject(new ClientInput("Delete OK."));
                                } else {
                                    outputStream.writeObject(new ClientInput("Delete ERR."));
                                }
                            } else {
                                outputStream.writeObject(new ClientInput(
                                        "Incorrect command."));
                                break;
                            }

                            break;

                        default:
                            unknownCommand(outputStream);
                            break;
                    }
                } else {
                    System.out.println("Text should input text ONLY");
                }
            } catch (SocketException e) {

                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }

        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unknownCommand(ObjectOutputStream objOutStream) throws IOException {
        ClientInput msg = new ClientInput("Unknown command.");
        objOutStream.writeObject(msg);
    }
    

    private OneDeposit getOneDepositByAccountId(int accountId) {
        for (OneDeposit dep : deposits.getListDeposites()) {
            if (dep.getAccountId() == accountId) {
                return dep;
            }
        }
        return null;
    }
    private Deposits getMultDepositsByDepositType(OneDeposit.Type depositType) {
        List<OneDeposit> depositList = new ArrayList<OneDeposit>();
        for (OneDeposit dep : deposits.getListDeposites()) {
            if (dep.getType() == depositType) {
                depositList.add(dep);
            }
        }
        return new Deposits(depositList);
    }
    
    private Deposits getMultDepositsByDepositorName(String depositor) {
        List<OneDeposit> depositList = new ArrayList<OneDeposit>();

        for (OneDeposit deposit : deposits.getListDeposites()) {
            if (deposit.getDepositor().equalsIgnoreCase(depositor)) {
                depositList.add(deposit);
            }
        }
        return new Deposits(depositList);
    }

    private Deposits getMultDepositsByBankTitle(String bankTitle) {
        List<OneDeposit> depositList = new ArrayList<OneDeposit>();

        for (OneDeposit dep : deposits.getListDeposites()) {
            if (dep.getName().equalsIgnoreCase(bankTitle)) {
                depositList.add(dep);
            }
        }
        return new Deposits(depositList);
    }

    private boolean deleteOneDepositByAccountID(int accountId) {
        for (Iterator<OneDeposit> iter = deposits.getListDeposites().listIterator(); iter.hasNext();) {
            OneDeposit deposit = iter.next();
            if (deposit.getAccountId() == accountId) {
                deposits.removeDeposit(deposit);
                return true;
            }
        }
        return false;
    }

}
