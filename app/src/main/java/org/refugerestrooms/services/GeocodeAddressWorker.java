package org.refugerestrooms.services;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodeAddressWorker extends Worker {
    private static final String TAG = "GeocodeAddressWorker";

    public static final String LOCATION_NAME_DATA_EXTRA = "LOCATION_NAME_DATA_EXTRA";
    public static final String RESULT_LAT = "RESULT_LAT";
    public static final String RESULT_LNG = "RESULT_LNG";

    public GeocodeAddressWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String name = getInputData().getString(LOCATION_NAME_DATA_EXTRA);
        if (name == null) {
            return Result.failure();
        }

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(name, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                Data outputData = new Data.Builder()
                        .putDouble(RESULT_LAT, address.getLatitude())
                        .putDouble(RESULT_LNG, address.getLongitude())
                        .build();
                return Result.success(outputData);
            }
        } catch (IOException e) {
            Log.e(TAG, "Service not available", e);
        }
        return Result.failure();
    }
}
