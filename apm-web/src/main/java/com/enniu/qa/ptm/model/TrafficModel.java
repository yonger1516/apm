package com.enniu.qa.ptm.model;

import java.util.Map;

/**
 * Created by fuyong on 7/3/15.
 */
public class TrafficModel {
	private int id;
	private int type;

	private Map<Api,Double> transactions;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Map<Api, Double> getTransactions() {
		return transactions;
	}

	public void setTransactions(Map<Api, Double> transactions) {
		this.transactions = transactions;
	}
}
