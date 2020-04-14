package chain;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Alexander Johnston 
 *         Copyright 2020
 *         A class for creating N-layer markov chains and adjusting the probabilities based on feedback of the past 
 */
public class NLayerMarkovChain<T> {

	// The n layer markov node
	NestedMarkovNode<T> nmn; 

	// The number of layers
	int layers;

	// The last int layers of objects that were returned by fun
	// These are used to give feedback to the markopv chain
	Deque<T> previousObjects;
	
	// The next objects to return from fun
	Deque<T> currentObjects;

	/**        Creates a markov chain with multiple layers to allow for memory of the past equal to the number of layers
	 *         Note: changes to object structures will be reflected in this markov chain 
	 * @param  list as the objects that will compose this markov chain
	 * @param  layers as the number of layers to compose this markov chain
	 * @throws IllegalArgumentException if List<T> list does not have at least 1 object or int layers is not at least 2
	 * @throws NullPointerException if List<T> list is null
	 */
	public NLayerMarkovChain(List<T> list, int layers) {
		Objects.requireNonNull(list);
		if(list.size() < 1) {
			throw new IllegalArgumentException("List<T> list passed into the NLayerMarkovChain constructor must have at least one element\n");
		}
		if(layers < 2) {
			throw new IllegalArgumentException("int layers passed into the NLayerMarkovChain constructor must be at least 2\n");
		}
		// Invariants secured
		this.layers = layers;
		previousObjects = new LinkedBlockingDeque<T>(layers);
		currentObjects = new LinkedBlockingDeque<T>(layers);
		List<Node<T>> mns = new ArrayList<Node<T>>();
		for(T t : list) {
			mns.add(new MarkovNode<T>(t, list));
		}
		if(layers == 2) {
			this.nmn = new NestedMarkovNode<T>(null, mns);
			return;
		}
		List<Node<T>> nmns = new ArrayList<Node<T>>();
		for(T t : list) {
			nmns.add(new NestedMarkovNode<T>(t, mns));
		}
		this.nmn = new NestedMarkovNode<T>(null, nmns);
		for(int i = 3; i < layers; i++) {
			List<Node<T>> nmns2 = new ArrayList<Node<T>>();
			for(T t : list) {
				nmns2.add(new NestedMarkovNode<T>(t, new ArrayList<Node<T>>(this.nmn.map.keySet())));
			}
			this.nmn = new NestedMarkovNode<T>(null, nmns2);
		}

	}

	@Override
	public String toString() {
		return nmn.toString();
	}
	
	

}