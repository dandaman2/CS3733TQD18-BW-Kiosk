package edu.wpi.cs3733d18.teamQ.pathfinding;

import java.util.*;

// DFS Depth-First-Search
public class DFS extends Algorithm{
    List<Integer> fixGlob = new ArrayList<Integer>(Collections.nCopies(N, 0));
    List<Integer> parentGlob = new ArrayList<Integer>(Collections.nCopies(N, 0));

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


        Queue<Integer> q = new PriorityQueue<>();

        DFS(start,-1,noTypes);

        //  RETRIEVE THE PATH
        return RetrievePath(finish,parentGlob);
    }

    private int DFS(int NODE,int par, ArrayList<String> noTypes){
        if(fixGlob.get(NODE) == 1)return 0;
        fixGlob.set(NODE, 1);
        parentGlob.set(NODE,par);

        for(int i=0;i<neighbours[NODE].size();i++){
            int to = neighbours[NODE].get(i);

            if(!isRestricted(NodesList.get(NODE),NodesList.get(to),noTypes)){
                continue;
            }

            DFS(to,NODE,noTypes);
        }

        return 0;
    }
}
