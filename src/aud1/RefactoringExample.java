package aud1;

public class RefactoringExample {

    /**
     * @param start
     * @param end
     * @return the count of all numbers between 'start' and 'end' that are divisible with the sum of their digits
     */
    public static int countAllNumbersDivisibleWithDigitsSum(int start, int end) {
        int count = 0;

        for (int i = start; i < end; i++) {
            if (i % getDigitsSum(i) == 0)
                ++count;
        }
        return count;
    }

    private static int getDigitsSum(int num) {
        int sum = 0;
        while (num > 0) {
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(countAllNumbersDivisibleWithDigitsSum(10, 50));
    }
}
