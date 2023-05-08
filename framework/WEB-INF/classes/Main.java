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
            File file = new File(
                    "C:\\Program Files\\ApacheSoftwareFoundation\\Tomcat 10.1_ApacheTomcat10\\webapps\\framework\\WEB-INF\\classes\\");
            Fonction func = new Fonction();
            HashMap<String, Mapping> MappingUrls = func.tout_fichier(
                    "C:\\Program Files\\ApacheSoftwareFoundation\\Tomcat 10.1_ApacheTomcat10\\webapps\\framework\\WEB-INF\\classes\\",
                    file, new HashMap<String, Mapping>());

            System.out.println("---------------------------------------------------------------------------");
            System.out.println("tailleur   " + MappingUrls.size());
            System.out.println("<h1>Nom class " + MappingUrls.get("test").getClassName() + "</h1>");
            System.out.println("Result method  " + MappingUrls.get("test").getMethod());
            for (String i : MappingUrls.keySet()) {
                System.out.println("Keysettt   " + i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
