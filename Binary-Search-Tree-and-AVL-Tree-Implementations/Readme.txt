Analysis for Data Structures and Algorithms

Project: Binary Search Tree and AVL Tree Implementations

Implemented a comprehensive Binary Search Tree (BST) with functions for adding, removing, and searching nodes, as well as finding minimum, maximum, predecessor, successor, floor, and ceiling values, along with BST verification. Extended BST to develop an AVL Tree that maintains balance through rotations after node additions and includes AVL-specific verification.

Binary Search Trees and AVL trees.

BinarySearchTree.java has the following functions implemented,
    (i) Add
    (ii) Contains
    (iii) Remove
    (iv) Find minimum
    (v) Find maximum
    (vi) Find predecessor
    (vii) Find successor
    (viii) Find floor value node of any node x
    (ix) Find ceil value node of any node x
    (x) Verification of BST

AVL extends the BinarySearchTree class and has the following fucntions implemented,
    (i) Add
    (ii) After Add, checks for unbalanced nodes, perfroms the possible rotations and balances the AVL Tree
    (iii) Verifictaion  of AVL


To run and test for BinarySearchTree, follow these steps:
    (i) javac Timer.java
    (ii) javac BinarySearchTree.java
    (iii) java BinarySearchTree p2\ testcases/test.txt ( test.txt can be replaced with the test file that needs to be tested)

Similary,
To run and test for AVL Tree, follow these steps:
    (i) javac Timer.java
    (ii) javac AVLTree.java
    (iii) javac AVLTreeDriver.java
    (iv) java AVLTreeDriver p2\ testcases/test.txt ( test.txt can be replaced with the test file that needs to be tested)