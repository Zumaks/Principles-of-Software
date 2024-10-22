package hw4;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;




public class Graph<N,E> {
	
	private Map<N,Map<N,List<E>>> G;
	
	
	
	
	/**
     * Abstraction Function:
     * - The Graph object represents a directed graph where nodes are represented by strings.
     * - Each key in the map G represents a node in the graph.
     * - The list of values associated with each key represents the edges from that node.
     * - Each edge is represented as a string in the format "destinationNode(weight)".
     * - If there is a reflexive edge, originNode(weight) should be shown in the list of children
     * - If there are no children, list is empty
     * - If there are no nodes whatsoever, the graph size will be 0
     */
	
	/**
     * Representation Invariant:
     * - G must not be null or contain null keys or values
     * - For each key-value pair in G:
     *     - The key must represent a node in the graph.
     *     - The list of values which is the value in the key-value pair must represent the edges from that node.
     * - No two nodes can be the same
     */
	
	
	
	
	
	
	
	
	
	/**
	 @effects this constructs a new graph with zero nodes and edges
	 */
	
    public Graph() {
    	this.G = new HashMap<>();
    	 //checkRep();
    }

    
    /**
    @param adjacencyListin a map of nodes with their corresponding edges and weight values
    @requires that adjacencyListin does not equal null and all nodes and edges in the graph are connected
    @effects constructs a new graph given the previously established adjacencyListin
     */

    public Graph( Map<N,Map<N,List<E>>> adjacencyListin) {
    	this.G = new HashMap<>(adjacencyListin);
    	 //checkRep();
    }
	
	/**
	@param String x and y which are nodes in adjacencyList
	@requires The graph G is not null, and vertices x and y exist in G.
	@returns: True if there is an edge from vertex x to vertex y in graph G, false otherwise.
	 */

public boolean isAdjacent(N x, N y) {
//		List<String> neighbors = G.get(x);
//		return neighbors != null && neighbors.contains(y);
	Map<N, List<E>> neighbors = G.get(x);
    return neighbors != null && neighbors.containsKey(y);
    //checkRep();
    }
	/**
	@param String x which is a node in adjacencyList
	@requires The graph G is not null, and vertex x exists in G.
	@returns: A list of vertices y such that there is an edge from vertex x to vertex y in graph G.
	 */

public List<E> isNeighbour(N x) {
	Map<N, List<E>> neighbors = G.getOrDefault(x, new HashMap<>());
    List<E> edges = new ArrayList<>();
    for (List<E> edgeList : neighbors.values()) {
        edges.addAll(edgeList);
    }
    return edges;
	 //checkRep();
    }


	/**
	@param String x which is a node
	@requires The graph G is not null
	@modifies The structure of the graph G
	@effects Adds a vertex x if it does not already exist.
	
	 */

public void addNode(N x) {
	G.putIfAbsent(x, new HashMap<>());
	 //checkRep();
    }


	/**
	 * @return a list of all nodes in the graph
	 */
	public List<N> getNodes() {
		return new ArrayList<>(G.keySet());
	}



	/**
	@param String x which is a node in adjacencyList
	@requires The graph G is not null, and vertex x exists in G.
	@modifies The structure of the graph G
	@effects Removes vertex x if it exists.
	
	 */

public void removeNode(N x) {
//	G.remove(x);
//    G.forEach((key, value) -> value.remove(x));
//	 G.remove(x);
//	    
//	    // Remove edges where the node is the source
//	    G.forEach((key, value) -> value.removeIf(edge -> edge.startsWith(x + "(")));
//	    
//	    // Remove edges where the node is the destination
//	    G.values().forEach(edges -> edges.removeIf(edge -> edge.startsWith(x + "(")));
//	    //checkRep();
	G.remove(x);
    G.values().forEach(neighbors -> neighbors.remove(x));
	}

	/**
	@param String x and y which are a nodes in adjacencyList, and new edge value z
	@requires The graph G is not null, and vertices x and y exist in G.
	@modifies The structure of the graph G
	@effects Adds an edge z from vertex x to vertex y if it does not already exist.
	
	 */

public void addEdge(N x, N y, E z) {
	G.computeIfAbsent(x, k -> new HashMap<>()).computeIfAbsent(y, k -> new ArrayList<>()).add(z);
}

	/**
	@param String x and y which are a nodes in adjacencyList, and new edge value z
	@requires The graph G is not null, and vertices x and y exist in G.
	@modifies The structure of the graph G
	@effects Remove the edge from vertex x to vertex y if it exists.
	
	 */

public void removeEdge(N x, N y) {
	Map<N, List<E>> neighbors = G.get(x);
    if (neighbors != null) {
        // Remove the entry associated with node y
        neighbors.remove(y);
    }
}
public Map<N, Map<N, List<E>>> getG() {
    return G;
}

public Iterator<N> listNodes() {

    // gets all the keys (nodes)
    List<N> nodes = new ArrayList<>(G.keySet());

    return nodes.iterator();

}


//
//private void checkRep() {
//    assert G != null : "Representation Invariant violated: G is null";
//    for (Map.Entry<String, List<String>> entry : G.entrySet()) {
//        String node = entry.getKey();
//        List<String> edges = entry.getValue();
//        assert node != null : "Representation Invariant violated: Node is null";
//        assert edges != null : "Representation Invariant violated: Edges list is null";
//    }
//}
//
//
//




}

