package tutorial;

import java.util.EmptyStackException;

public class Stack<T> {
	private int capacity = -1;
	private int pointer = 0;
	private T[] objects = null;
	
	@SuppressWarnings("unchecked")
	public Stack(int capacity) {
		this.capacity = capacity;
		objects = (T[]) new Object[capacity];
	}
	
	public void push(T o) {
		if (pointer >= Size())
			throw new RuntimeException("Stack exceeded capacity!");
		objects[pointer++] = o;
	}

	public T pop() {
		if (pointer <= 0)
			throw new EmptyStackException();
		return objects[--pointer];
	}

	public boolean isEmpty() {
		return pointer <= 0;
	}

	public int Size() {
		return capacity;
	}
	
}
