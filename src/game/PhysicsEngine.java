package game;

import core.Vector;

/**
 * Integrates a new game object position, based on position, forces and mass
 *
 * Units:
 * - Mass - kg
 * - Force - N
 * - Position - meters
 * - Velocity - m/s
 * - Acceleration - m/s^2
 */
public class PhysicsEngine {
    public PhysicsEngine() {
    }

    public void update(Player player, double timePassedSecs) {

        // compute acceleration
        Vector acceleration = computeAcceleration(player);
        // sets new velocity and position
        integrateVerlet(player, acceleration, timePassedSecs);

        player.setForce(new Vector()); // reset forces.
    }

    /**
     * Returns the acceleration according to Newton's laws of motion (sum F = ma)
     */
    private static Vector computeAcceleration(Player player) {

        double m = player.getMass();
        Vector forces = player.getForce();
        Vector accelaration = forces.div(m);

        return accelaration;
    }

    /**
     * Compute new velocity and position and sets them to the player.
     * Using Euler integral
     */
    private static void integrateEuler(Player player, Vector acceleration, double dt) {
        /*
         * vel = vel + acceleration*dt
         * pos = pos + vel*dt
         */

        Vector pos = player.getPosition();
        Vector vel = player.getVelocity();

        Vector newVel = vel.add(acceleration.mul(dt));
        ;
        Vector newPos = pos.add(newVel.mul(dt));

        player.setVelocity(newVel);
        player.setPosition(newPos);
    }

    /**
     * Compute new velocity and position and sets them to the player.
     * Using Verlet integral
     */
    private static void integrateVerlet(Player player, Vector acceleration, double dt) {

        /*
         * new_vel = vel + player.acceleration()*dt
         * pos = pos + (vel+new_vel)*0.5*dt
         * vel = new_vel
         */

        Vector pos = player.getPosition();
        Vector vel = player.getVelocity();

        Vector newVel = vel.add(acceleration.mul(dt));
        ;
        Vector newPos = pos.add(vel.add(newVel).mul(0.5 * dt)); // difference from Euler here

        player.setVelocity(newVel);
        player.setPosition(newPos);
    }

}
