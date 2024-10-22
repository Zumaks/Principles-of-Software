package hw5;

import hw4.Graph; 
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;

public class ProfessorPaths {
	Graph<String, String> graph; // Graph class from hw4 package

    // Constructor
    
    /**
     * Constructs a new ProfessorPaths object with a graph initialized with zero nodes and edges.
     * @effects Constructs a new ProfessorPaths object with a graph initialized with zero nodes and edges.
     */
    
    public ProfessorPaths() {
        this.graph = new Graph<>();
    }

    /**
     * Reads data from a file and constructs a new graph based on the provided adjacency list.
     * @param Filename the name of the file containing the adjacency list
     * @requires Filename is not null and the file exists
     * @effects Reads data from a file and constructs a new graph based on the provided csv data.
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
     * Formats the path between two professors.
     * @param path the path between two professors
     * @param professor1 the starting professor
     * @param professor2 the ending professor
     * @requires path, professor1, and professor2 are not null
     * @returns a formatted string representing the path between professor1 and professor2
     */
   
    private String formatPath(List<String> path, String professor1, String professor2) {
    	StringBuilder sb = new StringBuilder("path from " + professor1 + " to " + professor2 + ":\n");
        for (int i = 0; i < path.size() - 1; i++) {
            String[] parts1 = path.get(i).split("\\(");
            String currentProf = parts1[0].trim();
            String commonCourse = parts1[1].substring(0, parts1[1].indexOf(")"));

            String[] parts2 = path.get(i + 1).split("\\(");
            String nextProf = parts2[0].trim();

            sb.append(currentProf).append(" to ").append(nextProf).append(" via ").append(commonCourse).append("\n");
        }
        
        // Append the last professor (professor2) to complete the path
        String[] parts = path.get(path.size() - 1).split("\\(");
        String lastProf = parts[0].trim();
        String lastCourse = parts[1].substring(0, parts[1].indexOf(")"));
        sb.append(lastProf).append(" to ").append(professor2).append(" via ").append(lastCourse).append("\n");

        return sb.toString();
    }

    
    /**
     * Finds the path between two professors.
     * @param professor1 the starting professor
     * @param professor2 the ending professor
     * @requires professor1 and professor2 are not null
     * @returns a string representing the path between professor1 and professor2
     */
    
    
    // Method to find paths between professors
    public String findPath(String professor1, String professor2) {
    	if (!graph.getG().containsKey(professor1) && !graph.getG().containsKey(professor2)) {
            if (professor1.equals(professor2)) {
                return "unknown professor " + professor1 + "\n";
            }
            return "unknown professor " + professor1 + "\nunknown professor " + professor2 + "\n";
        } else if (!graph.getG().containsKey(professor1)) {
            return "unknown professor " + professor1 + "\n";
        } else if (!graph.getG().containsKey(professor2)) {
            return "unknown professor " + professor2 + "\n";
        } else if (professor1.equals(professor2)) {
            return "path from " + professor1 + " to " + professor2 + ":\n";
        }

        // Perform BFS
        Queue<String> queue = new LinkedList<>();
        Map<String, List<String>> paths = new HashMap<>();
        queue.add(professor1);
        paths.put(professor1, new ArrayList<>());
        while (!queue.isEmpty()) {
            String currentProf = queue.poll();
            if (currentProf.equals(professor2)) {
                // Found the destination professor
                List<String> path = paths.get(currentProf);
                return formatPath(path, professor1, professor2);
            }
            Map<String, List<String>> neighbors = graph.getG().get(currentProf);
            if (neighbors != null) {
                // Sort neighbors lexicographically
                List<String> sortedNeighbors = new ArrayList<>(neighbors.keySet());
                Collections.sort(sortedNeighbors);
                for (String neighborProf : sortedNeighbors) {
                    List<String> courses = neighbors.get(neighborProf);
                    for (String course : courses) {
                        if (!paths.containsKey(neighborProf)) {
                            List<String> path = new ArrayList<>(paths.get(currentProf));
                            path.add(currentProf + " (" + course + ")");
                            paths.put(neighborProf, path);
                            queue.offer(neighborProf);
                        }
                    }
                }
            }
        }

        // No path found
        return "path from " + professor1 + " to " + professor2 + ":\nno path found\n";
    }
        
    
    
    
//public static void main(String[] arg) {
////////
//String file = "data/courses.csv";
//ProfessorPaths n = new ProfessorPaths();
//n.createNewGraph(file);
//System.out.println(n.findPath("Mohammed J. Zaki", "Wilfredo Colon"));
//}
}
