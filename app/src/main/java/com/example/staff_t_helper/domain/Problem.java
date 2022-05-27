package com.example.staff_t_helper.domain;

public class Problem {

    private int id;

    private String name;

    private Person person;

    public Problem(int id, String name, Person person) {
        this.id = id;
        this.name = name;
        this.person = person;
    }

    public Problem(String name, Person person) {
        this.name = name;
        this.person = person;
    }

    public int getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public Person getPerson() {

        return person;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", person=" + person +
                '}';
    }
}
