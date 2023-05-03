package com.example.database;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class QueryRequest extends StringRequest {
    private Map<String, String> args;
    private static Response.ErrorListener err = error -> Log.d("please","Error listener response: " + error.getMessage());

    public QueryRequest(HashMap<String, String> params, String url, Response.Listener<String> listener){
        super(Method.POST, url, listener, err);
        args = params;
    }

    @Override
    protected Map<String, String> getParams() {
        return args;
    }
}