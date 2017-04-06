/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author royk09
 */
@XmlRootElement
public class OneDeposit implements Serializable {
	
	private String name; //Название банка
	private String country; //страна регистрации
	private String depositor; //имя вкладчика
	private int accountId; //номер счета
	private double amountDeposit; //сумма вклада
	private double profitability; //годовой процент
	private int timeConstraints; //срок вклада месяцев
	public enum Type {    
		onDemand, //вклад до востребования
		terminable, //срочный
		accounting, //расчетный
		cumulative, //накопительный
		saving, //сберегательный
		metallic // металлический
	}
        private Type type; //тип вклада
        	
        
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getDepositor() {
		return depositor;
	}

	public void setDepositor(String depositor) {
		this.depositor = depositor;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public double getAmountDeposit() {
		return amountDeposit;
	}

	public void setAmountDeposit(double amountDeposit) {
		this.amountDeposit = amountDeposit;
	}

	public double getProfitability() {
		return profitability;
	}

	public void setProfitability(double profitability) {
		this.profitability = profitability;
	}

	public int getTimeConstraints() {
		return timeConstraints;
	}

	public void setTimeConstraints(int timeConstraints) {
		this.timeConstraints = timeConstraints;
	}


        public OneDeposit(){

        }
	public OneDeposit(String name, String country, OneDeposit.Type type, String depositor, int accountId,
                       double amountDeposit, double profitability, int timeConstraints) {
		this.name = name;
		this.country = country;
		this.type = type;
		this.depositor = depositor;
		this.accountId = accountId;
		this.amountDeposit = amountDeposit;
		this.profitability = profitability;
		this.timeConstraints = timeConstraints;
	}

        @Override
            public String toString() {
		String result = new String();

		result += "accountId: " + accountId + '\n';
		result += "amountDeposit: " + amountDeposit + '\n';
		result += "country: " + country + '\n';
		result += "depositor: " + depositor + '\n';
		result += "name: " + name + '\n';
		result += "profitability: " + profitability + '\n';
		result += "timeConstraints: " + timeConstraints + '\n';
		result += "type: " + type + '\n';

		return result;
	}

}