package org.example;

import java.util.List;

public class StateNode {
    Board board;
    List<Move> path;

    public StateNode(Board board, List<Move> path) {
        this.board = board;
        this.path = path;
    }
}
