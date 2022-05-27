package com.example.staff_t_helper.domain.mapper;

import com.example.staff_t_helper.domain.Person;

import org.json.JSONException;
import org.json.JSONObject;

public class PersonMapper {

    public static Person personFromJson(JSONObject jsonObject){

        Person person = null;

        try {
            person = new Person(jsonObject.getInt("id") ,
                    jsonObject.getString("name"),
                    jsonObject.getString("surname"),
                    jsonObject.getInt("room_number"),
                    jsonObject.getInt("bed_number"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return person;
    }

    public static Person personFromProblemJson(JSONObject jsonObject){

        Person person = null;

        try {
            person = new Person(
                    jsonObject.getJSONObject("personDto").getInt("id"),
                    jsonObject.getJSONObject("personDto").getString("name"),
                    jsonObject.getJSONObject("personDto").getString("surname"),
                    jsonObject.getJSONObject("personDto").getInt("room_number"),
                    jsonObject.getJSONObject("personDto").getInt("bed_number")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return person;
    }

}
