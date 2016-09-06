package ch.vorburger.learning;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import ch.vorburger.utils.EvenMoreObjects;

public class ServiceQuestion {
	// TODO https://immutables.github.io

	private final String id;
	private final String text;
	private final List<String> choices;

	/**
	 * Free text question.
	 */
	public ServiceQuestion(String id, String text) {
		super();
		this.id = id;
		this.text = text;
		this.choices = Collections.emptyList();
	}
	
	/**
	 * Multiple Choice question.
	 */
	public ServiceQuestion(String id, String text, List<String> choices) {
		super();
		this.id = id;
		this.text = text;
		this.choices = Collections.unmodifiableList(choices);
		
		if (choices.isEmpty())
			throw new IllegalArgumentException("choices.isEmpty(), so it's a free-text not multiple choice question");
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public List<String> getChoices() {
		return choices;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, text, choices);
	}
	
	@Override
	public boolean equals(@Nullable Object other) {
		return EvenMoreObjects.equalsHelper(this, other,
				(one, another) -> Objects.equals(one.id, another.id)
				               && Objects.equals(one.text, another.text)
				               && Objects.equals(one.choices, another.choices));
	}

}
