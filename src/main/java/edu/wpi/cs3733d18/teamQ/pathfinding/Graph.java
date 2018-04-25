package edu.wpi.cs3733d18.teamQ.pathfinding;

import java.util.*;

public class Graph {
    ArrayList<Node> NodesList;


    static final int N = 3000;
    static final int INFINITY = 1000000000;
    ArrayList<Integer>[] neighbours;
    ArrayList<Double>[] edge_length;
    HashMap<String, Integer> ID_map = new HashMap<>();


    public ArrayList<Node> findPath(Node n1, Node n2, ArrayList<String> noTypes){
        return null;
    }

    public ArrayList<Node> findShortestPathByType(Node youHere, String type, ArrayList<String> restrictedTYPES) {
        return null;
    }

    public void setNodesList(ArrayList<Node> nodesList) {
        NodesList = nodesList;
    }

    public Graph() {}

    public ArrayList<Node> getNodesList() {
        return NodesList;
    }

    public ArrayList<Integer>[] getNeighbours() {
        return neighbours;
    }

    public ArrayList<Double>[] getEdge_length() {
        return edge_length;
    }

    public void init(){
        neighbours = new ArrayList[N+1];
        edge_length = new ArrayList[N+1];
        NodesList = new ArrayList<Node>(Collections.nCopies(N, new Node(0.0,0.0,0,0)));

        for(int i=0;i<N;i++) {
            neighbours[i] = new ArrayList<>();
            edge_length[i] = new ArrayList<>();
        }
    }

    public void init2(ArrayList<Node> nodes,ArrayList<Edge> edges){
        NodesList = new ArrayList<Node>(Collections.nCopies(N, new Node(0.0,0.0,0,0)));

        // initialize graph components
        neighbours = new ArrayList[N+1];
        edge_length = new ArrayList[N+1];

        for(int i=0;i<N;i++) {
            neighbours[i] = new ArrayList<>();
            edge_length[i] = new ArrayList<>();
        }


        int counter = 0;

        ArrayList<Node> ALL_NODES = new ArrayList<>();

        // storing all nodes in ALL_NODES
        // indexing all nodes
        for(int i=0;i<edges.size();i++){
            Edge e = edges.get(i);
            if(e == null || e.getDistance() <= 0.00000001)continue;

            Node n1 = e.getStartNode();
            Node n2 = e.getEndNode();
            if(!e.isEnabled())continue;
            if(!n1.isEnabled())continue;
            if(!n2.isEnabled())continue;

            String n1id= n1.getNodeID();
            String n2id= n2.getNodeID();

            if(!ID_map.containsKey(n1id)){
                ID_map.put(n1id,++counter);
            }
            n1.setIndex(ID_map.get(n1id));


            if(!ID_map.containsKey(n2id)){
                ID_map.put(n2id,++counter);
            }
            n2.setIndex(ID_map.get(n2id));
        }

     //   System.out.println(nodes.size() + " " + edges.size() + " <-------------------------------------------");

        /// setting nodes, from edges
        for(int i=0;i<edges.size();i++){
            Edge e = edges.get(i);
            if(e == null || e.getDistance() <= 0.00000001)continue;
            Node n1 = e.getStartNode();
            Node n2 = e.getEndNode();
            if(!e.isEnabled())continue;
            if(!n1.isEnabled())continue;
            if(!n2.isEnabled())continue;
            int from = e.getStartNode().getIndex();
            int to = e.getEndNode().getIndex();

     //       System.out.println(from + " " + to);

            NodesList.set(from, e.getStartNode());
            NodesList.set(to, e.getEndNode());
        }

        // add edges to graph
        for(int i=0;i<edges.size();i++){
            Edge e = edges.get(i);
            Node n1 = e.getStartNode();
            Node n2 = e.getEndNode();
            if(!e.isEnabled())continue;
            if(!n1.isEnabled())continue;
            if(!n2.isEnabled())continue;
            this.addEdge(edges.get(i));
        }
        System.out.println("Done Init2");
    }

    // adds edge to graph
    public void addEdge(Edge edge){
        if(edge == null || edge.getDistance() <= 0.00000001)return;
/*
        System.out.print(edge.getDistance()+ " ");
        System.out.print(edge.getStartNode().getNameLong() + "\n");
        System.out.print(edge.getEndNode().getNameLong() + " ");
*/
        Edge e = edge;
        Node n1 = e.getStartNode();
        Node n2 = e.getEndNode();
        if(!e.isEnabled())return;
        if(!n1.isEnabled())return;
        if(!n2.isEnabled())return;

        int from = edge.getStartNode().getIndex();
        int to = edge.getEndNode().getIndex();
        double len = edge.getDistance();

        if(edge.getStartNode().getFloor()!=edge.getEndNode().getFloor()){ // if elev or stairs
            len = 50.0;
        }

//        System.out.println(from + "    *****      " + to);

        neighbours[from].add(to);
        neighbours[to].add(from);

        len = edge.getStartNode().calcDistance(edge.getEndNode());

        edge_length[from].add(len);
        edge_length[to].add(len);

        NodesList.set(from, edge.getStartNode());
        NodesList.set(to, edge.getEndNode());
    }
}
