package com.example.staff_t_helper.domain.mapper;

import com.example.staff_t_helper.domain.Problem;

import org.json.JSONException;
import org.json.JSONObject;

public class ProblemMapper {

    public static Problem ProblemFromJson(JSONObject jsonObject) {

        Problem problem = null;

        try {
            problem = new Problem(
                    jsonObject.getInt("id"),
                    jsonObject.getString("name"),
                    PersonMapper.personFromProblemJson(jsonObject)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return problem;
    }

}
