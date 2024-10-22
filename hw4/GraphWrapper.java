package hw4;

import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
public class GraphWrapper{
	private Graph <String,String> New_Graph;
	
	/**
	 @effects this constructs a new graph with zero nodes and edges
	 */
	
	public GraphWrapper() {
		this.New_Graph = new Graph<>();
	}
	
	/**
	@param String x which is a node
	@requires New_graph is not null
	@modifies The structure of New_graph
	@effects Adds a vertex x if it does not already exist.
	
	 */
	
	public void addNode(String x) {
		New_Graph.addNode(x);
	}
	
	
	/**
	@param String x and y which are a nodes in adjacencyList, and new edge value z
	@requires The graph G is not null, and vertices x and y exist in G.
	@modifies The structure of the graph G
	@effects Adds an edge z from vertex x to vertex y if it does not already exist.
	
	 */

	public void addEdge(String x, String y, String z) {
		New_Graph.addEdge(x, y, z);
	}
	
	/**
	@param None
	@requires New_Graph is not null
	@returns a list of all the nodes in New_Graph
	 */
	
	public Iterator<String> listNodes() {
		List<String> nodes = New_Graph.getNodes();
	    Collections.sort(nodes); // Sort the list of children
	    return nodes.iterator();
	}
	
	/**
	@param a string ParentNode which is a node in new_graph
	@requires New_Graph is not null and ParentNode is not null
	@returns a list of all the children of ParentNode
	 */
	
	public Iterator<String> listChildren(String ParentNode) {
		Map<String,Map<String,List<String>>> Graph2 = New_Graph.getG();
		if (Graph2.get(ParentNode) == null) {
			List<String> children = new ArrayList<>();
			return children.iterator();
		}
		Map<String,List<String>> neww = Graph2.get(ParentNode);
		List<String> childrenList = new ArrayList<>();
        
        // Collect the children into a List
        for (String child : neww.keySet()) {
            for (String edgeLabel : neww.get(child)) {
                childrenList.add(child + "(" + edgeLabel + ")");
            }
        }
        
        Collections.sort(childrenList);
	    return childrenList.iterator();
//		return New_Graph.isNeighbour(ParentNode).iterator();
	    
	    
	    
	    
	}
	
	/**
	@param String x and y which are nodes in adjacencyList
	@requires The graph G is not null, and vertices x and y exist in G.
	@returns: True if there is an edge from vertex x to vertex y in graph G, false otherwise.
	 */
	
	public boolean isAdjacent(String x, String y) {
		return New_Graph.isAdjacent(x,y);
    }
	
	/**
	@param String x which is a node in adjacencyList
	@requires The graph G is not null, and vertex x exists in G.
	@modifies The structure of the graph G
	@effects Removes vertex x if it exists.
	
	 */

	public void removeNode(String x) {
		New_Graph.removeNode(x);
	}
	
	/**
	@param String x and y which are a nodes in adjacencyList, and new edge value z
	@requires The graph G is not null, and vertices x and y exist in G.
	@modifies The structure of the graph G
	@effects Remove the edge from vertex x to vertex y if it exists.
	
	 */

	public void removeEdge(String x, String y) {
		New_Graph.removeEdge(x, y);
	}
	
	
	
}