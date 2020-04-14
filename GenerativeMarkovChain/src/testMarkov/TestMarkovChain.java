package testMarkov;

import java.util.HashSet;
import java.util.Set;

import markov.MarkovChain;

public class TestMarkovChain {

	public static void main(String[] args) {
		testConstructor();
		testGood();
		testBad();
		testFun();
	}
	
	private static void testFun() {
		System.out.print("Fun Test:\n");
		Set<Double> choices = new HashSet<Double>();
		choices.add(0.0);
		choices.add(1.0);
		MarkovChain<Double> pftm = new MarkovChain<Double>(choices);
		pftm.good(1.0, 1.0, 0.5);
		pftm.good(null, 1.0, 0.5);
		System.out.print(pftm.toString());
		System.out.print("\n");
		int zz = 0;
		int zo = 0;
		int oz = 0;
		int oo = 0;
		double previous = 0.0;
		for(int i = 0; i < 1000; i++) {
			double d = pftm.fun();
			if(previous == 0.0) {
				if(d == 0.0) {
					zz++;
				} else if(d == 1.0) {
					zo++;
				}
			} else if(previous == 1.0) {
				if(d == 0.0) {
					oz++;
				} else if(d == 1.0) {
					oo++;
				}
			}
			previous = d;
		}
		System.out.print("0 to 0: ");
		System.out.print(zz);
		System.out.print("\n");
		System.out.print("0 to 1: ");
		System.out.print(zo);
		System.out.print("\n");
		System.out.print("1 to 0: ");
		System.out.print(oz);
		System.out.print("\n");
		System.out.print("1 to 1: ");
		System.out.print(oo);
		System.out.print("\n");
	}

	private static void testBad() {
		System.out.print("Bad Test:\n");
		Set<Double> choices = new HashSet<Double>();
		choices.add(0.0);
		choices.add(1.0);
		choices.add(2.0);
		MarkovChain<Double> mc = new MarkovChain<Double>(choices);
		try {
			mc.bad(1.0, null, 0.5);
		} catch(NullPointerException e) {
			System.out.print("Good null pass\n");
		} finally {
			System.out.print("Good null pass?\n");
		}
		try {
			mc.bad(1.0, 1.0, 1);
		} catch(IllegalArgumentException e) {
			System.out.print("Good wrong percent(1) pass\n");
		} finally {
			System.out.print("Good wrong percent(1) pass?\n");
		}
		try {
			mc.bad(1.0, 1.0, 0);
		} catch(IllegalArgumentException e) {
			System.out.print("Good wrong percent(0) pass\n");
		} finally {
			System.out.print("Good wrong percent(0) pass?\n");
		}
		try {
			mc.bad(1.0, 1.0, 2);
		} catch(IllegalArgumentException e) {
			System.out.print("Good wrong percent(2) pass\n");
		} finally {
			System.out.print("Good wrong percent(2) pass?\n");
		}
		try {
			mc.bad(1.0, 1.0, -1);
		} catch(IllegalArgumentException e) {
			System.out.print("Good wrong percent(-1) pass\n");
		} finally {
			System.out.print("Good wrong percent(-1) pass?\n");
		}
		System.out.print("Before Bad(1, 1)\n");
		System.out.print(mc.toString());
		mc.bad(1.0, 1.0, 0.5);
		System.out.print("After Bad(1, 1)\n");
		System.out.print(mc.toString());
		System.out.print("Before Bad(null, 1)\n");
		System.out.print(mc.toString());
		mc.bad(null, 1.0, 0.5);
		System.out.print("After Bad(null, 1)\n");
		System.out.print(mc.toString());
	}

	private static void testGood() {
		System.out.print("Good Test:\n");
		Set<Double> choices = new HashSet<Double>();
		choices.add(0.0);
		choices.add(1.0);
		choices.add(2.0);
		MarkovChain<Double> mc = new MarkovChain<Double>(choices);
		try {
			mc.good(1.0, null, 0.5);
		} catch(NullPointerException e) {
			System.out.print("Good null pass\n");
		} finally {
			System.out.print("Good null pass?\n");
		}
		try {
			mc.good(1.0, 1.0, 1);
		} catch(IllegalArgumentException e) {
			System.out.print("Good wrong percent(1) pass\n");
		} finally {
			System.out.print("Good wrong percent(1) pass?\n");
		}
		try {
			mc.good(1.0, 1.0, 0);
		} catch(IllegalArgumentException e) {
			System.out.print("Good wrong percent(0) pass\n");
		} finally {
			System.out.print("Good wrong percent(0) pass?\n");
		}
		try {
			mc.good(1.0, 1.0, 2);
		} catch(IllegalArgumentException e) {
			System.out.print("Good wrong percent(1) pass\n");
		} finally {
			System.out.print("Good wrong percent(1) pass?\n");
		}
		try {
			mc.good(1.0, 1.0, -1);
		} catch(IllegalArgumentException e) {
			System.out.print("Good wrong percent(-1) pass\n");
		} finally {
			System.out.print("Good wrong percent(-1) pass?\n");
		}
		System.out.print("Before Good(1, 1)\n");
		System.out.print(mc.toString());
		mc.good(1.0, 1.0, 0.5);
		System.out.print("After Good(1, 1)\n");
		System.out.print(mc.toString());
		System.out.print("Before Good(null, 1)\n");
		System.out.print(mc.toString());
		mc.good(null, 1.0, 0.5);
		System.out.print("After Good(null, 1)\n");
		System.out.print(mc.toString());
		System.out.print("\n");
	}

	private static void testConstructor() {
		System.out.print("Constructor Test:\n");
		try {
			new MarkovChain<Double>(null);
		} catch(NullPointerException e) {
			System.out.print("Constructor null pass\n");
		} finally {
			System.out.print("Constructor null pass?\n");
		}
		Set<Double> choices = new HashSet<Double>();
		choices.add(0.0);
		choices.add(1.0);
		choices.add(2.0);
		MarkovChain<Double> pftm = new MarkovChain<Double>(choices);
		System.out.print("Should be 0,1,2 with equal probability where one probFun branches into 2:\n");
		System.out.print(pftm.toString());
	}
	
}
