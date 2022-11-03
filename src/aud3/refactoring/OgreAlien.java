package aud3.refactoring;

public class OgreAlien extends Alien{
    public OgreAlien(int health, String name) {
        super(health, name);
    }

    @Override
    int damage() {
        return 6;
    }
}
