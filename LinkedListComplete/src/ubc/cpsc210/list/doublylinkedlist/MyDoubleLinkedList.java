package ubc.cpsc210.list.doublylinkedlist;

import ubc.cpsc210.list.IllegalIndexPosition;
import ubc.cpsc210.list.MyList;


public class MyDoubleLinkedList<E> implements MyList<E> {

	// dummy nodes for head and tail of list
	private MyDListNode<E> head, tail;
	
	// number of elements in list
	private int length;
	
	/**
	 * Constructor creates an empty list
	 */
	public MyDoubleLinkedList() {
		head = new MyDListNode<E>();
		tail = new MyDListNode<E>(null, head, null);
		head.setSuccessor(tail);
		length = 0;
	}
	
	@Override
	public boolean add(E element) {
		MyDListNode<E> newNode = new MyDListNode<E>(element, head, head.getSuccessor());
		head.getSuccessor().setPredecessor(newNode);
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
		MyDListNode<E> newNode = new MyDListNode<E>(element, null, null);

		// Find the node before the new node is to go
		MyDListNode<E> cursor = head;
		if (index > 0)
			cursor = find(index-1);
		// Insert the new node
		addAfter(cursor, newNode);
		length++;
	}

	@Override
	public boolean contains(E element) {
		return( find(head.getSuccessor(), element) != null );
	}

	@Override
	public E get(int index) throws IllegalIndexPosition {
		return find(index).getElement();
	}

	@Override
	public boolean remove(E element) {
		// Start at head
		MyDListNode<E> nodeToRemove = find(head.getSuccessor(), element);
		
		if (nodeToRemove == null)
			return false;
		else {
			// Remove the element;
			MyDListNode<E> predecessor = nodeToRemove.getPredecessor();
			predecessor.setSuccessor(nodeToRemove.getSuccessor());
			nodeToRemove.getSuccessor().setPredecessor(predecessor);
			
			nodeToRemove.setElement(null);
			nodeToRemove.setSuccessor(null);
			length = length - 1;
			return true;
		}
	}

	@Override
	public E remove(int index) throws IllegalIndexPosition {
		// Is the index legal?
		if ((index < 0) || (index >= length))
			throw new IllegalIndexPosition(index);
		
		MyDListNode<E> nodeToRemove = find(index);
		E elementToRemove = nodeToRemove.getElement();
		
		// remove node
		MyDListNode<E> predecessor = nodeToRemove.getPredecessor();
		predecessor.setSuccessor(nodeToRemove.getSuccessor());
		nodeToRemove.getSuccessor().setPredecessor(predecessor);
		
		nodeToRemove.setElement(null);
		nodeToRemove.setSuccessor(null);
		length = length - 1;
		return elementToRemove;
	}

	@Override
	public int size() {
		return length;
	}
	
	/**
	 * Adds a given node after a specified node in the list
	 * @param previous node will be inserted after this node
	 * @param newNode  the node to insert into the list
	 */
	private void addAfter(MyDListNode<E> previous, MyDListNode<E> newNode) {
		newNode.setSuccessor(previous.getSuccessor());
		newNode.setPredecessor(previous);
		previous.getSuccessor().setPredecessor(newNode);
		previous.setSuccessor(newNode);
	}
		
	/**
	 * Finds node containing a given element
	 * @param cursor  points to node at start of list to be searched
	 * @param e       the element to find
	 * @return        first node that contains the element e; 
	 *                null if no such node is found
	 */
	private MyDListNode<E> find(MyDListNode<E> cursor, E e) {
		while ( cursor != tail ) {
			if ( cursor.getElement().equals(e))
				return cursor;
			
			cursor = cursor.getSuccessor();
		}
		
		return null;
	}
	
	/**
	 * Finds the node at a given index in the list
	 * @param index  the index of the node to find
	 * @return       the node at the given index
	 * @throws       IllegalIndexPosition if there is no node at the given index in the list
	 */
	private MyDListNode<E> find(int index) throws IllegalIndexPosition {
		// Is index valid?
		if ((index < 0) || (index >= length)) 
			throw new IllegalIndexPosition(index);
		
		// Start at first node
		MyDListNode<E> cursor = head.getSuccessor();
		int i = 0;
		// Walk through list
		while (i < index) {
			cursor = cursor.getSuccessor();
			i++;
		}
		return cursor;
	}
	
	/**
	 * Represents a node in a doubly linked list.
	 */
	private class MyDListNode<T> {
		
		private MyDListNode<T> predecessor;
		private MyDListNode<T> successor;
		private T element;
		
		public MyDListNode() {
			predecessor = null;
			successor = null;
			element = null;
		}
		
		public MyDListNode(T theElement, MyDListNode<T> thePredecessor,
				MyDListNode<T> theSuccessor) {
			element = theElement;
			predecessor = thePredecessor;
			successor = theSuccessor;
		}
		
		public MyDListNode<T> getPredecessor() {
			return predecessor;
		}
		
		public MyDListNode<T> getSuccessor() {
			return successor;
		}
		
		public T getElement() {
			return element;
		}
		
		public void setSuccessor(MyDListNode<T> newSuccessor) {
			successor = newSuccessor;
		}
		
		public void setPredecessor(MyDListNode<T> newPredecessor) {
			predecessor = newPredecessor;
		}
		
		public void setElement(T newElement) {
			element = newElement;
		}
	}
}
