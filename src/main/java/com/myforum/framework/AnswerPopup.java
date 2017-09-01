package com.myforum.framework;

import java.io.Serializable;

public class AnswerPopup implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String answer;

	public AnswerPopup( ){ }


	public AnswerPopup( String answer ){
		this.answer = answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnswer() {
		return answer;
	}
	
	

}
