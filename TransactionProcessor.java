//Name: Max Chen
//Student ID: 111316366
//Stony Brook University
//CSE 114
//Fall 2017
//Final Project

package finalProject;

//Driver class for the final project
import java.util.*;
import java.io.*;

public class TransactionProcessor
{
	private static String getCardType (long number)
	{
		// Return a String indicating whether 'number' belongs to a 
		// CreditCard, RewardsCard, or a PrepaidCard (or null if it's none
		// of the three)
		
		String result;
		
		int firstTwo = Integer.parseInt(("" + number).substring(0,2));
		
		switch(firstTwo)
		{
			case 84:
			case 85: result = "CreditCard"; break;
			case 86:
			case 87: result = "RewardsCard"; break;
			case 88:
			case 89: result = "PrepaidCard"; break;
			default: result = null; // invalid card number
		}
		
		return result;
	}
	
	public static BankCard convertToCard(String data){
		Scanner datain = new Scanner(data);
		int expiration;
		double limit; 
		double balance;
		long cardnumber;
		String name;
		String type = null;
		//while(datain.hasNextLong()){
			cardnumber = datain.nextLong();
			if(getCardType(cardnumber) == (null)){
				datain.nextLine();
				
			}
			//else{
				type = getCardType(cardnumber);
				//testingSystem.out.print(type);
			//}
			if(type.equals("CreditCard")){
				CreditCard card;
				name = datain.next();
				name = checkForUnderscore(name);
				//testing System.out.print(name);
				expiration = datain.nextInt();
				if(datain.hasNextLine()){
					
					limit = Double.parseDouble(datain.nextLine());
					card = new CreditCard(name, cardnumber, expiration, limit);
				}
				else{
					card = new CreditCard(name, cardnumber, expiration);
				}
				datain.close();
				return card;

				
			}
			else if(type.equals("RewardsCard")){
				RewardsCard card;
				name = datain.next();
				name = checkForUnderscore(name);
				expiration = datain.nextInt();
				if(datain.hasNextLine()){
					
					limit = Double.parseDouble(datain.nextLine());
					card = new RewardsCard(name, cardnumber, expiration, limit);
				}
				else{
					card = new RewardsCard(name, cardnumber, expiration);
				}
				datain.close();
				return card;
				
				
			}
			else {
				PrepaidCard card;
				name = datain.next();
				name = checkForUnderscore(name);
				if(datain.hasNextLine()){
					balance = Double.parseDouble(datain.next());
					card = new PrepaidCard(name, cardnumber, balance);
				}
				else{
					card = new PrepaidCard(name, cardnumber);
				}
				datain.close();
				return card;
			}
			
			
			
			
			
		
	}
	
	public static CardList loadCardData(String fName){
		File f = new File(fName);
		CardList lis = new CardList();
		try{
		Scanner scan = new Scanner(f);
		while(scan.hasNextLine()){
			lis.add(convertToCard(scan.nextLine()));
		}
		scan.close();
		return lis;
		}
		catch(IOException IOE){
			return null;
		}
		
	}
	
	public static void processTransactions(String filename, CardList c){
		File f = new File(filename);
		int index = 0;
		try{
		Scanner scan = new Scanner(f);
		while(scan.hasNextLine()){
			String[] arr = scan.nextLine().split(" ");
			long cardnumber = Long.parseLong(arr[0]);
			for(int i = 0; i < c.size(); i++){
				if(cardnumber == c.get(i).number()){
					index = i;
				}
			}
			if(arr[1].equals("redeem")){
				
				int numpoints = Integer.parseInt(arr[2]);
				RewardsCard rc = (RewardsCard) c.get(index);
				//try {
				rc.redeemPoints(numpoints);
				//}
				//catch(RedeemPointsException RPE) {
				//	System.out.println(RPE.getMessage());
				//}
				
			}
			else if(arr[1].equals("top-up")){
				double numadd = Double.parseDouble(arr[2]);
				PrepaidCard pc = (PrepaidCard) c.get(index);
				pc.addFunds(numadd);
				
			}
			else if(arr[1].equals("advance")) {
				double add = Double.parseDouble(arr[2]);
				CreditCard cred = (CreditCard) c.get(index);
				cred.getCashAdvance(add);
			}
			else{
				Transaction trans = new Transaction(arr[1], arr[3], Double.parseDouble(arr[2]));
				trans.setMerchant(checkForUnderscore(trans.merchant()));
				c.get(index).addTransaction(trans);
			}
		}
		
		scan.close();
		}
		catch(IOException IOE){
			
		}
	}
	public static String checkForUnderscore(String name) {
		if(name.indexOf('_') != -1) {
			while(name.indexOf('_')!=-1) {
			int underscoreIndex = name.indexOf('_');
			String part1 = name.substring(0, underscoreIndex);
			String part2 = name.substring(underscoreIndex + 1);
			name = part1 + " " + part2;
			}
			return name;
		}
		else {
			return name;
		}
	}

	public static void main(String[] args){
		System.out.println("Enter card data file name: ");
		Scanner userin = new Scanner(System.in);
		String fil = userin.next();
		//File f = new File(fil);
		try{
		CardList cl = loadCardData(fil);
		System.out.println("Enter transaction data file name: ");
		String transfil = userin.next();
		processTransactions(transfil, cl);
		for(int i = 0; i < cl.size(); i++){
			cl.get(i).printStatement();
		}
		userin.close();
		}
		catch(Exception ex){
			System.out.println("LoadCardData failed.");
		}
		
	}
}