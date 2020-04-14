package markov;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Alexander Johnston
 *         Copyright 2020
 *         A class where a set of elements are picked from randomly to decide the next element that will be the output of fun()
 * @param <T> The type of the elements that will be picked from
 */
public class ProbFun<T> extends Node<T> {

	// The random number generator
	private ThreadLocalRandom tlr = ThreadLocalRandom.current();

	// The set of elements to be picked from, mapped to the probabilities of getting picked 
	private Map<T, Double> chain = new TreeMap<T, Double>();

	/**        Creates a ProbFun where there is an equal chance of getting any element from choices when fun() in called.
	 *         Note that the elements in choices passed into this constructor will NOT be copied and will be added by reference.
	 * @param  choices as the choices to be randomly picked from.
	 * @throws NullPointerException if choices is null.
	 * @throws IllegalArgumentException if there aren't at least two elements in choices.
	 */
	public ProbFun(Set<T> choices){
		Objects.requireNonNull(choices);
		if(choices.size() < 2) 
			throw new IllegalArgumentException("Must have more than 1 element in the choices passed to the ProbFun constructor\n");
		// Invariants secured
		for(T choice : choices) {
			this.chain.put(choice, 1.0/choices.size());
		}
		fixProbSum();
	}

	/**        Creates a ProbFun where the chance of getting any element from choices when fun() is called
	 *         is specified by the probabilities.
	 *         The elements in choices will NOT be copied and will be added by reference.
	 * @param  choices as the choices to be randomly picked from.
	 * @param  probabilities as the probability of getting each element in choices.
	 *         The probabilities MUST add up to 1.0 using double addition.
	 * @throws NullPointerException if choices or probabilities is null.
	 * @throws IllegalArgumentException if there isn't at least one element in choices 
	 *         or the probabilities don't add up to 1.0 using double addition.
	 */
	public ProbFun(Set<T> choices, double[] probabilities){
		Objects.requireNonNull(choices);
		Objects.requireNonNull(probabilities);
		if(choices.size() < 1 || probabilities.length < 1) 
			throw new IllegalArgumentException("Must have more than 1 element in the List<T> choices passed to the ProbFun constructor\n");
		if(choices.size() != probabilities.length) 
			throw new IllegalArgumentException("The List the the probability array passed to the ProbFun constructor must have the same number length\n");
		// Invariants secured
		double sum = 0;
		for(double d : probabilities) {
			sum += d;
		}
		if(sum != 1.0) { 
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < probabilities.length; i++) {
				sb.append((probabilities[i]));
				sb.append("\n");
			}
			throw new IllegalArgumentException("Probabilities don't add up to 1.0\n" + sb.toString());
		}
		Iterator<T> it = choices.iterator();
		for(int i = 0; i < choices.size(); i++) 
			this.chain.put( it.next(), probabilities[i]);
	}

	/**
	 * @return the Map of element-probability pairs that back this ProbFun. 
	 *         Any changes in the returned Map will be reflected in this ProbFun.
	 */
	@Override
	Map<T, Double> getChain() {
		return chain;
	}

	/** Scales the probabilities so they add up to 1.0.
	 * 
	 */
	private void scaleProbs() {
		double scale = 1.0/probSum();
		Set<Entry<T, Double>> probabilities = chain.entrySet();
		for(Entry<T, Double> e : probabilities) {
			e.setValue(e.getValue()*scale);
		}	
		fixProbSum();
	}

	/**
	 * @return the sum of all the probabilities in order to fix rounding error.
	 */
	private double probSum() {
		Collection<Double> probabilities = chain.values();
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
		Entry<T, Double> firstProb = this.chain.entrySet().iterator().next();
		firstProb.setValue(firstProb.getValue() + 1.0-probSum());		
	}

	/**        Gets the probability of this ProbFun returning element.
	 * @param  element as the element in this ProbFun to get the probability of it showing up.
	 * @return the an optional containing the probability of this ProbFun returning element.
	 * @throws NullPointerException if element is null.
	 */
	public Optional<Double> getProb(T element) {
		Objects.requireNonNull(element);
		return chain.containsKey(element) ? Optional.of(chain.get(element)) : Optional.empty();
	}

	/**
	 * @return the probabilities of getting the choices from this ProbFun.
	 */
	public double[] getProbs() {
		double[] probabilities = new double[this.chain.size()];
		Iterator<Double> d = chain.values().iterator();
		for(int i = 0; i < this.chain.size(); i++) 
			probabilities[i] = d.next();
		return probabilities;
	}

	/**        Update the probabilities of the choices in this ProbFun.
	 * @param  probabilities as the new probabilities of getting the choices from this ProbFun.
	 * @throws NullPointerException if probabilities is null.
	 * @throws IllegalArgumentException if the probabilities don't add up to 1.0
	 *         or probabilities is not the same length as the size of this ProbFun.
	 */
	public void setProbs(double[] probabilities) {
		Objects.requireNonNull(probabilities);
		if(chain.size() != probabilities.length) { 
			throw new IllegalArgumentException("Wrong number of probabilities");
		}
		// Invariants secured
		double sum = 0;
		for(double d : probabilities) {
			sum += d;
		}
		if(sum != 1.0) { 
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < probabilities.length; i++) {
				sb.append((probabilities[i]));
				sb.append("\n");
			}
			throw new IllegalArgumentException("Probabilities don't add up to 1.0\n" + sb.toString());
		}
		Set<T> keys = chain.keySet();
		Iterator<T> it = keys.iterator();
		for(int i = 0; i < keys.size(); i++) 
			chain.put(it.next(), probabilities[i]);
	}

	/** Sets the probabilities to there being an equal chance of getting any element from this ProbFun
	 * 
	 */
	public void clearProbs() {
		chain = (new ProbFun<T>(chain.keySet().stream().collect(Collectors.toSet()))).chain;
	}

	/**        Adds an element to this ProbFun, making the probability equal to 1.0/n
	 *         where n is the number of elements contained in this ProbFun.
	 * @param  element as the element to add.
	 * @throws NullPointerException if element is null.
	 */
	public void add(T element) {
		Objects.requireNonNull(element);
		// Invariants secured
		double probability = 1.0/(chain.size());
		chain.put(element, probability);
		scaleProbs();
	}

	/**        Adds an element to this ProbFun with the specified probability.
	 * @param  element as the element to add.
	 * @param  percent, between 0 and 1 exclusive, as the chance of this ProbFun returning element.
	 * @throws NullPointerException if element is null.
	 * @throws IllegalArgumentException if percent is not between 0.0 and 1.0 (exclusive).
	 */
	public void add(T element, double percent) {
		Objects.requireNonNull(element);
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to add() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		double scale = (1.0-percent);
		Set<Entry<T, Double>> probabilities = chain.entrySet();
		for(Entry<T, Double> e : probabilities) {
			e.setValue(e.getValue()*scale);
		}	
		chain.put(element, percent);
		scaleProbs();
	}

	/**        Removes an element from this ProbFun.
	 * @param  element as the element to remove.
	 * @return True if this probFun contained the element and it was removed, else false.
	 * @throws NullPointerException if element is null.
	 * @throws IllegalStateException if this ProbFun only has 2 elements.
	 */
	public boolean remove(T element) {
		Objects.requireNonNull(element);
		if(size() == 2) {
			throw new IllegalStateException("Can't remove another element from this ProbFun");
		}
		// Invariants secured
		if(chain.remove(element) == null) {
			return false;
		}
		scaleProbs();
		return true;
	}

	/** Removes elements with the lowest probability.
	 *  If size() == 2, no elements will be removed.
	 *  If size() == 2 after a removal, no more elements will be removed.
	 */
	public void purge() {
		double min = chain.values().stream().parallel().min(Double::compare).orElse(-1.0);
		if(min == -1) {
			return;
		} else if(size() == 2) {
			return;
		} else {
			Set<Entry<T, Double>> probabilities = chain.entrySet();
			for(Entry<T, Double> e : probabilities) {
				if(e.getValue() <= min) {
					remove(e.getKey());
					if(size() == 2) {
						scaleProbs();
						return;
					}
				}
			}
			scaleProbs();
		}
	}
	
	/**        Removes elements with probabilities less than or equal to the percent passed in. 
	 *         If size() == 2, no elements will be removed.
	 *         If size() == 2 after a removal, no more elements will be removed.
	 * @param  percent as the upper limit, inclusive, of the probability of elements to be removed.
	 * @throws IllegalArgumentException if percent is not between 0.0 and 1.0 (exclusive)
	 */
	public void purge(double percent) {
		if(percent >= 1.0 || percent <= 0.0) {
			throw new IllegalArgumentException("percent passed to pruge() is not between 0.0 and 1.0 (exclusive)");
		}
		// Invariants secured
		if(size() == 2) {
			return;
		} else {
			Map<T, Double> m = chain.entrySet().stream().
				    sorted(Entry.comparingByValue()).
				    collect(Collectors.toMap(Entry::getKey, Entry::getValue,
				                             (e1, e2) -> e1, LinkedHashMap::new));
			for(Entry<T, Double> e : m.entrySet()) {
				if(e.getValue() <= percent) {
					remove(e.getKey());
					if(size() == 2) {
						scaleProbs();
						return;
					}
				}
			}
			scaleProbs();
		}
	}

	/**        Adjust the probabilities to make element more likely to be returned when fun() is called.
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
		Double oldProb = chain.get(element);
		double add;
		if(oldProb > 0.5) 
			add = ((1.0-oldProb)*percent);
		else 
			add = (oldProb*percent);
		// TODO choose a value that won't cause the other probabilities to go to 0.0
		if(oldProb+add >= 1.0)
			return oldProb;
		double goodProbability = oldProb+add;
		chain.put(element, goodProbability);
		double leftover = 1.0-goodProbability;
		double sumOfLeftovers = probSum() - goodProbability;
		double leftoverScale = leftover/sumOfLeftovers;
		for(Entry<T, Double> e : chain.entrySet()) {
			e.setValue(e.getValue()*leftoverScale);
		}
		chain.put(element, goodProbability);
		fixProbSum();
		return chain.get(element);
	}

	/**        Adjust the probabilities to make the element less likely to be returned when fun() is called.
	 * @param  element as the element to make appear less often.
	 * @param  percent as the percentage, between 0 and 1 exclusive, 
	 *         of the probability of getting the element to subtract from the probability. 
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
		Double oldProb = chain.get(element);
		double sub = (oldProb*percent);
		if(oldProb-sub <= Double.MIN_VALUE)
			return oldProb;
		double badProbability = oldProb-sub;
		chain.put(element, badProbability);
		double leftover = 1.0-badProbability;
		double sumOfLeftovers = probSum() - badProbability;
		double leftoverScale = leftover/sumOfLeftovers;
		for(Entry<T, Double> e : chain.entrySet()) {
			e.setValue(e.getValue()*leftoverScale);
		}
		chain.put(element, badProbability);
		fixProbSum();
		return chain.get(element);
	}

	/**
	 * @return a randomly picked element from this ProbFun.
	 *         Any changes in the element will be reflected in this ProbFun.
	 */
	@Override
	public T fun() {
		double randomChoice = tlr.nextDouble();
		double sumOfProbabilities = 0.0;
		Iterator<Entry<T, Double>> entries = chain.entrySet().iterator();
		T previousElement = null;
		Entry<T, Double> e;
		while((randomChoice > sumOfProbabilities)) {
			e = entries.next();
			previousElement = e.getKey();
			sumOfProbabilities += e.getValue();
		} 
		return previousElement;
	}

	/**
	 * @return the number of elements in this ProbFun.
	 */
	public int size() {return chain.size();}

	/**       Private copy constructor for clone
	 * @param probFun as the ProbFun to copy
	 */
	private ProbFun(ProbFun<T> probFun) {
		for(Entry<T, Double> s : probFun.getChain().entrySet()) {
			this.chain.put(s.getKey(), s.getValue());
		}
	}

	@Override
	public ProbFun<T> clone() {
		return new ProbFun<T>(this);
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
		for(Entry<T, Double> e : chain.entrySet()) {
			sb.append("[");
			sb.append(e.getKey());
			sb.append(" = ");
			sb.append(e.getValue()*100.0);
			sb.append("%]");
		}
		sb.append("]\n");
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