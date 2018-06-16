package Game;

public enum Direction {
    LD, RU, LU, RD, L, R, U, D, STOP;

    public static double toDegree(Direction dir){
        switch (dir){
            case R: return 0;
            case RU: return 45;
            case U: return 90;
            case LU: return 135;
            case L: return 180;
            case LD: return 225;
            case D: return 270;
            case RD:  return 315;
            default: return 360;
        }
    }

    public static double getVectorX(Direction dir){
        switch (dir){
            case R: return 1;
            case RU: return Math.cos(Math.toRadians(45));
            case U: return 0;
            case LU: return -Math.cos(Math.toRadians(45));
            case L: return -1;
            case LD: return -Math.cos(Math.toRadians(45));
            case D: return 0;
            case RD:  return Math.cos(Math.toRadians(45));
            default: return -2;
        }
    }

    public static double getVectorY(Direction dir){
        switch (dir){
            case R: return 0;
            case RU: return Math.cos(Math.toRadians(45));
            case U: return 1;
            case LU: return Math.cos(Math.toRadians(45));
            case L: return 0;
            case LD: return -Math.cos(Math.toRadians(45));
            case D: return -1;
            case RD:  return -Math.cos(Math.toRadians(45));
            default: return -2;
        }
    }
}
