package game;

import core.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implements AI for PlayerController, using a simple but effective strategy.
 */
public class OpponentPlayerController extends PlayerController {

    private static final double RADIUS_FACTOR = 2.3;
    private Random random = new Random();
    private PlayerController target;

    public OpponentPlayerController(Player player) {
        super(player);
    }

    public void update(World world) {
        super.update(world);

        // if i am falling
        if (!getPlayer().isOnSurface())
            return;

        // if target is not valid
        if (target == null || !target.getPlayer().isOnSurface()) {
            // fill a list with available targets
            List<PlayerController> availablePlayers = new ArrayList<PlayerController>();
            for (PlayerController player : world.getPlayers()) {
                if (player != this && player.getPlayer().isOnSurface()) {
                    availablePlayers.add(player);
                }
            }

            // if none are available, return
            if (availablePlayers.size() == 0)
                return;

            // choose new target randomly
            final int index = random.nextInt(availablePlayers.size());
            target = availablePlayers.get(index);
        }

        // No target available
        if (target == null)
            return;

        // run towards target
        final Vector otherPosition = target.getPlayer().getPosition();
        final Vector direction = otherPosition.subtract(getPlayer().getPosition());
        startRunning(direction);

        // if close enough, push target
        double playerRadius = getPlayer().getRadius();
        double distance = getPlayer().getPosition().distance(otherPosition);
        if (distance < playerRadius * RADIUS_FACTOR)
            push(); // try to push
    }
}
