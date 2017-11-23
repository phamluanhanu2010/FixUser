package com.strategy.intecom.vtc.fixuser.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.strategy.intecom.vtc.fixuser.model.VtcModelAddress;
import com.strategy.intecom.vtc.fixuser.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import java.util.List;


/**
 * Created by Mr. Ha on 25/05/2016.
 */

public class VtcDBConnect extends VtcDBBase {

	private Context context;

	public VtcDBConnect(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * Storage All Address
	 */
	public List<VtcModelAddress> getAddressFromDB() {
		/**
		 * Get VtcModelAddress
		 *
		 * if exist return VtcModelAddress list, else return empty
		 *
		 * */
		String QUERRY_GET_ADDRESS_LIST = "SELECT * FROM " + VtcDBBase.TABLE_NAME_HISTORY_SEARCH_ADDRESS;

		return VtcModelAddress.setDataListAddress(OpenDB().rawQuery(QUERRY_GET_ADDRESS_LIST, new String[]{}));
	}

	public void checkAddressExists(VtcModelAddress modelAddress){

		if(modelAddress.isSave()) {
			String QUERRY_CHECK_UPDATE_INSERT =
					"INSERT or REPLACE INTO " + VtcDBBase.TABLE_NAME_HISTORY_SEARCH_ADDRESS +
							" (" + VtcDBBase.Search_distance + ", " + VtcDBBase.Search_address + ", " + VtcDBBase.Search_description + ", " + VtcDBBase.Search_longitude + ", " + VtcDBBase.Search_latitude + ", " + VtcDBBase.Search_isSave + ")" +
							" VALUES " +
							" (?,?,?,?,?,?);";

			Cursor cursor = OpenDB().rawQuery(QUERRY_CHECK_UPDATE_INSERT, new String[]{String.valueOf(modelAddress.getDistance()), modelAddress.getAddress(), modelAddress.getDescription(), String.valueOf(modelAddress.getLongitude()), String.valueOf(modelAddress.getLatitude()), String.valueOf(1)});

			if(cursor != null){
				if (cursor.moveToFirst()) {
					String temp_address = cursor.getString(cursor.getColumnIndex(VtcDBBase.Search_address));
					AppCore.showLog("-----: " + temp_address);
				}
				cursor.close();
			}

		}else {
			deleteAddressFromDB(modelAddress.getAddress());
		}
	}

	public List<VtcModelAddress> searchAddressFromDB(String address){

		/**
		 * Get VtcModelAddress
		 *
		 * if exist return VtcModelAddress list, else return empty
		 *
		 * */
		String QUERRY_GET_ADDRESS_LIST = "SELECT * FROM " + VtcDBBase.TABLE_NAME_HISTORY_SEARCH_ADDRESS + " WHERE " + VtcDBBase.Search_address + " LIKE '%" + address + "%'";

		return VtcModelAddress.setDataListAddress(OpenDB().rawQuery(QUERRY_GET_ADDRESS_LIST, new String[]{}));
	}

	public boolean setAddressIntoDB(VtcModelAddress modelAddress) {

		ContentValues insertAddress = new ContentValues();
		insertAddress.put(VtcDBBase.Search_distance, modelAddress.getDistance());
		insertAddress.put(VtcDBBase.Search_address, modelAddress.getAddress());
		insertAddress.put(VtcDBBase.Search_description, modelAddress.getDescription());
		insertAddress.put(VtcDBBase.Search_longitude, modelAddress.getLongitude());
		insertAddress.put(VtcDBBase.Search_latitude, modelAddress.getLatitude());

		if(modelAddress.isSave()){
			insertAddress.put(VtcDBBase.Search_isSave, 1);
		}else {
			insertAddress.put(VtcDBBase.Search_isSave, 0);
		}

		/**
		 * Insert VtcModelAddress
		 *
		 * return true == success, return false == not success
		 *
		 * */

		boolean isOk = false;

		if (OpenDB().insert(VtcDBBase.TABLE_NAME_HISTORY_SEARCH_ADDRESS, null, insertAddress) > 0)
			isOk = true;
		else isOk = false;

		return isOk;
	}

	public boolean updateAddressIntoDB(VtcModelAddress modelAddress) {

		ContentValues updatetAddress = new ContentValues();
		updatetAddress.put(VtcDBBase.Search_distance, modelAddress.getDistance());
		updatetAddress.put(VtcDBBase.Search_address, modelAddress.getAddress());
		updatetAddress.put(VtcDBBase.Search_description, modelAddress.getDescription());
		updatetAddress.put(VtcDBBase.Search_longitude, modelAddress.getLongitude());
		updatetAddress.put(VtcDBBase.Search_latitude, modelAddress.getLatitude());

		if(modelAddress.isSave()){
			updatetAddress.put(VtcDBBase.Search_isSave, 1);
		}else {
			updatetAddress.put(VtcDBBase.Search_isSave, 0);
		}

		/**
		 * Update VtcModelAddress by id VtcModelAddress
		 *
		 * return true == success, return false == not success
		 *
		 * */
		boolean isOk = false;

		if (OpenDB().update(VtcDBBase.TABLE_NAME_HISTORY_SEARCH_ADDRESS, updatetAddress, VtcDBBase.id + " = ?", new String[]{String.valueOf(modelAddress.getId())}) > 0)
			isOk = true;
		else isOk = false;

		return isOk;
	}

	public boolean deleteAddressFromDB(String strAddress) {

		/**
		 * Delete VtcModelAddress by address VtcModelAddress
		 *
		 * return true == success, return false == not success
		 *
		 * */
		boolean isOk;

		if (OpenDB().delete(VtcDBBase.TABLE_NAME_HISTORY_SEARCH_ADDRESS, VtcDBBase.Search_address + " = ?", new String[]{strAddress}) > 0)
			isOk = true;
		else isOk = false;

		return isOk;
	}
}
