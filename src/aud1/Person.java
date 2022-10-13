package aud1;

import java.util.Objects;

public class Person {

    private String name;
    private String surname;
    private int age;
    private String address;

    public Person() {
    }

    public Person(String name, String surname, int age, String address) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age
                && Objects.equals(name, person.name)
                && Objects.equals(surname, person.surname)
                && Objects.equals(address, person.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, age, address);
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Surname: %s, Age: %d, Address: %s", name, surname, age, address);
    }

    public static void main(String[] args) {
        Person person1 = new Person("Stephen", "Colbert", 30, "USA");
        Person person2 = new Person("John", "Oliver", 40, "UK");
        Person person3 = new Person("Trevor", "Noah", 50, "USA");

        Person person4 = new Person("Stephen", "Colbert", 30, "USA");

        System.out.println(person1);
        System.out.println(person2);
        System.out.println(person3);

        System.out.println(person1.equals(person4));
        System.out.println(person2.equals(person4));
    }
}
