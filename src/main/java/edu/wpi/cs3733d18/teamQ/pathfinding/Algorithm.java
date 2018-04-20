package edu.wpi.cs3733d18.teamQ.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Algorithm extends Graph {

    /*
    MyPair Class
    Used for PriorityQueue
    Overriden Comparator
    TODO: add heuristics to PriorityQueue
        Done
    TODO: add heuristics to comparator
        Done
    */

    public class MyPair implements Comparable<MyPair> {
        private Double Tot_Dist;
        private Double Real_Dist;
        private int nodeIndex;

        public MyPair(Double tot_Dist, Double real_Dist, int nodeIndex) {
            Tot_Dist = tot_Dist;
            Real_Dist = real_Dist;
            this.nodeIndex = nodeIndex;
        }

        @Override
        public int compareTo(MyPair other) {
            if(this.getTot_Dist() < other.getTot_Dist()) return -1;
            if(this.getTot_Dist() > other.getTot_Dist()) return 1;


            if(this.getReal_Dist() < other.getReal_Dist()) return -1;
            if(this.getReal_Dist() > other.getReal_Dist()) return 1;
            return 0;
        }

        // getters


        public Double getTot_Dist() {
            return Tot_Dist;
        }

        public Double getReal_Dist() {
            return Real_Dist;
        }

        public int getNodeIndex() {
            return nodeIndex;
        }
    }


    boolean isRestricted(Node node1, Node node2, ArrayList<String> noTypes) {
        for (int i = 0; i < noTypes.size(); i++)
            if (node1.getType().equals(noTypes.get(i)) &&
                    node2.getType().equals(noTypes.get(i))) return false;
        return true;
    }


    ArrayList<Node> RetrievePath(int finish, List<Integer> parent) {
        ArrayList<Node> ReturnList_NODES = new ArrayList<>();
        ArrayList<Integer> PathInt = new ArrayList<>();

        int pathIndex = finish;

        System.out.println(finish);
//        System.out.println(parent.get(pathIndex));

        if (parent.get(pathIndex) == 0) {
            System.out.println("NO PATH!!!");
            return ReturnList_NODES;
        }

        do {
            PathInt.add(pathIndex);
            pathIndex = parent.get(pathIndex);
        } while (pathIndex != -1);
        Collections.reverse(PathInt);

        for (int i = 0; i < PathInt.size(); i++) {
            ReturnList_NODES.add(NodesList.get(PathInt.get(i)));
            System.out.println(PathInt.get(i));
        }
        System.out.println("Done Path");

        return ReturnList_NODES;
    }
}
