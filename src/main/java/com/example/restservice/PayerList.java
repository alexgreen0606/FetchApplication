/**
 Written by Alex Green, 2022
 */
package com.example.restservice;

import java.util.ArrayList;
import java.util.List;

public class PayerList {

    private List<Payer> payers;

    public PayerList() {
        payers = new ArrayList<Payer>();
    }

    public List<Payer> getList(){
        return payers;
    }

    public void addElement(Payer t){
        payers.add(t);
    }

    public Payer getElement(int i){
        return payers.get(i);
    }

    // print all payers and their points (for '/print' call)
    public String printList(int totalPayers){

		String x = "{<br>";

		for(int i = 0; i < totalPayers; i++){

			x += ("\"" + payers.get(i).getPayer() + "\": " + String.valueOf(payers.get(i).getPoints()) + ",<br>");

		}

		x += "}";

		return x;

	}

    // print all payers and their points (for '/spend' call)
    public String printPayed(int totalPayers){

		String x = "[<br>";

		for(int i = 0; i < totalPayers; i++){

			x += ("{ \"payer\": \"" + payers.get(i).getPayer() + "\",  \"points\": " + String.valueOf(payers.get(i).getPoints()) + " },<br>");

		}

		x += "]";

		return x;

	}

}