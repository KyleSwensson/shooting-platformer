package ubc.cpsc210.list.linkedlist.tests;

import org.junit.Before;
import ubc.cpsc210.list.linkedlist.MyLinkedList;

public class SingleLinkedListTest extends MyLinkedListTests {

	@Before
	public void setup() {
		aList = new MyLinkedList<Integer>();
	}
}
