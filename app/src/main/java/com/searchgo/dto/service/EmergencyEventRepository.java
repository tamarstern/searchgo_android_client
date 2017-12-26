package com.searchgo.dto.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import static com.searchgo.constants.ServiceConstants.LIST_ITEMS_ELEM;


/**
 * Created by tamar.twena on 12/4/2017.
 */

public class EmergencyEventRepository extends ModelRepository<EmergencyEventServiceDto> {
    public EmergencyEventRepository() {
        super("EmergencyEvents", "EmergencyEvents", EmergencyEventServiceDto.class);
    }

    public RestContract createContract() {
        RestContract contract = super.createContract();

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/findWithFilter", "GET"),
                getClassName() + ".greet");

        return contract;
    }

    public void findEventsUserCreated(final ListCallback<EmergencyEventServiceDto> callback) {
        findWithFilter("{\"where\" : {\"name\" : { \"like\" : \"name\"}} }",callback);
    }

    public void getNearByEvents(LatLng from, int radius, final ListCallback<EmergencyEventServiceDto> callback) {
        findWithFilter("{\"where\" : {" +
                "\"startingPoint\" : {" +
                "\"near\" : {" +
                "\"lat\" : " + from.latitude + "," +
                "\"lng\" : " + from.longitude +
                "}," +
                "\"maxDistance\" : " + radius + "," +
                "\"unit\" : \"kilometers\"" +
                "}" +
                "}" +
                "}", callback);
    }
    /*
    * {
	"where": {
		"startingPoint": {
			"near": {
				"lat": 32.0852999,
				"lng": 34.7817676
			},
			"maxDistance": 20,
			"unit": "kilometers"
		}
	}
}
    *
    * */

    public void findWithFilter(String filter, final ListCallback<EmergencyEventServiceDto> callback) {
        invokeStaticMethod("greet", ImmutableMap.of("filter", filter), new Adapter.Callback() {

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }

            @Override
            public void onSuccess(String response) {

                Iterator<JsonElement> iterator = getJsonElementIterator(response);
                ArrayList<EmergencyEventServiceDto> listToReturn = new ArrayList<EmergencyEventServiceDto>();
                while(iterator.hasNext()) {
                    JsonObject nextElem = iterator.next().getAsJsonObject();
                    EmergencyEventServiceDto dto = createServiceDto(nextElem);
                    listToReturn.add(dto);
                }
                callback.onSuccess(listToReturn);
            }

            @NonNull
            private Iterator<JsonElement> getJsonElementIterator(String response) {
                JsonParser parser = new JsonParser();
                JsonElement parsedElem = parser.parse(response);
                JsonObject resultAsJsonObject = parsedElem.getAsJsonObject();
                JsonElement listElem = resultAsJsonObject.get(LIST_ITEMS_ELEM);
                JsonArray resultJsonArray = listElem.getAsJsonArray();
                return resultJsonArray.iterator();
            }
        });
    }

    @NonNull
    private EmergencyEventServiceDto createServiceDto(JsonObject nextElem) {
        EmergencyEventServiceDto dto = new EmergencyEventServiceDto();
        dto.setId(nextElem.get("id").getAsString());
        JsonElement address = nextElem.get("address");
        if(!(address instanceof JsonNull)) {
            dto.setAddress(address.getAsString());
        }
        dto.setName(nextElem.get("name").getAsString());
        dto.setCategory(nextElem.get("category").getAsString());
        JsonElement description = nextElem.get("description");
        if(!(description instanceof JsonNull)) {
            dto.setDescription(description.getAsString());
        }
        String createdOn = nextElem.get("createdOn").getAsString();
        Date createdOnDate = convertStringToDate(createdOn);
        dto.setCreatedOn(createdOnDate);
        String lastSeen = nextElem.get("lastSeen").getAsString();
        Date lastSeenDate = convertStringToDate(lastSeen);
        dto.setLastSeen(lastSeenDate);
        JsonElement startingPoint = nextElem.get("startingPoint");
        if(startingPoint != null && !(startingPoint instanceof JsonNull)) {
            if (startingPoint.isJsonObject()) {
                HashMap<String, Double> loc = new HashMap<>();
                JsonObject pointObject = startingPoint.getAsJsonObject();
                double lat = pointObject.get("lat").getAsDouble();
                double lng = pointObject.get("lng").getAsDouble();
                loc.put("lat", lat);
                loc.put("lng", lng);
                dto.setStartingPoint(loc);
            }
        }
        return dto;
    }

    private Date convertStringToDate(String dateAsString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        try {
            Date parsed = sdf.parse(dateAsString);
            return parsed;
        } catch (ParseException e) {
           Log.i("failConvert", "fail to convert date" , e);
        }
        return null;
    }
}