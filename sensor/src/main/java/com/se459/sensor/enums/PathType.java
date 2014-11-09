package com.se459.sensor.enums;

public enum PathType {
	UNKNOWN(0),
	OPEN(1),
	OBSTACLE(2),
	STAIRS(4);
	
	private int value;    

	private PathType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static PathType byValue(int value) {
		for (PathType type : PathType.values()) {
	        if (type.getValue() == value) {
	            return type;
	        }
	    }
	    return null;
	}
}
