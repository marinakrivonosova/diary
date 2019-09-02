package ru.marina.notes;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.net.httpserver.HttpServer;

public class Main {
    public static void main(final String[] args) {
        final Injector injector = Guice.createInjector(new MyModule());

//        final MealsRESTImplementation instance = injector.getInstance(MealsRESTImplementation.class);
//
//        final String mealId = instance.addMeal(new Meal("mealId", "title", "yammy", LocalDate.now(), Timestamp.from(Instant.now())));
//
//        System.out.println(mealId);
//
//        System.out.println(instance.getMeal(mealId));

        injector.getInstance(HttpServer.class).start();
    }
}
