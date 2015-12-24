package com.enniu.qa.ptm.dto;

import java.util.List;

/**
 * Created by fuyong on 7/15/15.
 */
public class ProjectOverallDTO {
	private ProjectDTO dto;
	private List<TestResultDTO> testResultDTOList;

	public ProjectDTO getDto() {
		return dto;
	}

	public void setDto(ProjectDTO dto) {
		this.dto = dto;
	}

	public List<TestResultDTO> getTestResultDTOList() {
		return testResultDTOList;
	}

	public void setTestResultDTOList(List<TestResultDTO> testResultDTOList) {
		this.testResultDTOList = testResultDTOList;
	}
}
