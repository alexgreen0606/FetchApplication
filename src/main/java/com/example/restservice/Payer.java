/**
 Written by Alex Green, 2022
 */
package com.example.restservice;

public class Payer{

	private final String payer;
	private int points;

	public Payer(String payer, int points){
		this.payer = payer;
		this.points = points;
	}

    public int getPoints(){
        return points;
    }

    public void addPoints(int i){
        this.points += i;
    }

    public String getPayer(){
        return payer;
    }

}