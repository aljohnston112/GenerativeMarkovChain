

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Alexander Johnston
 *         Copyright 2019
 *         A class where a group of Objects are picked from randomly to decide which Object will be the output of fun()
 * @param <T> The type of the Objects that will be picked from
 */
public class ProbFun<T> implements Serializable {

	private static final long serialVersionUID = 879228293146606500L;

	/**          
	 * @GuardedBy(this)
	 */
	private int previousIndex = -1;

	/**          The group of Objects to be picked from
	 * @GuardedBy(this)
	 */
	private List<T> choices = new ArrayList<T>();

	/**          The probabilities of picking each Object
	 * @GuardedBy(this)
	 */
	private double[] probabilities;

	/**        Creates a ProbFun where there is an equal chance of getting any Object from choices when fun() in called.
	 *         Note that an arrays passed into this constructor via the List<T> choices will NOT be copied and will be added by reference
	 * @param  choices as the choices to be randomly picked from
	 * @throws NullPointerException if List<T> choices is null
	 * @throws IllegalArgumentException if there isn't at least one element in List<T> choices
	 */
	public ProbFun(List<T> choices){
		Objects.requireNonNull(choices);
		if(choices.size() < 1) 
			throw new IllegalArgumentException("Must have more than 1 object in the List passed to the ProbFun constructor\n");
		for(int i = 0; i < choices.size(); i++) {
			this.choices.add(choices.get(i));
		}
	}

	/**        Creates a ProbFun where the chance of getting any Object from choices when fun() in called, is specified by the double[] probabilities
	 *         Note that an arrays passed into this constructor via the List<T> choices will NOT be copied and will be added by reference
	 * @param  choices as the choices to be randomly picked from
	 * @param  probabilities as the probability of getting each Object in List<T> choices
	 * @throws NullPointerException if List<T> choices or double[] probabilities is null
	 * @throws IllegalArgumentException if there isn't at least one element in List<T> choices or double[] probabilities
	 */
	public ProbFun(List<T> choices, double[] probabilities){
		Objects.requireNonNull(choices);
		Objects.requireNonNull(probabilities);
		if(choices.size() < 1 || probabilities.length < 1) 
			throw new IllegalArgumentException("Must have more than 1 object in the List<T> choices passed to the ProbFun constructor\n");
		if(choices.size() != probabilities.length) 
			throw new IllegalArgumentException("The List the the probability array passed to the ProbFun constructor must have the same number length\n");
		for(int i = 0; i < choices.size(); i++) 
			this.choices.add(choices.get(i));
		this.probabilities = new double[probabilities.length];
		for(int i = 0; i <probabilities.length; i++) 
			this.probabilities[i] = probabilities[i];
	}

	/**
	 * @return the number of Objects in this ProbFun
	 */
	public synchronized int size() {return (choices == null) ? 0 : choices.size();}

	/**
	 * @return the index from the choices array that was chosen last time fun() was called, if called, else -1
	 */
	public synchronized int previousIndex() {return previousIndex;}

	/** Sets the probabilities to there being an equal chance of getting any object from this ProbFun
	 * 
	 */
	public void clearProbs() {this.probabilities = null; getProbs();}

	/**        Gets the Object from the specified index
	 *         Note that the Object returned will be by reference and any changes in the structure will be reflected in this ProbFun
	 * @param  index as the index of the Object in this ProbFun
	 * @return the Optional Object from the specified index
	 * @throws IndexOutOfBoundsException if the int index is out of bounds
	 */
	public synchronized Optional<T> get(int index) {
		if(index < 0 || index >= this.choices.size())
			throw new IndexOutOfBoundsException(String.format("Index %d is not an index in this ProbFun\n", index));
		return ((index > 0) && (index <choices.size())) ?
				Optional.of(choices.get(index)) : Optional.empty();}

	/**
	 * @return a double[] with the probabilities of getting any one of the choices from this ProbabilityFunction
	 */
	public double[] getProbs() {
		double probability;
		double[] probabilities;
		synchronized (this) {
			probabilities = new double[this.choices.size()];
			if(this.probabilities != null) {
				for(int i = 0; i <this.probabilities.length; i++) 
					probabilities[i] = this.probabilities[i];
				return probabilities;
			}
			probability = 1.0/this.choices.size();
		}
		for(int i = 0; i < probabilities.length-1; i++) {
			probabilities[i] = probability;
		}
		// Needed to get the probabilities to add to 1.0
		double sum = 0;
		for(int i = 0; i < probabilities.length-1; i++) {
			sum += probabilities[i];
		}
		probabilities[probabilities.length-1] = 1.0-sum;
		synchronized (this) {
			this.probabilities = new double[probabilities.length];
			for(int i = 0; i <probabilities.length; i++) 
				this.probabilities[i] = probabilities[i];
		}
		return probabilities;
	}

	/**        Update the probabilities of getting any one of the choices from this ProbabilityFunction
	 * @param  probabilities as the new probabilities of getting any one of the choices from this ProbabilityFunction
	 * @throws IllegalArgumentException if the probabilities don't add up to 1.0 or the double[] probabilities is not the same length as the size of this ProbFun
	 */
	public void setProbs(double[] probabilities) {
		synchronized (this) {
			if(choices.size() != probabilities.length) 
				throw new IllegalArgumentException("Wrong number of probabilities");
		}
		double sum = 0;
		for(int i = 0; i < probabilities.length; i++) {
			sum += probabilities[i];
		}
		if(sum != 1.0) { 
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < probabilities.length; i++) {
				sb.append((probabilities[i]));
				sb.append("\n");
			}
			throw new IllegalArgumentException("Probabilities don't add up to 1.0\n" + sb.toString());
		}
		synchronized(this){
			this.probabilities = new double[probabilities.length];
			for(int i = 0; i <probabilities.length; i++) 
				this.probabilities[i] = probabilities[i];
		}
	}

	/**       Adds an Object to this ProbFun, making the probability equal to 1.0/n where n is the number of objects contained in this ProbFun
	 * @param object as the Object to add
	 */
	public void add(T object) {
		double probability;
		synchronized (this) {
			probability = 1.0/(choices.size()+1);
			choices.add(object);
		}
		double[] probabilities;
		synchronized (this) {
			probabilities = new double[this.probabilities.length+1];
			for(int i = 0; i < this.probabilities.length; i++) {
				probabilities[i] = this.probabilities[i];
			}
		}
		probabilities[probabilities.length-1] = probability;
		double sum = 0;
		for(int i = 0; i < probabilities.length; i++) {
			sum += probabilities[i];
		}
		double scale = 1.0/sum;
		for(int i = 0; i < probabilities.length; i++) {
			probabilities[i] *=scale;
		}
		// Needed to get the probabilities to add to 1.0
		sum = 0;
		for(int i = 0; i < probabilities.length-1; i++) {
			sum += probabilities[i];
		}
		probabilities[probabilities.length-1] = 1.0-sum;
		setProbs(probabilities);
	}

	/**        Removes an Object from this ProbFun 
	 * @param  index as the index to remove
	 * @throws IndexOutOfBoundsException if the int index is out of bounds
	 */
	public void remove(int index) {
		synchronized (this) {
			if((index < 0) || (index >= choices.size())) 
				throw new IndexOutOfBoundsException(String.format("Index, %f, passed to remove(), is not in this ProbFun\n", index));
			choices.remove(index);
		}
		double[] probabilities;
		synchronized (this) {
			probabilities = new double[this.probabilities.length-1];
			for(int i = 0, j = 0; i < probabilities.length; i++, j++) {
				if(i != index) {
					probabilities[i] = this.probabilities[j];
				} else {
					j++;
				}
			}
		}
		double sum = 0;
		for(int i = 0; i < probabilities.length; i++) {
			sum += probabilities[i];
		}
		double scale = 1.0/sum;
		for(int i = 0; i < probabilities.length; i++) {
			probabilities[i] *=scale;
		}
		// Needed to get the probabilities to add to 1.0
		sum = 0;
		for(int i = 0; i < probabilities.length-1; i++) {
			sum += probabilities[i];
		}
		probabilities[probabilities.length-1] = 1.0-sum;
		setProbs(probabilities);
	}

	/**        Adjust the probabilities to make the Object at previousIndex() more likely to be returned when fun() is called
	 * @param  index as the index to make appear more often
	 * @param  percent as the percentage (0.0-1.0; non inclusive) of the probability of getting the Object at the specified index
	 *         to add to the probability 
	 * @throws IllegalArgumentException if the percent isn't between 0 and 1 exclusive
	 * @throws IndexOutOfBoundsException if the index is out of bounds
	 */
	public void good(int index, double percent) {
		synchronized (this) {
			if(choices.size() <= 1) {return;}
			if(percent >= 1.0 || percent <= 0.0) {
				throw new IllegalArgumentException("double percent passed to good() is not between 0.0 and 1.0 (exclusive)");
			}
			if((index < 0) && (index >= choices.size())) {
				throw new IndexOutOfBoundsException(String.format("Index, %f, passed to good(int index, double percent) is not in bounds\n", index));
			}
		}
		double[] probabilities = getProbs();
		double add;
		double goodProbability;
		double sum = 0;
		synchronized (this) {
			if(this.probabilities[index] > 0.5) 
				add = ((1.0-this.probabilities[index])*percent);
			else 
				add = (this.probabilities[index]*percent);
			if(this.probabilities[index]+add >= 1.0)
				return;
			goodProbability = this.probabilities[index]+add;
			probabilities[index] = goodProbability;
			double leftover = 1.0-goodProbability;
			for(int i = 0; i < probabilities.length; i++) {
				sum += probabilities[i];
			}
			double sumOfLeftovers = sum - goodProbability;
			double leftoverScale = leftover/sumOfLeftovers;
			for(int i = 0; i < probabilities.length; i++) {
				probabilities[i] = this.probabilities[i]*leftoverScale;
			}
		}
		probabilities[index] = goodProbability;
		// Needed to get the probabilities to add to 1.0
		sum = 0;
		for(int i = 0; i < probabilities.length-1; i++) {
			sum += probabilities[i];
		}
		probabilities[probabilities.length-1] = 1.0-sum;
		setProbs(probabilities);
		System.out.print(probabilities[index]);
		System.out.print("\n");
	}

	/**        Adjust the probabilities to make the Object at the specified index less likely to be returned when fun() is called
	 * @param  percent as the percentage (0.0-1.0; non inclusive) of the probability of getting the Object at the specified index
	 *         to subtract from the probability 
	 * @param  index as the index to make appear less often
	 * @throws IllegalArgumentException if the percent isn't between 0 and 1 exclusive
	 * @throws IndexOutOfBoundsException if the index is out of bounds
	 */
	public void bad(int index, double percent) throws RuntimeException {
		synchronized (this) {
			if(choices.size() <= 1) {return;}
			if(percent >= 1.0 || percent <= 0.0) 
				throw new IllegalArgumentException("Percentage passed to bad() is not between 0.0 and 1.0 (exclusive)");
			if((index < 0) && (index >= choices.size())) 
				throw new IndexOutOfBoundsException(String.format("Index, %f, passed to bad() is not in the choice array bounds", index));
		}
		double[] probabilities = getProbs();
		double badProbability;
		synchronized (this) {
			double sub = (this.probabilities[index])*(percent);
			if((this.probabilities[index]-sub) < (Double.MIN_VALUE))
				return;
			badProbability = this.probabilities[index]-sub;
		}
		probabilities[index] = badProbability;
		double leftover = 1.0-badProbability;
		double sum = 0;
		for(int i = 0; i < probabilities.length; i++) {
			sum += probabilities[i];
		}
		double sumOfLeftovers = sum - badProbability;
		double leftoverScale = leftover/sumOfLeftovers;
		for(int i = 0; i < probabilities.length; i++) {
			probabilities[i] *= leftoverScale;
		}
		probabilities[index] = badProbability;
		// Needed to get the probabilities to add to 1.0
		sum = 0;
		for(int i = 0; i < probabilities.length-1; i++) {
			sum += probabilities[i];
		}
		probabilities[probabilities.length-1] = 1.0-sum;
		setProbs(probabilities);
		System.out.print(probabilities[index]);
		System.out.print("\n");
	}

	/**
	 * @return a randomly picked Object from this ProbFun
	 */
	public T fun() {
		double randomChoice = ThreadLocalRandom.current().nextDouble();
		double sumOfProbabilities = 0.0;
		synchronized (this) {
			if(probabilities != null) {
				for(int k = 0; (randomChoice > sumOfProbabilities); k++) {
					previousIndex = k;
					sumOfProbabilities += probabilities[k];
				} 
			} else 
				previousIndex = (int)Math.round(randomChoice*(choices.size()-1));
			return choices.get(previousIndex);
		}
	}

}