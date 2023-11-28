package maria.anikina.game;

import maria.anikina.exception.FieldNotCreatedException;

import java.util.Arrays;
import java.util.Scanner;

public class Game {
	private int n; // размеры поля
	private int[][] field;
	private int[][] nextStepField;
	private int livingCells = 0;

	public void startGame() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Создайте поле размером n*n. Введите желаемое значение n");
		n = scanner.nextInt();
		if (n < 2) {
			System.out.println("Введено некорректное значение n");
			return;
		}
		createField();
		System.out.println("Сколько живых ячеек планируете создать?");
		int count = scanner.nextInt();
		int counter = 0;
		while (counter < count) {
			System.out.println("Введите ряд и столбец ячейки соответственно, которую нужно объявить живой");
			int a = scanner.nextInt();
			if (a < 1 || a > n) {
				System.out.println("Передана некорректная координата, указывающая на ряд");
				return;
			}
			int b = scanner.nextInt();
			if (b < 1 || b > n) {
				System.out.println("Передана некорректная координата, указывающая на столбец");
				return;
			}
			fillWithLiveField(a, b);
			counter++;
		}
		System.out.println("Игра началась!");
		System.out.println("1 - это живая ячейка, 0 - мертвая");
		System.out.println("Поле, с которого начинается игра:");
		displayArrayElements(field);
		updateCellState(false);
		updateCellState(true);
		System.out.println("Следующий шаг");
		displayArrayElements(nextStepField);
		while (!Arrays.deepEquals(field, nextStepField)) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (field[i][j] != nextStepField[i][j]) {
						field[i][j] = nextStepField[i][j];
					}
				}
			}
			updateCellState(false);
			updateCellState(true);
			System.out.println("Следующий шаг");
			displayArrayElements(nextStepField);
		}
		System.out.println("Конец игры!");
	}

	public void createField() {
		field = new int[n][n];
		nextStepField = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				field[i][j] = 0;
				nextStepField[i][j] = 0;
			}
		}
	}

	public void fillWithLiveField(int line, int column) {
		try {
			if (field == null) {
				throw new FieldNotCreatedException("Поле не создано");
			}
			if (field[line - 1][column - 1] == 1) {
				System.out.println("Ячейка с координатами " + line + " и " + column + " уже является живой");
			} else {
				field[line - 1][column - 1] = 1;
				nextStepField[line - 1][column - 1] = 1;
			}
		} catch (FieldNotCreatedException e) {
			System.out.println("Чтобы заполнить поле жизнями, необходимо его создать");
		}
	}

	public void updateCellState(Boolean isAdditionLife) {
		int fieldSize = n;
		for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					Boolean isLivingCell = field[i][j]==(1);
					if (isLivingCell.equals(!isAdditionLife)) {
						if (i > 0 && i < fieldSize - 1) {
							if (j > 0) {
								countLiveCells(i - 1, j - 1);
								countLiveCells(i, j - 1);
								countLiveCells(i + 1, j - 1);
							}
							countLiveCells(i - 1, j);
							countLiveCells(i + 1, j);
							if (j < fieldSize - 1) {
								countLiveCells(i - 1, j + 1);
								countLiveCells(i, j + 1);
								countLiveCells(i + 1, j + 1);
							}
							createOrDeleteLive(livingCells, isAdditionLife, i, j);
							livingCells = 0;
						} else if (i == 0) {
							if (j  > 0) {
								countLiveCells(i, j - 1);
								countLiveCells(i + 1, j - 1);
							}
							countLiveCells(i + 1, j);
							if (j < fieldSize - 1) {
								countLiveCells(i, j + 1);
								countLiveCells(i + 1, j + 1);
							}
							createOrDeleteLive(livingCells, isAdditionLife, i, j);
							livingCells = 0;
						} else {
							if (j  > 0) {
								countLiveCells(i - 1, j - 1);
								countLiveCells(i, j - 1);
							}
							countLiveCells(i - 1, j);
							if (j < fieldSize - 1) {
								countLiveCells(i - 1, j + 1);
								countLiveCells(i, j + 1);
							}
							createOrDeleteLive(livingCells, isAdditionLife, i, j);
							livingCells = 0;
						}
					}

			}
		}
	}

	public void createOrDeleteLive(int livingCells, Boolean isAdditionLife, int i, int j) {
		if (isAdditionLife.equals(false)) {
			if (livingCells > 3 || livingCells < 2) {
				nextStepField[i][j] = 0;;
			}
		} else {
			if (livingCells == 3) {
				nextStepField[i][j] = 1;
			}
		}
	}

	public void displayArrayElements(int[][] array) {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print(array[i][j] + " ");
			}
			System.out.println(" ");
		}
		System.out.println(" ");
	}

	public void countLiveCells(int i, int j) {
		if (field[i][j] == 1) {
			livingCells++;
		}
	}
}
