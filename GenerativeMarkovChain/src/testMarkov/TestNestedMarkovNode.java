package testMarkov;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import markov.NestedMarkovNode;
import markov.NestedProbFun;
import markov.ProbFun;

public class TestNestedMarkovNode {

	public static void main(String[] args) {
		testConstructor();
		testGood();
		testBad();
		testFun();
	}

	private static void testFun() {
		System.out.print("Fun Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		double[] p = {0.3, 0.7};
		ProbFun<Integer> pf = new ProbFun<Integer>(choices, p);
		NestedProbFun<Integer> npf = new NestedProbFun<Integer>(pf);
		NestedMarkovNode<Integer> nmn = new NestedMarkovNode<Integer>(npf);
		List<Integer> previousObjects = new ArrayList<Integer>();
		previousObjects.add(0);
		nmn.bad(previousObjects, 0.5);
		previousObjects.add(1);
		nmn.bad(previousObjects, 0.5);
		previousObjects.add(1);
		nmn.bad(previousObjects, 0.5);
		System.out.print(nmn.toString());	
		int zzz = 0;
		int zzo = 0;
		int zoz = 0;
		int zoo = 0;
		int ozz = 0;
		int ozo = 0;
		int ooz = 0;
		int ooo = 0;
		
		int t0 = nmn.fun();
		int t1 = nmn.fun();
		int t2 = nmn.fun();
		for(int i = 0; i < 10000; i++) {
			if(t0 == 0 && t1 == 0 && t2 == 0) {
				zzz++;
			} else if(t0 == 0 && t1 == 0 && t2 == 1) {
				zzo++;
			} else if(t0 == 0 && t1 == 1 && t2 == 0) {
				zoz++;
			} else if(t0 == 0 && t1 == 1 && t2 == 1) {
				zoo++;
			} else if(t0 == 1 && t1 == 0 && t2 == 0) {
				ozz++;
			} else if(t0 == 1 && t1 == 0 && t2 == 1) {
				ozo++;
			} else if(t0 == 1 && t1 == 1 && t2 == 0) {
				ooz++;
			} else if(t0 == 1 && t1 == 1 && t2 == 1) {
				ooo++;
			} 
			t0 = t1;
			t1 = t2;
			t2 = nmn.fun();
		}
		System.out.print("\n");
		System.out.print("zzz: ");
		System.out.print(zzz);
		System.out.print("\n");
		System.out.print("zzo: ");
		System.out.print(zzo);
		System.out.print("\n");
		System.out.print("zoz: ");
		System.out.print(zoz);
		System.out.print("\n");
		System.out.print("zoo: ");
		System.out.print(zoo);
		System.out.print("\n");
		System.out.print("ozz: ");
		System.out.print(ozz);
		System.out.print("\n");
		System.out.print("ozo: ");
		System.out.print(ozo);
		System.out.print("\n");
		System.out.print("ooz: ");
		System.out.print(ooz);
		System.out.print("\n");
		System.out.print("ooo: ");
		System.out.print(ooo);
		System.out.print("\n");
		


	}

	private static void testBad() {
		System.out.print("Bad Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);choices.add(2);
		ProbFun<Integer> pf = new ProbFun<Integer>(choices);
		NestedProbFun<Integer> npf = new NestedProbFun<Integer>(pf);
		NestedMarkovNode<Integer> nmn = new NestedMarkovNode<Integer>(npf);
		List<Integer> previousObjects = null;
		try {
			nmn.bad(previousObjects, 0.5);
		} catch(NullPointerException e) {
			System.out.print("Null list pass\n");
		} finally {
			System.out.print("Null list pass?\n");
		}
		previousObjects = new ArrayList<Integer>();
		try {
			nmn.bad(previousObjects, 0.5);
		} catch(IllegalArgumentException e) {
			System.out.print("Empty list pass\n");
		} finally {
			System.out.print("Empty list pass?\n");
		}
		previousObjects.add(0);
		previousObjects.add(null);
		try {
			nmn.bad(previousObjects, 0.5);
		} catch(NullPointerException e) {
			System.out.print("Null element pass\n");
		} finally {
			System.out.print("Null element pass?\n");
		}
		previousObjects.remove(1);
		try {
			nmn.bad(previousObjects, 0.0);
		} catch(IllegalArgumentException e) {
			System.out.print("0% pass\n");
		} finally {
			System.out.print("0% pass?\n");
		}
		try {
			nmn.bad(previousObjects, 1.0);
		} catch(IllegalArgumentException e) {
			System.out.print("100% pass\n");
		} finally {
			System.out.print("100% pass?\n");
		}
		System.out.print("\nBefore bad([0], 0.5)\n");
		System.out.print(nmn.toString());
		System.out.print("\nAfter bad([0], 0.5)\n");
		nmn.bad(previousObjects, 0.5);
		System.out.print(nmn.toString());
		previousObjects.add(2);
		System.out.print("\nBefore bad([0, 2], 0.5)\n");
		System.out.print(nmn.toString());
		System.out.print("\nAfter bad([0, 2], 0.5)\n");
		nmn.bad(previousObjects, 0.5);
		System.out.print(nmn.toString());
		previousObjects.add(2);
		System.out.print("\nBefore bad([0, 2, 2], 0.5)\n");
		System.out.print(nmn.toString());
		System.out.print("\nAfter bad([0, 2, 2], 0.5)\n");
		nmn.bad(previousObjects, 0.5);
		System.out.print(nmn.toString());
	}

	private static void testGood() {
		System.out.print("Good Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);choices.add(2);
		ProbFun<Integer> pf = new ProbFun<Integer>(choices);
		NestedProbFun<Integer> npf = new NestedProbFun<Integer>(pf);
		NestedMarkovNode<Integer> nmn = new NestedMarkovNode<Integer>(npf);
		List<Integer> previousObjects = null;
		try {
			nmn.good(previousObjects, 0.5);
		} catch(NullPointerException e) {
			System.out.print("Null list pass\n");
		} finally {
			System.out.print("Null list pass?\n");
		}
		previousObjects = new ArrayList<Integer>();
		try {
			nmn.good(previousObjects, 0.5);
		} catch(IllegalArgumentException e) {
			System.out.print("Empty list pass\n");
		} finally {
			System.out.print("Empty list pass?\n");
		}
		previousObjects.add(0);
		previousObjects.add(null);
		try {
			nmn.good(previousObjects, 0.5);
		} catch(NullPointerException e) {
			System.out.print("Null element pass\n");
		} finally {
			System.out.print("Null element pass?\n");
		}
		previousObjects.remove(1);
		try {
			nmn.good(previousObjects, 0.0);
		} catch(IllegalArgumentException e) {
			System.out.print("0% pass\n");
		} finally {
			System.out.print("0% pass?\n");
		}
		try {
			nmn.good(previousObjects, 1.0);
		} catch(IllegalArgumentException e) {
			System.out.print("100% pass\n");
		} finally {
			System.out.print("100% pass?\n");
		}
		System.out.print("\nBefore good([0], 0.5)\n");
		System.out.print(nmn.toString());
		nmn.good(previousObjects, 0.5);
		System.out.print("After good([0], 0.5)\n");
		System.out.print(nmn.toString());
		previousObjects.add(2);
		System.out.print("\nBefore good([0, 2], 0.5)\n");
		System.out.print(nmn.toString());
		System.out.print("After good([0, 2], 0.5)\n");
		nmn.good(previousObjects, 0.5);
		System.out.print(nmn.toString());
		previousObjects.add(2);
		System.out.print("\nBefore good([0, 2, 2], 0.5)\n");
		System.out.print(nmn.toString());
		nmn.good(previousObjects, 0.5);
		System.out.print("\nAfter good([0, 2, 2], 0.5)\n");
		System.out.print(nmn.toString());
	}

	private static void testConstructor() {
		System.out.print("Constructor Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		ProbFun<Integer> pf = new ProbFun<Integer>(choices);
		NestedProbFun<Integer> npf = null;
		try {
			new NestedMarkovNode<Integer>(npf);
		} catch(NullPointerException e) {
			System.out.print("Null list pass\n");
		} finally {
			System.out.print("Null list pass?\n");
		}
		npf = new NestedProbFun<Integer>(pf);
		System.out.print("Should be a 3 layer NestedMarkovNode with equal ProbFuns:\n");
		NestedMarkovNode<Integer> nmn = new NestedMarkovNode<Integer>(npf);
		//NestedMarkovNode<Integer> nmn2 = new NestedMarkovNode<Integer>(nmn);
		//NestedMarkovNode<Integer> nmn3 = new NestedMarkovNode<Integer>(nmn2);
		//NestedMarkovNode<Integer> nmn4 = new NestedMarkovNode<Integer>(nmn3);
		System.out.print(nmn.toString());
	}
}
