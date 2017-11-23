package com.strategy.intecom.vtc.fixuser.utils.map;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

@SuppressWarnings("ResourceType")
public class GPSTracker implements LocationListener {
	private final Activity mContext;
	private boolean isGPSEnabled = Boolean.FALSE;
	private boolean isWifiEnabled = Boolean.FALSE;
	private boolean isConnection = Boolean.FALSE;
	private boolean canGetLocation = Boolean.FALSE;
	private Location location = null;
	private static double latitude;
	private static double longitude;

	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 60 * 1000; // 1 minute

	protected LocationManager locationManager;

	public GPSTracker(Activity context) {
		this.mContext = context;
		getLocation();
	}

	public boolean isConnection() {
		return isConnection;
	}

	private boolean checkEnableLocation() {

		try {
			locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			isWifiEnabled = Utils.isNetworkConnected(mContext);

			if (!isGPSEnabled || !isWifiEnabled) {
				isConnection = Boolean.FALSE;
				return false;
			} else {
				isConnection = Boolean.TRUE;
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private Location getLocation() {
		if(mContext != null) {
			mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (checkEnableLocation()) {
						initGetLocation();
					}
				}
			});
		}

		return location;
	}

	private void initGetLocation() {
		this.canGetLocation = true;
		if (isWifiEnabled) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER,
					MIN_TIME_BW_UPDATES,
					MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
			AppCore.showLog("Network", "Wifi Enable");
			if (locationManager != null) {
				location = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			}
		}

		if (isGPSEnabled) {
			if (location == null) {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER,
						MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				AppCore.showLog("GPS", "GPS Enabled");
				if (locationManager != null) {
					AppCore.showLog("GPS", "GPS locationManager");
					location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

				}
			}
		}

		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			AppCore.showLog("---------latitude-----------" + latitude);
			AppCore.showLog("---------longitude-----------" + longitude);
		}

	}

	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
	}

	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
			AppCore.showLog("---------latitude-----------" + latitude);
		}

		return latitude;
	}

	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
			AppCore.showLog("---------longitude-----------" + longitude);
		}

		return longitude;
	}

	public boolean canGetLocation() {
		return this.canGetLocation;
	}


	public static class Distance {
		private double latitude;
		private double longitude;

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

	}

	public void onLocationChanged(Location location) {
		AppCore.showLog("Update Location--------getLatitude : " + location.getLatitude() + " getLatitude--------- : " + location.getLongitude());

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		AppCore.showLog("onStatusChanged provider---- : " + provider + " ---status--- : " + status);

	}

	public void onProviderEnabled(String provider) {
		isConnection = Boolean.TRUE;
		AppCore.showLog("onProviderEnabled provider---- : " + provider);
	}

	public void onProviderDisabled(String provider) {
		isConnection = Boolean.FALSE;
		AppCore.showLog("onProviderDisabled provider---- : " + provider);

	}
}


