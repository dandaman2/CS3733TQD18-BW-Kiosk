package edu.wpi.cs3733d18.teamQ.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

//  A* Algorithm
public class Dijkstra extends Algorithm{
    /*
    findShortestPath:
    takes in 2 Nodes
    find shortest path from node 1 to node 2 by
    returns Path as a List of Node ( ArrayList<Node> )
    Dijkstra Algorithm
    */
    public ArrayList<Node> findPath(Node n1, Node n2, ArrayList<String> noTypes) {

        for (int i = 0; i < NodesList.size(); i++) {
            Node now = NodesList.get(i);
            if (n1.getNodeID().equals(now.getNodeID())) {
                n1.setIndex(now.getIndex());
            }
            if (n2.getNodeID().equals(now.getNodeID())) {
                n2.setIndex(now.getIndex());
            }
        }

        int start = n1.getIndex();
        int finish = n2.getIndex();


        List<Double> distance = new ArrayList<Double>(Collections.<Double>nCopies(N, (double) INFINITY));
        List<Integer> parent = new ArrayList<Integer>(Collections.nCopies(N, 0));
        List<Integer> fix = new ArrayList<Integer>(Collections.nCopies(N, 0));


        PriorityQueue<MyPair> pq=new PriorityQueue<>();

        distance.set(start, 0.0);
        parent.set(start, -1);

        pq.add(new MyPair(0.0, 0.0, start));


        // THIS IS Dijkstra
        // Does MAGIC
        while (!pq.isEmpty()) {
            MyPair TOP = pq.poll();

            int now = TOP.getNodeIndex();

            if (now == n2.getIndex()) {
                break; // found n2
            }

            if (fix.get(now) == 1) continue;
            fix.set(now, 1);

            for (int i = 0; i < neighbours[now].size(); i++) {
                int to = neighbours[now].get(i);

                if (!isRestricted(NodesList.get(to),NodesList.get(now), noTypes)) {
                    continue;
                }

                if (distance.get(to) > distance.get(now) + edge_length[now].get(i)) {
                    distance.set(to, distance.get(now) + edge_length[now].get(i));
                    parent.set(to, now);
                    pq.add(
                            new MyPair(
                                    distance.get(to)     // real distance
                                            +
                                            0, // heuristic distance
                                    distance.get(to),
                                    to
                            )
                    );
                }
            }
        }
        System.out.println("EE KACI      = " + distance.get(n2.getIndex()) );

        System.out.println("Parent of all Nodes");
        for (int i = 0; i < parent.size(); i++) {
            if (parent.get(i) == 0) continue;
        }

        //  RETRIEVE THE PATH
        return RetrievePath(finish,parent);
    }
}
