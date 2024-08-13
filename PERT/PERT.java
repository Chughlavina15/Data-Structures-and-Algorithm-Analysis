/* Starter code for PERT algorithm (Project 4)
 * @author rbk
 */

// change to your netid
//package dsa;

// replace dsa with your netid below
import dsa.Graph;
import dsa.Graph.Vertex;
import dsa.Graph.Edge;
import dsa.Graph.GraphAlgorithm;
import dsa.Graph.Factory;
import dsa.DFS;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class PERT extends GraphAlgorithm<PERT.PERTVertex> {
	public static final int NEGATIVEINFINITY = Integer.MIN_VALUE;
	public static final int INFINITY = Integer.MAX_VALUE;
    public static LinkedList<Vertex> finishList;
	
	
    public static class PERTVertex implements Factory {
	// Add fields to represent attributes of vertices here
	int earliestStart;
	int earliestFinish;
	int latestStart;
	int latestFinish;
	int slack;
	int vertexDuration;
	public PERTVertex(Vertex u) {
		earliestStart = 0;
		earliestFinish = 0;
		latestStart = 0;
		latestFinish = 0;
		slack = INFINITY;
		vertexDuration = 0;;
	}
	public PERTVertex make(Vertex u) { return new PERTVertex(u); }
    }

    // Constructor for PERT is private. Create PERT instances with static method pert().
    private PERT(Graph g) {
	super(g, new PERTVertex(null));
    }

    public void setDuration(Vertex u, int d) {
		get(u).vertexDuration = d;
    }

	public boolean pert(Graph g){
		DFS b = new DFS(g);
		boolean isDAG = b.dagAll(g);
		if(isDAG == true){

		}
		return isDAG;
	}

    // Implement the PERT algorithm. Returns false if the graph g is not a DAG.
    public boolean pert() {
		System.out.println("inside pert START ");
		for(Vertex vertex : finishList){
			System.out.println("vertex "+vertex+" duration "+get(vertex).vertexDuration);
		}
		initializeEarliestStart(finishList);
		System.out.println("inside pert after inilaiatse es ");
		for(Vertex vertex : finishList){
			System.out.println("vertex "+vertex+" es "+get(vertex).earliestStart);
		}
		updateEarliestFinsihTime(finishList);
		System.out.println("inside pert after inilaiatse ef es ");
		for(Vertex vertex : finishList){
			System.out.println("vertex "+vertex+" es "+get(vertex).earliestStart + " ef "+get(vertex).earliestFinish);
		}
		int cpl = criticalPath();
		System.out.println("CPL "+criticalPath());
		initializeLatestFinish(finishList, cpl);
		System.out.println("inside pert after inilaiatse lf ");
		for(Vertex vertex : finishList){
			System.out.println("vertex "+vertex+" lf "+get(vertex).latestFinish);
		}
		Collections.reverse(finishList);
		updateLatestStartTime(finishList);
		System.out.println("inside pert FINAL ");
		for(Vertex vertex : finishList){
			System.out.println("vertex "+vertex+" es "+get(vertex).earliestStart + " ef "+get(vertex).earliestFinish 
			+" ls "+get(vertex).latestStart+" lf "+get(vertex).latestFinish+" slack "+get(vertex).slack);
		}
		return false;
    }

	// initilaise earliest start time
	public void initializeEarliestStart(LinkedList<Vertex> toplogicaOrderList){
		for(Vertex vertex : toplogicaOrderList){
			get(vertex).earliestStart = 0;
		}
	}

	public void updateEarliestFinsihTime(LinkedList<Vertex> toplogicaOrderList){
		for(Vertex vertex : toplogicaOrderList){
			get(vertex).earliestFinish = get(vertex).earliestStart + get(vertex).vertexDuration;
			for (Edge edge : g.incident(vertex)){
				Vertex adjacentVertex = edge.otherEnd(vertex);
				if(get(adjacentVertex).earliestStart < get(vertex).earliestFinish){
					get(adjacentVertex).earliestStart = get(vertex).earliestFinish;
				}
			}
		}
	} 

	public void initializeLatestFinish(LinkedList<Vertex> toplogicaOrderList, int cpl){
		for(Vertex vertex : toplogicaOrderList){
			get(vertex).latestFinish = cpl;
		}
	}

	public void updateLatestStartTime(LinkedList<Vertex> reversedToplogicaOrderList){
		for(Vertex vertex : reversedToplogicaOrderList){
			get(vertex).latestStart = get(vertex).latestFinish - get(vertex).vertexDuration;
			get(vertex).slack = get(vertex).latestFinish - get(vertex).earliestFinish;
			for (Edge edge : g.inEdges(vertex)){
				Vertex adjacentVertex = edge.otherEnd(vertex);
				if(get(adjacentVertex).latestFinish > get(vertex).latestStart){
					get(adjacentVertex).latestFinish = get(vertex).latestStart;
				}
		}
	}
}

    // Find a topological order of g using DFS
    LinkedList<Vertex> topologicalOrder() { 
		return finishList;
    }


    // The following methods are called after calling pert().

    // Earliest time at which task u can be completed
    public int ec(Vertex u) {
	return 0;
    }

    // Latest completion time of u
    public int lc(Vertex u) {
	return 0;
    }

    // Slack of u
    public int slack(Vertex u) {
	return 0;
    }

    // Length of a critical path (time taken to complete project)
    public int criticalPath() {
		int cpl= NEGATIVEINFINITY;
		for(Vertex vertex : finishList){
			if(cpl < get(vertex).earliestFinish){
				cpl = get(vertex).earliestFinish;
			}
		}
		return cpl;
    }

    // Is u a critical vertex?
    public boolean critical(Vertex u) {
	return false;
    }

    // Number of critical vertices of g
    public int numCritical() {
	return 0;
    }

    /* Create a PERT instance on g, runs the algorithm.
     * Returns PERT instance if successful. Returns null if G is not a DAG.
     */
    public static PERT pert(Graph g, int[] duration) {
	PERT p = new PERT(g);
	DFS b = new DFS(g);
	for(Vertex u: g) {
	    p.setDuration(u, duration[u.getIndex()]);
	}
	// Run PERT algorithm.  Returns false if g is not a DAG
	if(b.dagAll(g) == false){
		return null;
	}
	finishList = b.dfsAll(g);
	Collections.reverse(finishList);
	System.out.println("list is "+finishList+" is dag "+b.dagAll(g));
	if(p.pert()) {
	    return p;
	} else {
	    return null;
	}
    }
    
    public static void main(String[] args) throws Exception {
	String graph = "8 11   1 2 1   1 3 1   2 4 1   3 5 1   3 6 1   4 6 1   4 7 1   5 7 1   5 8 1   6 8 1   7 8 1   3 3 2 3 7 4 5 1";
	Scanner in;
	// If there is a command line argument, use it as file from which
	// input is read, otherwise use input from string.
	in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
	Graph g = Graph.readDirectedGraph(in);
	g.printGraph(false);

	DFS b = new DFS(g);
	int[] duration = new int[g.size()];
	for(int i=0; i<g.size(); i++) {
	    duration[i] = in.nextInt();
	}
	PERT p = pert(g, duration);
	if(p == null) {
	    System.out.println("Invalid graph: not a DAG");
	} else {
	    System.out.println("Number of critical vertices: " + p.numCritical());
	    System.out.println("u\tEC\tLC\tSlack\tCritical");
	    for(Vertex u: g) {
		System.out.println(u + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
	    }
	}
    }
}
