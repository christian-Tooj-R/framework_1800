package etu1800.framework;

import etu1800.annotation.Urls;
import etu1800.framework.MethodAnnotation;
import etu1800.framework.ModelView;

/**
 *
 * @author Christian
 */
public class Etudiant {
    String Nom;
    int Age;

    public String getNom() {
        return Nom;
    }

    @Urls(value = "test")
    public ModelView anarana() {
        ModelView md = new ModelView();
        md.setView("gg");
        return md;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    // @Urls(value = "test2")
    public String test2() {
        return "hafakoa";
    }

}
