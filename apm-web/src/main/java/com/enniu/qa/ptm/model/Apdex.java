package com.enniu.qa.ptm.model;

/**
 * Created by fuyong on 7/6/15.
 */
public class Apdex {
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	private double score;

	public Apdex(double score){
		this.score=score;
	}
}
