package aud2;

public class StringPrefix {

    public static boolean isPrefix(String first, String second) {
        if (first.length() > second.length()) return false;

        for (int i = 0; i < first.length(); i++)
            if (first.charAt(i) != second.charAt(i))
                return false;
        return true;
    }

    public static void main(String[] args) {
        System.out.println(StringPrefix.isPrefix("test", "testing"));
        System.out.println(isPrefix("test", "apple"));
        System.out.println(isPrefix("test", "tesla"));
        System.out.println(isPrefix("testing", "test"));
    }
}
