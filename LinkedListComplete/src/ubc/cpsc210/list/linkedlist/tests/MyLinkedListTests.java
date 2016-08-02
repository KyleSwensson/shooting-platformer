package ubc.cpsc210.list.linkedlist.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import ubc.cpsc210.list.IllegalIndexPosition;
import ubc.cpsc210.list.MyList;

public abstract class MyLinkedListTests {

	protected MyList<Integer> aList;

	@Test
	public void addOneElement() {
		assertFalse(aList.contains(1));
		assertTrue(aList.add(1));
		assertTrue(aList.contains(1));
		assertTrue(aList.size() == 1);
	}
	
	@Test
	public void addManyElements() {
		for (int i = 0; i < 10; i++ )
			assertFalse(aList.contains(new Integer(i)));
		for (int i = 0; i < 10; i++)
			assertTrue(aList.add(new Integer(i)));
		for (int i = 0; i < 10; i++)
			assertTrue(aList.contains(new Integer(i)));
		assertTrue(aList.size() == 10);
	}
	
	@Test
	public void addTwoElementsAtIndex() throws IllegalIndexPosition {
		assertFalse(aList.contains(1));
		aList.add(0, new Integer(1));
		assertTrue(aList.contains(new Integer(1)));
		assertFalse(aList.contains(2));
		aList.add(1, new Integer(2));
		assertTrue(aList.contains(new Integer(2)));
		assertTrue(aList.size() == 2);
	}
	
	@Test
	public void addOneElementAtIndex() throws IllegalIndexPosition {
		assertFalse(aList.contains(1));
		aList.add(0, new Integer(1));
		assertTrue(aList.contains(1));
		assertTrue(aList.size() == 1);
	}	
	
	@Test
	public void addManyElementsAtIndex() throws IllegalIndexPosition {
		for (int i = 0; i < 10; i++ )
			assertFalse(aList.contains(new Integer(i)));
		// add to end of list
		for (int i = 0; i < 5; i++)
			aList.add(i, new Integer(i));
		// add in middle of list
		for (int i = 5; i < 10; i++)
			aList.add(aList.size() / 2, new Integer(i));
		for (int i = 0; i < 10; i++)
			assertTrue(aList.contains(new Integer(i)));
		assertTrue(aList.size() == 10);
	}
	
	@Test
	public void getOne() throws IllegalIndexPosition {
		aList.add(new Integer(1));
		assertTrue(aList.get(0).equals(new Integer(1)));
	}
	
	@Test
	public void getMany() throws IllegalIndexPosition {
		for (int i = 0; i < 10; i++)
			aList.add(i, new Integer(i));
		for (int i = 0; i < 10; i++)
			assertTrue(aList.get(i).equals(new Integer(i)));
	}
	
	@Test
	public void removeFromSingleElementListByIndexPosition() throws IllegalIndexPosition {
		aList.add(new Integer(1));
		assertTrue(aList.contains(new Integer(1)));
		aList.remove(0);
		assertFalse(aList.contains(new Integer(1)));
		assertTrue(aList.size() == 0);
	}
	
	@Test
	public void removeAtEndByIndexPosition() throws IllegalIndexPosition {
		// Make list be: 1 2 3 since elements added at start
		for (int i = 3; i > 0; i--)
			aList.add(new Integer(i));
		for (int i = 3; i > 0; i--)
			assertTrue(aList.contains(new Integer(i)));
		aList.remove(2);
		assertFalse(aList.contains(new Integer(3)));
		assertTrue(aList.contains(new Integer(2)));
		assertTrue(aList.contains(new Integer(1)));
		assertTrue(aList.size() == 2);
	}
	
	@Test
	public void removeTwoElementsByIndexPosition() throws IllegalIndexPosition {
		for (int i = 3; i > 0; i--)
			aList.add(new Integer(i));
		for (int i = 3; i > 0; i--)
			assertTrue(aList.contains(new Integer(i)));
		aList.remove(2);
		aList.remove(1);
		assertFalse(aList.contains(new Integer(3)));
		assertFalse(aList.contains(new Integer(2)));
		assertTrue(aList.contains(new Integer(1)));
		assertTrue(aList.size() == 1);
	}
	
	@Test
	public void removeAllByIndexPosition() throws IllegalIndexPosition {
		for (int i = 3; i > 0; i--)
			aList.add(new Integer(i));
		for (int i = 3; i > 0; i--)
			assertTrue(aList.contains(new Integer(i)));
		for (int i = 3; i > 0; i--)
			aList.remove(i-1);
		for (int i = 3; i > 0; i--)
			assertFalse(aList.contains(new Integer(i)));
		assertTrue(aList.size() == 0);
	}
	
	@Test
	public void removeFromSingleElementListByElement() {
		aList.add(new Integer(1));
		assertTrue(aList.contains(new Integer(1)));
		aList.remove(new Integer(1));
		assertFalse(aList.contains(new Integer(1)));
		assertTrue(aList.size() == 0);
	}
	
	@Test
	public void removeAtEndByElement() throws IllegalIndexPosition {
		// Make list be: 1 2 3 since elements added at start
		for (int i = 3; i > 0; i--)
			aList.add(new Integer(i));
		for (int i = 3; i > 0; i--)
			assertTrue(aList.contains(new Integer(i)));
		aList.remove(new Integer(3));
		assertFalse(aList.contains(new Integer(3)));
		assertTrue(aList.contains(new Integer(2)));
		assertTrue(aList.contains(new Integer(1)));
		assertTrue(aList.size() == 2);
	}
	
	@Test
	public void removeTwoElementsByElement() throws IllegalIndexPosition {
		for (int i = 3; i > 0; i--)
			aList.add(new Integer(i));
		for (int i = 3; i > 0; i--)
			assertTrue(aList.contains(new Integer(i)));
		aList.remove(new Integer(3));
		aList.remove(new Integer(2));
		assertFalse(aList.contains(new Integer(3)));
		assertFalse(aList.contains(new Integer(2)));
		assertTrue(aList.contains(new Integer(1)));
		assertTrue(aList.size() == 1);
	}
	
	@Test
	public void removeAllByElement() {
		for (int i = 3; i > 0; i--)
			aList.add(new Integer(i));
		for (int i = 3; i > 0; i--)
			assertTrue(aList.contains(new Integer(i)));
		for (int i = 3; i > 0; i--)
			aList.remove(new Integer(i));
		for (int i = 3; i > 0; i--)
			assertFalse(aList.contains(new Integer(i)));
		assertTrue(aList.size() == 0);
	}
	
	@Test (expected = IllegalIndexPosition.class)
	public void removeAtIndexTooSmall() throws IllegalIndexPosition {
		aList.remove(0);
	}
	
	@Test (expected = IllegalIndexPosition.class)
	public void removeAtIndexTooLarge() throws IllegalIndexPosition {
		for (int i = 0; i < 3; i++)
			aList.add(new Integer(i));
		aList.remove(3);
	}
	
	@Test (expected = IllegalIndexPosition.class)
	public void addAtIndexTooSmall() throws IllegalIndexPosition {
		aList.add(-1, new Integer(1));
	}
	
	@Test (expected = IllegalIndexPosition.class)
	public void addAtIndexTooLarge() throws IllegalIndexPosition {
		for (int i = 0; i < 3; i++)
			aList.add(new Integer(i));
		aList.add(4, new Integer(4));
	}
	
	@Test (expected = IllegalIndexPosition.class)
	public void getAtIndexTooSmall() throws IllegalIndexPosition {
		aList.get(0);
	}
	
	@Test (expected = IllegalIndexPosition.class)
	public void getAtIndexTooLarge() throws IllegalIndexPosition {
		for (int i = 0; i < 3; i++)
			aList.add(new Integer(i));
		aList.get(3);
	}
}
