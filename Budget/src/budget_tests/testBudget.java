package budget_tests;

import static org.junit.Assert.*;
import org.junit.*;
import budget.*;

public class testBudget {

	private Budget b1;
	private Budget b2;

	// Stuff you want to do before each test case
	@Before
	public void setup() throws Exception {
		System.out.println("\nTest starting.");
		b1 = new Budget();
		b2 = new Budget(); // family account
	}
	// Stuff you want to do after each test case
	@After
	public void teardown() {
		System.out.println("\nTest finished.");
	}
	
	// Test accessor methods
	@Test
	public void testGetName() {
		b1.setName("MyBudget");
		assertEquals("MyBudget",b1.getName());
	}
	
	@Test
	public void testGetFamilyAccountFalse() {
		b1.setFamilyAcc(false);
		assertEquals(false,b1.getFamilyAcc());
	}
	
	@Test
	public void testGetFamilyAccountTrue() {
		b2.setFamilyAcc(true);
		assertEquals(true,b2.getFamilyAcc());
	}
	
	
	
	
}
