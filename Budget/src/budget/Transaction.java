package budget;

import java.math.BigDecimal;

/**
 * Simple class that holds the value assigned to a transaction.
 * Objects of this type belong to a specific row.
 * Any non-number inputs are disregarded.  Value defaults to 0 in the case of invalid input.
 **/

public class Transaction {
	 /**
	  * This variable holds the value of the transaction.
	  */
	 private BigDecimal value;

	 /**
	  * This variable holds the name of the originator of the transaction.
	  */
	 private String user_name;
	 
	 /**
	  * This is the constructor for Transaction.  All other constructors are Deprecated and should not be used.
	  * @param whoDid This is simply the name of the originator for the Transaction object.
	  * @param amount The amount (as a string) 
	  */
	 public Transaction(String whoDid, String amount) {
		 setValue(amount);
		 setUser_name(whoDid);
	 }

	 /**
	  * The user will be needing to see transaction values, therefore, return the BigDecimal to avoid accuracy loss.
	  * @return Returns the String equivalent of the value class member. 
	  */
	 public String getValue() {
		 return value.toString();
	 }
	 
	 /**
	  * Returns the user_name to the caller, nothing special beyond that.
	  * @return A copy of the user_name class member. 
	  */
	 public String getUser_name() {
 		 return user_name;
   	 }

	 /**
	  * Set the value of the transaction
	  * @param value: A String value that the Transaction will be set to match.
	  */
	 public void setValue(String value) {
		 try {
			 value = value.replaceAll("[^-?.?0-9]", "");
			 this.value = new BigDecimal(value);
		 }
		 catch (NumberFormatException e) {
			 //If value is null, then we got here from the constructor.
			 //if(this.value == null)
			 this.value = new BigDecimal("0.00");
		 }
	 }

	 /**
	  * Sets the user name to the input.
	  * @param user_name The name to put on this transaction.
	  */
	 public void setUser_name(String user_name) {
		 this.user_name = user_name;
	 }

 }//End class.
