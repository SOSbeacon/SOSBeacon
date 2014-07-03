package util;

import entities.Location;

public class GeoArgs {
	boolean success;
	Location location;
	String message;
	public GeoArgs(Location location) {
		super();
		success=true;
		this.location = location;
	}
	public GeoArgs(String message) {
		super();
		success=false;
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
