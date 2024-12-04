package com.pluralsight;

import com.pluralsight.model.Actor;
import com.pluralsight.model.Film;
import com.pluralsight.utils.Utility;

import java.util.List;

import static com.pluralsight.utils.Utility.getInput;

public class Main {

    private final DataManager DATA_MANAGER = new DataManager();

    public static void main(String[] args) {
        displayActors();
        System.out.println();
        displayFilms();

    }

    public static void displayFilms() {
        int actorId = Integer.parseInt(Utility.getInput("Enter the actor ID: "));
        DataManager dataManager = new DataManager();
        List<Film> listFilms = dataManager.getFilmsByActorId(actorId);

        if (listFilms.isEmpty()) {
            System.out.println("No films found for actor ID: " + actorId);
        } else {
            System.out.println("Films:");
            Utility.printHeaderFilms();
            listFilms.forEach(System.out::println);
        }
    }

    public static void displayActors() {
        DataManager dataManager = new DataManager();
        String lastName = getInput("Enter actor last Name: ");
        List<Actor> listActors = dataManager.getActorsByLastName(lastName);

        if (listActors.isEmpty()) {
            System.out.println("No actors found for : " + lastName);
        } else {
            System.out.println("Actors:");
            Utility.printHeaderActors();
            listActors.forEach(System.out::println);
        }
    }
}