//Name: Max Chen
//Student ID: 111316366
//Stony Brook University
//CSE 114
//Fall 2017
//Final Project

package finalProject;

import java.util.*;

public abstract class BankCard {
	private String name;
	protected long cardnumber;
	protected double balance;
	protected List<Transaction> lis = new ArrayList<>();
	public BankCard(String cardholderName, long cardNumber){
		balance = 0;
		name = cardholderName;
		cardnumber = cardNumber;
	}
	public double balance(){
		return balance;
		
	}
	public long number(){
		return cardnumber;
	}
	public String cardHolder(){
		return name;
	}
	public String toString(){
		return "Card # " + cardnumber + "\t Balance: " + balance; 
	}
	public abstract boolean addTransaction(Transaction t);
	public abstract void printStatement();
}

class CreditCard extends BankCard{
	private int expDate;
	protected double cardLimit;
	public CreditCard(String cardHolder, long cardNumber, int expiration, double limit){
		super(cardHolder, cardNumber);
		expDate = expiration;
		cardLimit = limit;
	}
	public CreditCard(String cardHolder, long cardNumber, int expiration){
		super(cardHolder, cardNumber);
		cardLimit = 500.00;
		
	}
	
	public double limit(){
		return cardLimit;
	}
	
	public double availableCredit(){
		return cardLimit - balance;
	}
	
	public int expiration(){
		return expDate;
	}
	
	public String toString(){
		return super.toString() + " Expiration Date:" + expDate;
	}
	public boolean getCashAdvance(double requested) {
		double advance = .05 * requested;
		if(availableCredit() < requested + advance) {
			return false;
		}
		else {
			balance += requested;
			balance += advance;
			Transaction uAdvance = new Transaction("advance", "CSEBank", requested);
			lis.add(uAdvance);
			Transaction fee = new Transaction("fee", "Cash advance fee", advance);
			lis.add(fee);
			return true;
		}
		
		
	}
	
	public boolean addTransaction(Transaction t){
		if(t.type().equals("debit")){
			if(t.amount() <= this.availableCredit()){
				balance += t.amount();
				lis.add(t);
				return true;
			}
			else /*if(t.amount() > this.availableCredit())*/ {
				return false;
			}
		}
		else if(t.type().equals("credit")){
			balance += t.amount();
			lis.add(t);
			return true;
		}
		else{
			return false;
		}
	} 
	
	public void printStatement(){
		System.out.println("Cardholder Name: " + cardHolder() + " \nCard Number: " + number() + " Expiration Date: " + expiration() + " Balance: " + balance() +" Limit: " + cardLimit);
		System.out.println("Transactions:");
		for(int i = 0; i < this.lis.size(); i++){
			System.out.println(lis.get(i));
		}
		System.out.println();
	}
}

class PrepaidCard extends BankCard{
	public PrepaidCard(String cardHolder, long cardNumber, double balance){
		super(cardHolder, cardNumber);
		this.balance = balance;
	}
	
	public PrepaidCard(String cardHolder, long cardNumber){
		super(cardHolder, cardNumber);
		balance = 0;
	}
	
	public boolean addTransaction(Transaction t){
		if(t.type().equals("debit")){
			if(t.amount() <= balance){
				balance -= t.amount();
				lis.add(t);
				return true;
			}
			else{
				return false;
			}
		}
		else if(t.type().equals("credit")){
			balance -= t.amount();
			lis.add(t);
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean addFunds(double amount){
		if(amount>0){
			balance += amount;
			Transaction uPayment = new Transaction("top-up", "User payment", -1*amount);
			lis.add(uPayment);
			return true;
		}
		else{
			return false;
		}
	}
	
	public String toString(){
		return "Cardholder name: " + cardHolder() + " \nCard number: " + cardnumber + " Balance: " + balance;
	}
	
	public void printStatement(){
		
		System.out.println(toString());
		System.out.println("Transactions:");
		for(int i = 0; i < this.lis.size(); i++){
			System.out.println(lis.get(i));
		}
		System.out.println();

	}
	
	
	
}

class RewardsCard extends CreditCard{
	protected int rewardpoints;
	public RewardsCard(String holder, long number, int expiration){
		super(holder, number, expiration);
		rewardpoints = 0;
		this.cardLimit = 500;
	}
	public RewardsCard(String holder, long number, int expiration, double limit){
		super(holder, number, expiration, limit);
		rewardpoints = 0;
	}
	
	public int rewardPoints(){
		return rewardpoints;
	}
	
	public boolean redeemPoints(int points) /*throws RedeemPointsException*/{
		boolean changePoints = false;
		
		if(points <= this.rewardPoints()){
			if(points > balance * 100 && balance >0) {
				changePoints = true;
				//System.out.println("Unable to redeem " + points + " points. Redeeming " + (int)(balance*100) + " points instead.");
				points = (int)(balance * 100);
			}
			if(balance > 0) {
				balance -= points/100.00;
					if(changePoints) {
						balance = 0;
					}
				rewardpoints -= points;
				Transaction trans = new Transaction("redemption", "CSEBank", points/100.00);
				lis.add(trans);
				return true;
			}
			else {
				return false;
			}
		}
		
		else{
			return false;
			
		}
		//}
		//catch(RedeemPointsException RPE) {
		//	System.out.println(RPE.getMessage());
		//	return false;
			
		//}
	}
	
	
	public String toString(){
		return super.toString() + " Reward Points: " + this.rewardPoints();
	}
	
	
	
	public boolean addTransaction(Transaction t){
		if(t.type().equals("debit")){
			if(t.amount() <= this.availableCredit()){
				balance += t.amount();
				lis.add(t);
				rewardpoints += t.amount() * 100;
				return true;
			}
			else /*if(t.amount() > this.availableCredit())*/ {
				return false;
			}
		}
		else if(t.type().equals("credit")){
			balance += t.amount();
			lis.add(t);
			return true;
		}
		else{
			return false;
		}
	}
	
	public void printStatement(){
		System.out.println("Cardholder Name: " + cardHolder() + " \nCard Number: " + number() + " Expiration Date: " + expiration() + " Balance: " + balance() + " Limit: " + cardLimit);
		//System.out.println("Transactions: \n" + lis);
		System.out.print("Reward Points: " + rewardpoints + "\n");
		System.out.println("Transactions:");
		for(int i = 0; i < this.lis.size(); i++){
			System.out.println(lis.get(i));
		}
		System.out.println();

	}
	
}
//class RedeemPointsException extends Exception {
//	public RedeemPointsException(String message) {
//		super(message);
//	}
//	
//}