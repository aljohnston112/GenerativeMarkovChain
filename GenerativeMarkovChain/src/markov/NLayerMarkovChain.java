package markov;

import java.util.Objects;
import java.util.Set;

/**
 * @author Alexander Johnston 
 *         Copyright 2020
 *         A class for creating NLayerMarkovChains and adjusting the probabilities based on feedback of the past.
 * @param <T> as the type of elements that will compose the NLayerMarkovChain.
 */
public class NLayerMarkovChain<T> {

	// The Node that contains the markov chain
	Node<T> chain;

	/**        Creates an NLayerMarkovChain for generating sequences and adjusting probabilities to affect future generations.
	 * @param  choices as the choices that can appear at every step in the sequence.
	 * @param  layers as the number of elements that the series can generate before cascading previous elements.
	 * @throws NullPointerException if choices is null.
	 * @throws IllegalArgumentException if there aren't at least two elements in choices.
	 */
	public NLayerMarkovChain(Set<T> choices, int layers) {
		Objects.requireNonNull(choices);
		if(choices.size() < 2) {
			throw new IllegalArgumentException("choices passed into the NLayerMarkovChain constructor must have at least 2 elements");
		}
		if(layers < 1) {
			throw new IllegalArgumentException("Must have at least 1 layer when making a n-layer markov chain");
		}
		// Invariants secured
		if(layers == 1) {
			chain = new ProbFun<T>(choices);
		} else if(layers == 2) {
			chain = new NestedProbFun<T>(new ProbFun<T>(choices));
		} else {
			NestedMarkovNode<T> nmn = new NestedMarkovNode<T>(new NestedProbFun<T>(new ProbFun<T>(choices)));
			for(int i = 0; i < layers-3; i++) {
				nmn = new NestedMarkovNode<T>(nmn);
			}
			chain = nmn;
		}
	}
	
	/**
	 * @return the chain that makes up this NLayerMarkovNode so it's functions can be called.
	 */
	public Node<T> getChain(){
		return chain;
	}

}