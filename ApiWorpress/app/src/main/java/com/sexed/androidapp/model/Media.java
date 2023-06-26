package com.sexed.androidapp.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Media {

    @SerializedName("guid")

    private JsonObject guid;

    public JsonObject getGuid() {
        return guid;
    }

    public void setGuid(JsonObject guid) {
        this.guid = guid;
    }
}
