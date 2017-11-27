package com.searchgo.application;

import android.app.Application;

import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.remoting.adapters.RestContractItem;

import static com.searchgo.constants.ServiceConstants.SERVER_URL;

/**
 * Created by tamar.twena on 11/27/2017.
 */

public class SearchgoApplication extends Application{

    RestAdapter adapter;

    public RestAdapter getLoopBackAdapter() {
        if (adapter == null) {

            adapter = new RestAdapter(
                    getApplicationContext(), SERVER_URL);

     //       adapter.getContract().addItem(
     //               new RestContractItem("locations/nearby", "GET"),
      //              "location.nearby");
        }
        return adapter;
    }
}
