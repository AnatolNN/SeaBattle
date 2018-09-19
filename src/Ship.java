/**
 * Created by Анадер on 22.05.2018.
 */
public class Ship {
    private String nameOfShip;//название корабля
    private int sizeOfShip;//размер корабля
    private int livesOfShip;//количестово жизней корабля
    private Point[] coordinatesOfShip;// точки корабля на поле
    private boolean isVerticalTypeOfShip;// тип корабля вертикальный или горизонтальный
    private boolean isNotDamagedShip;// повреждён корабль или нет
    private boolean isAliveShip;// корабль жив или нет

    //конструктор для корабля
    Ship(String nameOfShip, boolean isVerticalTypeOfShip, int sizeOfShip, Point[] coordinatesOfShip) {
        this.nameOfShip = nameOfShip;
        this.isVerticalTypeOfShip = isVerticalTypeOfShip;
        this.sizeOfShip = sizeOfShip;
        this.coordinatesOfShip = coordinatesOfShip;
        livesOfShip = sizeOfShip;
        isNotDamagedShip = true;
        isAliveShip = true;
    }

    public String getNameOfShip() {
        return nameOfShip;
    }

    public int getSizeOfShip() {
        return sizeOfShip;
    }

    public int getLivesOfShip() {
        return livesOfShip;
    }

    public Point[] getCoordinatesOfShip() {
        return coordinatesOfShip;
    }

    boolean isNotDamagedShip() {
        return isNotDamagedShip;
    }

    boolean isAliveShip() {
        return isAliveShip;
    }


    //устанавливает корабль на поле по начальной точке
    void setCoordinates(Point point) {
        this.coordinatesOfShip = new Point[this.sizeOfShip];
        //ставит точки корабля вертикально вниз
        if (this.isVerticalTypeOfShip){
            for (int i = 0; i < (this.coordinatesOfShip.length); i++) {
                this.coordinatesOfShip[i] = new Point(point.getCoordinateX() + i, point.getCoordinateY(), 'O');
            }
            //ставит точки корабля горизонтально
        } else {
            for (int j = 0; j < (this.coordinatesOfShip.length); j++) {
                this.coordinatesOfShip[j] = new Point(point.getCoordinateX(), point.getCoordinateY() + j, 'O');
            }
        }
    }

    //проверка выстрела на попадание в данный корабль
    void checkIsDamage(Point point) {
        if(this.isAliveShip) {
            for (Point pointOfShip : coordinatesOfShip) {
                if ((pointOfShip.getCoordinateX() == point.getCoordinateX()) && (pointOfShip.getCoordinateY() == point.getCoordinateY())) {
                    System.out.println("Подбит " + this.nameOfShip + "!");
                    this.livesOfShip--;
                    checkIsAlive();
                }
            }
        }
    }
    //проверка корабля на уничтожение
    private boolean checkIsAlive() {
        if (this.livesOfShip > 0) {
            if (this.livesOfShip < this.sizeOfShip)
                isNotDamagedShip = false;
            return true;
        } else {
            isAliveShip = false;
            System.out.println(this.nameOfShip + " уничтожен!");
            return false;
        }
    }
}
