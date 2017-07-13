package services;

import services.utils.Emotion;

import java.util.Random;

/**
 * @author alexander.solovyov
 * @since 13.07.17
 */
public class RandomKitten {

    private boolean alwaysFail = false;

    private RandomKitten() {
        // HAHA
        // TRY TO CREATE THIS LOL
    }

    public Emotion getRandomEmotion() {
        return alwaysFail ? Emotion.SAD : Emotion.values()[new Random().nextInt(Emotion.values().length)];
    }

    public void setAlwaysFail(final boolean alwaysFail) {
        this.alwaysFail = alwaysFail;
    }
}
