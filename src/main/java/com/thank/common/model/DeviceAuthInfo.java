package com.thank.common.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

@Entity("deviceAuthInfo")
public class DeviceAuthInfo implements Serializable {
	private static final long serialVersionUID = 8632370359773997222L;
	private @Id @Indexed String deviceId;
	private @Indexed String emailAddress;
	public DeviceAuthInfo() {
		
	}
	public DeviceAuthInfo(String deviceId,String emailAddress) {
		this.deviceId=deviceId;
		this.emailAddress=emailAddress;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deviceId == null) ? 0 : deviceId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceAuthInfo other = (DeviceAuthInfo) obj;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "DeviceAuthInfo [deviceId=" + deviceId + ", emailAddress="
				+ emailAddress + "]";
	}
	
	
}
