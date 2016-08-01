package ubc.cpsc210.list;

public interface MyList<E> {
	
	/** 
	 * Add element to the list.
	 * @return True if element is successfully added
	 */
	public boolean add(E element);
	
	/**
	 * Add element at specified position in list.
	 * @throw IllegalIndexPosition if index not in [0, size()]
	 */
	public void add(int index, E element) throws IllegalIndexPosition;
	
	/**
	 * Does list contain element?
	 * @return True if element is in list
	 */
	public boolean contains(E element);
	
	/**
	 * Return element at index
	 * @return Element at index
	 * @throw IllegalIndexPosition if index not in list
	 */
	public E get(int index) throws IllegalIndexPosition;
	
	/**
	 * Remove given element from list
	 * @return True if element was in list and is removed
	 */
	public boolean remove(E element);
	
	/**
	 * Remove element at given index position
	 * @return Element at index position
	 * @throw IllegalIndexPosition if index not in list
	 */
	public E remove(int index) throws IllegalIndexPosition;
	
	/**
	 * @return Size of list
	 */
	public int size();

}
