package com.chendsir.tsstreamparser.bean;

public class Describe {
	public int getDescriptorTag() {
		return descriptorTag;
	}

	public void setDescriptorTag(int descriptorTag) {
		this.descriptorTag = descriptorTag;
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public int getServiceProviderNameLength() {
		return serviceProviderNameLength;
	}

	public void setServiceProviderNameLength(int serviceProviderNameLength) {
		this.serviceProviderNameLength = serviceProviderNameLength;
	}

	public String getServiceProviderName() {
		return serviceProviderName;
	}

	public void setServiceProviderName(String serviceProviderName) {
		this.serviceProviderName = serviceProviderName;
	}

	public int getServiceNameLength() {
		return serviceNameLength;
	}

	public void setServiceNameLength(int serviceNameLength) {
		this.serviceNameLength = serviceNameLength;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 *   service_descriptor: 0x48
	 */

	private int descriptorTag;

	/**
	 *  serviceType: 8bit
	 */
	private int serviceType;

	/**
	 * service_provider_name_length : 8 bit (1 byte)
	 */
	private int serviceProviderNameLength;

	/**
	 * service_provider_name :
	 */
	private String serviceProviderName;

	/**
	 * service_name_length : 8 bit (1 byte)
	 */
	private int serviceNameLength;

	/**
	 * service_name :
	 */
	private String serviceName;
}
