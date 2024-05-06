package com.unclecole.bosspoints.database;

import com.unclecole.bosspoints.database.serializer.Persist;
import com.unclecole.bosspoints.database.serializer.Serializer;

import java.util.HashMap;
import java.util.UUID;

public class PointsData {

    public static transient PointsData instance = new PointsData();
    public static transient HashMap<UUID,Integer> pointsList = new HashMap<>();

    public static int points;

    public static void save(String uuid) {
        points = pointsList.get(UUID.fromString(uuid));
        new Persist().save(instance, "/playerdata/" + uuid);
    }

    public static void load(String uuid) {
        points = 0;
        new Serializer().load(instance, PointsData.class, "/playerdata/" + uuid);
        pointsList.put(UUID.fromString(uuid), points);
    }

}