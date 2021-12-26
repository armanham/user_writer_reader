package com.company;

public class User {
    private String name;
    private String surName;
    private int age;

    public User(String name, String surName, int age) {
        this.name = name;
        this.surName = surName;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getSurName() {
        return surName;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "User{\n" + "name: " + name + "\n" + "surname: " + surName + "\n" + "age: " + age + "\n}\n";
    }
}
