//!!Reformat as singleton per intentions for use.

package budget;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;

public class GuiRow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6426077804619743846L;
	private static final GuiRow staticFrame = new GuiRow();
	private JTextField textTitle;
	private JTextField textEstimate;
	private JTable table;
	private DefaultTableModel model = new DefaultTableModel();
	private Row editsHere;
	//Terrible, but works >.> fix later!!!
	//Fix by: instead of passing objects, we are going to make Budget a singleton that takes data, then we can just call it up from anywhere to implement updates/changes.
	private Budget owner;
	

	
	private GuiRow() {
		editsHere = new Row("Empty", "0.00");
		//Set the Budget to the input budget here.
		//When it's a singleton, this will be easier.
		owner = new Budget();
		//owner = new Category("Empty");
		
		this.setBounds(900, 0, 400, 400);
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lblTitle = new JLabel("Title: ");
		lblTitle.setBounds(12, 2, 103, 15);
		getContentPane().add(lblTitle);
		
		textTitle = new JTextField();
		textTitle.setBounds(142, 0, 114, 19);
		getContentPane().add(textTitle);
		textTitle.setColumns(10);
		
		JLabel lblEstimate = new JLabel("Estimate: ");
		lblEstimate.setBounds(12, 29, 71, 15);
		getContentPane().add(lblEstimate);
		
		textEstimate = new JTextField();
		textEstimate.setBounds(142, 25, 114, 19);
		getContentPane().add(textEstimate);
		textEstimate.setColumns(10);
		
		JLabel lblTransactions = new JLabel("New label");
		lblTransactions.setBounds(12, 52, 70, 15);
		getContentPane().add(lblTransactions);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 79, 376, 190);
		getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JButton btnSaveButton = new JButton("Save");
		btnSaveButton.setBounds(317, 333, 71, 25);
		getContentPane().add(btnSaveButton);
		btnSaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					table.getCellEditor().stopCellEditing();
				}
				catch(Exception error) {
					System.out.println(error + " @GuiRow -- Probably just no cell in edit, working as intended.");
				}
				editsHere.setTitle(textTitle.getText());
				//Numbers scrubbed by constructor.
				editsHere.setEstimate(textEstimate.getText());
				//Method to change the transactions needs to be created.  Maybe send in a double index string array to change the transactions to?
				String toSet[][] = new String[model.getRowCount()][2];
				for(int i = 0; i < model.getRowCount(); i++) {
					toSet[i][0] = (String) model.getValueAt(i, 0);
					toSet[i][0] = toSet[i][0].replaceAll("[^-?.?0-9]", "");
					toSet[i][1] = (String) model.getValueAt(i, 1);
				}
				editsHere.updateTransactions(toSet);
				owner.changedData();
				changeInfo(editsHere);
			}
		});
		
		JButton btnCancelButton = new JButton("Cancel");
		btnCancelButton.setBounds(224, 333, 81, 25);
		getContentPane().add(btnCancelButton);
		btnCancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Basically throws everything out.
				changeInfo(editsHere);
				GuiRow.getInstance().setVisible(false);
			}
		});
		
		JButton btnAddRowButton = new JButton("Add Row");
		btnAddRowButton.setBounds(284, 273, 103, 25);
		getContentPane().add(btnAddRowButton);
		btnAddRowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Simply adds a new blank row to the model.
				model.addRow(new Vector<String>());
				model.fireTableDataChanged();
			}
		});
	}
	
	public static GuiRow getInstance() {
		return staticFrame;
	}
	
	public void changeInfo(Row current) {
		this.setTitle(current.getTitle() + " - Budget Manager");
		textTitle.setText(current.getTitle());
		textEstimate.setText(NumberFormat.getCurrencyInstance().format(new BigDecimal(current.getEstimate())));
		//!!Sloppy! make it's own table model in case it changes in the future!
		model = new DefaultTableModel();
		model.addColumn("Amount");
		model.addColumn("User");
		String toLoadIn[][] = current.getTransactions();
		for(int i = 0; i < toLoadIn.length; i++) {
			Vector<String> values = new Vector<String>();
			values.add(NumberFormat.getCurrencyInstance().format(new BigDecimal(toLoadIn[i][0])));
			values.add(toLoadIn[i][1]);
			model.addRow(values);
		}
		model.fireTableDataChanged();
		table.setModel(model);
		editsHere = current;
		this.setVisible(true);
	}
	
	public void setBudget(Budget owner) {
		this.owner = owner;
	}
}
