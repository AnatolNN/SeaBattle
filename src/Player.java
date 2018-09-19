import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Анадер on 22.05.2018.
 */
public class Player {
    private boolean isItHuman;// Играет человек или компьютер
    private String nameOfPlayer; // Имя игрока
    private int numberOfShipsAlive; // Количество живых кораблей игрока
    private boolean isAutomaticallyPutShipOnField; // Автоматически расстявлять корабли на поле или вручную
    private Field fieldOfPlayer; // Поле игрока

    private static int numberOfPlayers; // Число игроков
    private int idOfPlayer; // идентификатор игрока для жребия для первого хода
    private int numberOfShots; // количество выстрелов, сделанных игроком
    private int numberOfHits; // количество попаданий по кораблям соперника

    // конструктор
    public Player(boolean isItHuman){
        numberOfPlayers++;
        this.isItHuman = isItHuman;
        askName(isItHuman);
        fieldOfPlayer = new Field();
        idOfPlayer += numberOfPlayers;
        numberOfShipsAlive = Field.TOTAL_NUMBER_OF_SHIPS;
        numberOfShots = 0;
        numberOfHits = 0;
    }

    public boolean isItHuman() {
        return isItHuman;
    }

    public String getNameOfPlayer() {
        return nameOfPlayer;
    }

    public int getNumberOfShipsAlive() {
        return numberOfShipsAlive;
    }

    public boolean isAutomaticallyPutShipOnField() {
        return isAutomaticallyPutShipOnField;
    }

    public Field getFieldOfPlayer() {
        return fieldOfPlayer;
    }

    public int getIdOfPlayer() {
        return idOfPlayer;
    }

    public int getNumberOfShots() {
        return numberOfShots;
    }

    public int getNumberOfHits() {
        return numberOfHits;
    }

    public void setNumberOfHits(int numberOfHits) {
        this.numberOfHits = numberOfHits;
    }

    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    // спрашивает имя или даёт имя по умолчанию, если компьютер
    private void askName (boolean isItHuman){
        if(isItHuman) {
            System.out.println("Человек, введи своё имя или псевдоним:");
            this.nameOfPlayer = new Scanner(System.in).nextLine();
        } else {
            if (numberOfPlayers > 1) {
                System.out.println("Ладно, тогда я МЕГАТРОН!");
                this.nameOfPlayer = "МЕГАТРОН";
            } else {
                System.out.println("Ок, тогда я ОПТИМУС ПРАЙМ!");
                this.nameOfPlayer = "ОПТИМУС";
            }
        }
    }

    // спрашивает автоматически или вручную расставить корабли игрока
    void askIsAutomaticallyPutShipOnField() {
        if(isItHuman) {
            System.out.println(nameOfPlayer + ", если хочешь сам(а) расставить корабли, введи 0.\nЕсли доверишь это дело классу Random, нажми 1.");
            String r = new Scanner(System.in).nextLine();
            switch (r) {
                case "0":
                    System.out.println("Хочешь сам(а) расставить корабли? Ок!");
                    this.isAutomaticallyPutShipOnField = false;
                    break;
                case "1":
                    System.out.println("Доверяешь мне? Ок!");
                    this.isAutomaticallyPutShipOnField = true;
                    break;
                default:
                    System.out.println("Похоже, ты не понял(а) вопрос! Прочти ещё раз, что нужно сделать!");
                    askIsAutomaticallyPutShipOnField();
            }
        } else {
            System.out.println("Свои корабли я расставлю самостоятельно!");
            this.isAutomaticallyPutShipOnField = true;
        }
    }

    // спрашивает координату для устаноуки корабля и или для выстрела у живого игрока в консоли
    Point getCoordinateOfPointFromPlayer() {
        int coordinateX;
        int coordinateY;
        String x;
        String y;
        do {
            System.out.println(nameOfPlayer + ", введи координату по вертикали от 0 до 9:");
            x = new Scanner(System.in).nextLine();
        } while (!checkInputOfPlayer(x));
        coordinateX = Integer.parseInt(x);
        do {
            System.out.println(nameOfPlayer + ", введи координату по горизонтали от 0 до 9:");
            y = new Scanner(System.in).nextLine();
        } while (!checkInputOfPlayer(y));
        coordinateY = Integer.parseInt(y);
        System.out.println(nameOfPlayer + " выбрал(а) точку [" + coordinateX + ", " + coordinateY + "]");
        numberOfShots++;
        return new Point(coordinateX + 1, coordinateY + 1, '0');
    }

    // автоматически выдаёт точку для выстрела компьютера, здесь заложеня вся логика для выстрелов компа
    Point getCoordinateOfPointFromComputer(Player targetPlayer) {
        int coordinateX; // временные координаты точки для проверки
        int coordinateY;
        boolean isRepeat;// повторять ли итерацию
        int k = 0;// счётчик
        Random random = new Random();
        System.out.println(" ... ");
        try {
            Thread.sleep(500);// делаем задержку, чтобы немного замедлить комп
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // получаем координаты попаданий в повреждённый корабль соперника, потопленные исключаем
        ArrayList<Point> damagedPoints = targetPlayer.fieldOfPlayer.getDamagedPointsOfAliveShip();
//        boolean haveTargetPlayerDamagedShips = damagedPoints.size() == 0 ? false : true;
        switch (damagedPoints.size()) {
            case 0:
                // если таких точек нет, стреляем по полю рандомно, но игнорируем уже подбитые корабли и места вокруг них,
                //  а также места, куда уже стрелял
                do {
                    isRepeat = false;
                    coordinateX = random.nextInt(10) + 1;
                    coordinateY = random.nextInt(10) + 1;
                    // исключвем области вокруг потопленных кораблей, так как подбитых не обнаружено, так как корбали рядом ставить нельзя
                    for (int i = (coordinateX - 1); i <= (coordinateX + 1); i++) {
                        for (int j = (coordinateY - 1); j <= (coordinateY + 1); j++) {
                            if (targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'X') {
                                isRepeat = true;
                            }
                        }
                    }
                } while (isRepeat
                        // исключаем также точки, чьё значение не соответствует символам ~ и O
                        || !(targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == '~'
                        || targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == 'O'));
                break;

                // если одна подбитая точка, а корабль не потоплен, стреляем вокруг этой точки
            case 1:
                do {
                    isRepeat = false;
                    do {
                        // генерируем точку вокруг подбитой точки рандомно
                        coordinateX = damagedPoints.get(0).getCoordinateX() + random.nextInt(3) - 1;
                        coordinateY = damagedPoints.get(0).getCoordinateY() + random.nextInt(3) - 1;
                        // проверяем, чтобы точка не выходила за пределы игрового поля
                    } while (coordinateX < 1 || coordinateX > 10 || coordinateY < 1 || coordinateY > 10);
                    // проверяем точки в окрестностях на наличие убитых кораблей, так как их обстреливать нет смысла
                    for (int i = (coordinateX - 1); i <= (coordinateX + 1); i++) {
                        for (int j = (coordinateY - 1); j <= (coordinateY + 1); j++) {
                            if (targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'X') {
                                k++;// счётчик убитых клеток, если их больше одной, туда стрелять нет смысла
                            }
                        }
                    }
                    // если кроме нашей подбитой точки окрестностях имеются ещё такие, то нет смысла туда стрелять
                    if (k > 1) {
                        isRepeat = true;
                        k = 0;
                    }
                } while (isRepeat
                        // также нам нужны только символы ~ и O
                        || !((targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == '~'
                        || targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == 'O')
                        // а также нам нужно стрелять по кресту, по диагонали от исходной точки выстрелы не нужны
                        && (coordinateX == damagedPoints.get(0).getCoordinateX()
                        || coordinateY == damagedPoints.get(0).getCoordinateY())));
                break;
                // в случае, если попаданий больше одного, нужно стрелять по линии, то есть вариантов только 2
            default:
                // числа для 2 вариантов координат выстрела
                int r1;
                int r2;

                boolean b = random.nextBoolean();
                if (b) {
                    r1 = 0;
                    r2 = 1;
                } else {
                    r2 = 0;
                    r1 = 1;
                }
                // если линия попаданий по вертикали
                if (damagedPoints.get(0).getCoordinateX() == damagedPoints.get(1).getCoordinateX()) {
                    // массив коориднат по горизонтали
                    int[] coordinatesY = {damagedPoints.get(0).getCoordinateY() - 1,
                            damagedPoints.get(damagedPoints.size() - 1).getCoordinateY() + 1};
                    // по вертикали одна координата
                    coordinateX = damagedPoints.get(0).getCoordinateX();
                    // выбираем одну из точек
                    coordinateY = coordinatesY[r1];
                    // проверяем на попадание в игроваое поле, если нет, то берём другую точку
                    if (coordinateX < 1 || coordinateX > 10 || coordinateY < 1 || coordinateY > 10) {
                        coordinateY = coordinatesY[r2];
                    }
                    // проверяем на наличие точек убитых кораблей в окрестностьях выбранной точки
                    for (int i = (coordinateX - 1); i <= (coordinateX + 1); i++) {
                        for (int j = (coordinateY - 1); j <= (coordinateY + 1); j++) {
                            if (targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'X') {
                                k++;
                            }
                        }
                    }
                    // если есть совпадения, или точка не соответствует символам ~ и O, то берём другую точку
                    // по сути это срабатывает, когда r1 не вышла за пределы поля, но всё равно не проходит по прошлым проверкам
                    // если r1  отсеялось, то в этой замене уже нет смысла, до неё просто не дойдёт дело
                    if (k > 1
                            || !(targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == '~'
                            || targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == 'O')) {
                        coordinateY = coordinatesY[r2];
                    }
                // если линия попаданий по горизонтали, делаем все те же манипуляции, только по вертикали
                } else if (damagedPoints.get(0).getCoordinateY() == damagedPoints.get(1).getCoordinateY()) {
                    // массив коориднат по вертикали
                    int[] coordinatesX = {damagedPoints.get(0).getCoordinateX() - 1,
                            damagedPoints.get(damagedPoints.size() - 1).getCoordinateX() + 1};
                    // по горизонтали одна координата
                    coordinateY = damagedPoints.get(0).getCoordinateY();
                    // выбираем одну из точек
                    coordinateX = coordinatesX[r1];
                    // проверяем на попадание в игроваое поле, если нет, то берём другую точку
                    if (coordinateX < 1 || coordinateX > 10 || coordinateY < 1 || coordinateY > 10) {
                        coordinateX = coordinatesX[r2];
                    }
                    // проверяем на наличие точек убитых кораблей в окрестностьях выбранной точки
                    for (int i = (coordinateX - 1); i <= (coordinateX + 1); i++) {
                        for (int j = (coordinateY - 1); j <= (coordinateY + 1); j++) {
                            if (targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'X') {
                                k++;
                            }
                        }
                    }
                    // если есть совпадения, или точка не соответствует символам ~ и O, то берём другую точку
                    // по сути это срабатывает, когда r1 не вышла за пределы поля, но всё равно не проходит по прошлым проверкам
                    // если r1  отсеялось, то в этой замене уже нет смысла, до неё просто не дойдёт дело
                    if (k > 1
                            || !(targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == '~'
                            || targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == 'O')) {
                        coordinateX = coordinatesX[r2];
                    }
                // это пришлось написать, чтобы не ругался компилятор, на самом деле это не выполняется никогда
                // Если корабль не вертикальный и не горизонтальный, чего не может быть, если он ещё жив,
                // то делаем просто рандомный выстрел
                } else {
                    do {
                        isRepeat = false;
                        // рандомная координата
                        coordinateX = random.nextInt(10) + 1;
                        coordinateY = random.nextInt(10) + 1;
                        // проверка на попадание в поле
                        for (int i = (coordinateX - 1); i <= (coordinateX + 1); i++) {
                            for (int j = (coordinateY - 1); j <= (coordinateY + 1); j++) {
                                if (targetPlayer.fieldOfPlayer.getCellsOfPlayer()[i][j].getValue() == 'X') {
                                    isRepeat = true;
                                }
                            }
                        }
                        // проверка на выполнение предыдущих условий, а также, что символы ~ и O
                    } while (isRepeat
                            || !(targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == '~'
                            || targetPlayer.fieldOfPlayer.getCellsOfPlayer()[coordinateX][coordinateY].getValue() == 'O'));
                }
        }
        // озвучиваем выбранную точку
        System.out.println(nameOfPlayer + " выбрал(а) точку [" + (coordinateX - 1) + ", " + (coordinateY - 1) + "]");
        // инкрементируем число выстрелов игрока
        numberOfShots++;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // наконец, возвращаем точку для выстрела компьютера
        return new Point(coordinateX, coordinateY, '0');
    }

    // проверка введеной координаты на переводимость в Int, а также на попадание в поле
    // возвращает флаг о правильности ввода координаты
    private boolean checkInputOfPlayer(String tempCoordinate) {
        boolean isCorrectInput = true; // флаг о правильности введеной координаты
        int coordinate = 1; // компилятор заставил инициализировать
        try {
            Integer.parseInt(tempCoordinate); // проверка на перевод в int, чтобы не вводили всякую чушь
        }
        catch (NumberFormatException ex) { // если проверка не прошла
            System.out.println("Нужно ввести число от 0 до 9!");
            isCorrectInput = false;
        }
        finally {
            if (isCorrectInput) {
                coordinate = Integer.parseInt(tempCoordinate);
                // проверка на попадание в поле одной из координат
                if (0 > coordinate || coordinate > 9) {
                    System.out.println("Нужно ввести число от 0 до 9!");
                    isCorrectInput = false;
                }
            }
            return isCorrectInput;// флаг возвращается компьютеру
        }
    }

    // проверка на количество живых кораблей у игрока
    int checkNumberOfShipsAlive() {
        int k = 0;// счётчик живых кораблей
        // получаем у поля игрока переменную с количеством кораблей пробегаем по нему,
        // чтобы посчитать количество живых кораблей у игрока после выстрела в него,
        // чтобы проверить окончание игры
        for (int i = 0; i < fieldOfPlayer.getShipsOfPlayer().length; i++) {
            if (fieldOfPlayer.getShipsOfPlayer()[i].isAliveShip()) {
                k++;
            }
        }
        numberOfShipsAlive = k;
        return k;
    }
}
