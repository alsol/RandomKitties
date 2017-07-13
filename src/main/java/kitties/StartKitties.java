package kitties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.RandomKitten;
import services.StaticContext;
import services.utils.Emotion;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.path;
import static spark.Spark.post;

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

        post("/switch_service_to", ((request, response) -> {
            if (StringUtils.isEmpty(request.body())) {
                throw new IllegalStateException("Body should not be null or empty");
            }
            StaticContext.getBean(RandomKitten.class).setAlwaysFail(Boolean.parseBoolean(request.body()));
            response.status(200);
            return "OK";
        }));

        path("/random", () -> {
            before("/*", (q, a) -> log.info("Recieved an api call"));
            get("/smile", (request, response) -> StaticContext.getBean(RandomKitten.class).getRandomEmotion().getSmile());
            get("/emoji", (request, response) -> StaticContext.getBean(RandomKitten.class).getRandomEmotion().getEmoji());
            get("/text", (request, response) -> StaticContext.getBean(RandomKitten.class).getRandomEmotion().name().toLowerCase());
        });

        internalServerError("<html><h1 align=\"center\">500</h1>" +
                "<p align=\"center\"><strong>INTERNAL SERVER ERROR :<<<<<<<<</strong></p></br>" +
                "<p align=\"center\"><img alt=\"This is cat\" " +
                "src=\"https://vignette1.wikia.nocookie.net/animal-jam-clans-1/images/b/b3/Cat-cry-sad-tears-Favim.com-1987007.jpg/revision/latest?cb=20160515162716\">" +
                "</p>");

        notFound("<html><h1 align=\"center\">404</h1>" +
                "<p align=\"center\"><strong>PAGE NOT FOUND :<<<<<<<<</strong></p></br>" +
                "<p align=\"center\"><img alt=\"This is cat\" " +
                "src=\"http://i.dailymail.co.uk/i/pix/2014/04/11/article-2602528-1D06B2D200000578-424_634x471.jpg\">" +
                "</p>");
    }

}
