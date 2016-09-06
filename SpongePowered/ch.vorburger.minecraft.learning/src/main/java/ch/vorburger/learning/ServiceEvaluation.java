package ch.vorburger.learning;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;

public class ServiceEvaluation {
	// TODO https://immutables.github.io

	private final boolean correct;
	private final int totalScore;
	private final Optional<String> comment;
	private final Optional<String> url;
	
	public ServiceEvaluation(boolean correct, int totalScore) {
		this.correct = correct;
		this.totalScore = totalScore;
		this.comment = Optional.empty();
		this.url = Optional.empty();
	}

	public ServiceEvaluation(boolean correct, int totalScore, String comment) {
		this.correct = correct;
		this.totalScore = totalScore;
		this.comment = Optional.of(comment);
		this.url = Optional.empty();
	}

	public ServiceEvaluation(boolean correct, int totalScore, String comment, String url) {
		this.correct = correct;
		this.totalScore = totalScore;
		this.comment = Optional.of(comment);
		this.url = Optional.of(url);
	}

	public boolean isCorrect() {
		return correct;
	}
	
	public int getTotalScore() {
		return totalScore;
	}
	
	public Optional<String> getComment() {
		return comment;
	}
	
	public Optional<String> getURL() {
		return url;
	}
}
