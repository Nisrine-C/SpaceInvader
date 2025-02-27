package fxgl.spaceinvader;

public final class Config {
    private Config() {}

    public static final String SAVE_DATA_NAME = "./hiscore.dat";

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    //640 x 480
    //800 x 600

    public static final double LEVEL_START_DELAY = 1.5;

    public static final int SCORE_ENEMY_KILL = 250;
    public static final int SCORE_DIFFICULTY_MODIFIER = 1;

    public static final int START_LIVES = 3;

    public static final double PLAYER_MOVE_SPEED = 300;
    public static final double SHIELD_COOLDOWN = 6;

    public static final int STARS_MOVE_SPEED = 125;

    /**
     * Attack speed, bullet per second.
     */
    public static final double PLAYER_ATTACK_SPEED = 3.0;
    public static final double INVINCIBILITY_TIME = 1.0;

    public static final double LASER_METER_MAX = 50.0;

    /**
     * Recharge per killed enemy;
     */
    public static final double LASER_METER_RECHARGE = 1.0;




    public static final class Asset {
        public static final String SOUND_LOSE_LIFE = "lose_life.wav";
        public static final String SOUND_NEW_LEVEL = "level.wav";

        public static final String DIALOG_MOVE_LEFT = "dialogs/move_left.mp3";
        public static final String DIALOG_MOVE_RIGHT = "dialogs/move_right.mp3";
        public static final String DIALOG_SHOOT = "dialogs/shoot.mp3";

        public static final String FXML_MAIN_UI = "main.fxml";
    }
}