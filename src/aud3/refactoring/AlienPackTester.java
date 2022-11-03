package aud3.refactoring;

public class AlienPackTester {
    public static void main(String[] args) {
        AlienPack alienPack = new AlienPack(4);
        alienPack.addAlien(new FinkiAlien(100,"FINKI"), 0);
        alienPack.addAlien(new FinkiAlien(100,"FINKI1"), 1);
        alienPack.addAlien(new FinkiAlien(100,"FINKI2"), 2);
        alienPack.addAlien(new FinkiAlien(100,"FINKI3"), 3);

        System.out.println(alienPack.calculateDamage());

    }
}
