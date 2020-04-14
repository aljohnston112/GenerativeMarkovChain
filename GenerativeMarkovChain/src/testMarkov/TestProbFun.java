package testMarkov;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import markov.ProbFun;

public class TestProbFun {

	public static void main(String[] args) {
		testConstructor();
		testConstructor2();
		testSize();
		testClear();
		testGetProb();
		testGetProbs();
		testAdd();
		testAdd2();
		testPurge();
		testPurge2();
		testRemove();
		testGood();
		testBad();
	}

	private static void testPurge2() {
		System.out.print("Purge w/ prob Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);choices.add(2);choices.add(4);
		double[] p = {0.2, 0.3, 0.1, 0.4};
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		try {
			pf.purge(0);
		} catch(IllegalArgumentException e) {
			System.out.print("0% pass\n");
		} finally {
			System.out.print("0% pass?\n");
		}
		try {
			pf.purge(1);
		} catch(IllegalArgumentException e) {
			System.out.print("100% pass\n");
		} finally {
			System.out.print("100% pass?\n");
		}
		System.out.print("Before purge(0.35)\n");	
		System.out.print(pf.toString());
		pf.purge(0.35);
		System.out.print("\nAfter purge(0.35)\n");
		System.out.print(pf.toString());
		System.out.print("\n");	
	}

	private static void testPurge() {
		System.out.print("Purge Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.25, 0.75};
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		System.out.print("Before purge()\n");	
		System.out.print(pf.toString());
		pf.purge();
		System.out.print("\nAfter purge()\n");
		System.out.print(pf.toString());
		pf.add(2);
		System.out.print("\nBefore purge()\n");	
		System.out.print(pf.toString());
		pf.purge();
		System.out.print("\nAfter purge()\n");
		System.out.print(pf.toString());
		choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);choices.add(2);
		double[] p2 = {0.6, 0.2, 0.2};
		ProbFun<Integer> pf2 = new ProbFun<Integer>(choices, p2);
		System.out.print("\nBefore purge()\n");	
		System.out.print(pf2.toString());
		pf2.purge();
		System.out.print("\nAfter purge()\n");
		System.out.print(pf2.toString());
		System.out.print("\n");	
	}

	private static void testAdd2() {
		System.out.print("Add w/ prob Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.25, 0.75};
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		try {
			pf.add(null, 0.5);
		} catch(NullPointerException e) {
			System.out.print("Null add pass\n");
		} finally {
			System.out.print("Null add pass?\n");
		}
		try {
			pf.add(2, 0);
		} catch(IllegalArgumentException e) {
			System.out.print("0% pass\n");
		} finally {
			System.out.print("0% pass?\n");
		}
		try {
			pf.add(2, 1);
		} catch(IllegalArgumentException e) {
			System.out.print("100% pass\n");
		} finally {
			System.out.print("100% pass?\n");
		}
		System.out.print("Before add()\n");	
		System.out.print(pf.toString());
		System.out.print("\n");	
		pf.add(2, 0.5);
		System.out.print("After add(2, 0.5)\n");	
		System.out.print(pf.toString());
		System.out.print("\n");			
	}

	private static void testBad() {
		System.out.print("Bad Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.25, 0.75};
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		try {
			pf.bad(null, 0.5);
		} catch(NullPointerException e) {
			System.out.print("Null good pass\n");
		} finally {
			System.out.print("Null good pass?\n");
		}
		try {
			pf.bad(0, 1);
		} catch(IllegalArgumentException e) {
			System.out.print("Wrong %(1) pass\n");
		} finally {
			System.out.print("Wrong %(1) pass?\n");
		}
		try {
			pf.bad(0, 0);
		} catch(IllegalArgumentException e) {
			System.out.print("Wrong %(0) pass\n");
		} finally {
			System.out.print("Wrong %(0) pass?\n");
		}
		System.out.print("Probs should be 0.25, 0.75:\n");
		System.out.print(Arrays.toString(pf.getProbs()));
		System.out.print("\n");
		pf.bad(1, 0.5);
		System.out.print("Probs should be 0.62-0.63, 0.37-0.38:\n");
		System.out.print(Arrays.toString(pf.getProbs()));
		System.out.print("\n");
	}

	private static void testGood() {
		System.out.print("Good Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.25, 0.75};
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		try {
			pf.good(null, 0.5);
		} catch(NullPointerException e) {
			System.out.print("Null good pass\n");
		} finally {
			System.out.print("Null good pass?\n");
		}
		try {
			pf.good(0, 1);
		} catch(IllegalArgumentException e) {
			System.out.print("Wrong %(1) pass\n");
		} finally {
			System.out.print("Wrong %(1) pass?\n");
		}
		try {
			pf.good(0, 0);
		} catch(IllegalArgumentException e) {
			System.out.print("Wrong %(0) pass\n");
		} finally {
			System.out.print("Wrong %(0) pass?\n");
		}
		System.out.print("Probs should be 0.25, 0.75:\n");
		System.out.print(Arrays.toString(pf.getProbs()));
		System.out.print("\n");
		pf.good(0, 0.5);
		System.out.print("Probs should be 0.37-0.38, 0.62-0.63:\n");
		System.out.print(Arrays.toString(pf.getProbs()));
		System.out.print("\n");
	}

	private static void testRemove() {
		System.out.print("Remove Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.25, 0.75};
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		try {
			pf.remove(0);
		} catch(IllegalStateException e) {
			System.out.print("Short list pass\n");
		} finally {
			System.out.print("Short list pass?\n");
		}
		try {
			pf.remove(null);
		} catch(NullPointerException e) {
			System.out.print("Null remove pass\n");
		} finally {
			System.out.print("Null remove pass?\n");
		}
		pf.add(3);
		System.out.print("Before remove(0):\n");
		System.out.print(pf.toString());
		System.out.print("\n");	
		pf.remove(0);
		System.out.print("After remove(0):\n");
		System.out.print(pf.toString());
		System.out.print("\n");	
	}

	private static void testAdd() {
		System.out.print("Add Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.25, 0.75};
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		try {
			pf.add(null);
		} catch(NullPointerException e) {
			System.out.print("Null add pass\n");
		} finally {
			System.out.print("Null add pass?\n");
		}
		System.out.print("Before Add:\n");	
		System.out.print(pf.toString());
		System.out.print("\n");	
		pf.add(2);
		System.out.print("After Add:\n");	
		System.out.print(pf.toString());
		System.out.print("\n");	
	}

	private static void testGetProbs() {
		System.out.print("GetProbs Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.25, 0.75};
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		System.out.print("Probs should be 0.25, 0.75:\n");
		System.out.print(Arrays.toString(pf.getProbs()));
		System.out.print("\n");
	}

	private static void testGetProb() {
		System.out.print("GetProb Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.25, 0.75};
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		System.out.print("Prob of 0 should be 0.25:\n");
		System.out.print(pf.getProb(0));
		System.out.print("\n");
		System.out.print("Prob of 1 should be 0.75:\n");
		System.out.print(pf.getProb(1));
		System.out.print("\n");
	}

	private static void testClear() {
		System.out.print("Clear Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.25, 0.75};
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		System.out.print("Probs before clear:\n");
		System.out.print(pf.toString());
		System.out.print("\n");
		pf.clearProbs();
		System.out.print("Probs after clear:\n");
		System.out.print(pf.toString());
		System.out.print("\n");
	}

	private static void testSize() {
		System.out.print("Size Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.25, 0.75};
		System.out.print("Size should be 2: ");
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		System.out.print(pf.size());
		System.out.print("\n");
		}

	private static void testConstructor2() {
		System.out.print("2nd Constructor Test:\n");
		Set<Integer> choices = null;
		double[] p = new double[2];
		try {
			new ProbFun<Integer>(choices, p);
		} catch(NullPointerException e) {
			System.out.print("Null list pass\n");
		} finally {
			System.out.print("Null list pass?\n");
		}
		choices = new HashSet<Integer>();
		p = null;
		try {
			new ProbFun<Integer>(choices, p);
		} catch(NullPointerException e) {
			System.out.print("Null probs pass\n");
		} finally {
			System.out.print("Null probs pass?\n");
		}
		p = new double[2];		
		try {
			new ProbFun<Integer>(choices, p);
		} catch(IllegalArgumentException e) {
			System.out.print("Empty list pass\n");
		} finally {
			System.out.print("Empty list pass?\n");
		}
		choices.add(0); choices.add(1);
		try {
			new ProbFun<Integer>(choices, p);
		} catch(IllegalArgumentException e) {
			System.out.print("Wrong probs pass\n");
		} finally {
			System.out.print("Wrong probs pass?\n");
		}
		System.out.print("Should be 0 and 1 with 0.25 and 0.75 chance:\n");
		p[0] = 0.25; p[1] = 0.75;
		System.out.print(new ProbFun<Integer>(choices, p).toString());
		System.out.print("\n");
	}

	private static void testConstructor() {
		System.out.print("Constructor Test:\n");
		Set<Integer> choices = null;
		try {
			new ProbFun<Integer>(choices);
		} catch(NullPointerException e) {
			System.out.print("Null list pass\n");
		} finally {
			System.out.print("Null list pass?\n");
		}
		choices = new HashSet<Integer>();
		try {
			new ProbFun<Integer>(choices);
		} catch(IllegalArgumentException e) {
			System.out.print("Empty list pass\n");
		} finally {
			System.out.print("Empty list pass?\n");
		}
		System.out.print("Should be 0 and 1 with equal chance:\n");
		choices.add(0);choices.add(1);
		System.out.print(new ProbFun<Integer>(choices).toString());
	}
}
