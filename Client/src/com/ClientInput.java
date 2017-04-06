/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.Serializable;

/**
 *
 * @author admin
 */
public class ClientInput implements Serializable{

        private OneDeposit oneDeposit;
        private Deposits deposits;
	private static final long serialVersionUID = 1L;
	private InputType inputType;
	private String clientInputText;	
        public enum InputType {
		text, deposit, listDeposits
	}
        public void setInputType(InputType inputType) {
            this.inputType = inputType;
        }

        public void setClientInputText(String clientInputText) {
            this.clientInputText = clientInputText;
        }

        public void setOneDeposit(OneDeposit oneDeposit) {
            this.oneDeposit = oneDeposit;
        }

        public InputType getInputType() {
            return inputType;
        }

        public String getClientInputText() {
            return clientInputText;
        }

        public OneDeposit getOneDeposit() {
            return oneDeposit;
        }

        
	public Deposits getDeposits() {
		return deposits;
	}

	public void setDeposits(Deposits deposits) {
		this.deposits = deposits;
	}


	public ClientInput(String clientInputText) {
		this.clientInputText = clientInputText;
		inputType = InputType.text;
	}

	public ClientInput(OneDeposit oneDeposit) {
		this.oneDeposit = oneDeposit;
		inputType = inputType.deposit;
	}

	public ClientInput(Deposits deposits) {
		this.deposits = deposits;
		inputType = InputType.listDeposits;
	}

}
