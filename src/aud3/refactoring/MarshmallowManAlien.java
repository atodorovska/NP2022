package aud3.refactoring;

public class MarshmallowManAlien extends Alien{
    public MarshmallowManAlien(int health, String name) {
        super(health, name);
    }

    @Override
    int damage() {
        return 1;
    }
}
