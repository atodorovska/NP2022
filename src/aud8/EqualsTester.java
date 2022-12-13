package aud8;

import java.util.List;
import java.util.stream.IntStream;

public class EqualsTester {

    public static <T> boolean equals (List<T> left, List<T> right) {
        if (left.size()!=right.size()){
            return false;
        }

        return IntStream.range(0,left.size())
                .allMatch(i -> left.get(i).equals(right.get(i)));

//        for (int i=0;i<left.size();i++) {
//            if (!left.get(i).equals(right.get(i))){
//                return false;
//            }
//        }
//        return true;
    }
    public static void main(String[] args) {

    }
}
