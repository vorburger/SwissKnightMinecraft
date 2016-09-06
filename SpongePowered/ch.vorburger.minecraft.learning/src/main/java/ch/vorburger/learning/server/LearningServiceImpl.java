package ch.vorburger.learning.server;

import ch.vorburger.learning.ServiceEvaluation;
import ch.vorburger.learning.LearningService;
import ch.vorburger.learning.LearningServiceException;
import ch.vorburger.learning.ServiceQuestion;
import ch.vorburger.learning.UserID;

public class LearningServiceImpl implements LearningService {

	@Override
	public ServiceQuestion newQuestion(UserID uid) {
		// TODO this needs to go into the AdditionLearnlet
		return new ServiceQuestion("1", "What is 1 + 1?");
	}

	@Override
	public ServiceEvaluation answerFreeTextQuestion(String questionID, String answer) throws LearningServiceException {
		// TODO this needs to go into the AdditionLearnlet and some support class, and map the *Service to the *Learnlet world
		if (Integer.decode(answer.trim()) == 2)
			return new ServiceEvaluation(true, 0);
		else
			return new ServiceEvaluation(false, 0);
	}

	@Override
	public ServiceEvaluation answerMultipleChoiceQuestion(String questionID, int choice) throws LearningServiceException {
		throw new LearningServiceException(questionID + " was a free text and not a multiple choice question");
	}

}
