package visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class CallGraph {

    private static final Map<String, Collection<String>> methodCallMap = new HashMap<>();

    public static void addMethodCall(String callerMethod, String calledMethod) {
        methodCallMap.computeIfAbsent(callerMethod, k -> new ArrayList<>()).add(calledMethod);
    }

    public static void displayCallGraph() {
        for (Map.Entry<String, Collection<String>> entry : methodCallMap.entrySet()) {
            System.out.println("Méthode : " + entry.getKey() + " appelle " + entry.getValue());
        }
    }

    public static void visualizeCallGraph() {
        System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Graphe d'appel");

        for (Map.Entry<String, Collection<String>> entry : methodCallMap.entrySet()) {
            String from = entry.getKey();

            if (graph.getNode(from) == null) {
                graph.addNode(from).setAttribute("ui.label", from);
            }

            for (String to : entry.getValue()) {
                if (graph.getNode(to) == null) {
                    graph.addNode(to).setAttribute("ui.label", to);
                }

                String edgeId = from + "->" + to;
                if (graph.getEdge(edgeId) == null) {
                    graph.addEdge(edgeId, from, to, true);
                }
            }
        }

        graph.setAttribute("ui.stylesheet",
                "node { fill-color: lightblue; size: 20px; text-size: 14px; } " +
                        "edge { arrow-shape: arrow; }");
        graph.display();
    }
}