package com.unclecole.bosspoints.database.serializer;

import com.unclecole.bosspoints.BossPoints;

public class Serializer {


    /**
     * Saves your class to a .json file.
     */
    public void save(Object instance) {
        BossPoints.getPersist().save(instance);
    }

    /**
     * Loads your class from a json file
     *
   */
    public <T> T load(T def, Class<T> clazz, String name) {
        return BossPoints.getPersist().loadOrSaveDefault(def, clazz, name);
    }



}
