package services.utils;

/**
 * @author alexander.solovyov
 * @since 13.07.17
 */
public enum Emotion {

    SAD("\uD83D\uDE3F", ":<"),
    HAPPY("\uD83D\uDC31", ">:>");

    private final String emoji;
    private final String smile;

    Emotion(final String emoji, final String smile) {
        this.emoji = emoji;
        this.smile = smile;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getSmile() {
        return smile;
    }
}
