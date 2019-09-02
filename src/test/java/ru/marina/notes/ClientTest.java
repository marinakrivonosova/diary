package ru.marina.notes;

import org.glassfish.jersey.client.proxy.WebResourceFactory;
import ru.marina.notes.meals.MealsREST;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class ClientTest {
    public static void main(final String[] args) {
        final Client client = ClientBuilder.newClient();
        final WebTarget webTarget = client.target("http://localhost:8080/diary/rest");

        final MealsREST meals = WebResourceFactory.newResource(MealsREST.class, webTarget);
    }
}
