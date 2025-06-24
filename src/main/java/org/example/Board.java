package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {
    List<List<Integer>> tubes;
    int tubeVolume;
    int numColors;

    public Board (List<List<Integer>> tubes, int tubeVolume, int numColors) {
        this.tubes = tubes;
        this.tubeVolume = tubeVolume;
        this.numColors = numColors;
    }

    public Board deepCopy() {
        List<List<Integer>> newTubes = new ArrayList<>();
        for (List<Integer> tube : this.tubes) {
            newTubes.add(new ArrayList<>(tube));
        }
        return new Board(newTubes, this.tubeVolume, this.numColors);
    }

    public boolean isGoalState() {
        int filledTubesCount = 0;
        for (List<Integer> tube : tubes) {
            List<Integer> nonZeroColors = new ArrayList<>();
            for (Integer color : tube) {
                if (color != 0) {
                    nonZeroColors.add(color);
                }
            }

            if (nonZeroColors.isEmpty()) { // Пробирка пуста
                continue;
            }

            // Пробирка не пуста, проверяем, что все элементы одинаковые
            int firstColor = nonZeroColors.get(0);
            for (int i = 1; i < nonZeroColors.size(); i++) {
                if (nonZeroColors.get(i) != firstColor) {
                    return false; // Смешанные цвета в одной пробирке
                }
            }

            // Проверяем, что пробирка полностью заполнена, если содержит цвет
            if (nonZeroColors.size() != tubeVolume) {
                return false; // Не полностью заполнена одним цветом
            }

            filledTubesCount++;
        }

        return filledTubesCount == numColors;
    }

    public Board makeMove(int sourceIdx, int destIdx) {
        Board newBoard = this.deepCopy();
        List<Integer> sourceTube = newBoard.tubes.get(sourceIdx);
        List<Integer> destTube = newBoard.tubes.get(destIdx);

        // 1. Проверка, что пробирка-источник не пуста
        int topSourceColor = 0;
        int topSourceColorCount = 0;
        int sourceTopIndex = -1;

        for (int i = tubeVolume - 1; i >= 0; i--) {
            if (sourceTube.get(i) != 0) {
                topSourceColor = sourceTube.get(i);
                sourceTopIndex = i;
                break;
            }
        }
        if (topSourceColor == 0) { // Пробирка-источник пуста
            return null;
        }

        // Подсчет количества верхних капель одного цвета в источнике
        for (int i = sourceTopIndex; i >= 0; i--) {
            if (sourceTube.get(i) == topSourceColor) {
                topSourceColorCount++;
            } else {
                break;
            }
        }

        // 2. Проверка свободного места в пробирке-назначении
        int destEmptySpace = 0;
        int destBottomEmptyIndex = -1;
        for (int i = 0; i < tubeVolume; i++) {
            if (destTube.get(i) == 0) {
                destEmptySpace++;
                if (destBottomEmptyIndex == -1) {
                    destBottomEmptyIndex = i;
                }
            }
        }

        if (destEmptySpace == 0) { // Пробирка-назначение полна
            return null;
        }

        // 3. Проверка правила: переливать можно только в пустую пробирку или если верхний цвет совпадает
        int topDestColor = 0;
        for (int i = tubeVolume - 1; i >= 0; i--) {
            if (destTube.get(i) != 0) {
                topDestColor = destTube.get(i);
                break;
            }
        }

        if (topDestColor != 0 && topDestColor != topSourceColor) {
            return null; // Верхние цвета не совпадают
        }

        // 4. Определяем, сколько жидкости переливать
        int amountToPour = Math.min(topSourceColorCount, destEmptySpace);
        if (amountToPour == 0) {
            return null; // Ничего не можем перелить
        }

        // Выполняем переливание: сначала удаляем из источника
        List<Integer> pouredColors = new ArrayList<>();
        for (int i = 0; i < amountToPour; i++) {
            pouredColors.add(0, sourceTube.get(sourceTopIndex - i)); // Добавляем в начало, чтобы сохранить порядок
            sourceTube.set(sourceTopIndex - i, 0); // Обнуляем
        }

        // Затем добавляем в назначение
        for (int i = 0; i < amountToPour; i++) {
            destTube.set(destBottomEmptyIndex + i, pouredColors.get(i));
        }

        return newBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        // Сравниваем списки списков
        if (tubes.size() != board.tubes.size()) return false;
        for (int i = 0; i < tubes.size(); i++) {
            if (!tubes.get(i).equals(board.tubes.get(i))) {
                return false;
            }
        }
        return true;
    }

    // Необходимо для использования Board в Set/HashMap
    @Override
    public int hashCode() {
        // Генерируем хэш из всех вложенных списков
        return Objects.hash(tubes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Board:\n");
        for (int i = 0; i < tubes.size(); i++) {
            sb.append("  Tube ").append(i).append(": ").append(tubes.get(i)).append("\n");
        }
        return sb.toString();
    }

}
