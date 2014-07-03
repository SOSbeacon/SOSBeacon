package util;


import java.util.Vector;

import javax.microedition.location.Criteria;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.QualifiedCoordinates;

import entities.Location;

public class Geo {
	Vector v = new Vector();
	public void addGetLocationCompleted(IGetLocationCompleted li){
		if(v.contains(li))
			return;
		v.addElement(li);
	}

	void fireGetLocationCompleted(GeoArgs arg){
		int length = v.size();
		for(int i=0;i<length;i++){
			((IGetLocationCompleted)v.elementAt(i)).onGetLocationCompleted(arg);
		}
	}
	public void getCurrentLocation(){
		Thread tr = new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				fireGetLocationCompleted(new GeoArgs(new Location("105.78212","21.03131")));
//				Criteria c = new Criteria();
//				c.setVerticalAccuracy(50);
//				c.setHorizontalAccuracy(50);
//				c.setCostAllowed(false);
//				c.setPreferredPowerConsumption(Criteria.POWER_USAGE_HIGH);
//				try{
//					LocationProvider locationProvider = LocationProvider.getInstance(c);
//					try{
//						javax.microedition.location.Location loc = locationProvider.getLocation(60);
//						QualifiedCoordinates qua = loc.getQualifiedCoordinates();
//						String latitude=String.valueOf(qua.getLatitude());
//						String longitude=String.valueOf(qua.getLongitude());
//						System.out.println("GEO.lat: "+latitude);
//						System.out.println("GEO.long: "+longitude);
//						Location result=new Location(longitude,latitude);
//						fireGetLocationCompleted(new GeoArgs(result));
//					}catch(Exception e){
//						fireGetLocationCompleted(new GeoArgs(e.getMessage()));
//					}
//				}catch(Exception e){
//					fireGetLocationCompleted(new GeoArgs(e.getMessage()));
//				}
			}
		});
		tr.start();
	}
}
