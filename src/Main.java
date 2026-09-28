import com.fasterxml.jackson.databind.JsonNode;
import io.github.cdimascio.dotenv.Dotenv;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static void main(String []args){
        Dotenv dotenv = Dotenv.load();
        dotenv.get("MistralIA");
        String subReddit = "gaming";

        try {
            String url = "https://oauth.reddit.com/r/" + subReddit +".new?limit=100";
            Connection connection = Jsoup.connect(url)
                    .userAgent("SentFeel/1.0 (contacto: gabriel.fial2005@gmail.com)")
                    .ignoreHttpErrors(true);
            Connection.Response response = connection.execute();
            Map<String, String> posts = new HashMap<>();

            if (response.statusCode() == 200) {
                JsonNode root = mapper.readTree(response.body());
                JsonNode children = root.path("data").path("children");

                for (JsonNode child : children) {
                    JsonNode post = child.path("data");

                    String id = post.path("id").asText();
                    String title = post.path("title").asText();
                    String body = post.path("selftext").asText();

                    String text = body.isBlank() ? title : title + "\n" + body;
                    posts.put(id, text);
                }

                System.out.println("Posts guardados: " + posts.size());
            } else {
                System.out.println("Erro" + response.statusCode());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}