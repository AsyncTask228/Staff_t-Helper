package com.example.staff_t_helper.nodb;

import com.example.staff_t_helper.domain.Person;
import com.example.staff_t_helper.domain.Problem;

import java.util.ArrayList;
import java.util.List;

public class NoDb {
    private NoDb(){}

    public static final List<Problem> PROBLEM_LIST = new ArrayList<>();
    public static final List<Person> PERSON_LIST = new ArrayList<>();


}
