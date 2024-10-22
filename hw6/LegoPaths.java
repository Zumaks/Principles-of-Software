package hw6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import hw4.Graph;
import hw5.ProfessorParser;
import hw5.ProfessorPaths;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Comparator;



public class LegoPaths {
	Graph<String, String> graph;
	
	
	/*
	 * Abstraction Function 
	 * 
	 * A Path object represents a path from one node to another in a graph, 
	 * consisting of a sequence of nodes, corresponding weights for the edges between nodes, 
	 * a destination node, and a cost.
	 * nodes: Represents the sequence of nodes in the path.
	 * weights: Represents the corresponding weights for the edges between nodes.
	 * destination: Represents the destination node of the path.
	 * cost: Represents the total cost of the path.
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * Representation Invariant : 
	 * nodes cannot equal null
	 * weights cannot equal null
	 * destination cannot equal null
	 * cost has to be non-negative
	 * destination must be the last node in nodes
	 * For every position in the list of strings nodes, the element at that position cannot be null
	 * For every position in the list of weights nodes, the element at that position cannot be null
	 * The number of nodes must equal to the number of weights, no node can be without a weight
	 */
	
	
	private static class Path implements Comparable<Path> {
        List<String> nodes;
        List<Double> weights;
        String destination;
        double cost;
        
        /**
         * Constructs a Path object with given nodes, weights, and cost.
         *
         * @param nodes1   List of nodes in the path
         * @param weights1 Corresponding weights for the edges between nodes
         * @param cost1    Total cost of the path
         * @effects this creates a path and sets all path object values to what was
         * provided during the function call 
         */

        public Path(List<String> nodes1, List<Double> weights1, double cost1) {
            this.nodes = nodes1;
            this.weights = weights1;
            this.destination = nodes1.getLast();
            this.cost = cost1;

        }
        
        /**
         * Constructs a Path object with a destination node and cost.
         *
         * @param dest   Destination node of the path
         * @param cost2  Total cost of the path
         * @effects this creates a path and sets all path object values to what was
         * provided during the function call
         */
        
        public Path(String dest, double cost2) {
            initPath();
            this.nodes.add(destination);
            this.destination = dest;
            this.cost = cost2;

        }


        /**
         * Compares this path with another path based on their costs.
         *
         * @param other The other Path object to compare
         * @return A negative integer, zero, or a positive integer if this path is less than, equal to, or greater than the other path.
         */

        @Override
        public int compareTo(Path other) {
            return Double.compare(this.cost, other.cost);
        }
        
        /**
         * Initializes the lists of nodes and weights for the path.
         * @effects sets nodes and weights to empty new ArrayList
         */
        
        private void initPath() {
            this.nodes = new ArrayList<>();
            this.weights = new ArrayList<>();
        }
        
    }

	/**
	 * Returns an iterator over the nodes in the graph.
	 *
	 * @return An iterator over the nodes in the graph.
	 */

    public Iterator<String> listNodes(){
        return this.graph.listNodes();
    }
	
	
	
	
	
	
	
	
	
	
    /**
     * Creates a new graph based on the data read from the specified file.
     *
     * @param Filename the name of the file containing the graph data
     * @throws IOException if an I/O error occurs while reading the file
     * @modifies The structure of graph
     * @effects Transforms data from files to 
     */
	
	public void createNewGraph(String Filename) {
    	Map<String, List<String>> copy = new HashMap<>();
        List<String> profs = new ArrayList<>();
        try {
            ProfessorParser.readData(Filename, copy, profs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Map<String, List<String>>> formattedMap = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : copy.entrySet()) {
            String course = entry.getKey();
            List<String> professors = entry.getValue();
            for (String professor : professors) {
                Map<String, List<String>> professorMap = formattedMap.computeIfAbsent(professor, k -> new HashMap<>());
                for (String otherProfessor : professors) {
                    if (!otherProfessor.equals(professor)) {
                        professorMap.computeIfAbsent(otherProfessor, v -> new ArrayList<>()).add(course);
                    }
                }
            }
        }

        this.graph = new Graph<>(formattedMap);
    }
	
	
	/**
     * Finds the shortest path from the first node to the second node in the graph.
     *
     * @param first  the starting node
     * @param second the destination node
     * @return a string representing the shortest path from the first node to the second node
     */
	
	public String findPath(String first, String second) {

        Iterator<String> allNodes = this.listNodes();
        boolean node1 = false;
        boolean node2 = false;


        while(allNodes.hasNext()){
            String cur_node = allNodes.next();
            if(cur_node.equals(first)){
                node1 = true;
            }
            else if (cur_node.equals(second)){
                node2 = true;
            }
            if(node1 && node2){
                break;
            }
        }
        
        
        
        
        
        
        
        
        
        if( first.equals(second) && node1 ){
            return "path from " + first + " to " + second + ":\ntotal cost: 0.000\n";
        }

        if(!node2 &&!node1){
            if(first.equals(second)) {
                return "unknown part " + first + "\n";
            }
            String statementOne = "unknown part " + first;
            String statementTwo = "unknown part " + second;

            return statementOne + "\n" + statementTwo + "\n";
        }

        else if(!node2){
            return "unknown part " + second + "\n";
        }
        else if(!node1){
            return "unknown part " + first + "\n";
        }


        return Dijk(first, second);
    }
	
	
	
	/**
     * Formats the string so that it matches the expected outcomes
     *
     * @param string representing source node
     * @param string representing destination node  
     * @return a string representing the total cost which is correctly formatted
     */
	
	 private String formatTheString(String source, String destination, Path final_path) {

	        String format ="path from " + source + " to " + destination + ":\n" ;
	        format += source;

	        List<String> path = final_path.nodes;

	        for(int i = 1; i < path.size(); i++){
	            format += " to " + path.get(i) + " with weight %.3f\n".formatted(final_path.weights.get(i - 1));
	            if(i == path.size() - 1 ) {
	            	break; 
	            	}
	            format += path.get(i);

	        }

	        return format + "total cost: %.3f\n".formatted(final_path.cost);

	    }
	
	
	
	
	 /**
	     * Finds the shortest path from the first node to the second node in the graph.
	     *
	     * @param first  the starting node
	     * @param second the destination node
	     * @return a string representing that no path was found if indeed no path was found
	     */
	
	public String Dijk(String first, String second){
		
		
		
        PriorityQueue<Path> current = new PriorityQueue<>();

        Set<String> final_set = new HashSet<>();

        current.add(new Path(first, 0.0));


        while (!current.isEmpty()) {

        	
        	
        	

            Path minimalPath = current.poll();
            String minimalDestination = minimalPath.destination;
            if (minimalDestination.equals(second)) {
                return formatTheString(first, second, minimalPath);
            }
            if (final_set.contains(minimalDestination)) {
                continue;
            }
            
            
            
            
            
            
            
            Map<String, List<String>> all_children = graph.getG().getOrDefault(minimalDestination, new HashMap<>());
            List<String> childKeys = new ArrayList<>(all_children.keySet());
            
            Collections.sort(childKeys);
            
            
            for (String current_child : childKeys) {

                if(final_set.contains(current_child)){
                	
                    continue;
                }
                
                

                
                
                List<String> e = all_children.get(current_child);
                
                List<String> path = new ArrayList<>(minimalPath.nodes);
                
                List<Double> weight = new ArrayList<>(minimalPath.weights);

                path.add(current_child);
                weight.add((double) 1 / e.size());
                current.add(new Path(path, weight, minimalPath.cost + ((double) 1 / e.size())));

            }


            final_set.add(minimalDestination);

        }

        // Return if no path found
        return "path from " + first + " to " + second +  ":\n" + "no path found\n";

    }

	
	
	
	
//	
//	
//	
//	
//	private static class Path implements Comparable<Path> {
//		List<String>nodes;
//		List<Double> weights;
//		String destination;
//		double cost;
//		
//		public Path(List<String> nodes,List<Double> weights,double cost) {
//			this.nodes = nodes;
//			this.weights = weights;
//			this.cost = cost;
//		}
//		public void removeNode(String node) {this.nodes.remove(node);}
//		
//		private void initializePath() {
//			this.nodes = new ArrayList<>();
//			this.weights = new ArrayList<>();
//		}
//		public int compareTo(Path other) {return Double.compare(this.cost,other.cost);}
//		
//	}
//	
//	
//	public Iterator<String> listNodes(){ return this.graph.listNodes();}
//	
	
	
	
//public static void main(String[] arg){
//String filename = "data/lego1960.csv";
//LegoPaths legoGraph = new LegoPaths();
//legoGraph.createNewGraph(filename);
//
//System.out.print(legoGraph.findPath("upn0041a White Brick 2 x 4 without Bottom Tubes, Slotted (with 2 slots)","upn0175 Red Brick Round Corner 2 x 4 without Stud Notches"));
//
//
//}
//	
	
	
//	public static void main(String[] arg) {
//	//////
//	String file = "data/lego1960.csv";
//	ProfessorPaths n = new ProfessorPaths();
//	n.createNewGraph(file);
////	System.out.println(n.findPath("Mohammed J. Zaki", "Wilfredo Colon"));
//	}
}
