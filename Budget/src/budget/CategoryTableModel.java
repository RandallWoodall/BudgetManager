package budget;

import javax.swing.table.DefaultTableModel;

public class CategoryTableModel extends DefaultTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//
	//Function: isCellEditable(int, int)
	//Purpose: This will block off specific cells to avoid edits.
	public boolean isCellEditable(int row, int column) {
		//if(column == 1)
		//	return true;
		return false;
	}//End isCellEditable overloaded function.
	
	//This can be refactored to allow us to pull data directly from the table -- huge change in how we save/change things if we pull it off.
}
