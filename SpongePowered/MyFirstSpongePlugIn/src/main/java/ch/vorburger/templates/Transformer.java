package ch.vorburger.templates;

public interface Transformer<S,T> {

	T transform(S s);
	
}
