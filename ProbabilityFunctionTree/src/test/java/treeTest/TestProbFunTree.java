package treeTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tree.ProbFunTree;

public class TestProbFunTree {

	public static void main(String[] args) {
		/*
		testConstructor();
		testClearProbs();
		testAddToAll();
		testAddToAll2();
		testRemoveFromAll();
		testPruneAll();
		testPruneAll2();
		testGood();
		testBad();
		testFun();
		testParentSize();
		testSize();
		testClone();
		testClearHistory();
		testConstructorWithProbs();
		testAddLayer();
		testAddLayer2();
		testAdd();
		testAdd2();
		testRemove();
		testPrune();
		testPrune2();
		 */
		testAddIfPresent();
		testAddIfPresentToAll();

	}

	private static void testAddIfPresentToAll() {
		//NullPointerException - if ifPresent or elementToAdd are null.
		//IllegalArgumentException - if ifPresent is empty.
		System.out.print("AddIfPresentToAll() Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);choices.add(2);
		int layers = 4;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);

		List<Integer> ifPresent = null;
		Integer elementToAdd = 5;

		try {
			pf.addIfPresentToAll(ifPresent, elementToAdd);
		} catch(NullPointerException e) {
			System.out.print("Null List pass\n");
		} finally {
			System.out.print("Null List pass?\n");
		}

		ifPresent = new ArrayList<Integer>();
		ifPresent.add(0);
		elementToAdd = null;
		try {
			pf.addIfPresentToAll(ifPresent, elementToAdd);
		} catch(NullPointerException e) {
			System.out.print("Null element pass\n");
		} finally {
			System.out.print("Null element pass?\n");
		}

		ifPresent = new ArrayList<Integer>();
		elementToAdd = 5;
		try {
			pf.addIfPresentToAll(ifPresent, elementToAdd);
		} catch(IllegalArgumentException e) {
			System.out.print("Empty List pass\n");
		} finally {
			System.out.print("Empty List pass?\n");
		}

		ifPresent.add(0);ifPresent.add(2);ifPresent.add(1);
		System.out.print("Before AddIfPresentToAll([0, 2, 1], 5):\n");
		System.out.print(pf);
		pf.addIfPresentToAll(ifPresent, elementToAdd);
		System.out.print("After AddIfPresentToAll([0, 2, 1], 5):\n");
		System.out.print(pf);
	}

	private static void testAddIfPresent() {
		//NullPointerException - if ifPresent or elementToAdd are null.
		//IllegalArgumentException - if ifPresent is empty.
		System.out.print("AddIfPresent() Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);choices.add(2);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);

		List<Integer> ifPresent = null;
		Integer elementToAdd = 5;

		try {
			pf.addIfPresent(ifPresent, elementToAdd);
		} catch(NullPointerException e) {
			System.out.print("Null List pass\n");
		} finally {
			System.out.print("Null List pass?\n");
		}

		ifPresent = new ArrayList<Integer>();
		ifPresent.add(0);
		elementToAdd = null;
		try {
			pf.addIfPresent(ifPresent, elementToAdd);
		} catch(NullPointerException e) {
			System.out.print("Null element pass\n");
		} finally {
			System.out.print("Null element pass?\n");
		}

		ifPresent = new ArrayList<Integer>();
		elementToAdd = 5;
		try {
			pf.addIfPresent(ifPresent, elementToAdd);
		} catch(IllegalArgumentException e) {
			System.out.print("Empty List pass\n");
		} finally {
			System.out.print("Empty List pass?\n");
		}

		ifPresent.add(0);ifPresent.add(2);ifPresent.add(1);
		System.out.print("Before AddIfPresent([0, 2, 1], 5):\n");
		System.out.print(pf);
		pf.addIfPresent(ifPresent, elementToAdd);
		System.out.print("After AddIfPresent([0, 2, 1], 5):\n");
		System.out.print(pf);
	}

	private static void testPrune2() {
		//IllegalArgumentException - if percent is not between 0.0 and 1.0 (exclusive)
		System.out.print("Prune w/ Percent Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);choices.add(2);
		Set<Integer> choices2 = new HashSet<Integer>();
		choices2.add(5);choices2.add(6);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		pf.addToAll(3, choices2, .1);
		pf.addToAll(4, choices2, 0.05);
		try {
			pf.prune(0);
		} catch(IllegalArgumentException e) {
			System.out.print("0% pass\n");
		} finally {
			System.out.print("0% pass?\n");
		}
		try {
			pf.prune(1);
		} catch(IllegalArgumentException e) {
			System.out.print("1% pass\n");
		} finally {
			System.out.print("1% pass?\n");
		}
		System.out.print("Before Prune(0.05):\n");
		System.out.print(pf);
		pf.prune(0.05);
		System.out.print("After Prune(0.05):\n");
		System.out.print(pf);
		System.out.print("Before Prune(0.9):\n");
		System.out.print(pf);
		pf.prune(0.9);
		System.out.print("After Prune(0.9):\n");
		System.out.print(pf);
	}

	private static void testPrune() {
		System.out.print("Prune Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);choices.add(2);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		System.out.print("Before Prune():\n");
		System.out.print(pf);
		pf.prune();
		System.out.print("After Prune():\n");
		System.out.print(pf);
		Set<Integer> choices2 = new HashSet<Integer>();
		choices2.add(0);choices2.add(1);
		ProbFunTree<Integer> pf2 = new ProbFunTree<Integer>(choices2, layers);	
		System.out.print("Before Prune():\n");
		System.out.print(pf2);
		pf.prune();
		System.out.print("After Prune():\n");
		System.out.print(pf2);
		Set<Integer> choices3 = new HashSet<Integer>();
		choices3.add(0);choices3.add(1);
		ProbFunTree<Integer> pf3 = new ProbFunTree<Integer>(choices3, layers);	
		Set<Integer> choices4 = new HashSet<Integer>();
		choices4.add(4);choices4.add(3);
		pf3.addToAll(2, choices4, 0.7);
		System.out.print("Before Prune():\n");
		System.out.print(pf3);
		pf3.prune();
		System.out.print("After Prune():\n");
		System.out.print(pf3);
	}

	private static void testRemove() {
		//NullPointerException - if element is null.
		System.out.print("Remove Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		try {
			pf.remove(null);
		} catch(NullPointerException e) {
			System.out.print("Null element pass\n");
		} finally {
			System.out.print("Null element pass?\n");
		}
		System.out.print("Before remove(1):\n");
		System.out.print(pf);
		pf.remove(1);
		System.out.print("After remove(1):\n");
		System.out.print(pf);
		choices.add(2);
		pf = new ProbFunTree<Integer>(choices, layers);
		System.out.print("Before remove(0):\n");
		System.out.print(pf);
		pf.remove(0);
		System.out.print("After remove(0):\n");
		System.out.print(pf);
	}

	private static void testAdd2() {
		//NullPointerException - if element is null.
		//IllegalArgumentException - if percent is not between 0.0 and 1.0 (exclusive).
		System.out.print("Add w/ Percent Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		Set<Integer> choices2 = new HashSet<Integer>();
		choices2.add(4);choices2.add(3);
		try {
			pf.add(null, choices2, 0.1);
		} catch(NullPointerException e) {
			System.out.print("Null element pass\n");
		} finally {
			System.out.print("Null element pass?\n");
		}
		try {
			pf.add(2, choices2, 0);
		} catch(IllegalArgumentException e) {
			System.out.print("0% pass\n");
		} finally {
			System.out.print("0% pass?\n");
		}
		try {
			pf.add(2, choices2, 1);
		} catch(IllegalArgumentException e) {
			System.out.print("100% pass\n");
		} finally {
			System.out.print("100% pass?\n");
		}
		System.out.print("Before add(2, [3, 4], 0.1):\n");
		System.out.print(pf);
		pf.add(2, choices2, 0.1);
		System.out.print("After add(2, [3, 4], 0.1):\n");
		System.out.print(pf);
	}

	private static void testAdd() {
		//NullPointerException - if element is null.
		System.out.print("Add Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		Set<Integer> choices2 = new HashSet<Integer>();
		choices2.add(4);choices2.add(3);
		try {
			pf.add(null, choices2);
		} catch(NullPointerException e) {
			System.out.print("Null element pass\n");
		} finally {
			System.out.print("Null element pass?\n");
		}
		System.out.print("Before add(2, [3,4]):\n");
		System.out.print(pf);
		pf.add(2, choices2);
		System.out.print("After add(2, [3,4]):\n");
		System.out.print(pf);		
	}

	private static void testAddLayer2() {
		//NullPointerException - if choices is null.
		//IllegalArgumentException - if choices doesn't have at least one item.
		System.out.print("Add Layer Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		Map<Integer, Double> choices2 = null;
		try {
			pf.addLayer(choices2);
		} catch(NullPointerException e) {
			System.out.print("Null Map pass\n");
		} finally {
			System.out.print("Null Map pass?\n");
		}
		choices2 = new HashMap<Integer, Double>();
		try {
			pf.addLayer(choices2);
		} catch(IllegalArgumentException e) {
			System.out.print("Empty Map pass\n");
		} finally {
			System.out.print("Empty Map pass?\n");
		}
		choices2.put(2, 0.1);choices2.put(3, 0.5);
		try {
			pf.addLayer(choices2);
		} catch(IllegalArgumentException e) {
			System.out.print("Wrong prob sum pass\n");
		} finally {
			System.out.print("Wrong prob sum pass?\n");
		}
		choices2.put(2, 0.25);choices2.put(3, 0.75);
		System.out.print("Before addLayer([[2->0.25], [3->0.75]]):\n");
		System.out.print(pf);
		pf.addLayer(choices2);
		System.out.print("After addLayer([[2->0.25], [3->0.75]]):\n");
		System.out.print(pf);
	}

	private static void testAddLayer() {
		//NullPointerException - if choices is null.
		//IllegalArgumentException - if choices doesn't have at least one item.
		System.out.print("Add Layer Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		Set<Integer> choices2 = null;
		try {
			pf.addLayer(choices2);
		} catch(NullPointerException e) {
			System.out.print("Null Set pass\n");
		} finally {
			System.out.print("Null Set pass?\n");
		}
		choices2 = new HashSet<Integer>();
		try {
			pf.addLayer(choices2);
		} catch(IllegalArgumentException e) {
			System.out.print("Empty Set pass\n");
		} finally {
			System.out.print("Empty Set pass?\n");
		}
		System.out.print("Before addLayer([2, 3]):\n");
		System.out.print(pf);
		choices2.add(2);choices2.add(3);
		pf.addLayer(choices2);
		System.out.print("After addLayer([2, 3]):\n");
		System.out.print(pf);
	}

	private static void testConstructorWithProbs() {
		//NullPointerException - if choices or probs is null.
		//IllegalArgumentException - if there isn't at least one entry in choices,
		//choices contains duplicate entries, 
		//probs does not contain the same number of entries as choices,
		//probs entries do not add up to 1.0 using double addition, 
		//or layers is not at least 1.
		System.out.print("Constructor w/ Probs Test:\n");
		Map<Integer, Double> choices = null;
		int layers = 1;
		try {
			new ProbFunTree<Integer>(choices, layers);
		} catch(NullPointerException e) {
			System.out.print("Null Map pass\n");
		} finally {
			System.out.print("Null Map pass?\n");
		}
		choices = new HashMap<Integer, Double>();
		try {
			new ProbFunTree<Integer>(choices, layers);
		} catch(IllegalArgumentException e) {
			System.out.print("Empty Map pass\n");
		} finally {
			System.out.print("Empty Map pass?\n");
		}
		choices.put(2, 0.1);choices.put(3, 0.5);
		try {
			new ProbFunTree<Integer>(choices, layers);
		} catch(IllegalArgumentException e) {
			System.out.print("Wrong prob sum pass\n");
		} finally {
			System.out.print("Wrong prob sum pass?\n");
		}
		choices.put(2, 0.25);choices.put(3, 0.75);
		layers = 0;
		try {
			new ProbFunTree<Integer>(choices, layers);
		} catch(IllegalArgumentException e) {
			System.out.print("No layers pass\n");
		} finally {
			System.out.print("No layers pass?\n");
		}
		layers = 1;
		System.out.print("Single layer with 2 and 3 at 0.25 and 0.75:\n");
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		System.out.print(pf);
		System.out.print("Two layers with 2 and 3 at 0.25 and 0.75:\n");
		layers = 2;
		ProbFunTree<Integer> pf2 = new ProbFunTree<Integer>(choices, layers);
		System.out.print(pf2);
		System.out.print("Three layers with 2 and 3 at 0.25 and 0.75:\n");
		layers = 3;
		ProbFunTree<Integer> pf3 = new ProbFunTree<Integer>(choices, layers);
		System.out.print(pf3);
		System.out.print("Four layers with 2 and 3 at 0.25 and 0.75:\n");
		layers = 4;
		ProbFunTree<Integer> pf4 = new ProbFunTree<Integer>(choices, layers);
		System.out.print(pf4);		
	}

	private static void testClearHistory() {
		System.out.print("Clear History Test:\n");
		System.out.print("Check by debug\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		for(int i = 0; i < layers; i++) {
			pf.fun();
		}
		pf.clearHistory();
		System.out.print("\n");
	}

	private static void testClone() {
		System.out.print("Clone Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		System.out.print("Original copy:\n");
		System.out.print(pf);
		System.out.print("Copy:\n");
		ProbFunTree<Integer> pf2 = pf.clone();
		System.out.print(pf2);
		System.out.print("\n");
	}

	private static void testSize() {
		System.out.print("Size Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		System.out.print("Size should be 14:\n");
		System.out.print(pf.size());
		System.out.print("\n");
	}

	private static void testParentSize() {
		System.out.print("Parent Size Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		System.out.print("Parent size should be 2:\n");
		System.out.print(pf.parentSize());
		System.out.print("\n");
	}

	private static void testFun() {
		System.out.print("Fun Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);

		List<Integer> elements = new ArrayList<>();
		elements.add(0);elements.add(1);elements.add(1);
		pf.good(elements, 0.9);
		System.out.print("Before fun()\n");
		System.out.print(pf);

		int zzz = 0;
		int zzo = 0;
		int zoz = 0;
		int zoo = 0;
		int ozz = 0;
		int ozo = 0;
		int ooz = 0;
		int ooo = 0;

		int t0 = pf.fun();
		int t1 = pf.fun();
		int t2 = pf.fun();

		for(int  i = 0; i < 10000; i++) {
			if(t0 == 0) {
				if(t1 == 0) {
					if(t2 == 0) {
						zzz++;
					} else {
						zzo++;
					}
				} else {
					if(t2 == 0) {
						zoz++;
					} else {
						zoo++;
					}
				}
			} else {
				if(t1 == 0) {
					if(t2 == 0) {
						ozz++;
					} else {
						ozo++;
					}
				} else {
					if(t2 == 0) {
						ooz++;
					} else {
						ooo++;
					}
				}
			}
			t0 = t1;
			t1 = t2;
			t2 = pf.fun();
		}
		System.out.print("After fun()\n");
		System.out.print("zzz: ");
		System.out.print(zzz);
		System.out.print("\nzzo: ");
		System.out.print(zzo);
		System.out.print("\nzoz: ");
		System.out.print(zoz);
		System.out.print("\nzoo: ");
		System.out.print(zoo);
		System.out.print("\nozz: ");
		System.out.print(ozz);
		System.out.print("\nozo: ");
		System.out.print(ozo);
		System.out.print("\nooz: ");
		System.out.print(ooz);
		System.out.print("\nooo: ");
		System.out.print(ooo);
		System.out.print("\n");
	}

	private static void testBad() {
		//NullPointerException - if elements is null.
		//IllegalArgumentException - if the percent isn't between 0 and 1 exclusive or elements is empty.
		System.out.print("Bad Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);choices.add(2);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);

		List<Integer> elements = null;
		try {
			pf.bad(elements, 0.5);
		} catch(NullPointerException e) {
			System.out.print("Null elements pass\n");
		} finally {
			System.out.print("Null elements pass?\n");
		}
		elements = new ArrayList<>();
		try {
			pf.bad(elements, 0.5);
		} catch(IllegalArgumentException e) {
			System.out.print("Empty elements pass\n");
		} finally {
			System.out.print("Empty elements pass?\n");
		}
		elements.add(2);
		try {
			pf.bad(elements, 0);
		} catch(IllegalArgumentException e) {
			System.out.print("0% pass\n");
		} finally {
			System.out.print("0% pass?\n");
		}
		try {
			pf.bad(elements, 1);
		} catch(IllegalArgumentException e) {
			System.out.print("1% pass\n");
		} finally {
			System.out.print("1% pass?\n");
		}
		System.out.print("Before bad([2], 0.5):\n");
		System.out.print(pf);
		pf.bad(elements, 0.5);
		System.out.print("After bad([2], 0.5):\n");
		System.out.print(pf);

		elements = new ArrayList<>();
		elements.add(0);elements.add(1);elements.add(2);
		System.out.print("Before bad([0,1,2], 0.3):\n");
		System.out.print(pf);
		pf.bad(elements, 0.3);
		System.out.print("After bad([0,1,2], 0.3):\n");
		System.out.print(pf);
	}

	private static void testGood() {
		System.out.print("Good Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);choices.add(2);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);

		List<Integer> elements = null;
		try {
			pf.good(elements, 0.5);
		} catch(NullPointerException e) {
			System.out.print("Null elements pass\n");
		} finally {
			System.out.print("Null elements pass?\n");
		}
		elements = new ArrayList<>();
		try {
			pf.good(elements, 0.5);
		} catch(IllegalArgumentException e) {
			System.out.print("Empty elements pass\n");
		} finally {
			System.out.print("Empty elements pass?\n");
		}
		elements.add(2);
		try {
			pf.good(elements, 0);
		} catch(IllegalArgumentException e) {
			System.out.print("0% pass\n");
		} finally {
			System.out.print("0% pass?\n");
		}
		try {
			pf.good(elements, 1);
		} catch(IllegalArgumentException e) {
			System.out.print("1% pass\n");
		} finally {
			System.out.print("1% pass?\n");
		}
		System.out.print("Before good([2], 0.5):\n");
		System.out.print(pf);
		pf.good(elements, 0.5);
		System.out.print("After good([2], 0.5):\n");
		System.out.print(pf);

		elements = new ArrayList<>();
		elements.add(0);elements.add(1);elements.add(2);
		System.out.print("Before good([0,1,2], 0.3):\n");
		System.out.print(pf);
		pf.good(elements, 0.3);
		System.out.print("After good([0,1,2], 0.3):\n");
		System.out.print(pf);
	}

	private static void testPruneAll2() {
		//IllegalArgumentException - if percent is not between 0.0 and 1.0 (exclusive)
		System.out.print("PruneAll w/ Percent Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);choices.add(2);
		Set<Integer> choices2 = new HashSet<Integer>();
		choices2.add(5);choices2.add(6);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		pf.addToAll(3, choices2, .1);
		pf.addToAll(4, choices2, 0.05);
		try {
			pf.pruneAll(0);
		} catch(IllegalArgumentException e) {
			System.out.print("0% pass\n");
		} finally {
			System.out.print("0% pass?\n");
		}
		try {
			pf.pruneAll(1);
		} catch(IllegalArgumentException e) {
			System.out.print("1% pass\n");
		} finally {
			System.out.print("1% pass?\n");
		}
		System.out.print("Before PruneAll(0.05):\n");
		System.out.print(pf);
		pf.pruneAll(0.05);
		System.out.print("After PruneAll(0.05):\n");
		System.out.print(pf);
		System.out.print("Before PruneAll(0.9):\n");
		System.out.print(pf);
		pf.pruneAll(0.9);
		System.out.print("After PruneAll(0.9):\n");
		System.out.print(pf);
	}

	private static void testPruneAll() {
		System.out.print("PruneAll Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);choices.add(2);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		System.out.print("Before PruneAll():\n");
		System.out.print(pf);
		pf.pruneAll();
		System.out.print("After PruneAll():\n");
		System.out.print(pf);

		Set<Integer> choices2 = new HashSet<Integer>();
		choices2.add(0);choices2.add(1);
		ProbFunTree<Integer> pf2 = new ProbFunTree<Integer>(choices2, layers);	
		System.out.print("Before PruneAll():\n");
		System.out.print(pf2);
		pf.pruneAll();
		System.out.print("After PruneAll():\n");
		System.out.print(pf2);
		Set<Integer> choices3 = new HashSet<Integer>();
		choices3.add(0);choices3.add(1);
		ProbFunTree<Integer> pf3 = new ProbFunTree<Integer>(choices3, layers);	
		Set<Integer> choices4 = new HashSet<Integer>();
		choices4.add(4);choices4.add(3);
		pf3.addToAll(2, choices4, 0.7);
		System.out.print("Before PruneAll():\n");
		System.out.print(pf3);
		pf3.pruneAll();
		System.out.print("After PruneAll():\n");
		System.out.print(pf3);
	}

	private static void testRemoveFromAll() {
		//NullPointerException - if element is null.
		System.out.print("Remove From All Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		try {
			pf.removeFromAll(null);
		} catch(NullPointerException e) {
			System.out.print("Null element pass\n");
		} finally {
			System.out.print("Null element pass?\n");
		}
		System.out.print("Before removeFromAll(1):\n");
		System.out.print(pf);
		pf.removeFromAll(1);
		System.out.print("After removeFromAll(1):\n");
		System.out.print(pf);
		choices.add(2);
		pf = new ProbFunTree<Integer>(choices, layers);
		System.out.print("Before removeFromAll(0):\n");
		System.out.print(pf);
		pf.removeFromAll(0);
		System.out.print("After removeFromAll(0):\n");
		System.out.print(pf);
	}

	private static void testAddToAll2() {
		//NullPointerException - if element is null.
		//IllegalArgumentException - if percent is not between 0.0 and 1.0 (exclusive).
		System.out.print("Add To All w/ Percent Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		Set<Integer> choices2 = new HashSet<Integer>();
		choices2.add(4);choices2.add(3);
		try {
			pf.addToAll(null, choices2, 0.1);
		} catch(NullPointerException e) {
			System.out.print("Null element pass\n");
		} finally {
			System.out.print("Null element pass?\n");
		}
		try {
			pf.addToAll(2, choices2, 0);
		} catch(IllegalArgumentException e) {
			System.out.print("0% pass\n");
		} finally {
			System.out.print("0% pass?\n");
		}
		try {
			pf.addToAll(2, choices2, 1);
		} catch(IllegalArgumentException e) {
			System.out.print("100% pass\n");
		} finally {
			System.out.print("100% pass?\n");
		}
		System.out.print("Before addToAll(2, [3, 4], 0.1):\n");
		System.out.print(pf);
		pf.addToAll(2, choices2, 0.1);
		System.out.print("After addToAll(2, [3, 4], 0.1):\n");
		System.out.print(pf);
	}

	private static void testAddToAll() {
		//NullPointerException - if element is null.
		System.out.print("Add To All Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		Set<Integer> choices2 = new HashSet<Integer>();
		choices2.add(4);choices2.add(3);
		try {
			pf.addToAll(null, choices2);
		} catch(NullPointerException e) {
			System.out.print("Null element pass\n");
		} finally {
			System.out.print("Null element pass?\n");
		}
		System.out.print("Before addToAll(2, [3,4]):\n");
		System.out.print(pf);
		pf.addToAll(2, choices2);
		System.out.print("After addToAll(2, [3,4]):\n");
		System.out.print(pf);
	}

	private static void testClearProbs() {
		System.out.print("Clear Probs Test:\n");
		Set<Integer> choices = new HashSet<Integer>();
		choices.add(0);choices.add(1);
		int layers = 3;
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);	
		Set<Integer> choices2 = new HashSet<Integer>();
		choices2.add(4);choices2.add(3);
		pf.addToAll(2, choices, 0.5);
		System.out.print("Before ClearProbs:\n");
		System.out.print(pf);
		System.out.print("After ClearProbs:\n");
		pf.clearAllProbs();
		System.out.print(pf);
	}

	private static void testConstructor() {
		//NullPointerException - if choices is null.
		//IllegalArgumentException - if there aren't at least two elements in choices orlayers is not at least 1.
		System.out.print("Constructor Test:\n");
		Set<Integer> choices = null;
		int layers = 1;
		try {
			new ProbFunTree<Integer>(choices, layers);
		} catch(NullPointerException e) {
			System.out.print("Null Set pass\n");
		} finally {
			System.out.print("Null Set pass?\n");
		}
		choices = new HashSet<Integer>();
		try {
			new ProbFunTree<Integer>(choices, layers);
		} catch(IllegalArgumentException e) {
			System.out.print("Empty Set pass\n");
		} finally {
			System.out.print("Empty Set pass?\n");
		}
		choices.add(0);choices.add(1);
		layers = 0;
		try {
			new ProbFunTree<Integer>(choices, layers);
		} catch(IllegalArgumentException e) {
			System.out.print("No layers pass\n");
		} finally {
			System.out.print("No layers pass?\n");
		}
		layers = 1;
		System.out.print("Single layer with 0 and 1:\n");
		ProbFunTree<Integer> pf = new ProbFunTree<Integer>(choices, layers);
		System.out.print(pf);
		System.out.print("Two layers with 0 and 1:\n");
		layers = 2;
		ProbFunTree<Integer> pf2 = new ProbFunTree<Integer>(choices, layers);
		System.out.print(pf2);
		System.out.print("Three layers with 0 and 1:\n");
		layers = 3;
		ProbFunTree<Integer> pf3 = new ProbFunTree<Integer>(choices, layers);
		System.out.print(pf3);
		System.out.print("Four layers with 0 and 1:\n");
		layers = 4;
		ProbFunTree<Integer> pf4 = new ProbFunTree<Integer>(choices, layers);
		System.out.print(pf4);
	}

}