package asteroids;

public class Level {

    // Speeds
    public static double asteroid_speed;
    public static double mediumAsteroid_speed;
    public static double smallAsteroid_speed;
    public static double alien_speed;

    // Combat
    public static double bullet_speed;
    public static double alien_aim_accuracy;
    public static long alien_fire_delay;

    // Movement
    public static double spaceship_acceleration_increase;
    public static double spaceship_acceleration_reduction;
    public static double spaceship_rotationSpeed;

    // SpawnTimes
    public static long asteroid_spawntime;
    public static long alien_spawntime;

    public static void ChangeGameConfig(boolean difficulty) {

        // Speeds
        asteroid_speed = difficulty ? 1.3 : 1;
        mediumAsteroid_speed = difficulty ? 1.6 : 1.4;
        smallAsteroid_speed = difficulty ? 1.8 : 1.6;
        alien_speed = difficulty ? 1.3 : 1;

        // Combat
        bullet_speed = difficulty ? 4 : 4;
        alien_aim_accuracy = difficulty ? Math.PI / 12 : Math.PI / 12;
        alien_fire_delay = difficulty ? 1500000000l : 2500000000l;

        // Movement
        spaceship_acceleration_increase = difficulty ? 0.15 : 0.18;
        spaceship_acceleration_reduction = difficulty ? -0.02 : -0.02;
        spaceship_rotationSpeed = difficulty ? Math.PI / 45 : Math.PI / 45;

        // SpawnTimes
        asteroid_spawntime = difficulty ? 4000000000l : 5000000000l;
        alien_spawntime = difficulty ? 7000000000l : 9000000000l;
    }

}
