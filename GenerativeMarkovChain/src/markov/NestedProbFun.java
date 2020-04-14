package markov;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

/**
 * @author Alexander Johnston 
 *         Copyright 2020
 *         A class for creating NestedProbFuns for NestedMarkovNodes, NLayerMarkovChains, or stand-alone 2-layer markov chains
 *         and adjusting the probabilities based on feedback of the past.
 * @param <T> as the type of elements that will be contained in the NestedProbFun.
 */
public class NestedProbFun<T> extends MarkovNode<T> {

	// Contains the first ProbFun in this NestedProbFun
	private ProbFun<T> firstLink;

	// Maps the elements the firstLink can return to ProbFuns that contains every element in the firstLink
	private Map<T, Node<T>> chain2 = new HashMap<T, Node<T>>();

	// The previous element that will be used to select the ProbFun that will return next element
	private T previousSourceElement;

	// The previous element that was returned by the ProbFun that is mapped from previousSourceElement 
	private T previousDestinationElement;

	/**        Creates a NestedProbFun where the values returned by probFun when fun() is called are mapped to new ProbFuns.
	 *         Changes to elements in probFun will be reflected in this NestedProbFun.
	 * @param  probFun as the ProbFun whose output values are mapped to copies of ProbFun.
	 *         (Ex: A ProbFun with output choices [0,1] will create a map [[0]->[0,1], [1]->[0,1]]).
	 * @throws NullPointerException if probFun is null.
	 */
	public NestedProbFun(ProbFun<T> probFun) throws IllegalArgumentException {
		Objects.requireNonNull(probFun);
		// Invariants secured
		firstLink = probFun.clone();
		for(T t : probFun.getChain().keySet()) {
			chain2.put(t, probFun.clone());
		}
		chain = chain2;
	}

	@Override
	public ProbFun<T> getFirstLink() {
		return firstLink;
	}

	@Override
	public Map<T, Node<T>> getChain() {
		return chain2;
	}

	/**        Adjust the probabilities to make sourceElement more likely to return destinationElement when fun() is called.
	 * @param  sourceElement as the sourceElement in this NestedProbFun which may be null to adjust the probability of the ProbFun that generates the first element.
	 * @param  destinationElement as the destinationElement in this NestedProbFun.
	 * @param  percent as the percentage between 0 and 1 exclusive,
	 *         of the probability of sourceElement returning destinationElement to add to the probability. 
	 * @throws NullPointerException if destinationElement is null.
	 * @throws IllegalArgumentException if the percent isn't between 0 and 1 exclusive.
	 */
	public synchronized void good(T sourceElement, T destinationElement, double percent) {
		Objects.requireNonNull(destinationElement);
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to good() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		if(sourceElement == null) {
			firstLink.good(destinationElement, percent);
		} else {
			Node<T> n = chain2.get(sourceElement);
			firstLink.good(sourceElement, percent);
			if(n instanceof ProbFun<?>) {
				((ProbFun<T>) n).good(destinationElement, percent);
			} else if(n instanceof NestedProbFun<?>) {
				((NestedProbFun<T>) n).good(sourceElement, destinationElement, percent);
			}	
		}
	}

	/**        Adjust the probabilities to make sourceElement less likely to return destinationElement when fun() is called.
	 * @param  sourceElement as the sourceElement in this NestedProbFun 
	 *         which may be null to adjust the probability of the first returned element.
	 * @param  destinationElement as the destinationElement in this NestedProbFun.
	 * @param  percent as the percentage between 0 and 1 exclusive,
	 *         of the probability of sourceElement returning destinationElement to subtract from the probability. 
	 * @throws NullPointerException if destinationElement is null.
	 * @throws IllegalArgumentException if the percent isn't between 0 and 1 exclusive.
	 */
	public synchronized void bad(T sourceElement, T destinationElement, double percent) {
		Objects.requireNonNull(destinationElement);
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to good() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		if(sourceElement == null) {
			firstLink.bad(destinationElement, percent);
		} else {
			Node<T> n = chain2.get(sourceElement);
			firstLink.bad(sourceElement, percent);
			if(n instanceof ProbFun<?>) {
				((ProbFun<T>) n).bad(destinationElement, percent);
			} else if(n instanceof NestedProbFun<?>) {
				((NestedProbFun<T>) n).bad(sourceElement, destinationElement, percent);
			}	
		}
	}
	
	/**
	 * @return a randomly picked element from this NestedProbFun which is based on the previously returned element.
	 *         Note that any changes in the element will be reflected in this NestedProbFun.
	 */
	@Override
	public T fun() {
		if(previousSourceElement == null) {
			previousSourceElement = firstLink.fun();
			return previousSourceElement;
		}
		if(previousDestinationElement != null) {
			previousSourceElement = previousDestinationElement;
		}
		previousDestinationElement = chain2.get(previousSourceElement).fun();
		return previousDestinationElement;
	}
	
	/**       Private copy constructor for clone()
	 * @param nestedProbFun as the NestedProbFun to copy
	 */
	private NestedProbFun(NestedProbFun<T> nestedProbFun) {
		this.chain = chain2;
		this.firstLink = nestedProbFun.getFirstLink().clone();
		for(Entry<T, Node<T>> s : nestedProbFun.getChain().entrySet()) {
			this.chain2.put(s.getKey(), s.getValue().clone());
		}
	}
	
	@Override
	public NestedProbFun<T> clone() {
		return new NestedProbFun<T>(this);
	}

	@Override
	public String toString() {
		if(id == 0) {
			id = System.identityHashCode(this);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("First link:\n");
		sb.append(firstLink.toString());
		sb.append(" -> :");
		for(Entry<T, Node<T>> e : chain2.entrySet()) {
			sb.append(e.getValue());
			sb.append("     when first link = ");
			sb.append(e.getKey());
			sb.append("\n");
			sb.append(" and ");
		}
		sb.delete(sb.length()-6, sb.length());
		sb.append("\n");
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		if(id == 0) {
			id = System.identityHashCode(this);
		}
		return id;
	}

}