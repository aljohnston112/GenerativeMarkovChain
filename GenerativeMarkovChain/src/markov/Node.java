package markov;

import java.util.Map;

/**
 * @author Alexander Johnston 
 *         Copyright 2020
 *         An abstract class for Nodes that make up NestedMarkovNodes and NLayerMarkovChains.
 * @param <T> as the type of elements that will be contained in the Node.
 */
public abstract class Node<T> {
	
	// The id for hashCode()
	int id = 0;
	
	// The map from the output values of a ProbFun to probabilities or Nodes
	Map<T, ?> chain;
	
	/**
	 * @return the chain that makes up the Map from the output values of a ProbFun to probabilities or Nodes.
	 */
	abstract Map<T, ?> getChain();
	
	/**
	 * @return the next generated element. 
	 */
	public abstract T fun();
	
	@Override
	public abstract Node<T> clone();
	
	@Override
	public abstract String toString();
	
	@Override
	public abstract int hashCode();

}