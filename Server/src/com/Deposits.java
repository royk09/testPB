/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author royk09
 */
@XmlRootElement(name = "Deposits")
@XmlAccessorType(XmlAccessType.FIELD)
public class Deposits implements Serializable {

    @XmlElement(name = "deposit")
    private List<OneDeposit> listDeposites;

    private static String depositesFile = "deposits.xml";

    Deposits() {

    }

    public synchronized void setListDeposites(List<OneDeposit> listDeposites) {
        this.listDeposites = listDeposites;
    }

    public synchronized List<OneDeposit> getListDeposites() {
        return listDeposites;
    }

    public static String getDepositesFile() {
        return depositesFile;
    }

    public Deposits(List<OneDeposit> listDeposites) {
        this.listDeposites = listDeposites;
    }

    public static Deposits createListDeposites() {

        int id = 555;
        List<OneDeposit> listDeposites = new ArrayList<OneDeposit>();

        OneDeposit deposit;
        deposit = new OneDeposit();
        deposit.setAmountDeposit(12000);
        deposit.setName("PrivatBank");
        deposit.setCountry("Ukr");
        deposit.setType(OneDeposit.Type.onDemand);
        deposit.setDepositor("Polischuk V.V.");
        deposit.setAccountId(id++);
        deposit.setProfitability(1.05);
        deposit.setTimeConstraints(12);
        listDeposites.add(deposit);

        deposit = new OneDeposit();
        deposit.setAmountDeposit(24000);
        deposit.setName("PrivatBank");
        deposit.setCountry("Ukr");
        deposit.setType(OneDeposit.Type.cumulative);
        deposit.setDepositor("Sidorov O.S.");
        deposit.setAccountId(id++);
        deposit.setProfitability(1.15);
        deposit.setTimeConstraints(24);

        listDeposites.add(deposit);

        Deposits deposits = new Deposits(listDeposites);

        return deposits;
    }

    public synchronized void addDeposit(OneDeposit deposit) {
        getListDeposites().add(deposit);
        serializeFile();
    }

    public synchronized void removeDeposit(OneDeposit deposit) {
        getListDeposites().remove(deposit);
        serializeFile();
    }

    public synchronized void serializeFile() {

        StringWriter stringWriter = new StringWriter();

        try {
            JAXBContext writeContext = JAXBContext.newInstance(Deposits.class);
            Marshaller marshaller = writeContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this, stringWriter);
            stringWriter.close();
            FileWriter fileWriter = new FileWriter(depositesFile);
            fileWriter.write(stringWriter.toString());
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public synchronized void deserializeFile() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        FileReader reader = new FileReader(depositesFile);
        int simb;
        while ((simb = reader.read()) != -1) {
            stringBuilder.append((char) simb);
        }
        Deposits readedDeposits = null;
        try {
            JAXBContext readContext = JAXBContext.newInstance(Deposits.class);
            Unmarshaller unmarsh = readContext.createUnmarshaller();
            readedDeposits = (Deposits) unmarsh.unmarshal(new StringReader(stringBuilder.toString()));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        this.setListDeposites(readedDeposits.getListDeposites());
    }

}
