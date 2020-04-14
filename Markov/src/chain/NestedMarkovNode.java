package chain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * @author Alexander Johnston 
 *         Copyright 2020
 *         A class for creating nested nodes for an N-layer markov chain and adjusting the probabilities based on feedback of the past 
 * @param <U> as the type of the markov nodes that the starting object of type T maps to
 * @param <T> as the type of objects that compose type U and the type of the starting object that maps to the markov nodes of type U
 */
public class NestedMarkovNode<T> extends Node<T> {

	// The starting object that is mapped to every markov node
	T start;

	// The probability-markov node pairs that the start object maps to
	Map<Node<T>, Double> map;

	/**        Creates a node where a start Object is mapped to probability-markov node pairs
	 *         where each markov node in the pairs has an equal chance of following the start Object 
	 *         Note: changes to object structures will be reflected in this nested markov node 
	 * @param  start as the starting object that is mapped to every markov node in List<MarkovNode<T>> list
	 * @param  mns as the markov nodes that the start object maps to
	 * @throws IllegalArgumentException if List<MarkovNode<T>> list does not have at least 1 object
	 * @throws NullPointerException if List<MarkovNode<T>> list is null
	 */
	public NestedMarkovNode(T start, List<Node<T>> mns) throws IllegalArgumentException{
		Objects.requireNonNull(mns);
		if(mns.size() < 1) {
			throw new IllegalArgumentException("List<T> list passed into the MarkovNode constructor must have at least one element\n");
		}
		// Invariants secured
		this.map = new HashMap<Node<T>, Double>();
		this.start = start;
		double prob = 1.0/mns.size();
		double probSum = 0.0;
		for(Node<T> t : mns) {
			this.map.put(t, prob);
			probSum+=prob;
		}
		// Set sum to 1.0
		this.map.put(mns.get(0), this.map.get(mns.get(0))-(probSum-1.0));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Starting element ");
		if(start != null) {
			sb.append(this.start.toString());
		} else {
			sb.append("null");	
		}
		sb.append(" maps to the node where: \n");
		Set<Entry<Node<T>, Double>> s = this.map.entrySet();
		for(Entry<Node<T>, Double> e : s) {
			sb.append("         ");
			sb.append(e.getKey().toString().replace("\n", "\n         "));
			sb.replace(sb.length()-9, sb.length(), "");
			sb.append("with ");
			sb.append(e.getValue().toString());
			sb.append(" probability and the node where:");
			sb.append("\n");
		}
		sb.replace(sb.length()-21, sb.length(), "");
		sb.append("\n");
		return sb.toString();
	}

}