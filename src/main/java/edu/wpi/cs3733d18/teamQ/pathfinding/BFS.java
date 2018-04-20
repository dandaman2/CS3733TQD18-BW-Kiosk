package edu.wpi.cs3733d18.teamQ.pathfinding;

import java.util.*;

// BFS Breadth-First-Search
public class BFS extends Algorithm{
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


        Queue<Integer> q = new PriorityQueue<>();

        fix.set(start, 1);
        parent.set(start, -1);
        q.add(start);

        while (!q.isEmpty()) {
            int now = q.poll();
            double len = distance.get(now);

            if (now == finish) {
                break;
            }

            for (int i = 0; i < neighbours[now].size(); i++) {
                int to = neighbours[now].get(i);

                if (!isRestricted(NodesList.get(to),NodesList.get(now), noTypes)) {
                    continue;
                }

                if (fix.get(to) == 0) {
                    fix.set(to, 1);
                    parent.set(to, now);
                    distance.set(to, len + 1);
                    q.add(to);
                }
            }
        }

        //  RETRIEVE THE PATH
        return RetrievePath(finish,parent);
    }
}
