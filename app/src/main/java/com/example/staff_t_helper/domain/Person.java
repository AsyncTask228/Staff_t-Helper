package com.example.staff_t_helper.domain;

public class Person {

    private int id;

    private String name;

    private String surname;

    private int room_number;

    private int bed_number;

    public Person(int id, String name, String surname, int room_number, int bed_number) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.room_number = room_number;
        this.bed_number = bed_number;
    }

    public Person(String name, String surname, int room_number, int bed_number) {
        this.name = name;
        this.surname = surname;
        this.room_number = room_number;
        this.bed_number = bed_number;
    }


    public int getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public String getSurname() {

        return surname;
    }

    public int getRoom_number() {
        return room_number;
    }

    public int getBed_number() {

        return bed_number;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", room_number=" + room_number +
                ", bed_number=" + bed_number +
                '}';
    }
}
