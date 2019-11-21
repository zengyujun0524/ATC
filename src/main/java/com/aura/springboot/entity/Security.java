package com.aura.springboot.entity;

import javax.persistence.Entity;

@Entity
public class Security {

	int userId; // 用户ID
	String problem; // 密保问题
	String answer; // 密保答案

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Security(int userId, String problem, String answer) {

		this.userId = userId;
		this.problem = problem;
		this.answer = answer;
	}

	public Security() {
		super();
	}

}
