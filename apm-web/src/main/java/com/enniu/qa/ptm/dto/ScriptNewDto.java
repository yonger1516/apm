package com.enniu.qa.ptm.dto;

import com.enniu.qa.ptm.model.ScriptLanguage;

/**
 * Created by fuyong on 7/21/15.
 */
public class ScriptNewDto {

	private int id;
	private String scriptName;
	private ScriptLanguage scriptType;
	private String testUrl;
	private boolean hasLibAndResource;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public ScriptLanguage getScriptType() {
		return scriptType;
	}

	public void setScriptType(ScriptLanguage scriptType) {
		this.scriptType = scriptType;
	}

	public String getTestUrl() {
		return testUrl;
	}

	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}

	public boolean isHasLibAndResource() {
		return hasLibAndResource;
	}

	public void setHasLibAndResource(boolean hasLibAndResource) {
		this.hasLibAndResource = hasLibAndResource;
	}
}
