
package game;

/**
 * Simple wrapper for SoundEffect
 */
public class SoundEngine {
    public static void playStartRound() {
        SoundEffect.FinalWinner.stop();
        SoundEffect.StartRound.play();
    }

    public static void playWinRound() {
        SoundEffect.WinRound.play();
    }

    public static void playFallEffect() {
        SoundEffect.Lose.play();
    }

    public static void playPillar() {
        SoundEffect.Pillar.play();
    }

    public static void playFinalWinner() {
        SoundEffect.WinRound.stop();
        SoundEffect.FinalWinner.play();
    }

    public static void stopFinalWinner() {
        SoundEffect.FinalWinner.stop();
    }
}
