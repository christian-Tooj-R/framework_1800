package etu1800.framework;

import etu1800.annotation.*;
import etu1800.framework.MethodAnnotation;
import etu1800.framework.ModelView;

/**
 *
 * @author Christian
 */
@Singleton()
public class Etudiant {
    String Nom;
    int Age;

    public String getNom() {
        return Nom;
    }

    @Urls(value = "test.do", argName = "")
    public ModelView anarana() {
      //  this.setNom(thename);
      //  this.setAge(taona);

        ModelView md = new ModelView();
        md.setView("/Etudiant.jsp");
        md.addItem("nom", this.getNom());
        md.addItem("age", this.getAge());

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

    public String test2() {
        return "hafakoa";
    }

}
