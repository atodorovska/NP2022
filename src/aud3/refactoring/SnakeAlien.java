package aud3.refactoring;

public class SnakeAlien extends Alien{
    public SnakeAlien(int health, String name) {
        super(health, name);
    }

    @Override
    int damage() {
        return 10;
    }
}
