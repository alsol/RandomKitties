package services;

import services.utils.Emotion;

import java.util.Random;

/**
 * @author alexander.solovyov
 * @since 13.07.17
 */
public class RandomKitten {

    private RandomKitten() {
        // HAHA
        // TRY TO CREATE THIS LOL
    }

    public Emotion getRandomEmotion() {
        return Emotion.values()[new Random().nextInt(Emotion.values().length)];
    }

}
