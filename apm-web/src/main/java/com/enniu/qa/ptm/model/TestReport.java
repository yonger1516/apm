package com.enniu.qa.ptm.model;


import org.ngrinder.model.BaseModel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by fuyong on 7/3/15.
 */

@Entity
@Table(name = "t_test_report")
public class TestReport extends BaseModel<TestReport> {

    @OneToOne
    @JoinColumn(name="api_run_id",referencedColumnName = "id")
    private ApiTestRun apiTestRun;

    @Column(name = "start_time")
    private Date startTime;

    private long duration;
    private long transactions;
    private long errors;
    private long success;

    private double apdex;

    @Column(name = "min_rt")
    private double minRT;

    @Column(name = "avg_rt")
    private double avgRT;

    @Column(name = "max_rt")
    private double maxRT;

    @Column(name = "sdt_dev")
    private double sdtDev;

    @Column(name = "avg_tps")
    private double avgTps;

    @Column(name = "max_tps")
    private double maxTps;

    @Column(name = "avg_threads")
    private int avgThreads;

    @Column(name = "max_threads")
    private int maxThreads;

    @Column(name = "avg_hps")
    private double avgHps;

    @Column(name = "max_hps")
    private double maxHps;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public ApiTestRun getApiTestRun() {
        return apiTestRun;
    }

    public void setApiTestRun(ApiTestRun apiTestRun) {
        this.apiTestRun = apiTestRun;
    }

    public double getAvgHps() {
        return avgHps;
    }

    public void setAvgHps(double avgHps) {
        this.avgHps = avgHps;
    }

    public double getMaxHps() {
        return maxHps;
    }

    public void setMaxHps(double maxHps) {
        this.maxHps = maxHps;
    }

    public TestReport() {

    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getTransactions() {
        return transactions;
    }

    public void setTransactions(long transactions) {
        this.transactions = transactions;
    }

    public double getApdex() {
        return apdex;
    }

    public void setApdex(double apdex) {
        this.apdex = apdex;
    }

    public double getMinRT() {
        return minRT;
    }

    public void setMinRT(int minRT) {
        this.minRT = minRT;
    }

    public double getAvgRT() {
        return avgRT;
    }

    public void setAvgRT(int avgRT) {
        this.avgRT = avgRT;
    }

    public double getMaxRT() {
        return maxRT;
    }

    public void setMaxRT(int maxRT) {
        this.maxRT = maxRT;
    }

    public double getSdtDev() {
        return sdtDev;
    }

    public void setSdtDev(double sdtDev) {
        this.sdtDev = sdtDev;
    }

    public void setMinRT(double minRT) {
        this.minRT = minRT;
    }

    public void setAvgRT(double avgRT) {
        this.avgRT = avgRT;
    }

    public void setMaxRT(double maxRT) {
        this.maxRT = maxRT;
    }

    public double getAvgTps() {
        return avgTps;
    }

    public void setAvgTps(double avgTps) {
        this.avgTps = avgTps;
    }

    public double getMaxTps() {
        return maxTps;
    }

    public void setMaxTps(double maxTps) {
        this.maxTps = maxTps;
    }

    public int getAvgThreads() {
        return avgThreads;
    }

    public void setAvgThreads(int avgThreads) {
        this.avgThreads = avgThreads;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }


    public long getErrors() {
        return errors;
    }

    public void setErrors(long errors) {
        this.errors = errors;
    }

    public long getSuccess() {
        return success;
    }

    public void setSuccess(long success) {
        this.success = success;
    }

    public double getErrorRate() {
        return (double) getErrors() / transactions;
    }

    public double getSuccessRate() {
        return (double) getSuccess() / transactions;
    }

    public double getFailedRate() {
        return (double) (1 - getSuccessRate());
    }
}
