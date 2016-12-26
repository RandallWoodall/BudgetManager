package budget;

import java.io.*;
import java.math.BigDecimal;

import javax.swing.JList;

public class FileOps {

	//function to export budget to a text file
			public static void PDFExport(Budget b, File toExport) //throws IOException
			{
				PrintWriter exporter;
				try{
					exporter = new PrintWriter(toExport);
					//Write the Budget name to the file.
					exporter.println(b.getName());
					exporter.println();
					Category[] inc = b.getIncomeCategories();
					Category[] exp = b.getExpenseCategories();
					exporter.println("Income:");
					for(int i = 0; i < inc.length; i++)
					{
						Category a = inc[i];
						exporter.println("\t"+a.getCategoryName()+":");
						Row[] incr = a.returnRows();
						for(int j = 0; j < incr.length;j++)
						{
							Row r = incr[j];	
							if(b.getFamilyAcc())
								exporter.println("\t\t"+r.getTitle()+" $"+r.getActual()+" : "+r.getUserName());
							else
								exporter.println("\t\t"+r.getTitle()+" $"+r.getActual());
						}
								
					}
					exporter.println("Expenses:");
					for(int i = 0; i < exp.length; i++)
					{
						Category a = exp[i];
						exporter.println("\t"+a.getCategoryName()+":");
						Row[] incr = a.returnRows();
						for(int j = 0; j < incr.length;j++)
						{
							Row r = incr[j];
							if(b.getFamilyAcc())
								exporter.println("\t\t"+r.getTitle()+" $"+r.getActual()+" : "+r.getUserName());
							else
								exporter.println("\t\t"+r.getTitle()+" $"+r.getActual());
						}
					}
					exporter.println();
					exporter.println("Income Totals:\t$"+b.calcIncomeTotals());
					exporter.println("Expense Totals:\t$"+b.calcExpenseTotals());
					exporter.close();
				}
				catch(Exception e) {
					System.out.println("Unable to work on file: " + toExport + " for export! Operation failed!");
					return;
				}			
				return;
			}//end PDFExport
			
		//function to build budget from imported CSVFileInputStream reader = new FileInputStream(toLoad);
		public static void CSVImport(Budget b, File toImport, JList<Object> list)
		{ 
			String line = "";
			BufferedReader reader = null;
			String split = ",";
			try {
				reader = new BufferedReader(new FileReader(toImport));
				while((line = reader.readLine())!=null)
				{
					String[] values = line.split(split);
					int familyImport = values.length;
					System.out.println(familyImport);
					if(familyImport==5) 					
						b.setFamilyAcc(true);
					String amount = values[3];
					int index = 0;
					boolean check = false;
					if(values[0].toLowerCase().equals("income"))
					{
						Category[] incomeCats = b.getIncomeCategories();
						for(int i = 0; i < incomeCats.length;i++)
							if(incomeCats[i].toString().toLowerCase().equals(values[1].toLowerCase()))
							{
								index = i;
								check = true;
							}
						if(!check)
						{
							b.addIncome(values[1]);
							b.getCategoryIndex(b.getIncomeCategories().length).addRow(values[2],amount);
							if(familyImport==5)
								b.getCategoryIndex(b.getIncomeCategories().length).addRowTransaction(b.getCategoryIndex(b.getIncomeCategories().length).returnRows().length,values[4], amount);	
							else
								b.getCategoryIndex(b.getIncomeCategories().length).addRowTransaction(b.getCategoryIndex(b.getIncomeCategories().length).returnRows().length,"Unknown", amount);
						}
						else 
						{
							b.getCategoryIndex(index+1).addRow(values[2],amount);
							if(familyImport==5)
								b.getCategoryIndex(index+1).addRowTransaction(b.getCategoryIndex(index+1).returnRows().length,values[4], amount);
							else
								b.getCategoryIndex(index+1).addRowTransaction(b.getCategoryIndex(index+1).returnRows().length,"Unknown", amount);
						}	
					}
					else if(values[0].toLowerCase().equals("expense"))
					{
						Category[] expenseCats = b.getExpenseCategories();
						for(int i = 0; i < expenseCats.length;i++)
							if(expenseCats[i].toString().toLowerCase().equals(values[1].toLowerCase()))
							{
								index = i;
								check = true;
							}
						if(!check)
						{
							b.addExpense(values[1]);
							b.getCategoryIndex(b.getIncomeCategories().length+b.getExpenseCategories().length).addRow(values[2], amount);
							if(familyImport==5)
								b.getCategoryIndex(b.getIncomeCategories().length+b.getExpenseCategories().length).addRowTransaction(b.getCategoryIndex(b.getIncomeCategories().length+b.getExpenseCategories().length).returnRows().length, values[4], amount);
							else
								b.getCategoryIndex(b.getIncomeCategories().length+b.getExpenseCategories().length).addRowTransaction(b.getCategoryIndex(b.getIncomeCategories().length+b.getExpenseCategories().length).returnRows().length, "Unknown", amount);
						}
						else
						{
							b.getCategoryIndex(b.getIncomeCategories().length+index+1).addRow(values[2], amount);
							if(familyImport==5)
								b.getCategoryIndex(b.getIncomeCategories().length+index+1).addRowTransaction(b.getCategoryIndex(b.getIncomeCategories().length+index+1).returnRows().length, values[4], amount);
							else
								b.getCategoryIndex(b.getIncomeCategories().length+index+1).addRowTransaction(b.getCategoryIndex(b.getIncomeCategories().length+index+1).returnRows().length, "Unknown", amount);						
						}		
					}
					//error checking (neither income or expense)			
			 	}//end while loop
				list.setListData(b.categoryNames());
			} catch (FileNotFoundException e) {
				e.printStackTrace();	
			}  catch (IOException e) {
				e.printStackTrace();
			}	finally {
				if(reader!=null){
					try{
						reader.close();
					}
					catch (IOException e){
						e.printStackTrace();
					}
				}
			}
		}//end CSVImport

	public static void save(Budget toSave, File saveTo) {
		FileOutputStream saver;
		try {
			//!!MAP!!
			//1 - String
			//2 - int
			//3 - Row
			//4 - Category
			//5 - Transaction
			//6 - BigDecimal
			//7 - End of income categories
			saver = new FileOutputStream(saveTo);
			//Write the Budget name to the file.
			writeStringToFile(saver, toSave.getName());

			//!!Possible data leak, we may be allowing a dev full access to Rows here, refactor this later.
			//Write all the income categories to the file.
			Category incomeCategories[] = toSave.getIncomeCategories();
			for(int i = 0; i < incomeCategories.length; i++)
				writeCategoryToFile(saver, incomeCategories[i]);
			saver.write(7);
			//Write all the expense categories to the file.
			Category expenseCategories[] = toSave.getExpenseCategories();
			for(int i = 0; i < expenseCategories.length; i++)
				writeCategoryToFile(saver, expenseCategories[i]);
			saver.close();
		}
		catch(Exception e) {
			System.out.println("Unable to work on file: " + saveTo + " for saving! Operation failed!");
			return;
		}
	}//End save.

	private static void writeCategoryToFile(FileOutputStream file, Category category) {
		Row rowsToWrite [];
		try {
			file.write(4);
			writeStringToFile(file, category.getCategoryName());
			rowsToWrite = category.returnRows();
			for(int i = 0; i < rowsToWrite.length; i++)
				writeRowToFile(file, rowsToWrite[i]);
		}
		catch(Exception e) {
			System.out.println("Error writing category to file!");
		}
	}

	private static void writeStringToFile(FileOutputStream file, String toWrite) {
		try {
			file.write(1);
			byte stringStream[] = toWrite.getBytes();
			short size = (short) stringStream.length;
			//If size < 255, it only writes one byte, this is a quick hack >.>
			if(size < 255)
				file.write(0);
			file.write(size);
			file.write(stringStream);
		}
		catch(Exception e) {
			System.out.println("Error writing String!");
		}
	}

	private static void writeRowToFile(FileOutputStream file, Row toWrite) {
		String transactions [];
		try {
			file.write(3);
			writeStringToFile(file, toWrite.getTitle());
			file.write(6);
			BigDecimal estimate = new BigDecimal(toWrite.getEstimate());
			System.out.println(toWrite.getEstimate());
			writeStringToFile(file, estimate.toString());
			transactions = toWrite.getTransactionVals();//This needed to change with the addition of user_name in Transaction anyway.
			for(int i = 0; i < transactions.length; i++) {
				file.write(5);
				file.write(6);
				writeStringToFile(file, transactions[i]);
			}
		}
		catch(Exception e) {
			System.out.println("Error writing row to file!");
		}
	}

	/**
	 * This constructor will build a budget based off the input from a file selected by the user.
	 */
	public static void load(Budget loadTo, File toLoad) {
		//!!MAP!!
		//1 - String
		//2 - int
		//3 - Row
		//4 - Category
		//5 - Transaction
		//6 - BigDecimal
		//7 - End of income categories
		//Load a new budget first, we will then modify if the file is good.
		loadTo.clone(new Budget());
		//Unverified file is the expected input.
		byte identifier [] = new byte[1];
		int status;
		Category building;
		try {
			FileInputStream reader = new FileInputStream(toLoad);

			//Now for the difficult part of loading a new budget.
			//Trash the first identifier, since it's just telling us it's a string.
			reader.read(identifier);

			//Load the name now.
			loadTo.setName(readString(reader));

			//If there's anything else in the file, it will be categories.
			//Load the value of the next byte in as status
			reader.read(identifier);
			status = identifier[0];
			while(status == 4 && reader.available() > 0) {
				building = new Category("BuilderBlank");
				status = readCategory(reader, building);
				loadTo.addIncome(building);
			}
			reader.read(identifier);
			status = identifier[0];
			while(reader.available() > 0) 
			{
				building = new Category("BuilderBlank");
				status = readCategory(reader, building);
				loadTo.addExpense(building);
			}
			reader.close();
		}
		catch (Exception e) {
			System.out.println("File: " + toLoad + " not found!");
			System.out.println("Budget creation from file failed!");
			return;
		}
		loadTo.changedData();
	}//End file load.

	private static int readCategory(FileInputStream file, Category toLoad) {
		int status = 0;
		byte identifier[] = new byte[1];
		try {
			//If we made it here, we have already pulled the "4" for a category, now pull in the name.
			file.skip(1);
			toLoad.setName(readString(file));
			//Now we need to pull and load the transactions.
			file.read(identifier);
			status = identifier[0];
			while(status == 3 && file.available() > 0) {
				Row reading = new Row("Empty", "0.00");
				status = readRow(file, reading);
				toLoad.addRow(reading);
				//file.read(identifier);
				//status = identifier[0];
			}
			toLoad.changedData();
		}
		catch(Exception e) {
			System.out.println("Error while reading category!");
		}
		return status;
	}

	private static int readRow(FileInputStream file, Row toLoad) {
		byte identifier[] = new byte[1];
		int status = 0;
		try {
			//We will first be reading in the name, throw the identifier for.
			file.read(identifier);
			if(identifier[0] == 1)
				toLoad.setTitle(readString(file));
			//Now check the identifier for BigDecimal
			file.read(identifier);
			status = identifier[0];
			if(status == 6) {
				//Skip the identifier for String.
				file.skip(1);
				toLoad.setEstimate(readString(file));
				file.read(identifier);
				status = identifier[0];
			}
			//If there was no estimate, we may have a problem here...
			//Time to read in the Transaction objects.
			while(status == 5 && file.available() > 0) {
				file.skip(2);
				toLoad.addTransaction("Unknown", readString(file));
				file.read(identifier);
				status = identifier[0];
			}
		}
		catch(Exception e) {
			System.out.println("Error reading row! " + e);
		}
		return status;
	}

	private static String readString(FileInputStream file) {
		short len;
		byte lenBytes[] = new byte[2];
		byte stringStream[];
		try {
			file.read(lenBytes);
			len = (short) (((lenBytes[0]&0xFF)<<8) | (lenBytes[1]&0xFF));
			stringStream = new byte[len];
			file.read(stringStream);
			String toReturn = new String(stringStream);
			System.out.println(toReturn);
			return toReturn;
		}
		catch(Exception e) {
			System.out.println("Error reading string in!");
		}
		//shouldn't reach this unless there's an error...
		return "Error!!";
	}

}//End class.
