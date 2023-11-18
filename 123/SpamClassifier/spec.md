# 123 Classification Tree
___
## Background
The rise of "Machine Learning" and "Artificial Intelligence" has been hard to ignore in the past decade. While the more well-known applications include ChatGPT and Dall-E, there are a number of other uses for these powerful algorithms. These include assisting biologists in the drug discovery process, helping medical professions diagnose and treat diseases early, and relaying wildfire movement to firefighers preventing out of control spread.

In essence, these branches are subsets of Computer Science concerned with using trends from known input data to predict things about unknown and unseen data. Yet, it is important to remember that these algorithms aren't magic with limitless potential - they are simply high fidelity probability models built on a vast amount of input data. This means that your predictions are only as good as the data it is built upon, which can easily be biased in some way - as we've seen before. Thus, it is important to recognize and advocate for appropriate uses of these algorithms, regardless of how miraculous they seem.

## Assignment

Your goal for this assignment is to write a classification tree, a simplistic machine learning model that given some input data will predict some label for it. Below is an example relating to the weather:

TODO: Insert diagram

As seen above, in our classification tree the leaf nodes represent our predictive labels while the intermediary nodes represent a split on some feature of our data. To reach a classification for some input (x), you start at the root of the tree and determine whether the appropriate feature of x falls to the left or right of the split and travel in the corresponding direction. Repeating this process will eventually lead you to a classification for your input. Below we'll trace through an input with our weather example.

TODO: Insert example

In this assignment, you'll be writing a classification tree implementation and using it to predict given some email predict whether it's "spam" or "ham", and potentially extend this to a dataset of your choice! Below is an example of a potential classification tree created by our model.

TODO: Insert example

Note that for simplicity the only feature we'll be exploring is the percentage of certain words within our emails.
___
## Code
There's a decent amount of starter code, so here is a description of each of the classes and how they relate to one another
___
### Split
`Split` is a wrapper class that stores a feature and threshold for any intermediary node within our tree. Outside of the typical getter methods for the above, it has a number of useful methods:

```java
public String getFeature()
```
Returns the feature name without any specific component tied to it. In the case of our email example it would return "wordPercentage" without the specific word tied to it (instead of "wordPercentage~hello")

```java
public boolean evaluate(Classifiable value)
```
Returns whether or not the given value abides by the current split (i.e. whether or not you should travel left or right in a classification tree).

```java
public String toString()
```
Returns a String representation of the given Split. This consists of the feature information on the first line and the threshold on the second.
___
### Classifiable
This is an abstract class that any data we wish to classify must extend. Although there is a reason for it being an abstract class that will be discussed in the creative portion, for all intents you can consider it an interface as it defines three abstract methods that any datapoint must implement.

```java
public abstract double get(String feature);
```
Gets the corresponding value for the given feature. This returns a double since our tree is storing thresholds.

```java
public abstract List<String> getFeatures();
```
Returns a list of all features for a given datapoint. This is useful in determining whether or not this datapoint can be classified by a `Classifier`.

```java
public abstract Split partition(Classifiable other);
```
Returns a partition or `Split` between this datapoint and other. How this computed is  up to the implementer (and a large part of our model).

There also exist a number of useful constants within this class: 
```java
public static final String SPLITTER;
```
In the case that a feature doesn't relate to one specific aspect of our dataset, this String is used to differentiate between the feature name and the specifc aspect of the feature we're interested in. This can be more clearly be seen in the `Email` example discussed below. 

```java
public static final Set<String> DICT;
```
This is a dictionary of English words that exists to potentially be used in a creative extension.

A simple example for all above implementations an be seen in the provided `Email` class. Here the only feature we're concerned with is the percentage of any word contained within the original email appropriately named "wordPercentage". Note that for any split, we need to also keep track of the specific word assigned to it. We do this by appending said word after the feature name with some arbitrary split character (i.e. "wordPercentage~hello" refers to the percentage of "hello"s contained with the email).
___
### Classifier
This is another abstract class that your classification tree will extend. It defines a number of methods that are required to be considered a "classifier":

```java
public boolean canClassify(Classifiable input);
```
Given a piece of classifiable data, returns whether or not this tree is capable of classifying it.

You can imagine that it wouldn't make much sense to try and run an email input through our weather classifier above, which is why this method is useful! More concretely, given a piece of input this method should attempt to classify it and if at any point our current node splits on a feature that doesn't exist in our input, this method should return false.

```java
public String classify(Classifiable input);
```
Given a piece of classifiable data, return the appropriate label that this classifier predicts. If the input is unable to be classified by this classifier, this method should throw an `IllegalArrgumentException`.

This method should model the steps taken in our weather example above: at every point get the corresponding feature from our input data and determine if its less than our threshold. If so, continue left - otherwise continue right. Repeat this process until a leaf node is reached.

```java
public void save(PrintStream ps);
```
Saves this current classifier to the given PrintStream.

For our classification tree, this format should be pre-order. Every intermediary node will print two lines of data (one for feature and one for threshold). For leaf nodes, you should only print the label.

Note that this class also implements a `calculateAccuracy` method that returns the model's accuracy on provided testing data and labels.

You'll also have to implement two constructors for your Classification Tree, not listed in the interface
```java
public ClassificationTree(Scanner sc)
```
Load the classification tree from a file connected to the given Scanner. You may assume that the format of the input file matches that of the `save` method described above. Importantly, in this method you should only read data from the file using `nextLine` and convert it to the appropriate format using `Double.parseDouble`.

```java
public ClassificationTree(List<Classifiable> data, List<String> results)
```
Create a classification tree from the input data and corresponding results. This should be accomplished via the algorithm described below:

1) Traverse through the current classification tree until you reach a leaf node. If the label matches, do nothing (our model is accurate up to this point).
2) If the label is incorrect, create a split between the data used to create the original leaf node* and our current input (the `partition` method from the `Classifiable` interface will be particularly useful).
3) Insert a new intermediary node that uses the previously created split to correctly classify the new data.

*Note that ideally we'd like to keep track of all input data that falls under a specific leaf node such that when creating a new split, we can make sure it's valid for our entire dataset. For simplicity, only worry about the first datapoint used to create a label node.
___
## Creative Portion
For this assignment, there are x recommended creative extensions you can implement:
1) Add more features to the current model.
2) Extend the Classifiable class for a dataset of your choosing.
3) Create a classification forest.

All of these are described more in depth below.
___
### 1. Add more features to the current model.
Our current model isn't horrible, but could use some more information from the original dataset for determining splits. Your job is to add an additional feature (or features if you so choose) to the `Email` class. Some ideas include:

1) Character percentage.
2) Special character percentage.
3) Valid english word percentage (using `Classifiable.DICT`)
4) Your choice!

Note that we recommend whatever feature you add is also a percentage. Doing so will simplify the logic within `partition()` although you are welcome to consider additional features that interest you.

Your additions are also not required to *improve* the model in terms of its overall accuracy. If you find that your addition causes your model to perform worse, we'd encourage you to think about why that might be the case! (Does more data always mean a better model?)
___
### 2. Extend the Classifiable class for a dataset of your choosing.
Note that our `Classifier` can work on anything that extends the `Classifiable` class. Let's try it out with some more interesting data! In this extension you'll take an existing dataset, load it into a list of `Classifiable` objects and see how well our model works. Below is a list of datasets we'd recommend messing around with (although you're welcome to explore whatever interests you)
1) Weather and atmospheric data
2) TODO
3) Your choice!
___
### 3. Create a classification forest
Classification trees are models that tend to overfit to the training data they're built on - you can imagine that a model that creates a split for every single piece of input data will perfectly classify the input data but likely struggle on any unseen datapoints. One way to counteract this is to create something called a forest. Forests average out the results from a many trees, picking the label that appears the most. In this extension, you'll be creating a `ClassificationForest` class that embodies this concept. Namely, you should run an input through a provided number of trees and pick the label that appears most often, breaking ties arbitrarily.

Your new `Classifier` must implement the appropriate interface along with the following constructors:

```java
public ClassificationForest(int n, List<Classifiable> data, List<String> labels)
```
Construct a forest with `n` trees from the provided data and labels. Note that in order for this to be a valid forest of different trees, you must shuffle the data and labels the *same* way between tree construction.

```java
public ClassificationForest(Scanner sc)
```
Construct a forest from a scanner attached to a file. The first line of the file should be the total number of trees within the forest. Everything following is the stored tree data formatted as per the `ClassificationTree`'s `save` method. Your forest's save method should follow this format as well. Note that like our tree's Scanner constructor you should only be reading data from the scanner using `nextLine` and converting it to the approiate data type using `Integer.parseInt`

Once you've created your new model, test out it's accuracy. Does it perform better than a single tree like we'd expect? (It's ok if it doesn't!)