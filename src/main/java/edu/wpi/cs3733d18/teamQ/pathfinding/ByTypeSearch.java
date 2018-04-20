package edu.wpi.cs3733d18.teamQ.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class ByTypeSearch extends Algorithm {
    /*
    findShortestPathByPath:
        takes in a node and a type (string)
        find shortest path from node to a node with a give Type
        returns Path as a List of Node ( ArrayList<Node> )
    */
    public ArrayList<Node> findShortestPathByType(Node n1, String Type, ArrayList<String> noTypes) {

        for (int i = 0; i < NodesList.size(); i++) {
            Node now = NodesList.get(i);
            if (n1.getNodeID().equals(now.getNodeID())) {
                n1.setIndex(now.getIndex());
            }
        }

        int start = n1.getIndex();
        int finish = -1;

        List<Double> distance = new ArrayList<Double>(Collections.<Double>nCopies(N, (double) INFINITY));
        List<Integer> parent = new ArrayList<Integer>(Collections.nCopies(N, 0));
        List<Integer> fix = new ArrayList<Integer>(Collections.nCopies(N, 0));


        PriorityQueue<MyPair> pq = new PriorityQueue<>();

        distance.set(start, 0.0);
        parent.set(start, -1);

        pq.add(new MyPair(0.0, 0.0, start));
//        System.out.println(start + " ======!======= ");
//        System.out.println(n1.getNodeID() + " " + n1.getIndex() + " ======!======= ");
//        System.out.println(Type);


        // THIS IS DIJKSTRA
        // Does MAGIC
        while (!pq.isEmpty()) {
            MyPair TOP = pq.poll();

            int now = TOP.getNodeIndex();


//            System.out.println(now + " =============== " + NodesList.get(now).getType());

            if (NodesList.get(now).getType().equals(Type)) {
                finish = now;
                System.out.println(finish + "!!!!!!!!!!!!!!!!");
                break;
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
                    pq.add(new MyPair(distance.get(to), 0.0, to));
                }
            }
        }


        //  RETRIEVE THE PATH
        return RetrievePath(finish,parent);
    }
}
