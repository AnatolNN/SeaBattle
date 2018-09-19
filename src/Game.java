import java.util.Random;
import java.util.Scanner;

/**
 * Created by Анадер on 23.05.2018.
 */
public class Game {

    private Player player1;
    private Player player2;
    private boolean isGameOver;// флаг для проверки конца игры
    private static int numberOfMoves;// счётчик ходов

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    void go() {
        System.out.println("Приветствую тебя, человек! Это игра МОРСКОЙ БОЙ.");
        // создаём игроков, спрашивая, живые они или компьютеры, также там идёт запрос имени, создаются пустые поля
        player1 = new Player(isPlayByHuman());
        player2 = new Player(isPlayByHuman());
        // показываем пустые поля
        showFields();
        // спрашиваем у живых игроков, автоматически расставить корабли, или вручную
        player1.askIsAutomaticallyPutShipOnField();
        player2.askIsAutomaticallyPutShipOnField();
        // расставляем корабли в зависимости от выбранного ванианта
        if (player1.isAutomaticallyPutShipOnField())
            player1.getFieldOfPlayer().putShipOnFieldAutomatically(player1);
        else
            player1.getFieldOfPlayer().putShipOnFieldManually(player1);
        // расставляем корабли в зависимости от выбранного ванианта
        if (player2.isAutomaticallyPutShipOnField())
            player2.getFieldOfPlayer().putShipOnFieldAutomatically(player2);
        else
            player2.getFieldOfPlayer().putShipOnFieldManually(player2);
        // показать поля с кораблями
        showFields();
        // обнуляем число ходов
        numberOfMoves = 0;
        Random random = new Random();
        // кидаем жребий, кто будет ходить первым
        int idOfFirstPlayer = random.nextInt(2) + 1;
        System.out.print("Первым будет ходить игрок" + idOfFirstPlayer);
        // если ходит первым первый игрок
        if(idOfFirstPlayer == 1) {
            while(true) {
                numberOfMoves++; // инкрементируем каждый ход
                System.out.print("\n\nХод №" + numberOfMoves + ".");
                doShoot(player1, player2); // первый игрок стреляет по второму
                // проверяем на конец игры после каждого выстрела
                if(isGameOver) {
                    break;
                }
                doShoot(player2, player1); // вотрой игрок стреляет по первому
                // проверяем на конец игры после каждого выстрела
                if(isGameOver) {
                    break;// если игра окончена, выходим из цикла
                }
            }
        // если ходит первым второй игрок
        } else {
            while(true) {
                numberOfMoves++; // инкрементируем каждый ход
                System.out.print("\n\nХод №" + numberOfMoves + ".");
                doShoot(player2, player1); // вотрой игрок стреляет по первому
                // проверяем на конец игры после каждого выстрела
                if(isGameOver) {
                    break;
                }
                doShoot(player1, player2); // первый игрок стреляет по второму
                // проверяем на конец игры после каждого выстрела
                if(isGameOver) {
                    break;
                }
            }
        }
        // покажем поле в конце игры
        showFields();
    }

    // узнаём, кто будет играть живой игрок или компьютер
    boolean isPlayByHuman() {
        boolean answer = false;
        boolean isRepeat = true;
        String r;
        do {
            System.out.println("Игрок" + (Player.getNumberOfPlayers() + 1) + " будет компьютером или человеком?");
            System.out.println("Если компьютер, введи 0, если человек - 1.");
            r = new Scanner(System.in).nextLine();
            switch (r) {
                case "0":
                    answer = false;
                    isRepeat = false;
                    break;
                case "1":
                    answer = true;
                    isRepeat = false;
                    break;
                default:
                    System.out.println("Похоже, ты не понял(а) вопрос! Прочти ещё раз, что нужно сделать!");
            }
        } while(isRepeat);
        return answer;
    }

    // показываем оба поля игроков
    void showFields() {
        // верхние части полей первого и второго игроков
        System.out.print("\n  ");
        for (int j = 0; j < Field.SIZE_OF_FIELD; j++) {
            System.out.print(" " + j);
        }
        System.out.print("\t\t\t\t\t    "); // отступ между полями
        for (int j = 0; j < Field.SIZE_OF_FIELD; j++) {
            System.out.print(" " + j);
        }

        // следующая строка
        System.out.println();
        // рисуем боковые поля и основные для обоих игроков
        for (int i = 1; i < Field.SIZE_OF_FIELD + 1; i++) {
            System.out.print(" " + (i - 1)); // цифра вначале строки
            for (int j = 1; j < Field.SIZE_OF_FIELD + 1; j++) {
                System.out.print(" " + player1.getFieldOfPlayer().getCellsOfPlayer()[i][j].getValue());
            }
            System.out.print(" " + (i - 1)); // цифра в конце строки
            System.out.print("\t\t\t\t  ");// отступ между полями
            System.out.print(" " + (i - 1));// цифра вначале строки
            for (int j = 1; j < Field.SIZE_OF_FIELD + 1; j++) {
                System.out.print(" " + player2.getFieldOfPlayer().getCellsOfPlayer()[i][j].getValue());
            }
            System.out.print(" " + (i - 1));// цифра в конце строки
            System.out.println();
        }

        // нижние части полей первого и второго игроков
        System.out.print("  ");
        for (int j = 0; j < Field.SIZE_OF_FIELD; j++) {
            System.out.print(" " + j);
        }
        System.out.print("\t\t\t\t\t  ");// отступ между полями
        System.out.print("  ");
        for (int j = 0; j < Field.SIZE_OF_FIELD; j++) {
            System.out.print(" " + j);
        }
        System.out.println();

        // подписи полей
        System.out.print("Поле с кораблями игрока1");
        System.out.print("\t\t\t\t  ");
        System.out.print("Поле с кораблями игрока2");
        System.out.println();

        //Отображение имён игроков, сделал проверку, чтобы не вылезло исключение
        if(!(player1.getNameOfPlayer() == null))
            System.out.print(player1.getNameOfPlayer());
        System.out.print("\t\t\t\t\t\t\t\t\t  "); // отступ между подписями
        if(!(player2.getNameOfPlayer() == null))
            System.out.print(player2.getNameOfPlayer());
        System.out.println("\n");
    }

    // проводит выстрел одного игрока по полю другого
    void doShoot(Player shootPlayer, Player targetPlayer) {
        // пауза перед вытрелом
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Point pointToShoot; // точка для выстрела
        System.out.println();
        System.out.println("Ход игрока" + shootPlayer.getIdOfPlayer() + " " + shootPlayer.getNameOfPlayer());
        // показываем поле игрока, в которого стреляем
        targetPlayer.getFieldOfPlayer().showFieldOfPlayer(targetPlayer);
        // получаем координату для выстрела
        if(shootPlayer.isItHuman()){
            pointToShoot = shootPlayer.getCoordinateOfPointFromPlayer();
        } else {
            pointToShoot = shootPlayer.getCoordinateOfPointFromComputer(targetPlayer);
        }
        // проверяем на попадание, и если попали, делаем ещё выстрел
        boolean repeatTurn = checkForHitOfPlayer(pointToShoot, shootPlayer, targetPlayer);
        // проверяем наличие живых кораблей у обстреливаемого игрока
        targetPlayer.checkNumberOfShipsAlive();
        // даём промежуточные результаты стреляющего игрока
        System.out.println("Всего выстрелов: " + shootPlayer.getNumberOfShots() + "; попаданий: " + shootPlayer.getNumberOfHits());
        // проверяем конец игры
        isGameOver = checkForWin(shootPlayer, targetPlayer);// проверка на победителя и вывод информации игры в случае победы
        // если попали сначала проверим на победителя, если продолжаем, то стреляем ещё раз, а если промазали , передаём ход
        if (repeatTurn) {
            // если конец игры
            if (isGameOver) {
                targetPlayer.getFieldOfPlayer().showFieldOfPlayer(targetPlayer);
                return;
            } else
                //если делаем ещё выстрел
                doShoot(shootPlayer, targetPlayer);
            // если промазал
        } else
            targetPlayer.getFieldOfPlayer().showFieldOfPlayer(targetPlayer);
    }

    // проверяем выстрел
    boolean checkForHitOfPlayer(Point point, Player shootPlayer, Player targetPlayer) {
        switch (targetPlayer.getFieldOfPlayer().getCellsOfPlayer()[point.getCoordinateX()][point.getCoordinateY()].getValue()) {
            // если промазал
            case '~':
                System.out.println("Игрок" + shootPlayer.getIdOfPlayer() + " " + shootPlayer.getNameOfPlayer() + " промазал(а)! Готовься к обратке!");
                targetPlayer.getFieldOfPlayer().getCellsOfPlayer()[point.getCoordinateX()][point.getCoordinateY()].setValue('*');
                return false;
                // если уже стрелял сюда
            case '*':
                System.out.println("Игрок" + shootPlayer.getIdOfPlayer() + " " + shootPlayer.getNameOfPlayer() + ", ты уже стрелял(а) сюда! Готовься к обратке!");
                return false;
                // если попал
            case 'O':
                System.out.println("Игрок" + shootPlayer.getIdOfPlayer() + " " + shootPlayer.getNameOfPlayer() + " попал(а)! Стреляй ещё раз!");
                for (int i = 0; i < targetPlayer.getFieldOfPlayer().getShipsOfPlayer().length; i++) {
                    targetPlayer.getFieldOfPlayer().getShipsOfPlayer()[i].checkIsDamage(point);
                }
                targetPlayer.getFieldOfPlayer().getCellsOfPlayer()[point.getCoordinateX()][point.getCoordinateY()].setValue('X');
                shootPlayer.setNumberOfHits(shootPlayer.getNumberOfHits() + 1);
                return true;
                // если уже попадал сюда
            case 'X':
                System.out.println("Игрок" + shootPlayer.getIdOfPlayer() + " " + shootPlayer.getNameOfPlayer() + ", ты уже попал(а) сюда! Готовься к обратке!");
                return false;
                // это, чтобы успокоить компилятор
            default:
                System.out.println("Упс, чёт не то!");
                return false;
        }
    }

    // Не используется, хотел сделать для компа, потом оказалось, что нет смысла
    boolean checkForHitOfComp(Point point, Player shootPlayer, Player targetPlayer) {
        switch (targetPlayer.getFieldOfPlayer().getCellsOfPlayer()[point.getCoordinateX()][point.getCoordinateY()].getValue()) {
            case '~':
                System.out.println("Игрок" + shootPlayer.getIdOfPlayer() + " " + shootPlayer.getNameOfPlayer() + " промазал(а)! Готовься к обратке!");
                targetPlayer.getFieldOfPlayer().getCellsOfPlayer()[point.getCoordinateX()][point.getCoordinateY()].setValue('*');
                return false;
            case '*':
                System.out.println("Игрок" + shootPlayer.getIdOfPlayer() + " " + shootPlayer.getNameOfPlayer() + ", ты уже стрелял(а) сюда! Готовься к обратке!");
                return false;
            case 'O':
                System.out.println("Игрок" + shootPlayer.getIdOfPlayer() + " " + shootPlayer.getNameOfPlayer() + " попал(а)! Стреляй ещё раз!");
                for (int i = 0; i < targetPlayer.getFieldOfPlayer().getShipsOfPlayer().length; i++) {
                    targetPlayer.getFieldOfPlayer().getShipsOfPlayer()[i].checkIsDamage(point);
                }
                targetPlayer.getFieldOfPlayer().getCellsOfPlayer()[point.getCoordinateX()][point.getCoordinateY()].setValue('X');
                return true;
            case 'X':
                System.out.println("Игрок" + shootPlayer.getIdOfPlayer() + " " + shootPlayer.getNameOfPlayer() + ", ты уже попал(а) сюда! Готовься к обратке!");
                return false;
            default:
                System.out.println("Упс, чёт не то!");
                return false;
        }
    }

    // проверка на победителя после выстрела
    boolean checkForWin(Player shootPlayer, Player targetPlayer) {
        // если корабли ещё есть у обстреливаемого корабля
        if(targetPlayer.getNumberOfShipsAlive() > 0) {
            // если он последний
            if(targetPlayer.getNumberOfShipsAlive() == 1) {
                System.out.println("У игрока" + targetPlayer.getIdOfPlayer() + " " + targetPlayer.getNameOfPlayer() + " остался последний корабль!");
                // если не последний
            } else {
                System.out.print("У игрока" + targetPlayer.getIdOfPlayer() + " " + targetPlayer.getNameOfPlayer() + " осталось " + targetPlayer.getNumberOfShipsAlive());
                // если их больше или равно 5
                if (targetPlayer.getNumberOfShipsAlive() >= 5) {
                    System.out.println(" кораблей!");
                    // если 2, 3 или 4, такие заморочки, чтобы русский язык был нормальный
                } else {
                    if(targetPlayer.getNumberOfShipsAlive() >= 2){
                        System.out.println(" корабля!");
                    }
                }
            }
            return false;
            // если корабли кончились, то у нас победитель,  выдаём информацию и закругляемся
        } else {
            System.out.println("Все корабли игрока" + targetPlayer.getIdOfPlayer() + " " + targetPlayer.getNameOfPlayer() + " уничтожены!\nИгра окончена!");
            System.out.println("Игрок" + shootPlayer.getIdOfPlayer() + " " + shootPlayer.getNameOfPlayer() + " победил(а)!");
            System.out.println("Количество ходов: " + numberOfMoves);
            System.out.println("Количество выстрелов игрока1, " + player1.getNameOfPlayer() + ": " + player1.getNumberOfShots());
            System.out.println("Количество выстрелов игрока2, " + player2.getNameOfPlayer() + ": " + player2.getNumberOfShots());
            System.out.println("Количество очков (попаданий) игрока1, " + player1.getNameOfPlayer() + ": " + player1.getNumberOfHits());
            System.out.println("Количество очков (попаданий) игрока2, " + player2.getNameOfPlayer() + ": " + player2.getNumberOfHits());
            return true;
        }
    }
}
