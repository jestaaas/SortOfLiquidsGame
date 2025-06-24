package org.example;

import java.util.Objects;

public class Move {
    int source;
    int destination;

    public Move(int source, int destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "(" + source + ", " + destination + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return source == move.source && destination == move.destination;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination);
    }
}
