package markov;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @author Alexander Johnston
 *         Copyright 2020
 *         A class where a set of elements are picked from randomly to decide which element will be the output of fun()
 *         based on the last returned element.
 * @param <T> The type of the elements that will be picked from.
 */
public class MarkovChain<T> extends Node<T> {

	// Contains the first probability function that is used to get the initial value
	private ProbFun<T> firstLink;

	// Maps every element to a ProbFun that contains every element
	private Map<T, ProbFun<T>> chain;

	// The previous element that will be used to select the ProbFun that will return the next element
	private T previousSourceElement;

	// The previous element that was returned by the ProbFun that is mapped to from previousSourceElement 
	private T previousDestinationElement;

	/**        Creates a MarkovChain where all the probabilities are equal.
	 *         The elements in set will be added by reference.
	 * @param  set as the set of elements. 
	 * @throws NullPointerException if the set is null or it contains a null element.
	 * @throws IllegalArgumentException if the set doesn't have more than one element.
	 */
	public MarkovChain(Set<T> set) {
		Objects.requireNonNull(set);
		if(set.size() < 2) {
			throw new IllegalArgumentException("Must have more than 1 element in the set passed to the MarkovChain constructor");
		}
		for(T t : set) {
			if(t == null) {
				throw new NullPointerException("The elements set passed into the MarkovChain constructor must not contain a null element");
			}
		}
		// Invariants secured
		firstLink = new ProbFun<T>(set);
		this.chain = new HashMap<T, ProbFun<T>>();
		for(T choice : set) {
			this.chain.put(choice, new ProbFun<T>(set));
		}
	}

	/**        
	 * @return the ProbFun that returns the first element when fun() is called.
	 */
	public ProbFun<T> getFirstLink() {
		return firstLink;
	}

	@Override
	public Map<T, ProbFun<T>> getChain() {
		return chain;
	}

	/**        Adjust the probabilities to make sourceElement more likely to return destinationElement when fun() is called.
	 * @param  sourceElement as the source element in the MarkovChain 
	 *         which may be null to adjust the probability of the first returned element.
	 * @param  destinationElement as the destination element in the MarkovChain. 
	 * @param  percent as the percentage, between 0 and 1 exclusive, 
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
			chain.get(sourceElement).good(destinationElement, percent);
			firstLink.good(sourceElement, percent);
		}
	}

	/**        Adjust the probabilities to make sourceElement less likely to return destinationElement when fun() is called.
	 * @param  sourceElement as the source element in the MarkovChain
	 *         which may be null to adjust the probability of the first returned element.
	 * @param  destinationElement as the destination element in the MarkovChain.
	 * @param  percent as the percentage, between 0 and 1 exclusive, 
	 *         of the probability of sourceElement returning destinationElement to subtract from the probability.
	 * @throws NullPointerException if destinationElement is null.
	 * @throws IllegalArgumentException if the percent isn't between 0 and 1 exclusive.
	 */
	public synchronized void bad(T sourceElement, T destinationElement, double percent) {
		Objects.requireNonNull(destinationElement);
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to bad() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		if(sourceElement == null) {
			firstLink.bad(destinationElement, percent);
		} else {
			chain.get(sourceElement).bad(destinationElement, percent);
			firstLink.bad(sourceElement, percent);
		}
	}

	/**
	 * @return a randomly picked element from this MarkovChain 
	 *         which is based on the previously returned element and past feedback.
	 *         Any changes in the element will be reflected in this MarkovChain.
	 */
	@Override
	public T fun() {
		if(previousSourceElement == null) {
			previousSourceElement = firstLink.fun();
			return previousSourceElement;
		}
		if(previousDestinationElement == null) {
			previousDestinationElement =chain.get(previousSourceElement).fun();
			return previousDestinationElement;
		}
		previousSourceElement = previousDestinationElement;
		previousDestinationElement = chain.get(previousSourceElement).fun();
		return previousDestinationElement;
	}

	/**       Private constructor for clone()
	 * @param markovChain as the MarkovChain to copy
	 */
	private MarkovChain(MarkovChain<T> markovChain) {
		this.firstLink = markovChain.getFirstLink().clone();
		this.chain = new HashMap<T, ProbFun<T>>();
		for(Entry<T, ProbFun<T>> e : markovChain.getChain().entrySet()) {
			chain.put(e.getKey(), e.getValue().clone());
		}
	}

	@Override
	public Node<T> clone() {
		return new MarkovChain<T>(this);
	}

	@Override
	public String toString() {
		if(id == 0) {
			id = System.identityHashCode(this);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Markov Chain:\n");
		sb.append(firstLink.toString());
		Iterator<T> it = chain.keySet().iterator();
		for(ProbFun<T> t : chain.values()) {
			sb.append(" -> ");
			sb.append(t.toString());
			sb.append(" when ");
			sb.append(it.next().toString());
			sb.append("\n");
			
		}
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