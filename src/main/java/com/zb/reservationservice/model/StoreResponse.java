package com.zb.reservationservice.model;


import com.zb.reservationservice.entity.Store;
import lombok.Getter;

@Getter
public class StoreResponse {

    private long id;
    private String name;
    private String description;

    private double lat;
    private double lnt;

    public static StoreResponse of(Store store) {
        StoreResponse storeResponse = new StoreResponse();

        storeResponse.id = store.getId();
        storeResponse.name = store.getName();
        storeResponse.description = storeResponse.getDescription();
        storeResponse.lat = storeResponse.getLat();
        storeResponse.lnt = storeResponse.getLnt();

        return storeResponse;
    }
}
