package testMarkov;

import java.util.HashSet;
import java.util.Set;

import markov.NLayerMarkovChain;

public class TestNLayerMarkovChain {

	public static void main(String[] args) {
		System.out.print("Constructor Test:\n");
		testConstructor();
	}

	private static void testConstructor() {
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		NLayerMarkovChain<Integer> nlmc = new NLayerMarkovChain<Integer>(choices, 4);
		System.out.print(nlmc.getChain().toString());

	}

}
