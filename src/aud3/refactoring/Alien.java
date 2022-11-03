package aud3.refactoring;

public abstract class Alien {

    public int health; // 0=dead, 100=full strength
    public String name;

    public Alien(int health, String name) {
        this.health = health;
        this.name = name;
    }

    abstract int damage();
}
