package aud3.intro;

interface Printable {
    void print();
}

class NumberPrintable implements Printable {

    int number;

    public NumberPrintable(int number) {
        this.number = number;
    }

    @Override
    public void print() {
        System.out.printf("Number: %d\n", this.number);
    }
}

abstract class Animal {
    String name;

    public Animal(String name) {
        this.name = name;
    }

    //virtual string getSound()=0;
    abstract String getSound();
}

//class Cat : public Animal
class Cat extends Animal {

    //Cat (String name) : Animal(name)
    public Cat(String name) {
        super(name);
    }

    @Override
    String getSound() {
        return "MJAUUUUU";
    }
}

class Dog extends Animal {

    public Dog(String name) {
        super(name);
    }

    @Override
    String getSound() {
        return "AFAF";
    }
}


public class IntroTest {
    public static void main(String[] args) {
//        Animal [] animals = new Animal[2];
//        animals[0] = new Cat("garfild");
//        animals[1] = new Dog("dzhekijhoni");
//
//        for (Animal animal : animals){
//            System.out.println(animal.getSound());
//        }

        //1. Kreirame klasa koja sto go implementira interfejsot
        Printable p1 = new NumberPrintable(5);
        p1.print();

        //2. Anonimni klasi (java 8)
        Printable p2 = new Printable() {
            @Override
            public void print() {
                System.out.println("Anonimna klasa");
            }
        };
        p2.print();

        //3. Lambda izrazi (funkcionalnoto programiranje)
        //lambda izraz se koristi AKKO
        Printable p3 = () -> System.out.println("Lambda expression");
        p3.print();
    }
}
