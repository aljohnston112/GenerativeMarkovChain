package testChain;

import java.util.ArrayList;
import java.util.List;

import chain.MarkovNode;

/**
@author Alexander Johnston 
        Copyright 2020
        A class for testing nodes for an N-layer markov chain and adjusting the probabilities based on feedback of the past 
 */
public class TestMarkovNode {

	public static void main(String[] args) {
		testMarkovNodeConstructor();
	}

	private static void testMarkovNodeConstructor() {
		// NullPointerException tests
		Integer start = 0;
		List<Integer> list = null;
		try {
			new MarkovNode<Integer>(start, list);
		} catch (NullPointerException e) {
			System.out.print("Null list invariant blocked\n");
		} finally {
			System.out.print("Null list invariant blocked?\n");
		}
		// IllegalArgumentException test
		list = new ArrayList<Integer>();
		try {
			new MarkovNode<Integer>(start, list);
		} catch (IllegalArgumentException e) {
			System.out.print("Empty list invariant blocked\n");
		} finally {
			System.out.print("Empty list invariant blocked?\n");
		}
		// Initialization test
		list.add(1); list.add(2); list.add(3);
		System.out.print(new MarkovNode<Integer>(start, list).toString());
	}



}
