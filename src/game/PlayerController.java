
package game;

import core.Vector;

import java.util.Random;

/**
 * Contains logic and behaviors of a Player object (push, fall, etc.)
 * Responsible for applying the correct forces
 */
public class PlayerController {

    private final static double maxRunVelocity = 20;
    private final static double runForcePower = 13;
    private final static int pushVelocityChangeMin = 7;
    private final static int pushVelocityChangeMax = 10;
    private final static double pushDelayMilisec = 500;

    private final Random random = new Random();
    private final Player player;
    private int id;
    private boolean isRunning;
    private Vector runDirection;
    private boolean pushActivated;
    private double pushStartMilisec;

    public PlayerController(Player player) {
        this.player = player;
        isRunning = false;
        runDirection = new Vector(0, 0, -1); // face north
        pushStartMilisec = -pushDelayMilisec;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /** Start running in the specified direction */
    public void startRunning(Vector runDirection) {
        isRunning = true;
        this.runDirection = runDirection.normalize();
    }

    public void stopRunning() {
        isRunning = false;
    }

    /** Start push in the current direction */
    public void push() {
        double time = System.currentTimeMillis();

        // Prevent applying the force too much
        if (time >= pushStartMilisec + pushDelayMilisec)
            pushActivated = true;
    }

    /** Change the current player dirction */
    public void faceDirection(Vector direction) {
        Vector originalFaceDirection = new Vector(0, 0, -1);

        double angle;

        if (direction.size() == 0)
            angle = 0;
        else
            angle = Math.toDegrees(direction.angleBetween(originalFaceDirection));

        Vector orientation = player.getOrientation();
        player.setOrientation(angle, orientation.y(), orientation.z());
    }

    /** Apply the correct forces, depending on the inner state */
    public void update(World world) {
        boolean isOnSurface = player.isOnSurface();

        faceDirection(runDirection);

        // if on surface
        if (isOnSurface) {
            // can run and push

            // if running
            if (isRunning) {
                Vector velocity = player.getVelocity();
                // find angle between velocity and runDirection
                double angle = Math.toDegrees(velocity.angleBetween(runDirection));

                // Run if: (1) velocity < maxRunVelocity OR (2) running in that direction won't
                // increase current velocity
                if ((player.getVelocity().size() < maxRunVelocity) || (angle >= 90))
                    player.addForce(runDirection.mul(runForcePower));
            }
            // if push flag is active
            if (pushActivated) {
                double time = System.currentTimeMillis();
                pushStartMilisec = time;
                pushActivated = false;

                player.addVelocity(runDirection.mul(randomPushVelocityChange()));
            }
        }
    }

    /** Returns a random push velocity, to make the game less boring */
    private int randomPushVelocityChange() {
        int result = pushVelocityChangeMin +
                random.nextInt(pushVelocityChangeMax - pushVelocityChangeMin + 1);

        return result;
    }
}
