package tutorial;

public class UseStack {
	
	public UseStack() {
	}
	
	public void use_stack(Stack<Integer> stack)
	{
		if (stack.Size() == 100)
		{
			System.out.println("haha");
		}
		Integer int1 = stack.pop();
		if (int1 == 98)
		{
			int1--;
			stack.pop();
			stack.push(int1);
		}
		if (stack.Size() == 101)
		{
			System.out.println("reach here.");
		}
	}
	
}
