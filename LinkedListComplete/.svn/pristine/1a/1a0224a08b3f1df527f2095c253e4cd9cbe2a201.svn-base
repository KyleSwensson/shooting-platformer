package ubc.cpsc210.list.linkedlist;

import java.io.IOException;

import ubc.cpsc210.list.IllegalIndexPosition;
import ubc.cpsc210.list.MyList;

public class MyLinkedList<E> implements MyList<E> {

	// Dummy nodes for head and tail of list
	private MyListNode<E> head, tail;

	// Current length of list
	private int length;

	public MyLinkedList() {
		tail = new MyListNode<E>();
		head = new MyListNode<E>(null, tail);
		length = 0;
	}

	@Override
	public boolean add(E element) {
		MyListNode<E> newNode = new MyListNode<E>(element, null);
		newNode.setSuccessor(head.getSuccessor());
		head.setSuccessor(newNode);
		length++;
		return true;
	}

	@Override
	public void add(int index, E element) throws IllegalIndexPosition {
		// Is the index legal?
		if ((index < 0) || (index > length))
			throw new IllegalIndexPosition(index);

		// Create a new node
		MyListNode<E> newNode = new MyListNode<E>(element, null);

		// Find the node before the new node is to go
		MyListNode<E> cursor = head;
		if (index > 0)
			cursor = find(index-1);
		// Insert the new node
		addAfter(cursor, newNode);
		length++;
	}

	@Override
	public boolean contains(E element) {
		return (find(head.getSuccessor(), element) != null);
	}

	@Override
	public E get(int index) throws IllegalIndexPosition {
		return find(index).getElement();
	}

	@Override
	public boolean remove(E element) {
		// Start at head
		MyListNode<E> cursor = head;
		
		// Traverse list keeping one ahead of element to remove
		while (cursor != null && !cursor.getSuccessor().getElement().equals(element))
				cursor = cursor.getSuccessor();
		
		if (cursor == null)
			return false;
		else {
			// Remove the element
			MyListNode<E> nodeToRemove = cursor.getSuccessor();
			cursor.setSuccessor(nodeToRemove.getSuccessor());
			nodeToRemove.setElement(null);
			nodeToRemove.setSuccessor(null);
			length = length - 1;
			return true;
		}
	}

	@Override
	public E remove(int index) throws IllegalIndexPosition {
		MyListNode<E> nodeToRemove = null;
		E elementToRemove = null;
		
		// Is index valid?
		if ((index < 0) || (index >= length)) 
			throw new IllegalIndexPosition(index);
		
		if (index == 0) {
			nodeToRemove = head.getSuccessor();
			head.setSuccessor(nodeToRemove.getSuccessor());
		} else {
			// Node before the one I want to remove
			MyListNode<E> nodeBefore = find(index - 1);
			nodeToRemove = nodeBefore.getSuccessor();
			nodeBefore.setSuccessor(nodeToRemove.getSuccessor());
		}
		
		elementToRemove = nodeToRemove.getElement();
		nodeToRemove.setElement(null);
		nodeToRemove.setSuccessor(null);
		length = length - 1;
		return elementToRemove;
	}

	@Override
	public int size() {
		return length;
	}
	
	// recursive version
	/**
	 * Finds node containing a given element
	 * @param cursor  points to node at start of list to be searched
	 * @param e       the element to find
	 * @return        first node that contains the element e; 
	 *                null if no such node is found
	 */
//	private MyListNode<E> find(MyListNode<E> cursor, E e) {
//		if (cursor == tail)
//			return null;
//		if (cursor.getElement().equals(e))
//			return cursor;
//		return find(cursor.getSuccessor(), e);
//	}
	
	// iterative version
	/**
	 * Finds node containing a given element
	 * @param cursor  points to node at start of list to be searched
	 * @param e       the element to find
	 * @return        first node that contains the element e; 
	 *                null if no such node is found
	 */
	private MyListNode<E> find(MyListNode<E> cursor, E e) {
		while ( cursor != tail ) {
			if ( cursor.getElement().equals(e))
				return cursor;
			
			cursor = cursor.getSuccessor();
		}
		
		return null;
	}
	
	// recursive version
	/**
	 * Finds the node at a given index in the list
	 * @param index  the index of the node to find
	 * @return       the node at the given index
	 * @throws       IllegalIndexPosition if there is no node at the given index in the list
	 */
//	private MyListNode<E> find(int index) throws IllegalIndexPosition {
//		// Is index valid?
//		if ((index < 0) || (index >= length)) 
//			throw new IllegalIndexPosition(index);
//		
//		return find(head.getSuccessor(), index, 0);
//	}
	
	// recursive helper
	/**
	 * Find the node at a given index in the list
	 * @param cursor   node in the list
	 * @param index    index of the node to find
	 * @param acc      index of cursor in list
	 */
//	private MyListNode<E> find(MyListNode<E> cursor, int index, int acc) {
//		if (index == acc) 
//			return cursor;
//		return find(cursor.getSuccessor(), index, acc + 1);
//	}
	
	// iterative version
	/**
	 * Finds the node at a given index in the list
	 * @param index  the index of the node to find
	 * @return       the node at the given index
	 * @throws       IllegalIndexPosition if there is no node at the given index in the list
	 */
	private MyListNode<E> find(int index) throws IllegalIndexPosition {
		// Is index valid?
		if ((index < 0) || (index >= length)) 
			throw new IllegalIndexPosition(index);
		
		// Start at first node
		MyListNode<E> cursor = head.getSuccessor();
		int i = 0;
		// Walk through list
		while (i < index) {
			cursor = cursor.getSuccessor();
			i++;
		}
		return cursor;
	}
	
	/**
	 * Adds a given node after a specified node in the list
	 * @param previous node will be inserted after this node
	 * @param newNode  the node to insert into the list
	 */
	private void addAfter(MyListNode<E> previous, MyListNode<E> newNode) {
		newNode.setSuccessor( previous.getSuccessor() );
		previous.setSuccessor( newNode );
	}

	/**
	 * Implementation from reading.
	 * Represents a node in a singly linked list.
	 */
	private class MyListNode<T> {

		private T element;
		private MyListNode<T> successor;

		/**
		 * Create an empty MyListNode object.
		 */
		public MyListNode() {
			element = null;
			successor = null;
		}

		/**
		 * Create a MyListNode object with given values.
		 */
		public MyListNode(T theElement, MyListNode<T> theSuccessor) {
			element = theElement;
			successor = theSuccessor;
		}

		public T getElement() {
			return element;
		}

		public void setElement(T newElement) {
			element = newElement;
		}

		public MyListNode<T> getSuccessor() {
			return successor;
		}

		public void setSuccessor(MyListNode<T> newSuccessor) {
			successor = newSuccessor;
		}
	}
}
