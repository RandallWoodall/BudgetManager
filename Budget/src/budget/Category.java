package budget;

import java.awt.Toolkit;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Category {
	private String categoryName;
	private ArrayDeque<Row> rows;
	private CategoryTableModel categoryModel;
	
	public Category(String n) {
		categoryName = n;
		rows = new ArrayDeque<Row>();
		setupModel();
	}
	
	public String getCategoryName() {
		return new String(categoryName);
	}
	
	public String getEstimate() {
		BigDecimal sum = new BigDecimal(0.0);
		if(rows.isEmpty() == true) {
			return new String("0.00");
		}
		for(int i = 0; i < rows.size(); i++)
			sum = sum.add(new BigDecimal(((Row) rows.toArray()[i]).getEstimate()));
		return sum.toString();
	}
	
	//getActual returns the sum of the row actuals.
	public String getActual() {
		BigDecimal sum = new BigDecimal(0.0);
		if(rows.isEmpty() == true) {
			return new String("0.00");
		}
		for(int i = 0; i < rows.size(); i++)
			sum = sum.add(new BigDecimal(((Row) rows.toArray()[i]).getActual()));
		return sum.toString();
	}
	
	//getDiff returns the sum of the row differences.
	private String getDiff() {
		BigDecimal sum = new BigDecimal(0.0);
		if(rows.isEmpty() == true) {
			return new String("0.00");
		}
		for(int i = 0; i < rows.size(); i++)
			sum = sum.add(new BigDecimal(((Row) rows.toArray()[i]).getDiff()));
		return sum.toString();
	}
	
	//Basic mutators, starting out with no data validation
	public void setName(String n) {
		categoryName = n;
	}
	
	//getVector returns a vector to go into a table model.
	public Vector<String> getVector() {
		Vector<String> data = new Vector<String>();
		data.add(getCategoryName());
		data.add(NumberFormat.getCurrencyInstance().format(new BigDecimal(getEstimate())).toString());
		data.add(NumberFormat.getCurrencyInstance().format(new BigDecimal(getActual())).toString());
		data.add(NumberFormat.getCurrencyInstance().format(new BigDecimal(getDiff())).toString());
		return data;
	}
	
	//toString will print the name of the Category instead of the memory location.
	public String toString() {
		return new String(getCategoryName());
	}
	
	//getModel simply returns the current model for the Category
	public CategoryTableModel getModel() {
		return categoryModel;
	}
	
	public void changeRowEstimate(int i, String e)
	{
		getRowIndex(i).setEstimate(e);
	}
	
	public void addRowTransaction(int i, String user, String value) {
		getRowIndex(i).addTransaction(user, value);
	}
	
	public void addRow(String n, String value)
	{
		rows.addLast(new Row(n,value));
		categoryModel.addRow(createVector(rows.peekLast()));
		categoryModel.fireTableDataChanged();
	}
	
	@Deprecated
	public void addRow(Row toLoad) {
		rows.addLast(toLoad);
		categoryModel.addRow(createVector(rows.peekLast()));
		categoryModel.fireTableDataChanged();
	}
	
	//setupModel sets up the table model for a category.
	public void setupModel() {
		categoryModel = new CategoryTableModel();
		categoryModel.addColumn("Name");
		categoryModel.addColumn("Estimate");
		categoryModel.addColumn("Actual");
		categoryModel.addColumn("Difference");
		categoryModel.addRow(getVector());
	}
	
	//Find Row
	private Row getRowIndex(int target) {
		return (Row) rows.toArray()[target - 1];
	}

	//changedData
	public void changedData() {
		//setupModel(); <--This was destroying our old reference
		while(categoryModel.getRowCount() > 0) //Empty the bucket.
			categoryModel.removeRow(0);
		categoryModel.addRow(getVector());
		for(int i = 0; i < rows.size(); i++)
			categoryModel.addRow(createVector((Row) rows.toArray()[i]));
		categoryModel.fireTableDataChanged();
		calcOverFlow();
	}
	
	private Vector<Object> createVector(Row current) {
		Vector<Object> toAdd;
		toAdd = new Vector<Object>();
		toAdd.add(current.getTitle());
		toAdd.add(NumberFormat.getCurrencyInstance().format(new BigDecimal(current.getEstimate())));
		toAdd.add(NumberFormat.getCurrencyInstance().format(new BigDecimal(current.getActual())));
		toAdd.add(NumberFormat.getCurrencyInstance().format(new BigDecimal(current.getDiff())));
		return toAdd;
	}
	
	@Deprecated
	public Row[] returnRows() {
		Row toReturn[] = new Row[rows.toArray().length];
		for(int i = 0; i < toReturn.length; i++)
			toReturn[i] = ((Row)rows.toArray()[i]);
		return toReturn;
	}
	
	//Find a way to re-work the algorithm so that it only does one alert. 
	public void calcOverFlow(){
		
		for(int i = 0; i < rows.size(); i++){
	
		BigDecimal actual=(new BigDecimal(((Row) rows.toArray()[i]).getActual()));
		BigDecimal estimate=(new BigDecimal(((Row) rows.toArray()[i]).getEstimate()));
		BigDecimal percent=BigDecimal.valueOf(20.0);
		BigDecimal hundred=BigDecimal.valueOf(100.0);
		BigDecimal amountOverFall=estimate.divide(hundred);
		BigDecimal minimumAmount=amountOverFall.multiply(percent);
		if(estimate.compareTo(BigDecimal.valueOf(0.0))!=0){
		if(actual.compareTo(minimumAmount)==1 || actual.compareTo(minimumAmount)==0){
			System.out.println("Overflow");
			Toolkit.getDefaultToolkit().beep();
		    JOptionPane optionPane = new JOptionPane("You are reaching close to your estimated limit for "+((Row) rows.toArray()[i]).getTitle()+".",JOptionPane.WARNING_MESSAGE);
		    JDialog dialog = optionPane.createDialog("Warning!");
		    dialog.setAlwaysOnTop(true);
		    dialog.setVisible(true);
		}
		}
		}
	}

	public void dispRow(int row) {
		GuiRow frame = GuiRow.getInstance();
		if(row == 0) {
			frame.setVisible(false);
			return;
		}
		frame.changeInfo(getRowIndex(row));
	}
	
}//End class.
