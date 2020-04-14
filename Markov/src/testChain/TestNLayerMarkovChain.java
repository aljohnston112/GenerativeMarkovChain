package testChain;

import java.util.ArrayList;
import java.util.List;

import chain.NLayerMarkovChain;

public class TestNLayerMarkovChain {

	public static void main(String[] args) {
		TestNLayerMarkovChainConstructor();
	}

	private static void TestNLayerMarkovChainConstructor() {
		// * @throws IllegalArgumentException if List<T> list does not have at least 1 object or int layers is not at least 2
		// NullPointerException tests
		List<Integer> list = null;
		int layers = 2;
		try {
			new NLayerMarkovChain<Integer>(list, layers);
		} catch (NullPointerException e) {
			System.out.print("Null list invariant blocked\n");
		} finally {
			System.out.print("Null list invariant blocked?\n");
		}
		// IllegalArgumentException tests
		list  = new ArrayList<Integer>();
		try {
			new NLayerMarkovChain<Integer>(list, layers);
		} catch (IllegalArgumentException e) {
			System.out.print("Empty list invariant blocked\n");
		} finally {
			System.out.print("Empty list invariant blocked?\n");
		}
		list.add(0); list.add(1);
		layers = 1;
		try {
			new NLayerMarkovChain<Integer>(list, layers);
		} catch (IllegalArgumentException e) {
			System.out.print("Not enough layers invariant blocked\n");
		} finally {
			System.out.print("Not enough layers invariant blocked?\n");
		}
		layers = 2;
		//System.out.print(new NLayerMarkovChain<Integer>(list, layers));
		layers = 3;
		//System.out.print(new NLayerMarkovChain<Integer>(list, layers));
		layers = 4;
		//System.out.print(new NLayerMarkovChain<Integer>(list, layers));
		layers = 5;
		System.out.print(new NLayerMarkovChain<Integer>(list, layers));

		
		

	}

}
