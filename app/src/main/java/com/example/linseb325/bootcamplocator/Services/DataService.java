package com.example.linseb325.bootcamplocator.Services;

import com.example.linseb325.bootcamplocator.Model.BootcampLocation;

import java.util.ArrayList;

/**
 * Created by linseb325 on 2/9/18.
 */

public class DataService {
    private static final DataService instance = new DataService();

    public static DataService getInstance() {
        return instance;
    }

    private DataService() {

    }


    public ArrayList<BootcampLocation> getBootcampLocationsNear(int zipCode) {

        // Pretending we downloaded data from the server
        ArrayList<BootcampLocation> list = new ArrayList<>();
        list.add(new BootcampLocation(43.254467f, -87.915633f, "CUW", "12800 N. Lake Shore Drive", "url"));
        list.add(new BootcampLocation(43.250125f, -87.924786f, "Highland House", "123 Taco Ave.", "url"));
        list.add(new BootcampLocation(43.254585f, -87.922438f, "Missing Links Golf Course", "3 Links Drive", "url"));

        return list;
    }




}
