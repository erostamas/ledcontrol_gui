package com.erostamas.ledcontrol;

import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class RedisClient extends AsyncTask<Void, Integer, Map<String, String>> {

    private String _mapName;
    private String _redisIpAddress;
    private Map<String, String> _results;
    private RedisResponseUser _redisResponseUser;

    public RedisClient(String redisIpAddress, String mapName, Map<String, String> results, RedisResponseUser redisResponseUser) {
        _redisIpAddress = redisIpAddress;
        _mapName = mapName;
        _results = results;
        _redisResponseUser = redisResponseUser;
    }

    @Override
    protected Map<String, String> doInBackground(Void... params) {
        Map<String, String> ret = new HashMap<String, String>() {{}};
        try {
            Jedis jedis = new Jedis(_redisIpAddress, 6379);
            jedis.connect();

            if (jedis.exists(_mapName)) {

                ret = jedis.hgetAll(_mapName);

                Log.d("RedisClient", "MAP from redis: " + _results);
            }

        } catch (Exception e) {
            Log.e("RedisClient", "Exception during accessing redis: " + e.getMessage());
        }
        return ret;
    }

    @Override
    protected void onPostExecute(Map<String, String> values) {
        _results.clear();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            _results.put(entry.getKey(), entry.getValue());
        }
        _redisResponseUser.onRedisReplyReceived();
    }
}

