package com.example.tomho.specforu.internaldatahandler;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONFileHandler {

    public JSONFileHandler() {

    }

    public JSONObject parserJSON(String jsonString){
        try {
            JSONObject reader = new JSONObject(jsonString);
            return reader;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
