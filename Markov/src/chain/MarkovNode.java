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
 *         A class for creating nodes for an N-layer markov chain and adjusting the probabilities based on feedback of the past 
 * @param <T> as the type of objects in this markov node
 */
public class MarkovNode<T> extends Node<T> {

	// The starting object that is mapped to every other
	T start;
	
	// The probability-Object pairs that the start object maps to
	Map<T, Double> map;
	
	/**        Creates a node where a start Object is mapped to probability-Object pairs
	 *         where each Object in the pairs has an equal chance of following the start Object 
	 *         Note: changes to object structures will be reflected in this markov node 
	 * @param  start as the starting object that is mapped to every object in List<T> list
	 * @param  list as the objects that the start object maps to
	 * @throws IllegalArgumentException if List<T> list does not have at least 1 object
	 * @throws NullPointerException if List<T> list is null
	 */
	public MarkovNode(T start, List<T> list) throws IllegalArgumentException{
		Objects.requireNonNull(list);
		if(list.size() < 1) {
			throw new IllegalArgumentException("List<T> list passed into the MarkovNode constructor must have at least one element\n");
		}
		// Invariants secured
		this.map = new HashMap<T, Double>();
		this.start = start;
		double prob = 1.0/list.size();
		double probSum = 0.0;
		for(T t : list) {
			this.map.put(t, prob);
			probSum+=prob;
		}
		// Set sum to 1.0
		this.map.put(list.get(0), this.map.get(list.get(0))-(probSum-1.0));
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
		sb.append(" maps to: \n");
		Set<Entry<T, Double>> s = this.map.entrySet();
		for(Entry<T, Double> e : s) {
			sb.append(e.getKey().toString());
			sb.append(" with ");
			sb.append(e.getValue().toString());
			sb.append(" probability and");
			sb.append("\n");
		}
		sb.replace(sb.length()-5, sb.length(), "");
		sb.append("\n");
		return sb.toString();
	}
	
}
