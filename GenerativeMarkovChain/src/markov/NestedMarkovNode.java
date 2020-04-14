package markov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Alexander Johnston
 *         Copyright 2020
 *         A class where a set of elements are picked from randomly to decide which element will be the output of fun()
 *         based on the last returned elements.
 * @param <T> The type of the elements that will be picked from.
 */
public class NestedMarkovNode<T> extends MarkovNode<T> {

	// Contains the first Node that is used to get the initial element
	private Node<T> firstLink;

	// Maps every element to a Node that contains every element
	private Map<T, Node<T>> firstChain;

	// Contains a List with Maps that map the Nodes in the first chain 
	// to a Map from the Node's output elements to new Nodes containing the same element.
	// Each subsequent entry in the list maps the value Nodes from the previous entry
	// to a Map from the Node's elements to new Nodes containing the same element. 
	private List<Map<Node<T>, Map<T, Node<T>>>> chain2;

	// The previous elements returned by fun()
	private LinkedBlockingDeque<T> previousElements;

	/**        Creates a NestedMarkovNode by adding a new chain to node.
	 *         The elements in node will be added by reference.
	 * @param  node as the MarkovNode to add a chain to.
	 * @throws NullPointerException if node is null.
	 */
	public NestedMarkovNode(MarkovNode<T> node) {
		Objects.requireNonNull(node);
		// Invariants secured
		firstLink = node.getFirstLink().clone();
		firstChain = node.getChain();
		if(node instanceof NestedProbFun<?>) {
			chain2 = new ArrayList<Map<Node<T>, Map<T, Node<T>>>>();
			HashMap<Node<T>, Map<T, Node<T>>> hm = new HashMap<Node<T>, Map<T, Node<T>>>();
			for(Entry<T, Node<T>> e : firstChain.entrySet()) {
				HashMap<T, Node<T>> m = new HashMap<T, Node<T>>();
				Set<Entry<T, Node<T>>> s2 = firstChain.entrySet();
				for(Entry<T, Node<T>> n : s2) {
					m.put(n.getKey(), n.getValue().clone());
				}
				hm.put(e.getValue(), m);
			}
			chain2.add(hm);
		} else if(node instanceof NestedMarkovNode<?>) {
			chain2 = ((NestedMarkovNode<T>)node).getChainMap();
			Map<Node<T>, Map<T, Node<T>>> mnmn = chain2.get(chain2.size()-1);
			Map<Node<T>, Map<T, Node<T>>> tempmnmn = new HashMap<Node<T>, Map<T, Node<T>>>();
			for(Entry<Node<T>, Map<T, Node<T>>> enmn : mnmn.entrySet()) {
				Set<T> s = enmn.getValue().keySet();
				for(Entry<T, Node<T>> en : enmn.getValue().entrySet()) {
					Map<T, Node<T>> tempmn = new HashMap<T, Node<T>>();
					for(T t : s) {
						tempmn.put(t, en.getValue().clone());
					}
					tempmnmn.put(en.getValue(), tempmn);
				}
			}
			chain2.add(tempmnmn);
		}
		previousElements = new LinkedBlockingDeque<T>(chain2.size()+2);
	}
	
	/**
	 * @return the List of Maps that compose the chain of this NestedMarkovNode
	 */
	public List<Map<Node<T>, Map<T, Node<T>>>> getChainMap() {
		return chain2;
	}
	
	@Override
	public Node<T> getFirstLink() {
		return firstLink;
	}
	
	@Override
	public Map<T, Node<T>> getChain() {
		return firstChain;
	}

	/**        Adjust the probabilities to make the previousElements more likely to return from fun() in that order.
	 * @param  previousElements as the elements in the order that should appear more often.
	 * @param  percent as the percentage between 0 and 1 exclusive, 
	 *         of the probability to adjust all the nodes that determine the order of elements specified.
	 * @throws NullPointerException if previousElements is null or any of it's elements are null.
	 * @throws IllegalArgumentException if percent isn't between 0 and 1 exclusive, or if previousElements is empty
	 */
	public synchronized void good(List<T> previousElements, double percent) {
		Objects.requireNonNull(previousElements);
		if(previousElements.isEmpty()) {
			throw new IllegalArgumentException("previousElements passed into good() must have at least one element");
		}
		for(T t : previousElements) {
			if(t == null) {
				throw new NullPointerException("The elements in previousElements passed into good() must have non null elements");
			}
		}
		// Invariants secured
		if(previousElements.size() == 1) {
			getTypeAndGood(firstLink, previousElements.get(0), percent);
		} else if(previousElements.size() == 2) {
			getTypeAndGood(firstChain.get(previousElements.get(0)), previousElements.get(1), percent);
			getTypeAndGood(firstLink, previousElements.get(0), percent);
		} else {
			getTypeAndGood(firstChain.get(previousElements.get(0)), previousElements.get(1), percent);
			getTypeAndGood(firstLink, previousElements.get(0), percent);
			Node<T> n = firstChain.get(previousElements.get(0));
			for(int i = 0; i < chain2.size(); i++) {
				n = chain2.get(i).get(n).get(previousElements.get(i+1));
				getTypeAndGood(n, previousElements.get(i+2), percent);
			}
		}
	}

	/**       Gets the type of a Node and calls the good() function.
	 * @param n as the Node to determine the type of and call good().
	 * @param previousElement as the element to make more likely to appear.
	 * @param percent as the percent of the probability of fun() returning previousElement to increase the probability by.
	 */
	private void getTypeAndGood(Node<T> n, T previousElement, double percent) {
		if(n instanceof NestedProbFun<?>) {
			((NestedProbFun<T>)n).good(null, previousElement, percent);
		} else if(n instanceof NestedMarkovNode<?>) {
			getTypeAndGood(n, previousElement, percent);
		} else if(n instanceof ProbFun<?>) {
			((ProbFun<T>)n).good(previousElement, percent);
		}
	}

	/**        Adjust the probabilities to make the list of previousElements less likely to return from fun() in that order.
	 * @param  previousElements as the elements in the order that should appear less often.
	 * @param  percent as the percentage, between 0 and 1 exclusive,
	 *         of the probability to adjust all the nodes that determine the order of elements specified.
	 * @throws NullPointerException if previousElements is null or any of it's elements are null.
	 * @throws IllegalArgumentException if the percent isn't between 0 and 1 exclusive, or if previousElements is empty.
	 */
	public synchronized void bad(List<T> previousElements, double percent) {
		Objects.requireNonNull(previousElements);
		if(previousElements.isEmpty()) {
			throw new IllegalArgumentException("List<T> previousElements passed into bad() must have at least one element");
		}
		for(T t : previousElements) {
			if(t == null) {
				throw new NullPointerException("The elements in previousElements passed into bad() must have non null elements");
			}
		}
		// Invariants secured
		if(previousElements.size() == 1) {
			getTypeAndBad(firstLink, previousElements.get(0), percent);
		} else if(previousElements.size() == 2) {
			getTypeAndBad(firstChain.get(previousElements.get(0)), previousElements.get(1), percent);
			getTypeAndBad(firstLink, previousElements.get(0), percent);
		} else {
			getTypeAndBad(firstChain.get(previousElements.get(0)), previousElements.get(1), percent);
			getTypeAndBad(firstLink, previousElements.get(0), percent);
			Node<T> n = firstChain.get(previousElements.get(0));
			for(int i = 0; i < chain2.size(); i++) {
				n = chain2.get(i).get(n).get(previousElements.get(i+1));
				getTypeAndBad(n, previousElements.get(i+2), percent);
			}
		}
	}
	
	/**       Gets the type of a Node and calls the bad() function.
	 * @param n as the Node to determine the type of and call bad().
	 * @param previousElement as the element to make less likely to appear.
	 * @param percent as the percent of the probability of fun() returning previousElement to decrease the probability by.
	 */
	private void getTypeAndBad(Node<T> n, T previousElement, double percent) {
		if(n instanceof NestedProbFun<?>) {
			((NestedProbFun<T>)n).bad(null, previousElement, percent);
		} else if(n instanceof NestedMarkovNode<?>) {
			getTypeAndGood(n, previousElement, percent);
		} else if(n instanceof ProbFun<?>) {
			((ProbFun<T>)n).bad(previousElement, percent);
		}
	}
	
	/**
	 * @return a randomly picked element from this NestedMarkovNode which is based on the previously returned elements.
	 *         Any changes in the element will be reflected in this NestedMarkovNode.
	 */
	@Override
	public T fun() {
		if(previousElements.isEmpty()) {
			T t = firstLink.fun();
			previousElements.add(t);
			return t;
		} else if(previousElements.size() == 1) {
			Node<T> n = firstChain.get(previousElements.peekLast());
			T t = n.fun();
			previousElements.add(t);
			return t;
		} else if(previousElements.size()-1 == chain2.size()) {
			int size = previousElements.size()-2;
			T lt = previousElements.removeLast();
			Node<T> n = chain2.get(size).get(firstChain.get(previousElements.peekLast())).get(lt);
			T t = n.fun();
			previousElements.remove();
			previousElements.add(lt);
			previousElements.add(t);
			return t;
		} else {
			int size = previousElements.size()-2;
			int isize = size;
			Iterator<T> it = previousElements.iterator();
			Node<T> n = chain2.get(size-isize).get(firstChain.get(it.next())).get(it.next());
			isize--;
			while(isize != -1) {
				n = chain2.get(isize).get(n).get(it.next());
				isize--;
			}
			T t = n.fun();
			previousElements.add(t);
			return t;
		}
	}
	
	/**       Private constructor for clone().
	 * @param nestedMarkvoNode as the NestedMarkvoNode to copy.
	 */
	private NestedMarkovNode(NestedMarkovNode<T> nestedMarkvoNode) {
		this.firstLink = nestedMarkvoNode.getFirstLink().clone();
		firstChain = new HashMap<T, Node<T>>(); 
		chain2 = new ArrayList<Map<Node<T>, Map<T, Node<T>>>>();
		for(Entry<T, Node<T>> e : nestedMarkvoNode.getChain().entrySet()) {
			this.firstChain.put(e.getKey(), e.getValue().clone());
		}
		for(Map<Node<T>, Map<T, Node<T>>> s : nestedMarkvoNode.getChainMap()) {
			Map<Node<T>, Map<T, Node<T>>> tm2 = new HashMap<Node<T>, Map<T, Node<T>>>();
			for(Entry<Node<T>, Map<T, Node<T>>> e : s.entrySet()) {
				Map<T, Node<T>> tm = new HashMap<T, Node<T>>();
				for(Entry<T, Node<T>> e2 : e.getValue().entrySet()) {
					tm.put(e2.getKey(),e2.getValue().clone());
				}
				tm2.put(e.getKey().clone(), tm);
			}
			chain2.add(tm2);
		}
	}

	@Override
	public Node<T> clone() {
		return new NestedMarkovNode<T>(this);
	}

	@Override
	public String toString() {
		if(id == 0) {
			id = System.identityHashCode(this);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Nested markov node:\n");
		sb.append(firstLink.toString());
		sb.append(" -> ");
		for(Entry<T, Node<T>> m : firstChain.entrySet()) {
			sb.append(m.getValue().toString());
			sb.append(" when ");
			sb.append(m.getKey().toString());
			sb.append("\n");
			sb.append(" and ");
		}
		sb.delete(sb.length()-4, sb.length());
		for(Map<Node<T>, Map<T, Node<T>>> m : chain2) {
			sb.append(" -> ");
			for(Entry<Node<T>, Map<T, Node<T>>> e : m.entrySet()) {
				sb.append(e.getKey().toString());
				sb.append("     -> ");
				for(Entry<T, Node<T>> l : e.getValue().entrySet()) {
					sb.append(l.getValue().toString());
					sb.append("     when ");
					sb.append(l.getKey().toString());
					sb.append("\n");
					sb.append("     and ");
				}
				sb.delete(sb.length()-9, sb.length());
				sb.append("  -> ");
			}
		}
		sb.delete(sb.length()-5, sb.length());
		sb.append("\n");
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