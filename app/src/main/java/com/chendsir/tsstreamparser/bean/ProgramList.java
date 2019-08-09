package com.chendsir.tsstreamparser.bean;

public class ProgramList {

	private int programNumber;

	private int programMapPid;

	public ProgramList(int programNumber, int programMapPid) {
		this.programNumber = programNumber;
		this.programMapPid = programMapPid;
	}

	public int getProgramNumber() {
		return programNumber;
	}

	public void setProgramNumber(int programNumber) {
		this.programNumber = programNumber;
	}

	public int getProgramMapPid() {
		return programMapPid;
	}

	public void setProgramMapPid(int programMapPid) {
		this.programMapPid = programMapPid;
	}
}
