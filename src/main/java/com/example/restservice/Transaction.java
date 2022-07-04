/**
 Written by Alex Green, 2022
 */
package com.example.restservice;

import java.util.Date;

public class Transaction  implements Comparable<Transaction>{

	private final Date time;
	private final String payer;
	private int points;

	public Transaction(Date time, String payer, int points) {
		this.time = time;
		this.payer = payer;
		this.points = points;
	}

	public Date getTime() {
		return time;
	}

	public String getPayer() {
		return payer;
	}

	public int getPoints(){
		return points;
	}

	public void changePoints(int i){
		this.points += i;
		return;
	}

	// modify the compareTo() so transaction list sorts by timestamp
	@Override
  	public int compareTo(Transaction o) {
    	return getTime().compareTo(o.getTime());
  	}

}