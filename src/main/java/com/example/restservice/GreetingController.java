/**
 Written by Alex Green, 2022
 */
package com.example.restservice;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.text.SimpleDateFormat;

import java.sql.Timestamp;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class GreetingController {

	List<Transaction> transactions = new ArrayList<Transaction>(); // stores all transactions, sorted by timestamp
	int totalTrans = 0; // number of transactions in 'transactions'
	
	List<Payer> payers = new ArrayList<Payer>(); // stores all payers and their total points
	int totalPayers = 0; // number of payers in 'payers'

	int totalPoints = 0; // the user's total points in their account
	
	// Add a new transaction to the account.
	@GetMapping("/transaction")
	public String addTrans(@RequestParam(value="payer") String payer, @RequestParam(value="points") int points, @RequestParam(value="timestamp") String time) throws Exception{

		Date timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(time);
		payer = payer.toUpperCase();
		Transaction newTrans = new Transaction(timestamp, payer, points);

		// if the payer is returning, update their total points
		boolean newPayer = true;
		for(int i = 0; i < totalPayers; i++){
			if (payer.equals(payers.get(i).getPayer())){
				if (payers.get(i).getPoints() + points < 0) return payer += " points must not go negative.";
				payers.get(i).addPoints(points); // update the payer's total points
				newPayer = false;
				break;
			}
		}
		if(newPayer){
			if (points < 0) return payer += " points must not go negative.";
			payers.add(new Payer(payer, points));
			totalPayers++;
		}

		totalPoints += points;

		transactions.add(newTrans); // add the new transaction to the list
		totalTrans++;

		Collections.sort(transactions); // sort the transaction list by timestamp

		return "Transaction added.";

	}

	// Spend the user's points and print which payers were charged.
	@GetMapping("/spend")
	public String spendPoints(@RequestParam(value="points") int points) throws Exception{

		if((totalPoints - points) < 0){ // user does not have enough points
			return "You do not have enough points to spend. Your point balance is: " + String.valueOf(totalPoints);
		}

		if (points == 0) return "";

		if (points < 0) return "Cannot spend negative points.";

		int pointsSpent = 0; // the total points spent so far

		PayerList charged = new PayerList(); // list of all payers charged in this transaction, and amounts charged
		int chargedNum = 0; // number of payers in 'charged'

		Transaction curr; // the transaction being examined
		int currPoints = 0; // points of the curr transaction
		String currPayer; // payer of the curr transaction

		for(int i = 0; i < totalTrans; i++){ // for every transaction,

			curr = transactions.get(i);
			currPoints = curr.getPoints();
			currPayer = curr.getPayer();

			if(currPoints == 0){ // skip the transaction if its points have been spent
				continue;
			}

			if((pointsSpent + currPoints) > points){ // this transaction has more than enough points to fulfill the spend request
				currPoints = points - pointsSpent; // update currPoints to only charge the payer the needed amount
			}
			
			// check if the curr payer has already been charged
			boolean newPayer = true;
			for(int j = 0; j < chargedNum; j++){
				if(charged.getElement(j).getPayer().equals(currPayer)){ // curr payer exists in 'charged', update its points
					newPayer = false;
					charged.getElement(j).addPoints(-currPoints);
					break;
				}
			}

			// add the new payer to 'charged', or update the existing payer's points
			if(newPayer){
				charged.addElement(new Payer(currPayer, -currPoints));
				chargedNum++;
			}
			
			pointsSpent += currPoints; 

			totalPoints -= currPoints; // update user's total points
			transactions.get(i).changePoints(-currPoints); // update this transaction so it does not affect future spends

			// update the payer's total points
			for(int j = 0; j < totalPayers; j++){
				if(payers.get(j).getPayer().equals(currPayer)){
					payers.get(j).addPoints(-currPoints);
				}
			}

			if (pointsSpent == points) break; // stop looping once all desired points have been spent

		}

		return charged.printPayed(chargedNum);

	}

	// Print all payers and their respective points.
	@GetMapping("/print")
	public String printPayers() {

		if (totalPayers == 0) return ""; // there have been no transactions yet, print nothing

		PayerList totalPayerPoints = new PayerList(); // list of payers to be printed

		for(int i = 0; i < totalPayers; i++){

			totalPayerPoints.addElement(payers.get(i)); // add the current payer to the list

		}

		return totalPayerPoints.printList(totalPayers);

	}

}