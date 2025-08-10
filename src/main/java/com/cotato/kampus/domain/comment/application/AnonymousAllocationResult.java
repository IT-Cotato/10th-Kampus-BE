package com.cotato.kampus.domain.comment.application;

public record AnonymousAllocationResult(
	Integer anonymousNumber,
	boolean needsIncrement
) {
	public static AnonymousAllocationResult of(Integer anonymousNumber, boolean needsIncrement) {
		return new AnonymousAllocationResult(anonymousNumber, needsIncrement);
	}
}
