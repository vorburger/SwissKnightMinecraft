package ch.vorburger.learnlet.examples;

import java.util.Locale;

import org.eclipse.jdt.annotation.NonNull;

import ch.vorburger.learnlet.Evaluation;
import ch.vorburger.learnlet.Learnlet;
import ch.vorburger.learnlet.Question;

public class AdditionLearnlet implements Learnlet {

	@Override
	public Question newQuestion(int level) {
		// TODO helper to partition difficulties
		if (level < 3) {
			level = 3;
		} else { // max.
			level = 10;
		}
		
		// TODO random helper; http://stackoverflow.com/questions/363681/generating-random-integers-in-a-specific-range
		int a = 1;
		int b = 1;
		
		AdditionQuestion q = new AdditionQuestion();
		q.a = a;
		q.b = b;
		return q;
	}

	@Override
	public Evaluation answerFreeTextQuestion(Question question, String answer) {
		// TODO use some generics pattern to avoid both these kind of type casts
		AdditionQuestion question2 = (AdditionQuestion) question;
		int answer2 = Integer.decode(answer.trim());
		return new Evaluation(question2.a + question2.b == answer2);
	}
	
	static class AdditionQuestion implements Question {
		int a;
		int b;
		
		@Override
		public @NonNull String getText(Locale locale) {
			return a + " + " + b + " = ?";
		}
	}

}
