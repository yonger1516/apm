package com.enniu.qa.ptm.model;

/**
 * Created by fuyong on 7/3/15.
 */
public enum  StatusCategory {
	/**
	 * Ready to run..
	 */
	PREPARE("blue.png", false, true, false),
	/**
	 * Processing.
	 */
	PROGRESSING("blue_anime.gif", true, false, false),
	/**
	 * Testing..
	 */
	TESTING("green_anime.gif", true, false, false),
	/**
	 * Finished normally.
	 */
	FINISHED("green.png", false, true, true),
	/**
	 * Stopped by error .
	 */
	ERROR("red.png", false, true, true),
	/**
	 * Stopped by user.
	 */
	STOP("grey.png", false, true, true);

	private final boolean stoppable;
	private final boolean deletable;
	private final boolean reportable;
	private final String iconName;

	StatusCategory(String iconName, boolean stoppable, boolean deletable, boolean reportable) {
		this.iconName = iconName;
		this.stoppable = stoppable;
		this.deletable = deletable;
		this.reportable = reportable;
	}

	public boolean isStoppable() {
		return stoppable;
	}

	public boolean isDeletable() {
		return deletable;
	}

	public String getIconName() {
		return iconName;
	}

	public boolean isReportable() {
		return reportable;
	}
}
