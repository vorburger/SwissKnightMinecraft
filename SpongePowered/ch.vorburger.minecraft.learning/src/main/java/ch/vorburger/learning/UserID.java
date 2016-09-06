package ch.vorburger.learning;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import ch.vorburger.utils.EvenMoreObjects;

public class UserID {
	// TODO https://immutables.github.io
	
	private final String scheme;
	private final String uuid;
	// TODO private final Locale locale;
	
	public UserID(String scheme, String uuid) {
		super();
		this.scheme = scheme;
		this.uuid = uuid;
	}

	public String getScheme() {
		return scheme;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(scheme, uuid);
	}
	
	@Override
	public boolean equals(@Nullable Object other) {
		return EvenMoreObjects.equalsHelper(this, other,
				(one, another) -> Objects.equals(one.scheme, another.scheme)
				               && Objects.equals(one.uuid, another.uuid));
	}
	
	@Override
	public String toString() {
		return new StringBuilder(scheme).append("::").append(uuid).toString();
	}
	
}
