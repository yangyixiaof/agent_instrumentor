package cn.edu.tsinghua.thss.generic;

public class WildCardDemo1 {
    public static void main(String[] args) {
        GenericStack<Integer> intStack = new GenericStack<Integer>();
        intStack.push(1);
        intStack.push(2);
        intStack.push(-2);

        System.out.println("The max number is " + max(intStack));
    }

    private static double max(GenericStack<? extends Number> stack) {
        double max = stack.pop().doubleValue();

        while (!stack.isEmpty()) {
            double value = stack.pop().doubleValue();
            if (value > max) {
                max = value;
            }
        }

        return max;
    }
}
