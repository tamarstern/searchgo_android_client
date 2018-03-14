package com.searchgo.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.searchgo.dto.service.EmergencyEventServiceDto;
import com.searchgo.nativeServices.SaveNewEventService;
import com.searchgo.utils.ActivityUtils;

public class SaveNewEventTask extends AsyncTask<Object, Void, JsonElement> {

	private EmergencyEventServiceDto dto;
	private Activity activity;


	public SaveNewEventTask(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected JsonElement doInBackground(Object... params) {

		try {
			dto = (EmergencyEventServiceDto) params[0];
			SaveNewEventService service = new SaveNewEventService(dto,activity);
			return service.service();
		} catch (Exception e) {
			ActivityUtils.HandleConnectionUnsuccessfullToServer(e);
			return null;
		}
	}

	@Override
	protected void onPostExecute(JsonElement result) {
		if (result == null) {
			return;
		}
		JsonObject resultJsonObject = result.getAsJsonObject();

	}



}
