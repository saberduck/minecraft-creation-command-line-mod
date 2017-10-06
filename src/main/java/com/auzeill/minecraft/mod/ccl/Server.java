package com.auzeill.minecraft.mod.ccl;

import com.auzeill.minecraft.mod.ccl.world.CopiedArea;
import com.auzeill.minecraft.mod.ccl.world.Serializer;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.auzeill.minecraft.mod.ccl.world.Serializer.GSON;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class Server {

  private static final int SERVER_PORT = 4567;

  public ConcurrentLinkedQueue<CopiedArea> queue = new ConcurrentLinkedQueue<>();

  private static Server INSTANCE;

  public static synchronized Server getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new Server();
      INSTANCE.bootstrap();
    }
    return INSTANCE;
  }

  private void bootstrap() {
    port(SERVER_PORT);
    get("/", (req, res) -> "Hello World");

    post("/", (request, response) -> {
      CopiedArea area = GSON.fromJson(request.body(), CopiedArea.class);
      return queue.offer(area);
    });
  }

  public static void submit(CopiedArea area) {
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      String serverHost = System.getProperty("server", "localhost");
      HttpPost post = new HttpPost("http://" + serverHost + ":" + SERVER_PORT);
      post.setEntity(new StringEntity(Serializer.GSON.toJson(area)));
      CloseableHttpResponse response = client.execute(post);
      System.out.println(EntityUtils.toString(response.getEntity()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
