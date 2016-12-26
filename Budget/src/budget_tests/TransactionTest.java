package budget_tests;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import budget.Transaction;

import org.junit.*;

public class TransactionTest {
	
	private Transaction toTest;

	@Before
	public void setup() {
		toTest = new Transaction("RW", "100.00");
	}
	
	/**
	 * Tests the default constructor, and then the getValue method.
	 */
	@Test
	public void testGetValue() {
		assertTrue(toTest.getValue().equals(new BigDecimal("100.00")));
	}

	/**
	 * Tests setValue with number string input
	 */
	@Test
	public void testSetValueString() {
		assertTrue(toTest.getValue().equals(new BigDecimal("100.00")));
	}
	
	/**
	 * Test setValue with a non-number string.
	 */
	@Test
	public void testSetValue2() {
		toTest.setValue("cow");
		assertTrue(toTest.getValue().equals(new BigDecimal("100.00")));
	}
	
	/**
	 * Test setValue with a negative number.
	 */
	@Test
	public void testSetValue3() {
		toTest.setValue("-100.00");
		assertTrue(toTest.getValue().equals(new BigDecimal("100.00")));
	}
	
	/**
	 * Attempt to change the name outside of the Transaction object.
	 */
	@Test
	public void testOutsideChangesName() {
		String test1 = toTest.getUser_name();
		test1 = "notRight";
		assertTrue(toTest.getUser_name() != test1);
	}

}
