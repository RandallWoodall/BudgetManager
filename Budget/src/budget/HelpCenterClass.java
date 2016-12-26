package budget;
import java.awt.*;

import javax.swing.*;

public class HelpCenterClass extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public HelpCenterClass() {
		JFrame frame = new JFrame("User Manual");
		JPanel panel = new JPanel();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		StringBuilder helpBuild = new StringBuilder();
		helpBuild.append("<html>Create new budget: File -> New Budget<br><br>");
		helpBuild.append("Save budget: File -> Save Budget<br><br>");
		helpBuild.append("Load Budget: File -> Load Budget<br><br>");
		helpBuild.append("Import CSV file: File -> Import CSV<br><br>");
		helpBuild.append("Export budget to text file: File -> Export to TXT<br><br>");
		helpBuild.append("Save budget: File -> Save Budget<br><br>");
		helpBuild.append("Exit: File -> Exit<br><br>");
		helpBuild.append("Add category:" + "\n" + "File -> Add -> Add Income/Expense category<br><br>");
		helpBuild.append("Add sub-category: File -> Add Sub-Category<br>");
		helpBuild.append("To add sub-category, you must click the main category and then add sub-category<br><br>");
		helpBuild.append("Add transaction/estimate: Add -> Add transaction/estimate<br>");
		helpBuild.append("To add transaction/estimate amount, first add sub-category and click sub-category to add transaction/estimate amount<br><br>");
		helpBuild.append("Remove category: Remove -> Remove Category<br>");
		helpBuild.append("To remove a certain category, first click the category and them perform remove action<br><br>");
		helpBuild.append("Set family account: Settings -> Toggle Family Account<br><br>");
		helpBuild.append("The Toggle Family Account allows users to set the family account to true and false alternatively<br><br>");
		helpBuild.append("Set aside amount of savings: Savings -> Travel Savings<br><br>");

		JLabel label = new JLabel("Help");
		label.setText(helpBuild.toString());
		frame.add(label);
		frame.setSize(new Dimension(475,575));
		frame.setResizable(Boolean.FALSE);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}


