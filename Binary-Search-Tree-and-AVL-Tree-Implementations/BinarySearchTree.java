//package lxc220045;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.Scanner;

public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T> {
    static class Entry<T> {
        T element;
        Entry<T> left, right;

        // Constructor for Entry in BST tree
        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;
        }
    }

    // this class would store all the details of the BST tree which would be used when validating AVL
    static class BSTEntryObject<T extends Comparable<? super T>> {
        boolean isvalid;
        T rightMin;
        T leftMax;

        public BSTEntryObject(boolean isvalid) {
            this.isvalid = isvalid;
            this.rightMin = null;
            this.leftMax = null;
        }

    // Constructor for the BST tree with storing default minimum and maximum values
        public BSTEntryObject(boolean isvalid, T rMin, T lMax) {
            this.isvalid = isvalid;
            this.rightMin = rMin;
            this.leftMax = lMax;
        }
    }

    Entry<T> root;
    int size;
    Deque<Entry<T>> stack;
    // define stack

    public BinarySearchTree() {
        root = null;
        size = 0;
    }

	// this function is called by find(e) would create a stack which would store the path from the root to the parent of the node
    public Entry<T> find(Entry<T> t, T x){
        stack = new ArrayDeque<Entry<T>>();
        Entry<T> temp = t;
        if(temp == null || temp.element == x){
            return temp;
        }
        while(true){
            if(x.compareTo(temp.element)<0){
                if(temp.left == null){
                    break;
                }
                stack.add(temp);
                temp = temp.left;
            }
            else if(x.compareTo(temp.element)==0){
                break;
            }
            else if(temp.right==null){
                break;
            }
            else{
                stack.add(temp);
                temp=temp.right;
            }
        }
        return temp;
    }

    // finding element e in BST
    public Entry<T> find(T e){
        return find(root, e);
    }
    /** TO DO: Is x contained in tree?
     */
    public boolean contains(T x) {
        Entry<T> t = find(root, x);
        if(t!=null && x.compareTo(t.element)==0){
            return true;
        }
        return false;
    }


    // addding node with element x in BST
    public boolean add(T x) {
        Entry<T> temp = root;
        Entry<T> prev = root;
        if(root == null){
            root = new Entry(x, null, null);
            return true;
        }
        while(temp!=null){
            if(x.compareTo(temp.element)<0){
                prev = temp;
                temp = temp.left;
            }
            else if(x.compareTo(temp.element)>0){
                prev = temp;
                temp = temp.right;
            }
            else if(x.compareTo(temp.element)==0){
                return false;
            }
        }
        if(x.compareTo(prev.element)<0){
            prev.left = new Entry(x, null, null);
        }
        else{
            prev.right = new Entry(x, null, null);
        }
        size++;
        return true;
    }

    // after removing a node, we do splice based on which node the removal is performed
    public void splice(Entry<T> node){
        Entry<T> parent;
        Entry<T> child = node.left==null? node.right: node.left;
        if(stack.isEmpty()){
            root = child;
            return;
        }
        parent = stack.getLast();
        if(parent.left!=null &&parent.left.element == node.element){
                parent.left = child;
        }
        else{
            parent.right = child;
        }
    }

    
    // removing node from BST
    public T remove(T x) {
        // tree is empty
        if(size == 0 || root == null){
            return null;
        }
        Entry<T> t = find(root, x);
        // node with element x is not present in the tree
        if(t.element.compareTo(x) != 0){
            return null;
        }
        // node has either left or right subtree
        if(t.left == null || t.right == null){
            splice(t);
        }
        // node has both children
        else{
            Entry<T> minRight = find(t.right, x);
            stack.push(t);
            t.element = minRight.element;
            splice(minRight);
        }
        size --;
        return x;
    }


    public Iterator<T> iterator() {
        return null;
    }

    // Finding minimum in the tree which will be the leftmost node in the left subtree
    public T min() {
        if(root == null){
            return null;
        }
        Entry<T> t = root;
        while(t.left != null){
            t = t.left;
        }
        return t.element;
    }

    // max element will be the rightmost element in the right subtree
    public T max() {
        if(root == null)
        {
            return null;
        }
        Entry<T> t = root;
        while(t.right != null){
            t = t.right;
        }
        return t.element;
    }

    // finds the largest key that is no bigger than x.  Returns null if there is no such key.
    public T floor(T x) {
        if(size == 0 || root == null){
            return null;
        }
        Entry<T> nodeFromTree = find(x);
        if(nodeFromTree == null) {
             return null;
        }
        if(nodeFromTree.element.compareTo(x) == 0){  
            return nodeFromTree.element;
        }
        else if(nodeFromTree.element.compareTo(x) < 0){
            return nodeFromTree.element;
        }
        else {
            for (Entry<T> nodeEntry : stack) {
                if(nodeEntry == null)   break;
                if(nodeEntry.element.compareTo(x) < 0){
                    return nodeEntry.element;
                }
            }
        }
        return null;
    }

    // finds smallest key that is no smaller than x.  Returns null if there is no such key.
    public T ceiling(T x) {
        if(size == 0 || root == null){
            return null;
        }
        Entry<T> nodeFromTree = find(x);
        if(nodeFromTree == null) {
             return null;
        }
        if(nodeFromTree.element.compareTo(x) == 0){  
            return nodeFromTree.element;
        }
        else if(nodeFromTree.element.compareTo(x) > 0){
            return nodeFromTree.element;
        }
        else {
            for (Entry<T> nodeEntry : stack) {
                if(nodeEntry == null)   break;
                if(nodeEntry.element.compareTo(x) > 0){
                    return nodeEntry.element;
                }
            }
        }
        return null;
    }

    // finds predecessor of x.  If x is not in the tree, return floor(x).  Return null if there is no such key.
    public T predecessor(T x) {
        Entry<T> t;
        t = find(root, x);
        if(t.element != x){
            return null;
        }
        Entry<T> pred;
        if(t.left!=null){
            pred = find(t.left, x);
            return pred.element;
        }
        while(!stack.isEmpty()){
            Entry<T> stackTop = stack.pop();
            if(x!=stackTop.element && x.compareTo(stackTop.element)>0){
                return stackTop.element;
            }
        }
        return null;
    }

    //finds successor of x.  If x is not in the tree, return ceiling(x).  Return null if there is no such key.
    public T successor(T x) {
        Entry<T> t;
        t = find(root, x);
        if(t.element != x){
            return null;
        }
        Entry<T> pred;
        if(t.left!=null){
            pred = find(t.left, x);
            return pred.element;
        }
        while(!stack.isEmpty()){
            Entry<T> stackTop = stack.pop();
            if(x!=stackTop.element && x.compareTo(stackTop.element)>0){
                return stackTop.element;
            }
        }
        return null;
    }

   //  Creating an array with the elements using in-order traversal of tree
    public Comparable[] toArray() {
        Comparable[] arr = new Comparable[size];
        /* write code to place elements in array here */
        int i = 0;
        Entry<T> node = root;
        while(node != null && i<size) {
            node = node.left;
            arr[i]= node.element;
            i++;
            node = node.right;
        }
        return arr;
    }


    // validating BST
    public BSTEntryObject<T> validateBST(Entry<T> node) {
        if(node == null)
            return new BSTEntryObject<T>(true, null, null);
        // to validate BST, we need to make sure that the maximum in the left subtree is less than the root
        // and minimum from the right subtree is greater than  the element present at the root
        T rightMin = node.element;
        T leftMax = node.element;

        if(node.left != null) {
            BSTEntryObject<T> leftSubtree = validateBST(node.left);
            if(leftSubtree.leftMax.compareTo(node.element) > 0){
            leftMax = leftSubtree.leftMax;
            }
            if(!leftSubtree.isvalid || leftMax.compareTo(node.element) > 0)  
            {
                // violating the conditions for BST
                return new BSTEntryObject<T>(false, rightMin, leftMax);   
            }
        }

        if(node.right != null) {
            BSTEntryObject<T> rightSubtree = validateBST(node.right);
            if(rightSubtree.rightMin.compareTo(node.element) < 0){
                 rightMin = rightSubtree.rightMin;
            }
            if(!rightSubtree.isvalid || rightMin.compareTo(node.element) < 0){
                 // violating the conditions for BST
                return new BSTEntryObject<T>(false, rightMin, leftMax);
             } 
        }

        // storing true in case of valid BST
        return new BSTEntryObject<T>(true, rightMin, leftMax);
    }

    public boolean verify() {
        if(size == 0){
            // empty trees are valid
            return true;   
         } 

        BSTEntryObject<T> treeObject = validateBST(root);
        // returns the value stored in the BST Object
        return treeObject.isvalid;
    }
	

    public static void main(String[] args) throws FileNotFoundException {
        BinarySearchTree<Long> bst = new BinarySearchTree<>();
        Scanner sc;
        if (args.length > 0) {
            File file = new File(args[0]);
            sc = new Scanner(file);
        } else {
            sc = new Scanner(System.in);
        }
        String operation = "";
        long operand = 0;
        int modValue = 999983;
        long result = 0;
        // Initialize the timer
        Timer timer = new Timer();
        while (!((operation = sc.next()).equals("End"))) {
            switch (operation) {
                case "Add":
                    operand = sc.nextInt();
                    if (bst.add(operand)) {
                        result = (result + 1) % modValue;
                    }
                    break;

                case "Remove":
                    operand = sc.nextInt();
                    if (bst.remove(operand) != null) {
                        result = (result + 1) % modValue;
                    }
                    break;

                case "Contains":
                    operand = sc.nextInt();
                    if (bst.contains(operand)) {
                        result = (result + 1) % modValue;
                    }
                    break;
                case "Predecessor":
                    operand = sc.nextInt();
                    System.out.println("Predecessor of "+operand+" is "+bst.predecessor(operand));
                    break;
                default:
                    break;
            }
        }
        if (bst.verify()){
              System.out.println("BST is Valid");
        }
        else    
        {
            System.out.println("BST is not Valid");
        }

        // End Time
        timer.end();
        System.out.println();
        System.out.println(result);
        System.out.println(timer);
    }


    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    // Inorder traversal of tree
    void printTree(Entry<T> node) {
        if (node != null) {
            printTree(node.left);
            System.out.print(" " + node.element);
            printTree(node.right);
        }
    }
}




