package com.se459.sensor.enums;

public enum SurfaceType {
	BAREFLOOR(1),
	LOWPILE(2),
	HIGHPILE(4);
	
	private int value;    

	private SurfaceType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static SurfaceType byValue(int value) {
		for (SurfaceType type : SurfaceType.values()) {
	        if (type.getValue() == value)
	            return type;
	    }
	    return null;
	}
}
