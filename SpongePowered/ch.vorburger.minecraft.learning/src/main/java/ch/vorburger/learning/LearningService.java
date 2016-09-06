package ch.vorburger.learning;

public interface LearningService {

	ServiceQuestion newQuestion(UserID uid) throws LearningServiceException;
	
	ServiceEvaluation answerFreeTextQuestion(String questionID, String answer) throws LearningServiceException;

	ServiceEvaluation answerMultipleChoiceQuestion(String questionID, int choice) throws LearningServiceException;

}
