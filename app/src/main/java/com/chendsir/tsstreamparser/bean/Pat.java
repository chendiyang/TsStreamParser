package com.chendsir.tsstreamparser.bean;

import java.util.ArrayList;
import java.util.List;

public class Pat {

	private int tableId = 0x00;

	private int sectionSyntaxIndicator = 0x1;

	private int zero = 0x0;

	private int reserverdOne;

	private int sectionLength;

	private int transportStreamId;

	private int reserverdTwo;

	private int versionNumber;

	private int currentNextIndicator;

	private int sectionNumber;

	private int lastSectionNumber;

	private int reserverdThree;

	private int networkPid;
	/**
	 *  PMT 表的信息用一个数组来表示
	 */
   private List<ProgramList>  patProgramList = new ArrayList<>();

   private int crc32;

   public Pat(byte[] data){
	   int tableId = data[0] & 0xFF;
	   int sectionSyntaxIndicator = (data[1] >> 7) & 0x1;
	   int zero = (data[1] >> 6) & 0x1;
	   int reservedOne = (data[1] >> 4) & 0x3;
	   int sectionLength = (((data[1] & 0xF) << 8) | (data[2] & 0xFF)) & 0xFFF;
	   int transportStreamId = ((data[3] & 0xFF) | (data[4] & 0xFF)) & 0xFFFF;
	   int reservedTwo = (data[5] >> 6) & 0x3;
	   int versionNumber = (data[5] >> 1) & 0x1F;
	   int currentNextIndicator = data[5] & 0x1;
	   int sectionNumber = data[6] & 0xFF;
	   int lastSectionNumber = data[7] & 0xFF;

	   this.tableId = tableId;
	   this.sectionSyntaxIndicator = sectionSyntaxIndicator;
	   this.zero = zero;
	   this.reserverdOne = reservedOne;
	   this.sectionLength = sectionLength;
	   this.transportStreamId = transportStreamId;
	   this.reserverdTwo = reservedTwo;
	   this.versionNumber = versionNumber;
	   this.currentNextIndicator = currentNextIndicator;
	   this.sectionNumber = sectionNumber;
	   this.lastSectionNumber = lastSectionNumber;
   };

	public int getTableId() {
		return tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public int getSectionSyntaxIndicator() {
		return sectionSyntaxIndicator;
	}

	public void setSectionSyntaxIndicator(int sectionSyntaxIndicator) {
		this.sectionSyntaxIndicator = sectionSyntaxIndicator;
	}

	public int getZero() {
		return zero;
	}

	public void setZero(int zero) {
		this.zero = zero;
	}

	public int getReserverdOne() {
		return reserverdOne;
	}

	public void setReserverdOne(int reserverdOne) {
		this.reserverdOne = reserverdOne;
	}

	public int getSectionLength() {
		return sectionLength;
	}

	public void setSectionLength(int sectionLength) {
		this.sectionLength = sectionLength;
	}

	public int getTransportStreamId() {
		return transportStreamId;
	}

	public void setTransportStreamId(int transportStreamId) {
		this.transportStreamId = transportStreamId;
	}

	public int getReserverdTwo() {
		return reserverdTwo;
	}

	public void setReserverdTwo(int reserverdTwo) {
		this.reserverdTwo = reserverdTwo;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	public int getCurrentNextIndicator() {
		return currentNextIndicator;
	}

	public void setCurrentNextIndicator(int currentNextIndicator) {
		this.currentNextIndicator = currentNextIndicator;
	}

	public int getSectionNumber() {
		return sectionNumber;
	}

	public void setSectionNumber(int sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

	public int getLastSectionNumber() {
		return lastSectionNumber;
	}

	public void setLastSectionNumber(int lastSectionNumber) {
		this.lastSectionNumber = lastSectionNumber;
	}

	public List<ProgramList> getPatProgramList() {
		return patProgramList;
	}

	public void setPatProgramList(List<ProgramList> patProgramList) {
		this.patProgramList = patProgramList;
	}

	public int getCrc32() {
		return crc32;
	}

	public void setCrc32(int crc32) {
		this.crc32 = crc32;
	}

	public int getReserverdThree() {
		return reserverdThree;
	}

	public void setReserverdThree(int reserverdThree) {
		this.reserverdThree = reserverdThree;
	}

	public int getNetworkPid() {
		return networkPid;
	}

	public void setNetworkPid(int networkPid) {
		this.networkPid = networkPid;
	}
}
