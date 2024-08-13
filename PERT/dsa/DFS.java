/** Breadth-first search
 *  @author rbk
 *  Version 1.0: 2018/10/16
 */

 package dsa;

 import dsa.Graph.*;
 
 import java.io.File;
 import java.util.Queue;
 import java.util.LinkedList;
 import java.util.Scanner;
 
 public class DFS extends Graph.GraphAlgorithm<DFS.DFSVertex> {
     public static final int INFINITY = Integer.MAX_VALUE;
     LinkedList<Vertex> dfsPostOrderTraversal;
     Vertex src;
 
     enum Status{
         NEW,
         ACTIVE,
         FINISHED
     }
 
     // Class to store information about vertices during DFS
     public static class DFSVertex implements Factory {
     Status status;
     Vertex parent;
     int distance;  // distance of vertex from source
     public DFSVertex(Vertex u) {
         status = Status.NEW;
         parent = null;
         distance = INFINITY;
     }
     public DFSVertex make(Vertex u) { return new DFSVertex(u); }
     }
 
     // code to initialize storage for vertex properties is in GraphAlgorithm class
     public DFS(Graph g) {
     super(g, new DFSVertex(null));
     }

     public void initializeGraph(Graph g){
        for(Vertex u: g){
            get(u).status = Status.NEW;
            get(u).parent = null;
        }
     }
 
     public boolean isDAG(Vertex src) {
         this.src = src;
         // code for dfs
         get(src).status = Status.ACTIVE;
         for (Edge edge : g.incident(src) ){
             Vertex adjacentVertex = edge.otherEnd(src);
             if (get(adjacentVertex).status == Status.ACTIVE){
                 return false;
             }
             else if (get(adjacentVertex).status == Status.NEW){
                 if (isDAG(adjacentVertex) == false){
                     return false;
                 }
             } 
         }
         get(src).status = Status.FINISHED;
         return true;
     }

     public void dfs(Vertex src, LinkedList<Vertex> dfLinkedList){
        this.src = src;
         // code for dfs
         get(src).status = Status.ACTIVE;
         for (Edge edge : g.incident(src) ){
            Vertex adjacentVertex = edge.otherEnd(src);
            if (get(adjacentVertex).status == Status.NEW){
                get(adjacentVertex).parent = src;
                dfs(adjacentVertex, dfLinkedList);
            } 
         }
        get(src).status = Status.FINISHED;
        dfLinkedList.add(src); 
     }
 
    public boolean dagAll(Graph g){
        initializeGraph(g);
        for(Vertex u: g){
            if(get(u).status == Status.NEW){
                if(isDAG(u) == false){
                    return false;
                }
             }
        }
        return true;
    }

     public LinkedList<Vertex> dfsAll(Graph g){
        initializeGraph(g);
        dfsPostOrderTraversal = new LinkedList<>();
        System.out.println("dfs started");
        for(Vertex u: g){
            if(get(u).status == Status.NEW){
                dfs(u, dfsPostOrderTraversal);
            }
        }
        System.out.println("dfs ended");
        System.out.println("list is "+dfsPostOrderTraversal);
        return dfsPostOrderTraversal;
    }

 
     // Run depth-first search algorithm on g from source src
     public static DFS depthFirstSearch(Graph g, Vertex src) {
        DFS b = new DFS(g);
        boolean isDAGFlag = b.dagAll(g);
        System.out.println("is DAG check "+ isDAGFlag);
        if ( isDAGFlag == true){
            b.dfsAll(g);
        }
        return b;
     }
     
     public static DFS depthFirstSearch(Graph g, int s) {
         return depthFirstSearch(g, g.getVertex(s));
     }
     
     public static void main(String[] args) throws Exception {
     String string = "4 4   1 2 100   3 2 100   4 3 100   4 1 100   1";
     Scanner in;
     // If there is a command line argument, use it as file from which
     // input is read, otherwise use input from string.
     in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
     // Read graph from input
     Graph g = Graph.readDirectedGraph(in);
     // last number specifies source s
     int s = in.nextInt();
 
     // Create an instance of DFS and run bfs from source s
     DFS b = depthFirstSearch(g, s);
 
     g.printGraph(false);
 
    //  System.out.println("Output of DFS:\nNode\tDist\tParent\n----------------------");
    //  for(Vertex u: g) {
    //      if(b.get(u).distance == INFINITY) {
    //      System.out.println(u + "\tInf\t--");
    //      } else {
    //      System.out.println(u + "\t" + b.get(u).distance + "\t" + b.get(u).parent);
    //      }
    //  }
     }
 }
 
 /* Sample run:
 ______________________________________________
 Graph: n: 7, m: 8, directed: true, Edge weights: false
 1 :  (1,2) (1,3)
 2 :  (2,4)
 3 :  (3,4)
 4 :  (4,5)
 5 :  (5,1)
 6 :  (6,7)
 7 :  (7,6)
 ______________________________________________
 Output of DFS:
 Node	Dist	Parent
 ----------------------
 1	0	null
 2	1	1
 3	1	1
 4	2	2
 5	3	4
 6	Inf	--
 7	Inf	--
 */
 