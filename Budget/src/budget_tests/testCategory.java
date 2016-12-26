package budget_tests;

import static org.junit.Assert.*;
import org.junit.*;

import budget.*;

public class testCategory {

	private Row r1;
	private Row r2;

	// Stuff you want to do before each test case
	@Before
	public void setup() throws Exception {
		System.out.println("\nTest starting.");
		r1 = new Row("Rent","600.00");
		r2 = new Row("Groceries","300.00");
	}
	// Stuff you want to do after each test case
	@After
	public void teardown() {
		System.out.println("\nTest finished.");
	}
	
	@Test
	public void testGetTitle() {
		assertEquals("Rent",r1.getTitle());
		assertEquals("Groceries",r2.getTitle());
	}
	
	@Test
	public void testEstimate() {
		assertEquals("600.00",r1.getEstimate());
		assertEquals("300.00",r2.getEstimate());
	}
	
	@Test
	public void testSetTitle() {
		r1.setTitle("Utilities");
		assertEquals("Utilities",r1.getTitle());
		r2.setTitle("Clothes");
		assertEquals("Clothes",r2.getTitle());
	}
	
	@Test
	public void testSetEstimate() {
		r1.setEstimate("900.00");
		assertEquals("900.00",r1.getEstimate());
		r2.setEstimate("350.00");
		assertEquals("350.00",r2.getEstimate());
	}
	
	
}
