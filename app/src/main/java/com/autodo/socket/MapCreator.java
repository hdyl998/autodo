package com.autodo.socket;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于创建JSON的工具
 * Created by Administrator on 2018/1/22.
 */

public class MapCreator {


    private Map<String, Object> hashMap = new HashMap<>();

    public static MapCreator create() {
        return new MapCreator();
    }

    public MapCreator add(String key, String object) {
        hashMap.put(key, object);
        return this;
    }

    public MapCreator add(String key, Object object) {
        hashMap.put(key, String.valueOf(object));
        return this;
    }

    public MapCreator add(String key, MapCreator creator) {
        hashMap.put(key, creator.toJSON());
        return this;
    }

    public MapCreator add(String key, int object) {
        hashMap.put(key, String.valueOf(object));
        return this;
    }

    @Override
    public String toString() {
        return toJSONString();
    }


    public String toJSONString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        return new JSONObject(hashMap);
    }
}
