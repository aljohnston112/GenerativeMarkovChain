package testMarkov;

import java.util.HashSet;
import java.util.Set;

import markov.NestedProbFun;
import markov.ProbFun;

public class TestNestedProbFun {


	public static void main(String[] args) {
		testConstructor();
		testFun();
		testGood();
		testBad();
	}

	private static void testBad() {
		System.out.print("Bad Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.25, 0.75};
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		NestedProbFun<Integer> nmn = new NestedProbFun<Integer>(pf);
		try {
			nmn.bad(0, null, 0.1);
		} catch(NullPointerException e) {
			System.out.print("Null list pass\n");
		} finally {
			System.out.print("Null list pass?\n");
		}
		try {
			nmn.bad(0, 1, 0);
		} catch(IllegalArgumentException e) {
			System.out.print("0 percent pass\n");
		} finally {
			System.out.print("0 percent pass?\n");
		}
		try {
			nmn.bad(0, 1, 1);
		} catch(IllegalArgumentException e) {
			System.out.print("1 percent pass\n");
		} finally {
			System.out.print("1 percent pass?\n");
		}
		System.out.print("Before bad:\n");
		System.out.print(nmn.toString());
		System.out.print("\nAfter bad(null, 1)\n");
		nmn.bad(null, 1, 0.5);
		System.out.print(nmn.toString());
		System.out.print("\nAfter bad(1, 1)\n");
		nmn.bad(1, 1, 0.5);
		System.out.print(nmn.toString());
		System.out.print("\n");		
	}

	private static void testGood() {
		System.out.print("Good Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.25, 0.75};
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		NestedProbFun<Integer> nmn = new NestedProbFun<Integer>(pf);
		try {
			nmn.good(0, null, 0.1);
		} catch(NullPointerException e) {
			System.out.print("Null list pass\n");
		} finally {
			System.out.print("Null list pass?\n");
		}
		try {
			nmn.good(0, 1, 0);
		} catch(IllegalArgumentException e) {
			System.out.print("0 percent pass\n");
		} finally {
			System.out.print("0 percent pass?\n");
		}
		try {
			nmn.good(0, 1, 1);
		} catch(IllegalArgumentException e) {
			System.out.print("1 percent pass\n");
		} finally {
			System.out.print("1 percent pass?\n");
		}
		System.out.print("Before good:\n");
		System.out.print(nmn.toString());
		System.out.print("\nAfter good(null, 1)\n");
		nmn.good(null, 1, 0.5);
		System.out.print(nmn.toString());
		System.out.print("\nAfter good(1, 1)\n");
		nmn.good(1, 1, 0.5);
		System.out.print(nmn.toString());
		System.out.print("\n");
	}

	private static void testFun() {
		System.out.print("Fun Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.25, 0.75};
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		NestedProbFun<Integer> nmn = new NestedProbFun<Integer>(pf);
		int zz = 0;
		int zo = 0;
		int oz = 0;
		int oo = 0;
		Integer t0 = nmn.fun();
		Integer t1 = nmn.fun();
		for(int i = 0; i < 1000; i++) {
			if(t0 == 0) {
				if(t1 == 0) {
					zz++;
				} else {
					zo++;
				}
			} else {
				if(t1 == 0) {
					oz++;
				} else {
					oo++;
				}
			}
			t0 = t1;
			t1 = nmn.fun();
		}
		System.out.print(nmn.toString());
		System.out.print("\n00: ");
		System.out.print(zz);
		System.out.print("\n01: ");
		System.out.print(zo);
		System.out.print("\n10: ");
		System.out.print(oz);
		System.out.print("\n11: ");
		System.out.print(oo);
		System.out.print("\n");

	}

	private static void testConstructor() {
		System.out.print("Constructor Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.25, 0.75};
		ProbFun<Integer> pf = null;
		try {
			new NestedProbFun<Integer>(pf);
		} catch(NullPointerException e) {
			System.out.print("Null list pass\n");
		} finally {
			System.out.print("Null list pass?\n");
		}
		pf = new ProbFun<Integer>(choices, p);
		NestedProbFun<Integer> nmn = new NestedProbFun<Integer>(pf);
		System.out.print("Should be 0 and 1 with probabilities 0.25 and 0.75:\n");
		System.out.print(nmn.toString());
		System.out.print("\n");
	}

}
