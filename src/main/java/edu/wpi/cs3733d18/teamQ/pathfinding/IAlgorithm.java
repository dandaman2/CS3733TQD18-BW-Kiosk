package edu.wpi.cs3733d18.teamQ.pathfinding;

import java.util.ArrayList;
import java.util.List;

public interface IAlgorithm {
    public ArrayList<Node> RetrievePath(int finish, List<Integer> parent);
}