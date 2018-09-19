import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Анадер on 22.05.2018.
 */
public class Field {
    static final int SIZE_OF_FIELD = 10;// размер поля
    private static final int NUMBER_OF_SHIPS_5 = 0;// количество пятипалубников
    private static final int NUMBER_OF_SHIPS_4 = 1;// количество четырёхпалубников
    private static final int NUMBER_OF_SHIPS_3 = 2;// количество трёхпалубников
    private static final int NUMBER_OF_SHIPS_2 = 3;// количество двухпалубников
    private static final int NUMBER_OF_SHIPS_1 = 4;// количество однопалубников
    static final int TOTAL_NUMBER_OF_SHIPS = NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4 +
            NUMBER_OF_SHIPS_3 + NUMBER_OF_SHIPS_2 + NUMBER_OF_SHIPS_1; // общее количество кораблей
    private Random random = new Random();// просто рандом
    private Point[][] cellsOfPlayer;// двумерный массив ячеек поля игрока
    private Ship[] shipsOfPlayer;// массив кораблей игрока

    //конструктор для поля игрока
    Field() {
        cellsOfPlayer = new Point[SIZE_OF_FIELD + 2][SIZE_OF_FIELD + 2];//создаём квадратный массив игрового поля с дополнительными полями по периметру
        initField();
        shipsOfPlayer = new Ship[TOTAL_NUMBER_OF_SHIPS]; // создаём массив кораблей игрока

    }

    public Random getRandom() {
        return random;
    }

    public Point getPointOfField(int x, int y) {
        return cellsOfPlayer[x][y];
    }

    public void setPointOfField(int x, int y, Point point) {
        this.cellsOfPlayer[x][y] = point;
    }

    Point[][] getCellsOfPlayer() {
        return cellsOfPlayer;
    }

    Ship[] getShipsOfPlayer() {
        return shipsOfPlayer;
    }

    public void setShipsOfPlayer(Ship[] shipsOfPlayer) {
        this.shipsOfPlayer = shipsOfPlayer;
    }

    // инициализация поля, расставляем символы ~ по всем координатам поля
    private void initField() {
        for (int i = 0; i < this.cellsOfPlayer.length; i++) {
            for (int j = 0; j < this.cellsOfPlayer.length; j++) {
                this.cellsOfPlayer[i][j] = new Point(i, j, '~');
            }
        }
    }

    // отображаем поле игрока в консоли
    void showFieldOfPlayer(Player player) {
        //верхнее дополнительное поле с цифрами
        System.out.print("  ");
        for (int j = 0; j < SIZE_OF_FIELD; j++) {
            System.out.print(" " + j);
        }
        System.out.println();

        //Боковые поля плюс основное поле
        for (int i = 1; i < SIZE_OF_FIELD + 1; i++) {
            System.out.print(" " + (i - 1));
            for (int j = 1; j < SIZE_OF_FIELD + 1; j++) {
                System.out.print(" " + cellsOfPlayer[i][j].getValue());
            }
            System.out.print(" " + (i - 1));
            System.out.println();
        }

        //нижнее дополнительное поле с цифрами
        System.out.print("  ");
        for (int j = 0; j < SIZE_OF_FIELD; j++) {
            System.out.print(" " + j);
        }
        System.out.println();

        // подпись под полем игрока
        System.out.print("Поле с кораблями игрока" + player.getIdOfPlayer());
        if(!(player.getNameOfPlayer() == null))
            System.out.print(" " + player.getNameOfPlayer());
        System.out.println();
    }

    // установка координат корабля на поле игрока вручную
    void putShipOnFieldManually(Player player) {
        System.out.println(player.getNameOfPlayer() + ", расставляй корабли!");
        System.out.println("Тебе предстоит разместить на поле " + SIZE_OF_FIELD + " на " + SIZE_OF_FIELD + " 1 четырёхпалубник, 2 трёхпалубника, 3 двухпалубника и 4 однопалубника!");
        System.out.println("Расставлять координаты корабля нужно будет либо по вертикали, либо по горизонтали.");
        System.out.println("Между кораблями должен быть отступ в 1 клетку, по диагонали тоже!");
        System.out.println("Если облажаешься, будешь ставить корабль по новой!");
        System.out.println("Поехали!");
        System.out.println("\n Далее будем устанавливать начальные точки кораблей, остальные палубы достраиваются влево или вниз");
        System.out.println("в зависимости от того, горизонтальный корабль или вертикальный.");
        showFieldOfPlayer(player);
        initShips(false, player);
        showFieldOfPlayer(player);
        System.out.println("Отлично, ты расставил(а) все корабли! \n");
    }

    // установка координат корабля на поле игрока автоматически
    void putShipOnFieldAutomatically(Player player) {
        System.out.println("Сейчас всё раскидаем!");
        initShips(true, player);
        showFieldOfPlayer(player);
        System.out.println("Корабли расставлены!");
    }

    // расстановка всех кораблей на поле игрока
    private void initShips(boolean isAutomaticallyPutShipsOnField, Player player) {
        // расстановка пятипалубников
        int k = 0;
        for (int i = 0;
             i < NUMBER_OF_SHIPS_5;
             i++) {
            k++;
            String name = "пятипалубник_" + k;
            int size = 5;
            if(isAutomaticallyPutShipsOnField) {
                setShipAutomatically(player, i, name, size);
            } else {
                setShipManually(player, i, name, size);
            }
        }

        // расстановка четырёхпалубников
        k = 0;
        for (int i = NUMBER_OF_SHIPS_5;
             i < (NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4);
             i++) {
            k++;
            String name = "Четырёхпалубник_" + k;
            int size = 4;
            if(isAutomaticallyPutShipsOnField) {
                setShipAutomatically(player, i, name, size);
            } else {
                setShipManually(player, i, name, size);
            }
        }
        // расстановка трёхпалубников
        k = 0;
        for (int i = (NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4);
             i < (NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4 + NUMBER_OF_SHIPS_3);
             i++) {
            k++;
            String name = "Трёхпалубник_" + k;
            int size = 3;
            if(isAutomaticallyPutShipsOnField) {
                setShipAutomatically(player, i, name, size);
            } else {
                setShipManually(player, i, name, size);
            }
        }

        // расстановка двухпалубников
        k = 0;
        for (int i = (NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4 + NUMBER_OF_SHIPS_3);
             i < (NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4 + NUMBER_OF_SHIPS_3 + NUMBER_OF_SHIPS_2);
             i++) {
            k++;
            String name = "Двухпалубник_" + k;
            int size = 2;
            if(isAutomaticallyPutShipsOnField) {
                setShipAutomatically(player, i, name, size);
            } else {
                setShipManually(player, i, name, size);
            }
        }

        // расстановка однопалубников
        k = 0;
        for (int i = (NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4 + NUMBER_OF_SHIPS_3 + NUMBER_OF_SHIPS_2);
             i < (NUMBER_OF_SHIPS_5 + NUMBER_OF_SHIPS_4 + NUMBER_OF_SHIPS_3 + NUMBER_OF_SHIPS_2 + NUMBER_OF_SHIPS_1);
             i++) {
            k++;
            String name = "Однопалубник_" + k;
            int size = 1;
            if(isAutomaticallyPutShipsOnField) {
                setShipAutomatically(player, i, name, size);
            } else {
                setShipManually(player, i, name, size);
            }
        }
    }

    // установка корабля с заданными параметрами на поле вручную
    private void setShipManually(Player player, int i, String name, int size) {
        Point pointToPutShip;
        Point[] coordinatesOfShip;
        int k = 0;
        boolean isVertical;
        // спрашиваем координаты точки корабля, пока она не подойдёт
        do {
            if(k > 0)
                System.out.println("Здесь разместить корабль не получится! Попробуй ещё раз!");
            System.out.println("\n Ставим " + name);
            pointToPutShip = player.getCoordinateOfPointFromPlayer();
            isVertical = askIsVerticalShip(size);
            k++;
        } while (!isCorrectCoordinateForPutShip(pointToPutShip, shipsOfPlayer[i], name, isVertical, size));
        coordinatesOfShip = putShipOnField(pointToPutShip, shipsOfPlayer[i], name, isVertical, size); // ставим корабль на поле
        System.out.println("Отлично! Корабль установлен на поле!");
        shipsOfPlayer[i] = new Ship(name, isVertical, size, coordinatesOfShip); // создаём корабль и помещаем в массив кораблей игрока
        showFieldOfPlayer(player);
    }

    // установка корабля с заданными параметрами на поле автоматически
    private void setShipAutomatically(Player player, int i, String name, int size) {
        Point pointToPutShip;
        Point[] coordinatesOfShip;
        boolean isVertical;
        // рандомно получаем координаты точки корабля, пока она не подойдёт
        do {
            System.out.println("\nСтавим " + name + "...");
            pointToPutShip = new Point((random.nextInt(10) + 1), (random.nextInt(10) + 1), '~');
            isVertical = random.nextBoolean();
        } while(!isCorrectCoordinateForPutShip(pointToPutShip, shipsOfPlayer[i], name, isVertical, size));
        coordinatesOfShip = putShipOnField(pointToPutShip, shipsOfPlayer[i], name, isVertical, size);// ставим корабль на поле
        System.out.println("...Успешно установлен!");
        shipsOfPlayer[i] = new Ship(name, isVertical, size, coordinatesOfShip); // создаём корабль и помещаем в массив кораблей игрока
        showFieldOfPlayer(player);
    }

    // спрашиваем у игрока, вертикальный корабль или горизонтальный
    private boolean askIsVerticalShip(int size) {
        boolean isCorrectAnswer;
        boolean answer = false;
        if(size > 1) {
            // пока ответ не будет корректный, будем спрашивать
            do {
                System.out.println("Если будешь ставить корабль горизонтально, введи 0. Если вертикально - 1");
                String r = new Scanner(System.in).nextLine();
                switch (r) {
                    case "0":
                        System.out.println("Хочешь горизонтально? Ок!");
                        isCorrectAnswer = true;
                        answer = false;
                        break;
                    case "1":
                        System.out.println("Хочешь вертикально? Ок!");
                        isCorrectAnswer = true;
                        answer = true;
                        break;
                    default:
                        System.out.println("Похоже, ты не понял(а) вопрос! Прочти ещё раз, что нужно сделать!");
                        isCorrectAnswer = false;
                }
            } while (!isCorrectAnswer);
        }
        return answer;
    }

    //проверяем, сможем ли мы разместить такой корабль на поле
    private boolean isCorrectCoordinateForPutShip(Point point, Ship ship, String name, boolean isVertical, int size) {
        if (isVertical){
            if(point.getCoordinateX() >= 1 && (point.getCoordinateX() + size - 1) <= 10) { // проверка на попадание в поле
                for (int i = (point.getCoordinateX() - 1); i < (point.getCoordinateX() + size + 1); i++) { // проверка на пересечение с другими кораблями по вертикали
                    for (int j = point.getCoordinateY() - 1; j <= point.getCoordinateY() + 1; j++) { // проверка на пересечение с другими кораблями по горизонтали
                        if (cellsOfPlayer[i][j].getValue() == 'O') {
                            return false;
                        }
                    }
                }
            } else
                return false;
        } else {
            if(point.getCoordinateY() >= 1 && (point.getCoordinateY() + size - 1) <= 10) {
                for (int j = (point.getCoordinateY() - 1); j < (point.getCoordinateY() + size + 1); j++) {// проверка на пересечение с другими кораблями по горизонтали
                    for (int i = point.getCoordinateX() - 1; i <= point.getCoordinateX() + 1; i++) {// проверка на пересечение с другими кораблями по вертикали
                        if (cellsOfPlayer[i][j].getValue() == 'O') {
                            return false;
                        }
                    }
                }
            } else
                return false;
        }
        return true;
    }

    // установка координат корабля на поле
    private Point[] putShipOnField(Point point, Ship ship, String name, boolean isVertical, int size) {
        Point[] coordinatesOfShip = new Point[size];
        if (isVertical){
            for (int i = 0; i < coordinatesOfShip.length; i++) {
                coordinatesOfShip[i] = new Point(point.getCoordinateX() + i, point.getCoordinateY(), 'O');
            }
        } else {
            for (int j = 0; j < coordinatesOfShip.length; j++) {
                coordinatesOfShip[j] = new Point(point.getCoordinateX(), point.getCoordinateY() + j, 'O');
            }
        }
        for (Point aCoordinatesOfShip : coordinatesOfShip) {
            cellsOfPlayer[aCoordinatesOfShip.getCoordinateX()][aCoordinatesOfShip.getCoordinateY()] = aCoordinatesOfShip;
        }
        return coordinatesOfShip;
    }

    // метод проверки и получения координат подбитого корабля
    public ArrayList<Point> getDamagedPointsOfAliveShip() {
        ArrayList<Point> damagedPoints = new ArrayList<>();
        for (int i = 0; i < getShipsOfPlayer().length; i++) {
            if (getShipsOfPlayer()[i].isAliveShip())
                if (!getShipsOfPlayer()[i].isNotDamagedShip())
                    for (int j = 0; j < getShipsOfPlayer()[i].getSizeOfShip(); j++) {
                        if(getShipsOfPlayer()[i].getCoordinatesOfShip()[j].getValue() == 'X')
                            damagedPoints.add(getShipsOfPlayer()[i].getCoordinatesOfShip()[j]);
                    }
        }
        return damagedPoints;
    }
}
