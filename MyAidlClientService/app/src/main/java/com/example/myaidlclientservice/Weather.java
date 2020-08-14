package com.example.myaidlclientservice;

import android.os.Parcel;
import android.os.Parcelable;

public class Weather implements Parcelable {
	private String type;
	private int temperature;

	public static final Creator<Weather> CREATOR = new Creator<Weather>() {
		@Override
		public Weather createFromParcel(Parcel in) {
			return new Weather(in);
		}

		@Override
		public Weather[] newArray(int size) {
			return new Weather[size];
		}
	};

	public Weather() {
		type = "null";
		temperature = -255;
	}

	protected Weather(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(type);
		out.writeInt(temperature);
	}

	public void readFromParcel(Parcel in) {
		type = in.readString();
		temperature = in.readInt();
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
}
