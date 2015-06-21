package ch.vorburger.templates;

public interface Template { // TODO extends Transformer

	String generate(); // NO "source", any required 'input' beans are @Inject'ed or so
	
}
