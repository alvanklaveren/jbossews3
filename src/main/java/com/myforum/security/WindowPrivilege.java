package com.myforum.security;

/*
 * Contains the privileges for a given webpage/panel
 * This class is package private, i.e. it can and should not be referred to in other packages
 */
class WindowPrivilege {

	@SuppressWarnings("rawtypes")
	private Class windowId;
	private boolean canRead = false;
	private boolean canWrite = false;
	private boolean canOpen = false;
	
	@SuppressWarnings("rawtypes")
	public WindowPrivilege(Class windowId, boolean canRead, boolean canWrite, boolean canOpen){
		this.windowId 		= windowId;
		this.canRead 		= canRead;
		this.canWrite		= canWrite;
		this.canOpen		= canOpen;
	}
	
	@SuppressWarnings("rawtypes")
	public void setWindowId(Class windowId) {
		this.windowId = windowId;
	}
	public void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}
	public void canWrite(boolean canWrite) {
		this.canWrite = canWrite;
	}
	public void setCanOpen(boolean canOpen) {
		this.canOpen = canOpen;
	}

	@SuppressWarnings("rawtypes")
	public Class getWindowId() {
		return windowId;
	}
	public boolean CanRead() {
		return canRead;
	}
	public boolean canWrite() {
		return canWrite;
	}
	public boolean canOpen() {
		return canOpen;
	}
}
