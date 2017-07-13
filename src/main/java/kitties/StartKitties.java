package kitties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.RandomKitten;
import services.StaticContext;
import services.utils.Emotion;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.notFound;
import static spark.Spark.path;

/**
 * @author alexander.solovyov
 * @since 13.07.17
 */
public class StartKitties {

    private static final Logger log = LoggerFactory.getLogger(StartKitties.class);

    public static void main(final String[] args) {
        get("/hello", ((request, response) -> "world!"));

        get("/kittie", (((request, response) -> {
            final Emotion result = StaticContext.getBean(RandomKitten.class).getRandomEmotion();

            final String typeParam = request.queryParamOrDefault("type", "lol");

            switch (typeParam) {
                case "emoji":
                    return result.getEmoji();
                case "smile":
                    return result.getSmile();
                default:
                    return result;
            }
        })));

        path("/random", () -> {
            before("/*", (q, a) -> log.info("Recieved an api call"));
            get("/smile", (request, response) -> StaticContext.getBean(RandomKitten.class).getRandomEmotion().getSmile());
            get("/emoji", (request, response) -> StaticContext.getBean(RandomKitten.class).getRandomEmotion().getEmoji());
            get("/text", (request, response) -> StaticContext.getBean(RandomKitten.class).getRandomEmotion().name().toLowerCase());
        });

        notFound("<html><h1 align=\"center\">404</h1>" +
                "<p align=\"center\"><strong>PAGE NOT FOUND :<<<<<<<<</strong></p></br>" +
                "<p align=\"center\"><img alt=\"This is cat\" " +
                "src=\"http://i.dailymail.co.uk/i/pix/2014/04/11/article-2602528-1D06B2D200000578-424_634x471.jpg\">" +
                "</p>");
    }

}
