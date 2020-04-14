package testChain;

import java.util.ArrayList;
import java.util.List;

import chain.MarkovNode;
import chain.NestedMarkovNode;
import chain.Node;

public class TestNestedMarkovNode {

	public static void main(String[] args) {
		testNestedMarkovNodeConstructor();
	}

	private static void testNestedMarkovNodeConstructor() {
		Integer start = null;
		List<Node<Integer>> list = null;
		// NullPointerException
		try {
			new NestedMarkovNode<Integer>(start, list);
		} catch (NullPointerException e) {
			System.out.print("Null list invariant blocked\n");
		} finally {
			System.out.print("Null list invariant blocked?\n");
		}
		list = new ArrayList<Node<Integer>>();
		// IllegalArgumentException test
		try {
			new NestedMarkovNode<Integer>(start, list);
		} catch (IllegalArgumentException e) {
			System.out.print("Empty list invariant blocked\n");
		} finally {
			System.out.print("Empty list invariant blocked?\n");
		}
		// Initialization test
		// Single nest
		Integer mnStart = 0;
		for(int i = 0; i < 2; i++) {
			List<Integer> mnList = new ArrayList<Integer>();
			mnList.add(1); mnList.add(2);
			list.add(new MarkovNode<Integer>(mnStart, mnList));
		}
		System.out.print(new NestedMarkovNode<Integer>(start, list).toString());
		// Double nest
		List<Node<Integer>> listnmn = new ArrayList<Node<Integer>>();
		start = 0;
		listnmn.add(new NestedMarkovNode<Integer>(start, list));
		listnmn.add(new NestedMarkovNode<Integer>(start, list));
		System.out.print(new NestedMarkovNode<Integer>(start, listnmn).toString());
	}

}
