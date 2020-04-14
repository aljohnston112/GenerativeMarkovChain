package markov;

import java.util.Map;

/**
 * @author Alexander Johnston
 *         Copyright 2020
 *         An abstract class that nodes of a multi layered markov chain are made out of.
 * @param <T> The type of the elements that the markov chains will contain.
 */
public abstract class MarkovNode<T> extends Node<T> {

	// The ProbFun that determines the first element
	public abstract Node<T> getFirstLink();
	
	@Override
	public abstract Map<T, Node<T>> getChain();
	
}
