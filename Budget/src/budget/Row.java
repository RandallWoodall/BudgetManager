/**
 * A Row object has a title, an estimated value, and an actual value that is the sum of a list of Transaction objects.
 */
package budget;

import java.math.BigDecimal;
import java.util.ArrayDeque;

public class Row {
	/**
	 * title is the name of the Row. Eg: Rent, Groceries, Paycheck
	 */
	private String title;
	/**
	 * estimate is a user-provided value, denoting the estimated expense in this Row.
	 */
	private BigDecimal estimate;
	/**
	 * actual is an ArrayDeque of Transactions.  The sum of these Transactions is the actual expense for the Row.
	 */
	private ArrayDeque<Transaction> actual;
	
	
	/**
	 * The constructor for the Row, there are no other Row constructors available.
	 * @param title The new title for this Row.
	 * @param estimate The estimate to set this new Row at -- generally 0.00.
	 */
	public Row(String title, String estimate) {
		setTitle(title);
		actual = new ArrayDeque<Transaction>();
		setEstimate(estimate);
	}
	
	/**
	 * Access for title.
	 * @return A copy of the title String, editing outside the class will not work.
	 */
	public String getTitle() {
		return new String(title);
	}
	
	/**
	 * Access for estimate data member.
	 * @return A string value representing the estimate data member.
	 */
	public String getEstimate() {
		return new String (estimate.toString());
	}
	
	/**
	 * Proxy method for the private method of the same name.  Controls the input to the private recursive method to ensure validity.
	 * @return The string equivalent to the sum of the values of the Transaction objects in the actuall Array Deque.
	 */
	public String getActual() {
		return getActual(0);
	}
	
	
	/**
	 * Private implementation of getActual.  This method is implemented recursively for readability.
	 * @param i the current object we are operating on.
	 * @return the string corresponding to the sum of the values of the Transaction objects held by the Row.
	 */
	private String getActual(int i) {
		Transaction toGrab;
		String value;
		
		//Base case 1: No elements
		if(actual.isEmpty()) return "0.00";
		
		toGrab = actual.pop();
		value = toGrab.getValue();
		actual.addLast(toGrab);
		
		//Base case 2: this is the last item from the ArrayDeque
		if(i == actual.size() - 1) return value;
		
		//Recursive step: this is not the last item.
		else return ((new BigDecimal(value)).add(new BigDecimal(getActual(++i)))).toString();
	}
	
	public String getUserName(){
		return getUserName(0);
	}
	
	private String getUserName(int i){
		Transaction toGrab;
		String value;
		
		toGrab = actual.pop();
		value = toGrab.getUser_name();
		actual.addLast(toGrab);
		
		if(i == actual.size() - 1) return value;
		
		//Recursive step: this is not the last item.
		else return ((new BigDecimal(value)).add(new BigDecimal(getUserName(++i)))).toString();
	}
	
	/**
	 * Calculates the difference between the estimate and the actual.
	 * @return the difference between estimate and actual.
	 */
	public String getDiff() {
		return (new BigDecimal(getEstimate()).subtract(new BigDecimal(getActual()))).toString();
	}
	
	/**
	 * Accepts a String object, ensures that there is some text for the name, and sets the Row title to this object.
	 * @param title the desired name for the Row.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Makes sure we have a number input, then sets the estimate to this value.
	 * @param estimate A String object that is verified and assigned to this.estimate.
	 */
	public void setEstimate(String estimate) {
		try {
			estimate = estimate.replaceAll("[^-?.?0-9]", "");
			this.estimate = (new BigDecimal(estimate));
		}
		catch(Exception e) {
			System.out.println("Error in setEstimate (in Row): " + e);
			System.out.println("Estimate value left at: " + getEstimate());
		}
	}
	
	/**
	 * Instantiates and adds a Transaction object described by the input data to the actual ArrayDeque.
	 * @param user The person who originated the Transaction.
	 * @param toAdd The amount to use for the Transaction value.
	 */
	public void addTransaction(String user, String toAdd) {
		actual.addLast(new Transaction(user, toAdd));
	}
	
	/**
	 * Deprecated function builds a String[] with values from Transaction in actual.
	 * Save/Load must be refactored before this is removed.
	 * @return a String array filled with numerical Strings.
	 */
	@Deprecated
	public String [] getTransactionVals() {
		if(actual.isEmpty())
			return new String[] {};
		String toReturn [] = new String[actual.toArray().length];
		for(int i = 0; i < actual.toArray().length; i++) {
			toReturn[i] = ((Transaction)actual.toArray()[i]).getValue().toString();
		}
		return toReturn;
	}
	
	//Return the transactions as a 2d String array.
	public String[][] getTransactions() {
		String toReturn[][] = new String[actual.size()][2];
		for(int i = 0; i < actual.size(); i++) {
			toReturn[i][0] = ((Transaction)(actual.toArray()[i])).getValue();
			toReturn[i][1] = ((Transaction)(actual.toArray()[i])).getUser_name();
		}
		return toReturn;
	}

	//Updates the values in actual
	public void updateTransactions(String[][] toSet) {
		for(int i = 0; i < toSet.length; i++) {
			if(i >= actual.size()) {
				actual.addLast(new Transaction(toSet[i][1], toSet[i][0]));
				continue;
			}
			Transaction checking = actual.pop();
			if(toSet[i][0] == null)
				checking.setValue("0.00");
			else if(!(checking.getValue().equals(toSet[i][0])))
				checking.setValue(toSet[i][0]);
			if(toSet[i][1] == null)
				checking.setUser_name("");
			else if(checking.getUser_name() == null || !(checking.getUser_name().equals(toSet[i][1])))
				checking.setUser_name(toSet[i][1]);
			actual.addLast(checking);
		}
	}
	
}//End class.
