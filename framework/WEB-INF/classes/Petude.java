package etu1800.framework;

import etu1800.annotation.Urls;

public class Petude {
    String Nom = "bogosy ";
    int Age = 29;

    @Urls(value = "affiche1")
    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    @Urls(value = "affiche2")
    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }
}