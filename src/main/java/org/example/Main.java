package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<List<Integer>> initialStateSimple = new ArrayList<>();
        initialStateSimple.add(Arrays.asList(1, 2)); // Пробирка 0
        initialStateSimple.add(Arrays.asList(2, 1)); // Пробирка 1
        initialStateSimple.add(Arrays.asList(0, 0)); // Пробирка 2 (пустая)
        int tubeVolumeSimple = 2;

        System.out.println("Решение для упрощенного примера:");
        List<Move> solutionSimple = solve(initialStateSimple, tubeVolumeSimple);

        if (solutionSimple != null) {
            System.out.println("Найдено решение за " + solutionSimple.size() + " ходов:");
            for (Move move : solutionSimple) {
                System.out.print(move + " ");
            }
            System.out.println();
        } else {
            System.out.println("Решение не найдено.");
        }

        System.out.println("----------------------------------------");

    }

    public static List<Move> solve(List<List<Integer>> initialTubes, int tubeVolume) {
        Set<Integer> allColors = new HashSet<>();

        for (List<Integer> tube : initialTubes) {
            for (Integer color : tube) {
                if (color != 0) {
                    allColors.add(color);
                }
            }
        }

        int numColors = allColors.size();

        Board initialBoard = new Board(initialTubes, tubeVolume, numColors);
        Queue<StateNode> queue = new LinkedList<>();
        Set<Board> visited = new HashSet<>();

        queue.add(new StateNode(initialBoard, new ArrayList<>()));
        visited.add(initialBoard);

        while (!queue.isEmpty()) {
            StateNode currentNode = queue.poll();
            Board currentBoard = currentNode.board;
            List<Move> currentPath = currentNode.path;

            if (currentBoard.isGoalState()) {
                return currentPath;
            }

            int numTubes = currentBoard.tubes.size();
            for (int sourceIdx = 0; sourceIdx < numTubes; sourceIdx++) {
                for (int destIdx = 0; destIdx < numTubes; destIdx++) {
                    if (sourceIdx == destIdx) {
                        continue;
                    }

                    Board nextBoard = currentBoard.makeMove(sourceIdx, destIdx);

                    if (nextBoard != null && !visited.contains(nextBoard)) {
                        visited.add(nextBoard);
                        List<Move> newPath = new ArrayList<>(currentPath);
                        newPath.add(new Move(sourceIdx, destIdx));
                        queue.add(new StateNode(nextBoard, newPath));
                    }
                }
            }
        }
        return null;
    }
}