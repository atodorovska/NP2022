package aud3.refactoring;

public class FinkiAlien extends Alien{
    public FinkiAlien(int health, String name) {
        super(health, name);
    }

    @Override
    int damage() {
        return 100;
    }
}
