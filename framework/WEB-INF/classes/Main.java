package main;

import etu1800.framework.*;
import java.util.List;

import etu1800.framework.servlet.FrontServlet;
import util.Fonction;
import java.io.File;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        FrontServlet fs = new FrontServlet();
        // List<Class<?>> cls = fs.getClassesInPackage(
        // "C:\\Program Files\\ApacheSoftwareFoundation\\Tomcat
        // 10.1_ApacheTomcat10\\webapps\\framework\\WEB-INF\\classes\\");
        // System.out.println(cls.size());
        try {
            Fonction func = new Fonction();
            Etudiant etu = new Etudiant();
            func.verifInputName2(etu.getClass(), "Nom", etu);
            System.out.println("le nom     " + etu.getNom());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
