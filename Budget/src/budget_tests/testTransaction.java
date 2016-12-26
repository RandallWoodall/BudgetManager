package budget_tests;

import static org.junit.Assert.*;

import org.junit.*;

import budget.*;

import java.util.*;

import junit.framework.Test;

public class testTransaction {

	private Transaction t1;
	private Transaction t2;

	// Stuff you want to do before each test case
	@Before
	public void setup() throws Exception {
		System.out.println("\nTest starting.");
		t1 = new Transaction("Peter","500.00");
		t2 = new Transaction("Jenny","100.00");
	}
	// Stuff you want to do after each test case
	@After
	public void teardown() {
		System.out.println("\nTest finished.");
	}
	
	@Test
	public void testGetValue() {
		assertEquals("500.00",t1.getValue());
		assertEquals("100.00",t2.getValue());
	}
	
	@Test
	public void testUserName() {
		assertEquals("Peter",t1.getUser_name());
		assertEquals("Jenny",t2.getUser_name());
	}
	
	// Make sure to test the wrong parameter
	@Test
	public void testSetValueNull() {
		t1.setValue("");
		assertEquals("0.00",t1.getValue());
		t2.setValue("");
		assertEquals("0.00",t2.getValue());
	}
	
	// Test when input the string value "cow"/ negative values
	
	@Test
	public void testSetValueCorrect() {
		t1.setValue("20.00");
		assertEquals("20.00",t1.getValue());
		t2.setValue("10.00");
		assertEquals("10.00",t2.getValue());
	}
	
	
	//Attemp to change the name outside of the Transaction object
	@Test
	public void testSetUserName() {
		t1.setUser_name("Josh");
		assertEquals("Josh",t1.getUser_name());
		t2.setUser_name("Anna");
		assertEquals("Anna",t2.getUser_name());
	}
	
	
}
