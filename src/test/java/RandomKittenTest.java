import org.testng.annotations.Test;
import services.RandomKitten;
import services.StaticContext;
import services.utils.Emotion;
import spark.utils.Assert;

/**
 * @author alexander.solovyov
 * @since 14.07.17
 */
public class RandomKittenTest {

    private final RandomKitten randomKitten = StaticContext.getBean(RandomKitten.class);

    @Test
    public void test_random() {
        final Emotion emotion = randomKitten.getRandomEmotion();
        Assert.notNull(emotion);
    }

}
