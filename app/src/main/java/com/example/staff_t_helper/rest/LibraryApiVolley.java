package com.example.staff_t_helper.rest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.staff_t_helper.MainActivity;
import com.example.staff_t_helper.domain.Problem;
import com.example.staff_t_helper.domain.mapper.ProblemMapper;
import com.example.staff_t_helper.nodb.NoDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LibraryApiVolley implements LibraryApi {

    public static final String API_TEST = "API_TEST";
    private final Context context;
    public static final String BASE_URL = "http://192.168.1.102:8081";
    private Response.ErrorListener errorListener;

    public LibraryApiVolley(Context context) {
        this.context = context;

        errorListener  = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(API_TEST, error.toString());
            }
        };
    }


    @Override
    public void fillProblem() {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = BASE_URL + "/problem";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        NoDb.PROBLEM_LIST.clear();

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                Problem problem = ProblemMapper.ProblemFromJson(jsonObject);
                                NoDb.PROBLEM_LIST.add(problem);
                            }
                            try {
                                ((MainActivity)context).updateAdapter();
                            }catch (Exception e){
                                Log.e("fillProblem", e.getMessage());
                            }

                            Log.d(API_TEST, NoDb.PROBLEM_LIST.toString());

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                 errorListener
        );

        requestQueue.add(arrayRequest);
    }

    @Override
    public void fillPerson() {

    }

    @Override
    public void deleteProblem(int id) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = BASE_URL + "/problem" + "/" + id;

        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fillProblem();
                        Log.d(API_TEST, response);
                    }
                },
                errorListener
        );

        requestQueue.add(request);
    }
}
