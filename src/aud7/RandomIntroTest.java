package aud7;

import java.util.Random;

public class RandomIntroTest {
    public static void main(String[] args) {

         //100 slucjani integer

        int sum = 0;
        Random random = new Random();
        for (int i=0;i<100000;i++){

            int randInt = random.nextInt(100)+1; //1-100
            sum+=randInt;
//            System.out.println(randInt);
        }

        System.out.printf("Average: %.2f%n", sum/100000.0);
    }
}
