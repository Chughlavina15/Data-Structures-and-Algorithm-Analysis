
/** Starter code for AVL Tree
 */
 
// replace package name with your netid
//package lxc220045;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

//import BinarySearchTree.Entry;
public class AVLTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {
    static class Entry<T> extends BinarySearchTree.Entry<T> {
		Entry<T> left, right;
        int height;
		// Constructor for Entry in AVL tree
		Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            height = 0;
        }
		// Constructor for creating Entry in AVL from BST Entry
        Entry(T x, BinarySearchTree.Entry<T> left, BinarySearchTree.Entry<T> right) {
            super(x, left, right);
            height = 0;
        }

    }
	// this class would store all the details of the AVL tree which would be used when validating AVL
	static class AVLEntryObject<T extends Comparable<? super T>> {
        boolean isvalid; // a flag which would store true if the AVL is valid and false incase if its Invalid
        T minValRight;
        T maxValLeft;
        int height;

    // Constructor for the AVL tree with storing default minimum and maximum values
		public AVLEntryObject(boolean isvalid) {
            this.isvalid = isvalid;
            this.minValRight = null;
            this.maxValLeft = null;
            this.height = 0;
        }

    // AVL constructor storing the maximum and minimum values of the left and the right sub trees respectlively    
		public AVLEntryObject(boolean isvalid, T rightMin, T leftMax, int height) {
            this.isvalid = isvalid;
            this.minValRight = rightMin;
            this.maxValLeft = leftMax;
            this.height = height;
        }
    }

    AVLTree() {
	super();
    }

	// stack for storing path of AVL tree from root to parent of node (path from root to parent of node)
	Entry<T> root;
    int size;
	Deque<Entry<T>> AVLstack; 


	// calculates height recursively for left and right subtree and returns the max of both and adding 1
	int getHeight(BinarySearchTree.Entry<T> newNode){
		if(newNode == null){
			return -1;
		}
		int leftHeight = getHeight(newNode.left);
		int rightHeight = getHeight(newNode.right);
		return 1 + Math.max(leftHeight, rightHeight); 
	}

	// balance factor is the subtraction of heights of left and right subtree
	int getBalanceFactor(Entry<T> t){
		return getHeight(t.left) - getHeight(t.right);
	}


	// Case when the newly inserted node is the left child of the parent rooted at left sub tree
	public Entry<T> LL(Entry<T> node){
		Entry<T> child = node.left;
		node.left = child.right;
		child.right = node;

		node.height = Math.max(getHeight(node.left), getHeight(node.right)) +1;
		child.height = Math.max(getHeight(child.left), child.height) + 1;
		if(node.element == root.element){
			root = child;
		}
        return child;
	}
	
	// Case when the newly inserted node is the right child of the parent rooted at right sub tree
	public Entry<T> RR(Entry<T> node){
		Entry<T> child = node.right;
		node.right = child.left;
        child.left = node;

		node.height = Math.max(getHeight(node.left), getHeight(node.right)) +1;
        child.height =  Math.max(getHeight(child.left), child.height) + 1;
        return child;
	}

	// here, combination of 2 single rotatios are needed to balance the node
	public Entry<T> LR(Entry<T> node){
		node.left = LL(node.left);
		return LL(node);
	}

	// here, comombination of 2 single rotatios are needed to balance the node
	public Entry<T> RL(Entry<T> node){
		node.right = LL(node.right);
		return RR(node);
	}

	// balancing the node
	public void balanceNodex(Entry<T> node){
		int balanceFactor = getBalanceFactor(node);	
		if(Math.abs(balanceFactor) <= 1 ){
			return;
		}
		Entry<T> temp = root;
		Entry<T> newNode = find(node.element);
		Entry<T> parent = AVLstack.getLast();
		// unbalancing present in the left subtree
		if(balanceFactor > 0){
			// case for LL single rotation
			if(parent.left!=null && parent.left.element.compareTo(node.element) == 0){
				temp = LL(node);
			}
			// case for LR double rotation
			else if(parent.right!=null && parent.right.element.compareTo(node.element)==0){
				temp = LR(node);
			}

		}
		// unbalancing present in the right subtree
		else{
			// case for RR single rotation 
			if(parent.right!=null && parent.right.element.compareTo(node.element)==0){
				temp = RR(node);
			}
			// case for RL double rotation
			else if(parent.left!=null && parent.left.element.compareTo(node.element)==0){
				temp = RL(node);
			}
		}
		// if the unbalanced node is the root node
		if(root == node) {
			root = temp;
			return;
		}
	}
	
	// this function is called by find(e) would create a stack which would store the path from the root to the parent of the node
	public Entry<T> find(Entry<T> t, T x){
        AVLstack = new ArrayDeque<Entry<T>>();
        Entry<T> temp = t;
        if(temp == null || temp.element == x){
            return temp;
        }
        while(true){
            if(x.compareTo(temp.element)<0){
                if(temp.left == null){
                    break;
                }
                AVLstack.add(temp);
                temp = temp.left;
            }
            else if(x.compareTo(temp.element)==0){
                break;
            }
            else if(temp.right==null){
                break;
            }
            else{
                AVLstack.add(temp);
                temp=temp.right;
            }
        }
		// returning the node found in the tree
        return temp;
    }
	
	// finding node with value e
	public Entry<T> find(T e){
        return find(root, e);
    }

	// adding node in AVL
	public boolean add(T x){
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
		Entry<T> newNode = find(x);
		if(newNode.element.compareTo(x)!=0){
			return false;
		}
		// after adding the node, we call the find(e) to store the path from root to parent of e in the AVLstack
		for (Entry<T> ele : AVLstack) {
            // System.out.println("ele element: " + ele.element);
            int leftHeight = getHeight(ele);
            int rightHeight = getHeight(ele);
            AVLstack.pop();

            ele.height = Math.max(leftHeight, rightHeight) + 1;
            balanceNodex(ele);
        }
        return true;
	}

	// using the bottom up approach to validate AVL
	public AVLEntryObject<T> validateAVL(BinarySearchTree.Entry<T> node) {
        if(node == null)
		{
            return new AVLEntryObject<T>(true, null, null, -1);
		}

        T rightMin = node.element;
        T leftMax = node.element;
        int leftSubtreeHeight = -1;
        int rightSubtreeHeight = -1;
        int nodeHeight = 0;

        if(node.left != null) {
            AVLEntryObject<T> leftSubtree = validateAVL(node.left);
            leftSubtreeHeight = leftSubtree.height;

            if(!leftSubtree.isvalid || leftMax.compareTo(node.element) > 0){
				// violating condition for BST
				// left sub tree is invalid or node element is greater than the permissible value
				return new AVLEntryObject<T>(false, rightMin, leftMax, leftSubtreeHeight+1);   
			}
        }

        if(node.right != null) {
            AVLEntryObject<T> rightSubtree = validateAVL(node.right);
            if(rightSubtree.minValRight.compareTo(node.element) < 0){
				
				rightMin = rightSubtree.minValRight;
			}

            rightSubtreeHeight = rightSubtree.height;

            if(!rightSubtree.isvalid || rightMin.compareTo(node.element) < 0){
				// violating condition for BST
				// right sub tree is invalid or node element is lesser than the permissible value
				return new AVLEntryObject<T>(false, rightMin, leftMax, rightSubtreeHeight+1);
        	}
		}

		// now we check the height of the node
        nodeHeight = Math.max(leftSubtreeHeight, rightSubtreeHeight) + 1;

        // for AVL to be balanced, the difference between left and right sub trees should be less than equal to 1
		if(Math.abs(leftSubtreeHeight - rightSubtreeHeight) > 1){
            return new AVLEntryObject<T>(false, rightMin, leftMax, nodeHeight); //propagate false flag to parent
		}

        return new AVLEntryObject<T>(true, rightMin, leftMax, nodeHeight);
    }

    public boolean verify() {
        if(size == 0){
			// if the tree is empty
			return true;    
		}

        AVLEntryObject<T> treeObject = validateAVL(root);
        return treeObject.isvalid;
    }

}


