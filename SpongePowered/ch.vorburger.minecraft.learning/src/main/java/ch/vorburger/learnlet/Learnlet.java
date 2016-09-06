package ch.vorburger.learnlet;

public interface Learnlet {

	Question newQuestion(int level /* TODO , Locale locale */);
	
	Evaluation answerFreeTextQuestion(Question question, String answer);

}
