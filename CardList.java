//Name: Max Chen
//Student ID: 111316366
//Stony Brook University
//CSE 114
//Fall 2017
//Final Project

package finalProject;
import java.util.*;

public class CardList {
	private List<BankCard> arrl;
	
	public CardList(){
		arrl = new ArrayList<>();
	}
	
	public void add(BankCard b){
		arrl.add(b);
	}
	public void add(int index, BankCard b){
		if(index >=0 && index <= arrl.size()){
			arrl.add(index, b);
		}
		else{
			arrl.add(b);
		}
	}
	public int size(){
		return arrl.size();
	}
	public BankCard get(int index){
		if(index >= arrl.size() || index < 0){
			return null;
		}
		else{
			return arrl.get(index);
		}
	}
	public int indexOf(long cardNumber){
		
		for(int i = 0; i < arrl.size(); i++){
			
			if( arrl.get(i).number() == cardNumber){
				return i;
			}
		}
		return -1;

	}
}

