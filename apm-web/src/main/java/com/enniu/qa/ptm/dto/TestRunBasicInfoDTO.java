package com.enniu.qa.ptm.dto;

import org.ngrinder.model.Status;

import java.util.Date;

/**
 * Created by Administrator on 2015/8/13 0013.
 */
public class TestRunBasicInfoDTO {
    private long id;
    private Status status;
    private Date startTime;
    private long duration;

    private double hps;
    private double avgRT;
    private double sdtDev;
    private double apDex;
    private double maxTPS;
    private double successRate;

    private long commitId;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getHps() {
        return hps;
    }

    public void setHps(double hps) {
        this.hps = hps;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public double getAvgRT() {
        return avgRT;
    }

    public void setAvgRT(double avgRT) {
        this.avgRT = avgRT;
    }

    public double getSdtDev() {
        return sdtDev;
    }

    public void setSdtDev(double sdtDev) {
        this.sdtDev = sdtDev;
    }

    public double getApDex() {
        return apDex;
    }

    public void setApDex(double apDex) {
        this.apDex = apDex;
    }

    public double getMaxTPS() {
        return maxTPS;
    }

    public void setMaxTPS(double maxTPS) {
        this.maxTPS = maxTPS;
    }

    public long getCommitId() {
        return commitId;
    }

    public void setCommitId(long commitId) {
        this.commitId = commitId;
    }
}
