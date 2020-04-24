# ProbabilityFunctionTree

A Java project for creating probability function trees, which are trees that contain a probability function. Nodes can be added, in which case, each Object in the ProbFunTree is mapped to another probability function. This means that after fun() is called and returns the result of the probability function, that result will be used to choose the next probability function when fun() is called. ProbFunTrees have methods to respond to feedback, add or remove elements, and free up memory by pruning the lowest probability elements. 

Other methods are in development that will provide ways to search for sequences in the tree and add elements to the nodes underneath, or make a new node with the elements if there isn't a node underneath. 

Other classes are in development that will allow ProbFunTrees to be built using supplied data.

TL;DR: The idea for this project came from tinkering with Markov Chains. Essentially, it is a Markov Chain where the elements in the chain are map to other Markov Chains.
