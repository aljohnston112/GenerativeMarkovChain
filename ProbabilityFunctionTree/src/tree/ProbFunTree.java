package tree;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 *         A tree node where a set of elements are picked from randomly to decide which child will randomly produce the next element when fun() is called.
 * @author Alexander Johnston
 * @since  Copyright 2020
 * @param  <T> The type of the elements that will be picked from
 */
public class ProbFunTree<T> implements Serializable {

	private static final long serialVersionUID = -6556634307811294014L;

	// The set of elements to be picked from, mapped to the probabilities of getting picked 
	private Map<T, Double> probMap = new TreeMap<T, Double>();

	// The set of elements to be picked from, mapped to the probabilities of getting picked 
	private Map<T, ProbFunTree<T>> children = new TreeMap<T, ProbFunTree<T>>();

	private ProbFunTree<T> parent = null;

	private T previousElement = null;

	private int id = 0;

	private int layers;

	private double roundingError = 0;

	/**        Creates a ProbFunTree where there is an equal chance of getting any element from choices when fun() in called.
	 *         Note that the elements in choices passed into this constructor will NOT be copied and will be added by reference.
	 * @param  choices as the choices to be randomly picked from.
	 * @param  layers as the number of layers for this ProbFunTree to generate.
	 *         Ex: for choices[0, 1] and layers=2, the following data structure will be made,
	 *         <br>{@literal [[0->0.5][1->0.5]]} where the first choice is propagated like so 
	 *         {@literal [[0->[[0->0.5][1->0.5]]][1->[[0->0.5][1->0.5]]]]}.
	 * @throws NullPointerException if choices is null.
	 * @throws IllegalArgumentException if there isn't at least one element in choices, or 
	 *         layers is not at least 1.
	 */
	public ProbFunTree(Set<T> choices, int layers){
		Objects.requireNonNull(choices);
		if(choices.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 element in the choices passed to the ProbFunTree constructor\n");
		if(layers < 1) {
			throw new IllegalArgumentException("layers passed into the ProbFunTree constructor must be at least 1");
		}
		// Invariants secured
		this.layers = layers;
		for(T choice : choices) {
			this.probMap.put(choice, 1.0/choices.size());
		}
		fixProbSum();
		layers--;
		if(layers != 0) {
			for(T t: probMap.keySet()) {
				children.put(t, new ProbFunTree<T>(probMap.keySet(), layers, this));
			}
		}
	}

	/**        Creates a ProbFunTree where getting a key from this ProbFunTree's fun() function is based on the probability values.
	 *         The entries in probMap passed into this constructor will NOT be copied and will be added by reference.
	 * @param  probMap as the Object-probability pairs where the probabilities must add up to 1.0 using double addition.
	 * @param  layers as the number of layers for this ProbFunTree to generate.
	 *         Ex: for probMap {@literal [0->0.25, 1->0.75]} and layers=2, the following data structure will be made,
	 *         <br>{@literal [[0->0.25][1->0.75]]} where the first choice is propagated like so 
	 *         {@literal [[0->[[0->0.25][1->0.75]]][1->[[0->0.25][1->0.75]]]]}.
	 * @throws NullPointerException if probMap is null.
	 * @throws IllegalArgumentException if there isn't at least one entry in probMap, 
	 *         probMap values do not add up to 1.0 using double addition, or 
	 *         layers is not at least 1.
	 */
	public ProbFunTree(Map<T, Double> probMap , int layers){
		Objects.requireNonNull(probMap);
		if(probMap.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 entry in the probMap passed to the ProbFunTree constructor\n");
		if(layers < 1) {
			throw new IllegalArgumentException("layers passed into the ProbFunTree constructor must be at least 1");
		}
		double sum = 0;
		for(double d : probMap.values()) {
			sum += d;
		}
		if(sum != 1.0) {
			throw new IllegalArgumentException("probMap values must add up to 1.0 using double addition "
					+ "when passed to the ProbFunTree constructor\n");
		}
		// Invariants secured
		this.layers = layers;
		for(Entry<T, Double> choice : probMap.entrySet()) {
			this.probMap.put(choice.getKey(), choice.getValue());
		}
		fixProbSum();
		layers--;
		if(layers != 0) {
			for(T t: probMap.keySet()) {
				children.put(t, new ProbFunTree<T>(probMap, layers, this));
			}
		}
	}

	/**        Private constructor for tracking parent nodes in the ProbFunTree.
	 * @param  choices as the elements for the ProbFunTree to generate.
	 * @param  layers as the number of layers to make the ProbFunTree.
	 * @param  parent as the parent node in the ProbFunTree.
	 * @throws NullPointerException if choices is null.
	 * @throws IllegalArgumentException if there isn't at least one element in choices, or 
	 *         layers is not at least 1.	 
	 */
	private ProbFunTree(Set<T> choices, int layers, ProbFunTree<T> parent){
		Objects.requireNonNull(choices);
		if(choices.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 element in the choices passed to the ProbFunTree constructor\n");
		if(layers < 1) {
			throw new IllegalArgumentException("layers passed into the ProbFunTree constructor must be at least 1");
		}
		// Invariants secured
		this.layers = layers;
		this.parent = parent;
		for(T choice : choices) {
			this.probMap.put(choice, 1.0/choices.size());
		}
		fixProbSum();
		layers--;
		if(layers != 0) {
			for(T t: probMap.keySet()) {
				children.put(t, new ProbFunTree<T>(probMap.keySet(), layers, this));
			}
		}
	}

	/**        Private constructor for tracking parent nodes in the ProbFunTree.
	 * @param  probMap as the Object-probability pairs where the probabilities must add up to 1.0 using double addition.
	 * @param  layers as the number of layers to make the ProbFunTree.
	 * @param  parent as the parent node in the ProbFunTree.
	 * @throws NullPointerException if probMap is null.
	 * @throws IllegalArgumentException if there isn't at least one entry in probMap, 
	 *         probMap entries do not add up to 1.0 using double addition, or
	 *         layers is not at least 1.
	 */
	private ProbFunTree(Map<T, Double> probMap , int layers, ProbFunTree<T> parent){
		Objects.requireNonNull(probMap);
		if(probMap.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 entry in the probMap passed to the ProbFunTree constructor\n");
		if(layers < 1) {
			throw new IllegalArgumentException("layers passed into the ProbFunTree constructor must be at least 1");
		}
		double sum = 0;
		for(double d : probMap.values()) {
			sum += d;
		}
		if(sum != 1.0) {
			throw new IllegalArgumentException("probMap values must add up to 1.0 using double addition "
					+ "when passed to the ProbFunTree constructor\n");
		}
		// Invariants secured
		this.layers = layers;
		this.parent = parent;
		for(Entry<T, Double> choice : probMap.entrySet()) {
			this.probMap.put(choice.getKey(), choice.getValue());
		}
		fixProbSum();
		layers--;
		if(layers != 0) {
			for(T t: probMap.keySet()) {
				children.put(t, new ProbFunTree<T>(probMap, layers, this));
			}
		}
	}

	/**        the Map of element-probability pairs that make up the parent of this ProbFunTree. 
	 *         Any changes in the returned Map will be reflected in this ProbFunTree.
	 * @return the Map of element-probability pairs that make up the parent of this ProbFunTree. 
	 *         Any changes in the returned Map will be reflected in this ProbFunTree.
	 */
	public Map<T, Double> getParentMap() {
		return probMap;
	}

	/**        Returns the Map of element-ProbFunTree pairs that make up the children of this ProbFunTree. 
	 *         Any changes in the returned Map will be reflected in this ProbFunTree.
	 * @return the Map of element-ProbFunTree pairs that make up the children of this ProbFunTree. 
	 *         Any changes in the returned Map will be reflected in this ProbFunTree.
	 */
	public Map<T, ProbFunTree<T>> getChildMap() {
		return children;
	}

	/** Scales the probabilities so they add up to 1.0.
	 * 
	 */
	private void scaleProbs() {
		double scale = 1.0/probSum();
		Set<Entry<T, Double>> probabilities = probMap.entrySet();
		for(Entry<T, Double> e : probabilities) {
			e.setValue(e.getValue()*scale);
		}	
		fixProbSum();
	}

	/**
	 * @return the sum of all the probabilities in order to fix rounding error.
	 */
	private double probSum() {
		Collection<Double> probabilities = probMap.values();
		double sum = 0;
		for(Double d : probabilities) {
			sum += d;
		}
		return sum;
	}

	/** Fixes rounding error in the probabilities by adding up the probabilities 
	 *  and changing the first probability so all probabilities add up to 1.0.
	 */
	private void fixProbSum() {
		Entry<T, Double> firstProb = this.probMap.entrySet().iterator().next();
		roundingError  = 1.0-probSum();
		firstProb.setValue(firstProb.getValue() + roundingError);		
	}

	/** Sets the probabilities to there being an equal chance of getting any element from this ProbFunTree
	 * 
	 */
	public void clearProbs() {
		probMap = (new ProbFunTree<T>(probMap.keySet().stream().collect(Collectors.toSet()), 1)).probMap;
		for(ProbFunTree<T> p : children.values()) {
			p.clearProbs();
		}
	}

	/** Due to propagation of past values, history may not produce favorable results, therefore this method
	 *  clears the history, but not the probabilities produced by feedback,
	 *  so the next generation is way more likely to produce favorable results
	 *  after good() or bad() have been called one or more times.
	 */
	public void clearHistory() {
		this.previousElement = null;
		for(ProbFunTree<T> t : children.values()) {
			t.clearHistory();
		}
	}

	/**        Adds an element to the parent of this ProbFunTree, making the probability equal to 1.0/n
	 *         where n is the number of elements contained in the parent of this ProbFunTree.
	 * @param  element as the element to add to the parent of this ProbFunTree.
	 * @throws NullPointerException if element is null.
	 */
	private void add(T element) {
		Objects.requireNonNull(element);
		// Invariants secured
		double probability = 1.0/(probMap.size());
		if(!probMap.containsKey(element)) {
			probMap.put(element, probability);
		}
		scaleProbs();
	}

	/**        Adds an element to every node in this ProbFunTree, making the probability equal to 1.0/n
	 *         where n is the number of elements contained in this ProbFunTree.
	 *         If the element already exists in a node, then the element will not be updated.
	 *         In order to overwrite old element probabilities, you must remove the element using removeFromAll().
	 * @param  element as the element to add to every node.
	 * @throws NullPointerException if element is null.
	 */
	public void addToAll(T element) {
		Objects.requireNonNull(element);
		// Invariants secured
		if(!children.isEmpty() && !children.containsKey(element)) {
			children.put(element, new ProbFunTree<>(probMap.keySet(), layers-1, this));
		}
		add(element);
		for(ProbFunTree<T> p : children.values()) {
			p.addToAll(element);
		}
	}

	/**        Adds an element to the parent of this ProbFunTree with the specified probability.
	 * @param  element as the element to add to the parent of this ProbFunTree.
	 * @param  percent, between 0 and 1 exclusive, as the chance of the parent of this ProbFunTree returning element.
	 * @throws NullPointerException if element is null.
	 * @throws IllegalArgumentException if percent is not between 0.0 and 1.0 (exclusive).
	 */
	private void add(T element, double percent) {
		Objects.requireNonNull(element);
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to add() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		double scale = (1.0-percent);
		Set<Entry<T, Double>> probabilities = probMap.entrySet();
		for(Entry<T, Double> e : probabilities) {
			if(!e.getKey().equals(element)) {
				e.setValue(e.getValue()*scale);
			}
		}
		if(!probMap.containsKey(element)) {
			probMap.put(element, percent);
		}
		scaleProbs();
	}

	/**        Adds an element to every node in this ProbFunTree with the specified probability.
	 *         If the element already exists in a node, then the element will not be updated.
	 *         In order to overwrite old element probabilities, you must remove the element using removeFromAll().
	 * @param  element as the element to add to every node.
	 * @param  percent between 0 and 1 exclusive, as the chance of 
	 *         the parent and all it's children of this ProbFunTree returning element.
	 * @throws NullPointerException if element is null.
	 * @throws IllegalArgumentException if percent is not between 0.0 and 1.0 (exclusive).
	 */
	public void addToAll(T element, double percent) {
		Objects.requireNonNull(element);
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to add() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		if(!children.isEmpty() && !children.containsKey(element)) {
			children.put(element, new ProbFunTree<>(probMap.keySet(), layers-1, this));
		}
		add(element, percent);
		for(ProbFunTree<T> p : children.values()) {
			p.addToAll(element, percent);
		}

	}

	/**        Adds elements to a new layer that will be added to this ProbFunTree's descendants that have the greatest depth,
	 *         making the probability of each element added equal to 1.0/n, 
	 *         where n is the number of elements in choices.
	 * @param  choices as the elements to add to this ProbFunTree's descendants that have the greatest depth.
	 * @throws NullPointerException if choices is null.
	 * @throws IllegalArgumentException if choices doesn't have at least one item.
	 */
	public void addLayer(Set<T> choices) {
		Objects.requireNonNull(choices);
		if(choices.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 element in the choices passed to addLayer\n");
		// Invariants secured
		addLayer(this, choices);
		incrementLayer();
	}

	private void incrementLayer() {
		if(!children.isEmpty()) {
			layers++;
			for(ProbFunTree<T> pf : children.values()) {
				pf.incrementLayer();
			}
		}
	}

	/**        Adds elements to a new layer that will be added to this ProbFunTree's descendants that have the greatest depth,
	 *         making the probability of each element added equal to 1.0/n, 
	 *         where n is the number of elements in choices.
	 * @param  parent as the parent ProbFunTree to check the children of 
	 *         to see if they are the descendants that have the greatest depth.
	 *         If they are, the layer will be added.
	 * @param  choices as the elements to add to this ProbFunTree's descendants that have the greatest depth.
	 * @throws NullPointerException if choices or parent is null.
	 * @throws IllegalArgumentException if choices doesn't have at least one item.
	 */
	private void addLayer(ProbFunTree<T> parent, Set<T> choices) {
		Objects.requireNonNull(parent);
		Objects.requireNonNull(choices);
		if(choices.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 element in the choices passed to addLayer\n");
		// Invariants secured
		for(ProbFunTree<T> child : parent.children.values()) {
			if(child.children.isEmpty()) {
				for(T t : child.probMap.keySet()) {
					child.children.put(t, new ProbFunTree<T>(choices, 1, parent));
				}
			} else {
				addLayer(child, choices);
			}
		}
	}

	/**        Adds probMap keys to a new layer that will be added to this ProbFunTree's descendants that have the greatest depth,
	 *         making the probability of each element added equal to the values in probMap.
	 * @param  probMap as the Object-probability pairs where the probabilities must add up to 1.0 using double addition.
	 * @throws NullPointerException if probMap is null.
	 * @throws IllegalArgumentException if probMap doesn't have at least one entry, 
	 *         or probMap values aren't under 1.0 using double addition.
	 */
	public void addLayer(Map<T, Double> probMap) {
		Objects.requireNonNull(probMap);
		if(probMap.size() < 1) 
			throw new IllegalArgumentException("Must have at least 1 entry in the probMap passed to addLayer\n");
		double sum = 0;
		for(double d : probMap.values()) {
			sum += d;
		}
		if(sum != 1.0) {
			throw new IllegalArgumentException("probMap must have values that add up to 1.0 using double addition when passed to addLayer\n");
		}
		// Invariants secured
		addLayer(this, probMap);
		incrementLayer();
	}

	/**        Adds probMap keys to a new layer that will be added to this ProbFunTree's descendants that have the greatest depth,
	 *         making the probability of each element added equal to the probability values in probMap.
	 * @param  probMap as the Object-probability pairs where the probabilities must add up to 1.0 using double addition.
	 * @throws NullPointerException if parent or probMap is null.
	 * @throws IllegalArgumentException if probMap doesn't have at least one entry, 
	 *         or probMap values don't add up to 1.0 using double addition.
	 */
	private void addLayer(ProbFunTree<T> parent, Map<T, Double> probMap) {
		Objects.requireNonNull(probMap);
		Objects.requireNonNull(parent);
		if(probMap.size() < 1) {
			throw new IllegalArgumentException("Must have at least 1 element in the choices passed to addLayer\n");
		}
		double sum = 0;
		for(double d : probMap.values()) {
			sum += d;
		}
		if(sum != 1.0) {
			throw new IllegalArgumentException("probMap must have values that add up to 1.0 using double addition when passed to addLayer\n");
		}
		// Invariants secured
		for(ProbFunTree<T> child : parent.children.values()) {
			if(child.children.isEmpty()) {
				for(T t : child.probMap.keySet()) {
					child.children.put(t, new ProbFunTree<T>(probMap, 1, parent));
				}
			} else {
				addLayer(child, probMap);
			}
		}
	}

	/**        Removes an element from the parent of this ProbFunTree unless there is only one element.
	 * @param  element as the element to remove from the parent of this ProbFunTree.
	 * @return True if this ProbFunTree's parent contained the element and it was removed, else false.
	 * @throws NullPointerException if element is null.
	 */
	private boolean remove(T element) {
		Objects.requireNonNull(element);
		if(parentSize() == 1) {
			return false;
		}
		// Invariants secured
		if(probMap.remove(element) == null) {
			return false;
		} else {
			children.remove(element);
		}
		scaleProbs();
		return true;
	}

	/**        Removes an element from every node in this ProbFunTree.
	 *         If an node in the tree has only 1 element, then element will not be removed that that specific node.
	 * @param  element as the element to remove from every node in this ProbFunTree.
	 * @throws NullPointerException if element is null.
	 */
	public void removeFromAll(T element) {
		Objects.requireNonNull(element);
		if(parentSize() != 1) {
			remove(element);
		}
		// Invariants secured
		for(ProbFunTree<T> p : children.values()) {
			p.removeFromAll(element);
		}
	}

	/** Removes elements with the lowest probability from the parent of this ProbFunTree.
	 *  If elements have the same maximum probability of occurring, no elements will be removed.
	 *  If, after a removal, elements have the same maximum probability of occurring, no more elements will be removed.	 
	 *  If parentSize() == 1, no elements will be removed.
	 *  If parentSize() == 1 after a removal, no more elements will be removed.
	 */
	private void purge() {
		double min = probMap.values().stream().parallel().min(Double::compare).orElse(-1.0);
		double max = probMap.values().stream().parallel().max(Double::compare).orElse(-1.0);
		if(max == min) {
			return;
		}
		if(min == -1) {
			return;
		} else if(parentSize() == 1) {
			return;
		} else {
			Set<Entry<T, Double>> probabilities = probMap.entrySet();
			Iterator<Entry<T, Double>> it = probabilities.iterator();
			Entry<T, Double> e;
			while(it.hasNext()) {
				e = it.next();
				if(e.getValue() <= min && e.getValue() < max-roundingError) {
					it.remove();
					children.remove(e.getKey());
					if(parentSize() == 1) {
						scaleProbs();
						return;
					}
				}
			}
			scaleProbs();
		}
	}

	/** Removes elements with the lowest probabilities in this ProbFunTree.
	 *  If elements have the same maximum probability of occurring, no elements will be removed.
	 *  If, after a removal, elements have the same maximum probability of occurring, no elements will be removed.	
	 *  If a node has one element, no elements will be removed.
	 *  If a node has one element after a removal, no more elements will be removed.
	 */
	public void purgeAll() {
		purge();
		for(ProbFunTree<T> p : children.values()) {
			p.purgeAll();
		}
	}

	/**        Removes elements with probabilities less than or equal to percent chance of occurring
	 *         from this ProbFunTree. 
	 *         If a node has one element, no elements will be removed.
	 *         If a node has one element after a removal, no more elements will be removed.
	 * @param  percent as the upper limit, inclusive, of the probability of elements being returned to be removed from this ProbFunTree.
	 * @throws IllegalArgumentException if percent is not between 0.0 and 1.0 (exclusive)
	 */
	private void purge(double percent) {
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to pruge() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		double max = probMap.values().stream().parallel().max(Double::compare).orElse(-1.0);
		double min = probMap.values().stream().parallel().min(Double::compare).orElse(-1.0);
		if(parentSize() == 1 || (max <= percent && min == max)) {
			return;
		} else {
			for(Entry<T, Double> e : probMap.entrySet()) {
				if(e.getValue() <= min && e.getValue() < max-roundingError) {
					remove(e.getKey());
					if(parentSize() == 1) {
						scaleProbs();
						return;
					}
				}
			}
			scaleProbs();
		}
	}

	/**        Removes elements with probabilities less than or equal to percent of being generated
	 *         from this ProbFunTree. 
	 *         If a node has one element, no elements will be removed.
	 *         If a node has one element after a removal, no more elements will be removed.
	 * @param  percent as the upper limit, inclusive, of the probability of elements being returned to be removed from this ProbFunTree.
	 * @throws IllegalArgumentException if percent is not between 0.0 and 1.0 (exclusive)
	 */
	public void purgeAll(double percent) {
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to pruge() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		if(parentSize() != 1) {
			purge(percent);
		} 
		for(ProbFunTree<T> p : children.values()) {
			p.purgeAll(percent);
		}
	}

	/**        Adjust the probability to make element more likely to be returned when fun() is called from the parent of this ProbFunTree.
	 * @param  element as the element to make appear more often
	 * @param  percent as the percentage between 0 and 1 (exclusive), 
	 *         of the probability of getting element to add to the probability.
	 * @return the adjusted probability.
	 * @throws NullPointerException if element is null.
	 * @throws IllegalArgumentException if the percent isn't between 0 and 1 exclusive.
	 */
	public synchronized double good(T element, double percent) {
		Objects.requireNonNull(element);
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to good() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		Double oldProb = probMap.get(element);
		double add;
		if(oldProb > 0.5) 
			add = ((1.0-oldProb)*percent);
		else 
			add = (oldProb*percent);
		if(oldProb+add >= (1.0-roundingError))
			return oldProb;
		double goodProbability = oldProb+add;
		probMap.put(element, goodProbability);
		double leftover = 1.0-goodProbability;
		double sumOfLeftovers = probSum() - goodProbability;
		double leftoverScale = leftover/sumOfLeftovers;
		for(Entry<T, Double> e : probMap.entrySet()) {
			e.setValue(e.getValue()*leftoverScale);
		}
		probMap.put(element, goodProbability);
		fixProbSum();
		return probMap.get(element);
	}

	/**        Adjust the probabilities to make the elements more likely to be returned when fun() is called
	 *         in the order they appear in elements.
	 * @param  elements as the elements to make appear more often in the order they should appear in.
	 * @param  percent as the percentage between 0 and 1 (exclusive), 
	 *         of the probabilities of getting the elements to add to the probabilities.
	 * @throws NullPointerException if elements is null.
	 * @throws IllegalArgumentException if the percent isn't between 0 and 1 exclusive
	 *         or elements is empty.
	 */
	public synchronized void good(List<T> elements, double percent) {
		Objects.requireNonNull(elements);
		if(elements.isEmpty()){
			throw new IllegalArgumentException("elements passed to good() must not be empty");
		}
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to good() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		List<T> l = new ArrayList<>();
		for(T t : elements) {
			l.add(t);
		}
		good(l.get(0), percent);
		if(l.size() == 1) {
			return;
		}
		children.get(l.remove(0)).good(l, percent);
	}

	/**        Adjust the probability to make element less likely to be returned when fun() is called from the parent of this ProbFunTree.
	 * @param  element as the element to make appear less often
	 * @param  percent as the percentage between 0 and 1 (exclusive), 
	 *         of the probability of getting element to subtract from the probability.
	 * @return the adjusted probability.
	 * @throws NullPointerException if element is null.
	 * @throws IllegalArgumentException if the percent isn't between 0 and 1 exclusive.
	 */
	public synchronized double bad(T element, double percent) {
		Objects.requireNonNull(element);
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to good() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		Double oldProb = probMap.get(element);
		double sub = (oldProb*percent);
		if(oldProb-sub <= roundingError)
			return oldProb;
		double badProbability = oldProb-sub;
		probMap.put(element, badProbability);
		double leftover = 1.0-badProbability;
		double sumOfLeftovers = probSum() - badProbability;
		double leftoverScale = leftover/sumOfLeftovers;
		for(Entry<T, Double> e : probMap.entrySet()) {
			e.setValue(e.getValue()*leftoverScale);
		}
		probMap.put(element, badProbability);
		fixProbSum();
		return probMap.get(element);
	}

	/**        Adjust the probabilities to make the elements less likely to be returned when fun() is called
	 *         in the order they appear in elements.
	 * @param  elements as the elements to make appear less often in the order they should not appear in.
	 * @param  percent as the percentage between 0 and 1 (exclusive), 
	 *         of the probabilities of getting the elements to subtract from the probabilities.
	 * @throws NullPointerException if elements is null.
	 * @throws IllegalArgumentException if the percent isn't between 0 and 1 exclusive 
	 *         or elements is empty.
	 */
	public synchronized void bad(List<T> elements, double percent) {
		Objects.requireNonNull(elements);
		if(elements.isEmpty()){
			throw new IllegalArgumentException("elements passed to bad() must not be empty");
		}
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to bad() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		List<T> l = new ArrayList<>();
		for(T t : elements) {
			l.add(t);
		}
		bad(l.get(0), percent);
		if(l.size() == 1) {
			return;
		}
		children.get(l.remove(0)).bad(l, percent);
	}

	/**        Returns a randomly picked element from this ProbFunTree, based on the previously returned elements.
	 * @return a randomly picked element from this ProbFunTree.
	 *         Any changes in the element will be reflected in this ProbFunTree.
	 */
	public T fun() {
		ArrayList<T> previousElements = new ArrayList<T>();
		if(previousElement == null) {
			return nextValue();
		} else if(!children.isEmpty()) {
			ProbFunTree<T> pf = children.get(previousElement);
			T t = pf.previousElement;
			while(t != null) {
				previousElements.add(t);
				if(!pf.children.isEmpty()) {
					pf = pf.children.get(t);
					t = pf.previousElement;
				} else {
					t = null;
				}
			}
			Iterator<T> it = previousElements.iterator();
			if(previousElements.size() == layers-1 && it.hasNext()) {
				this.previousElement = it.next();
				pf = children.get(previousElement);
				while(it.hasNext()) {
					pf.previousElement = it.next();
					pf = pf.children.get(pf.previousElement);
				}
				return pf.fun();
			} else {
				return children.get(previousElement).fun();
			}
		} else {
			return nextValue();
		}
	}

	/**        For generating the next value.
	 * @return the next generated value.
	 */
	private T nextValue() {
		
		double randomChoice = ThreadLocalRandom.current().nextDouble();
		double sumOfProbabilities = 0.0;
		Iterator<Entry<T, Double>> entries = probMap.entrySet().iterator();
		T element = null;
		Entry<T, Double> e;
		while((randomChoice > sumOfProbabilities)) {
			e = entries.next();
			element = e.getKey();
			sumOfProbabilities += e.getValue();
		} 
		previousElement = element;	
		return element;
	}

	/**        Returns the number of elements in the parent of this ProbFunTree.
	 * @return the number of elements in the parent of this ProbFunTree.
	 */
	public int parentSize() {return probMap.size();}

	/**        Returns the number of elements in this whole ProbFunTree, 
	 *         which includes the number of elements in the parent plus the number of elements in every descendant.
	 * @return the number of elements in this whole ProbFunTree, 
	 *         which includes the number of elements in the parent plus the number of elements in every descendant.
	 */
	public int size() {
		int size = parentSize();
		for(ProbFunTree<T> p : children.values()) {
			size += p.size();
		}
		return size;
	}

	/**       Private copy constructor for clone
	 * @param probFunTree as the ProbFunTree to copy
	 */
	private ProbFunTree(ProbFunTree<T> probFunTree) {
		for(Entry<T, Double> s : probFunTree.probMap.entrySet()) {
			this.probMap.put(s.getKey(), s.getValue());
		}
		for(Entry<T, ProbFunTree<T>> e : probFunTree.children.entrySet()) {
			this.children.put(e.getKey(), e.getValue().clone());
		}
		if(!children.isEmpty()){
			for(ProbFunTree<T> e : children.values()) {
				e.parent = this;
			}
		}
	}

	@Override
	public ProbFunTree<T> clone() {
		return new ProbFunTree<T>(this);
	}

	@Override
	public String toString() {
		if(id == 0) {
			id = System.identityHashCode(this);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("PF ");
		sb.append(id);
		sb.append(": [");
		for(Entry<T, Double> e : probMap.entrySet()) {
			sb.append("[");
			sb.append(e.getKey());
			sb.append(" = ");
			sb.append(e.getValue()*100.0);
			sb.append("%]");
		}
		if(!children.isEmpty()) {
			sb.append("]\n");
			for(int i = 0; i < 15; i++) {
				sb.append(" ");
			}
		}
		if(parent != null) {
			for(int i = 0; i < 31; i++) {
				sb.append(" ");
			}
		}
		sb.append("Children: [");
		int count = 0;
		for(Entry<T, ProbFunTree<T>> e : children.entrySet()) {
			count++;
			sb.append("[");
			sb.append(e.getKey());
			sb.append(" = ");
			sb.append(e.getValue());
			sb.delete(sb.length()-1, sb.length());
			sb.append("]\n");
			if(parent != null && parent.parent != null && count == children.size()) {
				sb.delete(sb.length()-2, sb.length());
				sb.append("\n");
			}
			for(int i = 0; i < 26; i++) {
				sb.append(" ");
			}
			if(parent != null) {
				for(int i = 0; i < 31; i++) {
					sb.append(" ");
				}
			}
		}
		if(children.isEmpty()) {
			sb.delete(sb.length()-11, sb.length());

			if(parent != null) {
				sb.delete(sb.length()-31, sb.length());
			}
			sb.append("]");

			sb.append("\n");
		} else {
			sb.delete(sb.length()-28, sb.length());
			if(parent != null) {
				sb.delete(sb.length()-31, sb.length());
			}
			sb.append("]]");
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		if(id == 0) {
			id = System.identityHashCode(this);
		}
		return id;
	}

}