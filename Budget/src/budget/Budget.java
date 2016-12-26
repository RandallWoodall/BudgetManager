package budget;

import java.awt.Toolkit;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * This Object houses all the data members and functions that a budget needs to be functional and useful. 
 */
public class Budget {
	private String name;
	private Row categoryTotals;
	private Row incomeTotals;
	private Row expenseTotals;
	private ArrayDeque<Category> incomeCategories;
	private ArrayDeque<Category> expenseCategories;
	private BudgetTableModel model;
	private boolean familyAcc;

	/**
	 * Default constructor: Does basic setup functionality to a default setting.
	 */
	public Budget() {
		//Just get everything setup, print to terminal that things were setup.
		setName("Your Budget");

		categoryTotals = new Row("Totals", "0.00");
		incomeTotals = new Row("Income Totals", "0.00");
		expenseTotals = new Row("Expense Totals", "0.00");

		incomeCategories = new ArrayDeque<Category>();
		expenseCategories = new ArrayDeque<Category>();

		setupModel();
		
		familyAcc = false;
	}//End default constructor.

	//Accessors
	/**
	 * Returns a String.  This is a public method because the user will see the name of the budget.
	 * @return The name of the budget.
	 */
	public String getName() {
		return name;
	}//End getName.
	
	public boolean getFamilyAcc() {
		return familyAcc;
	}//End getFamilyAcc.

	/**
	 * This function returns the model outlining the budget for a table object. 
	 * @return The model that represents this Budget object.
	 */
	public BudgetTableModel getModel() {
		return model;
	}//End BudgetTableModel

	//--Other accessors here.
	//End accessors.

	//Mutators
	/**
	 * This function sets the name of the budget.  The argument is taken and modified to match style "name"'s budget - creation date.
	 * @param n The user specified portion of the budget name.
	 */
	public void setName(String n) {
		//No data validation needed.  Don't care what they name it.
		name = n;
	}//End setName.

	
	public boolean setFamilyAcc(boolean f) {
		//No data validation needed.  Don't care what they name it.
		familyAcc = f;
		System.out.println("Family Account Status: " + familyAcc);
		return familyAcc;
		
	}//End setFamilyAcc.
	
	//--Other mutators here.
	//End mutators.

	//Special functions
	/**
	 * Blank setup for a BudgetTableModel.
	 */
	public void setupModel() {
		model = new BudgetTableModel();
		model.addColumn("Name");
		model.addColumn("Estimate");
		model.addColumn("Actual");
		model.addColumn("Difference");
		for(int i = 0; i < incomeCategories.size(); i++)
			model.addRow(((Category)incomeCategories.toArray()[i]).getVector());
		model.addRow(createVector(incomeTotals));
		for(int i = 0; i < expenseCategories.size();i++)
			model.addRow(((Category)expenseCategories.toArray()[i]).getVector());
		model.addRow(createVector(expenseTotals));
		model.addRow(createVector(categoryTotals));
	}//End setupModel
	
	private Vector<String> createVector(Row buildFrom) {
		Vector<String> toReturn = new Vector<String>();
		toReturn.add(buildFrom.getTitle());
		toReturn.add(NumberFormat.getCurrencyInstance().format(new BigDecimal(buildFrom.getEstimate())));
		toReturn.add(NumberFormat.getCurrencyInstance().format(new BigDecimal(buildFrom.getActual())));
		toReturn.add(NumberFormat.getCurrencyInstance().format(new BigDecimal(buildFrom.getDiff())));
		return toReturn;
	}

	/**
	 * Add a Category named with the input String n to the incomeCategories ArrayDeque. 
	 * @param n The name of the new Category to add to the ArrayDeque.
	 */
	public void addIncome(String n) {
		incomeCategories.addLast(new Category(n));
		model.addRow(incomeCategories.peekLast().getVector());
		model.moveRow(model.getRowCount() - 1, model.getRowCount() - 1, incomeCategories.size() -1);
	}//End addIncome
	
	//Possible refactor location
	public void addIncome(Category toAdd) {
		incomeCategories.addLast(toAdd);
		model.addRow(incomeCategories.peekLast().getVector());
		model.moveRow(model.getRowCount() - 1, model.getRowCount() - 1, incomeCategories.size() -1);
	}

	/**
	 * Add a Category named with the input String n to the expenseCategories ArrayDeque.
	 * @param n The name of the new Category to add to the ArrayDeque.
	 */
	public void addExpense(String n) {
		expenseCategories.addLast(new Category(n));
		model.addRow(expenseCategories.peekLast().getVector());
		//Just a bit more of a complicated shift.
		model.moveRow(model.getRowCount() - 1, model.getRowCount() -1, incomeCategories.size() + expenseCategories.size());
	}//End addExpense

	public void addExpense(Category toAdd) {
		expenseCategories.addLast(toAdd);
		model.addRow(expenseCategories.peekLast().getVector());
		//Just a bit more of a complicated shift.
		model.moveRow(model.getRowCount() - 1, model.getRowCount() -1, incomeCategories.size() + expenseCategories.size());
	}

	/**
	 * Remove a Category object the Budget object and remove it from the table the index i comes in from a JList index
	 * Formatted {"Budget", [Titles of all incomeCategories], [Titles of all expenseCategories]}.
	 * @param i the index i comes in from a JList index formatted {"Budget", [Titles of all incomeCategories], [Titles of all expenseCategories]}
	 * by the function getCategories.
	 */
	public void removeCategory(int i) {
		Category toRemove = getCategoryIndex(i);
		int target;

		if(i <= incomeCategories.size()) {
			incomeCategories.removeFirstOccurrence(toRemove);
			target = i -1;
		}
		else {
			expenseCategories.removeFirstOccurrence(toRemove);
			target = incomeCategories.size() + i;
		}

		model.removeRow(target); 
		model.fireTableDataChanged();
		//This will use i as the index of an income category to remove.
	}//End removeCategory

	/**
	 * Dumps the old budget values and copies to another budget.  This is a very dirty method. 
	 * !!Change this so that maybe we aren't passing Budget's around outside this class!!
	 * @param n a Budget object to clone to.
	 */
	public void clone(Budget n) {
		this.name = n.getName();
		//Make this use accessors to be cleaner.
		this.incomeCategories = n.incomeCategories;
		this.expenseCategories = n.expenseCategories;
		this.model = n.getModel();
		/*
		 * calcAllTotals();
		 */
		model.fireTableDataChanged();
	}//End clone

	/**
	 * This function builds an Object array based on the current state of the Budget. 
	 * @return an object array filled such that: {"Budget", [Titles from all income Categories], [Titles from all expense Categories]}
	 */
	public Object[] categoryNames() {
		//Array is the size of our 3 basic cattegories + all others.
		//Commented out section adds Income Totals and Expense Totals + their calculations
		Object nameList[] = new Object[1+incomeCategories.size()+expenseCategories.size()];

		nameList[0] = "Budget";

		for(int i = 0; i < incomeCategories.size(); i++)
			nameList[1 + i] = incomeCategories.toArray()[i];

		for(int i = 0; i < expenseCategories.size(); i++)
			nameList[1 + incomeCategories.size() + i] = expenseCategories.toArray()[i];

		model.fireTableDataChanged();

		return nameList;
	}//End categoryNames

	/**
	 * Given a target index from a JList of the format {"Budget", [Titles from all income Categories], [Titles from all expense Categories]},
	 * This function will return the Category from the correct ArrayDeque.
	 * !!This function should be private to reduce coupling potential.!!
	 * @param target This integer is the target index from an array of Objects that corresponds to a specific Category.
	 * @return       The Category specified by the target index.
	 */
	public Category getCategoryIndex(int target) {
		Category toReturn = new Category("Empty");

		if(target > 0 && incomeCategories.isEmpty() == false && target <= incomeCategories.size()) {
			toReturn = (Category) incomeCategories.toArray()[target - 1];
		}
		else if(incomeCategories.isEmpty() == true)
			toReturn = (Category) expenseCategories.toArray()[target - 1];
		else
			toReturn = (Category) expenseCategories.toArray()[target - incomeCategories.size() - 1];
		return toReturn;
	}//End getCategoryNamed

	/**
	 * This function is multi-step.  The first action it performs is to go through each Category and tell it to update it's table,
	 * it then updates it's own table based on these updates.
	 */
	public void changedData() {
		//Check for and make changes.
		for(int i = 0; i < incomeCategories.size(); i++)
			((Category)incomeCategories.toArray()[i]).changedData();
		for(int i = 0; i < expenseCategories.size(); i++)
			((Category)expenseCategories.toArray()[i]).changedData();
		calcIncomeTotals();
		calcExpenseTotals();
		calcTotalTotals();
		//Everything has checked its data for updates and recalculated as needed.

		//Affect the changes in the budget itself.
		setupModel();
		
		//Budget re-built with changes.

		//Tell the model to update.
		model.fireTableDataChanged();
		calcTotalOverFlow();
	}//End changedData

	public BigDecimal calcIncomeTotals() {
		incomeTotals = new Row("Income Totals", "0.00");
		BigDecimal sum = new BigDecimal(0.0);
		BigDecimal sum1 = new BigDecimal(0.0);
		for(int i = 0; i < incomeCategories.size(); i++) {
			sum = sum.add(new BigDecimal(((Category)incomeCategories.toArray()[i]).getEstimate()));
			sum1 = sum1.add(new BigDecimal(((Category)incomeCategories.toArray()[i]).getActual()));
		}//End for.
		incomeTotals.setEstimate(sum.toString());
		incomeTotals.addTransaction("System", sum1.toString());
	    return sum1;
	}

	public BigDecimal calcExpenseTotals() {
		expenseTotals = new Row("Expense Totals", "0.00");
		BigDecimal sum = new BigDecimal(0.0);
		BigDecimal sum1 = new BigDecimal(0.0);
		for(int i = 0; i < expenseCategories.size(); i++) {
			sum = sum.add(new BigDecimal(((Category)expenseCategories.toArray()[i]).getEstimate()));
			sum1 = sum1.add(new BigDecimal(((Category)expenseCategories.toArray()[i]).getActual()));
		}//End for.
		expenseTotals.setEstimate(sum.toString());
		expenseTotals.addTransaction("System", sum1.toString());
		return sum;
	}

	private BigDecimal calcTotalTotals() {
		categoryTotals = new Row("Totals", "0.00");
		BigDecimal sum = new BigDecimal(0.0);
		sum = (new BigDecimal(incomeTotals.getEstimate())).subtract(new BigDecimal(expenseTotals.getEstimate()));
		categoryTotals.setEstimate(sum.toString());
		sum = new BigDecimal(0.0);
		sum = (new BigDecimal(incomeTotals.getActual()).subtract(new BigDecimal(expenseTotals.getActual())));
		categoryTotals.addTransaction("System", sum.toString());
		return sum;
	}

	public void changeRowEstimate(int category, int row, String e) {
		getCategoryIndex(category).changeRowEstimate(row, e);
		changedData();
	}

	public void addRowTransaction(int category, int row, String user, String value) {
		getCategoryIndex(category).addRowTransaction(row, user, value);
		changedData();
	}

	public CategoryTableModel getCategoryModel(int i) {
		return getCategoryIndex(i).getModel();
	}

	public void addCategoryRow(int i, String title) {
		getCategoryIndex(i).addRow(title, "0.00");
	}

	/**
	 * Makes copies of the income categories so we can't change the real values and sends the copies back.
	 * @return All of the categories with no separation
	 */
	public Category[] getIncomeCategories() {
		Category categories[] = new Category[incomeCategories.size()];
		for(int i = 0; i < categories.length; i++)
			categories[i] = (Category) incomeCategories.toArray()[i];
		return categories;
	}

	public Category[] getExpenseCategories() {
		Category categories[] = new Category[expenseCategories.size()];
		for(int i = 0; i < categories.length; i++)
			categories[i] = (Category) expenseCategories.toArray()[i];
		return categories;
	}

	public BigDecimal calcSavings(String check) {
		
		BigDecimal amount= calcIncomeTotals();
        BigDecimal percent=BigDecimal.valueOf(Double.parseDouble(check));
		System.out.println(check);
		System.out.println(percent);
		BigDecimal divisor=BigDecimal.valueOf(100.0);
		System.out.println(divisor);
		BigDecimal saving=amount.divide(divisor);
		System.out.println(saving);
		saving=saving.multiply(percent);
		Category savings=new Category("Savings");
		addExpense(savings);
		savings.addRow("Travel Savings", saving.toString());
	
		//savings.setEstimate(saving.doubleValue());
		return saving;
	}
	
	public void calcTotalOverFlow(){
		BigDecimal totalIncome= calcIncomeTotals();
		BigDecimal total=calcTotalTotals();
		BigDecimal percent=BigDecimal.valueOf(20.0);
		BigDecimal hundred=BigDecimal.valueOf(100.0);
		BigDecimal amountOverFall=totalIncome.divide(hundred);
		BigDecimal minimumAmount=amountOverFall.multiply(percent);
		BigDecimal zero=BigDecimal.valueOf(0);
		if(total.compareTo(minimumAmount)==-1 || total.compareTo(minimumAmount)==0){
			System.out.println("Overflow");
			Toolkit.getDefaultToolkit().beep();
			if(total.compareTo(zero)!=-1){
		    JOptionPane optionPane = new JOptionPane("You have "+ NumberFormat.getCurrencyInstance().format(total)+ " remaining!",JOptionPane.WARNING_MESSAGE);
		    JDialog dialog = optionPane.createDialog("Warning!");
		    dialog.setAlwaysOnTop(true);
		    dialog.setVisible(true);
			}
		}
	}

	public void openRowEdit(int selectedIndex, int row) {
		getCategoryIndex(selectedIndex).dispRow(row);
	}
	
}//End class