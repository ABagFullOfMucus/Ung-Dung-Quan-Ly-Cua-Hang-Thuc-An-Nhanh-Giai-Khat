package model;

public abstract class Person extends Party {
    public Person() {
    }

    public Person(String id, String name, String phoneNumber, String address) {
        super(id, name, phoneNumber, address);
    }
}
