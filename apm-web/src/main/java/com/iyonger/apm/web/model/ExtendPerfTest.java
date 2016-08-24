package com.iyonger.apm.web.model;

import org.ngrinder.model.PerfTest;

/**
 * Created by Administrator on 2015/8/11 0011.
 */
public class ExtendPerfTest extends PerfTest {
    private int projectId;
    private String source;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
