    package org.kirill.todo;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**ls
 * Created by kirill on 18.02.16.
 */
public class ToDoApplication {

    private static Map<Integer, Activity> todos = new HashMap<>();

    public static void main(String[] args) {
        System.out.println(PortResolver.getPort());
        // mock
        fillTodos();
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.route(HttpMethod.OPTIONS, "/").handler(routingContext -> {

            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");
            addCorsHeaders(response);
            response.end(Json.encode(new ArrayList<>(todos.values())));
        });
        router.route(HttpMethod.GET, "/").handler(routingContext -> {

            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");
            addCorsHeaders(response);
            response.end(Json.encode(new ArrayList<>(todos.values())));
        });
        router.route(HttpMethod.POST, "/:name").handler(routingContext -> {

            // This handler will be called for every request
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");
            String name = routingContext.request().getParam("name");
            addCorsHeaders(response);
            response.end("Hello, " + name + ", from Vert.x-Web!");
        });

        server.requestHandler(router::accept)
                .listen(PortResolver.getPort(), "0.0.0.0");
    }

    private static void addCorsHeaders(HttpServerResponse response) {
        response.putHeader("Access-Control-Allow-Origin", "*")
                .putHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                .putHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private static void fillTodos() {
        todos.put(1, new Activity(1, "Hell, yeah!", false, 1));
        todos.put(2, new Activity(2, "Hell, yeah!", false, 3));
        todos.put(3, new Activity(3, "Hell, yeah!", false, 1));
    }

}
