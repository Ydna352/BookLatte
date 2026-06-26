package com.andrea.booklatte;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    private HashMap<String,String> parseJsonObject(JSONObject object){
        HashMap<String,String> dataList = new HashMap<>();
        try{
            //obtener el nombre del objeto
            String name = object.getString("name");
            //obtener la latitud del objeto
            String latitude = object.getJSONObject("geometry").
                    getJSONObject("location").getString("lat");
            //obtener la longitud del objeto
            String longitude = object.getJSONObject("geometry").
                    getJSONObject("location").getString("lng");
            dataList.put("name",name);
            dataList.put("lat",latitude);
            dataList.put("lng",longitude);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return dataList;
    }

    private List<HashMap<String,String>> parseJsonArray(JSONArray jsonArray)
    {
        List<HashMap<String,String>> dataList = new ArrayList<>();
        for(int i=0; i<jsonArray.length();i++){
            try {
                HashMap<String,String> data = parseJsonObject((JSONObject)jsonArray.get(i));
                dataList.add(data);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return dataList;
    }

    public List<HashMap<String,String>> parseResult(JSONObject object){
        //inicializar json array
        JSONArray jsonArray = null;
        try {
            jsonArray = object.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return parseJsonArray(jsonArray);
    }
}
