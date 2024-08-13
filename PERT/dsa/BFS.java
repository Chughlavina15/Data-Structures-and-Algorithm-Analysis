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


public class BFS extends Graph.GraphAlgorithm<BFS.BFSVertex> {
    public static final int INFINITY = Integer.MAX_VALUE;
    Vertex src;

    // Class to store information about vertices during BFS
    public static class BFSVertex implements Factory {
	boolean seen;
	Vertex parent;
	int distance;  // distance of vertex from source
	public BFSVertex(Vertex u) {
	    seen = false;
	    parent = null;
	    distance = INFINITY;
	}
	public BFSVertex make(Vertex u) { return new BFSVertex(u); }
    }

    // code to initialize storage for vertex properties is in GraphAlgorithm class
    public BFS(Graph g) {
	super(g, new BFSVertex(null));
    }

    public void bfs(Vertex src) {
		// defining a queue for bfs traversal
		Queue<Vertex> queue = new LinkedList<Vertex>();

		// initializing all the vertices in the graph
		for(Vertex vertex : g){
			get(vertex).parent = null;
			get(vertex).distance = INFINITY;
			get(vertex).seen = false;
		}

		// updating paramters for the source vertex
		this.src = src;
		get(src).distance = 0;
		get(src).seen = true;
		queue.add(src);
		// iterating through the queue
		while(!queue.isEmpty()){
			Vertex currentVertex = queue.remove();

			// iterating through all the edges incident from the current node
			for(Edge edge : g.incident(currentVertex)){
				// child vertex of this edge

				Vertex childVertex = edge.otherEnd(currentVertex);

				// if this vertex is already seen, we continue to the next edge incident from current vertex
				if(get(childVertex).seen == true){
					continue;
				}

				// marking the current vertex as seen 
				// updating the parent of child vertex to the current vertex
				// updating distance for the child vertex
				get(childVertex).seen = true;
				get(childVertex).parent = currentVertex;
				get(childVertex).distance = get(currentVertex).distance + 1;

				// adding this vertex in the queue
				queue.add(childVertex);

			}
		}
	
    }

    // Run breadth-first search algorithm on g from source src
    public static BFS breadthFirstSearch(Graph g, Vertex src) {
		BFS b = new BFS(g);
		b.bfs(src);
		return b;
    }
    
    public static BFS breadthFirstSearch(Graph g, int s) {
	return breadthFirstSearch(g, g.getVertex(s));
    }
    
    public static void main(String[] args) throws Exception {
	String string = "6 12   1 2 2   1 3 4   1 5 6   2 3 8   2 4 10   3 6 2   4 3 4   5 2 6   5 3 8   6 5 10   6 1 2  6 4 2   1";
	Scanner in;
	// If there is a command line argument, use it as file from which
	// input is read, otherwise use input from string.
	in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
	// Read graph from input
    Graph g = Graph.readDirectedGraph(in);
	// last number specifies source s
	int s = in.nextInt();

	// Create an instance of BFS and run bfs from source s
	BFS b = breadthFirstSearch(g, s);

	g.printGraph(false);

	System.out.println("Output of BFS:\nNode\tDist\tParent\n----------------------");
	for(Vertex u: g) {
	    if(b.get(u).distance == INFINITY) {
		System.out.println(u + "\tInf\t--");
	    } else {
		System.out.println(u + "\t" + b.get(u).distance + "\t" + b.get(u).parent);
	    }
	}
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
Output of BFS:
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
