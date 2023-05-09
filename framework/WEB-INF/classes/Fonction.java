/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.*;
import etu1800.annotation.*;
import java.util.*;
import java.util.regex.Pattern;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.*;
import etu1800.framework.*;

/**
 *
 * @author Christian
 */
public class Fonction {

    public String processUrl(String url_input, String ctx) {
        ctx += "/";
        int ctx_ind = url_input.indexOf(ctx);
        System.out.print(ctx_ind);
        String url = url_input.substring(ctx_ind + ctx.length());

        return url;
    }

    public int somme(int a, int b) {
        return a + b;
    }

    public HashMap<String, Mapping> tout_fichier(String emplacement_des_classes, File dir,
            HashMap<String, Mapping> resultat) throws Exception {
        // HashMap<String, Mapping> resultat = new HashMap<String, Mapping>();
        File[] liste = dir.listFiles();
        for (int i = 0; i < liste.length; i++) {
            if (liste[i].isDirectory()) {
                resultat = tout_fichier(emplacement_des_classes, liste[i], resultat);
            } else if (liste[i].getName().contains(".class")) {
                System.out.println("Liste name   " + liste[i].getName());
                String separator = "\\";
                String classe_avec_son_package = liste[i].toString()
                        .replace(emplacement_des_classes, "")
                        .replace(separator, ".")
                        .replace(".class", "");

                Class A = Class.forName(classe_avec_son_package);
                Method[] meth = A.getDeclaredMethods();

                System.out.println("class avec package   " + classe_avec_son_package);
                for (int j = 0; j < meth.length; j++) {
                    if (meth[j].isAnnotationPresent(Urls.class)) {
                        System.out.println("Metaodyy   " + meth[j].getName());
                        String valeur_annotation = meth[j].getAnnotation(Urls.class).value();
                        System.out.println("cleuuuussss    " + valeur_annotation);
                        resultat.put(valeur_annotation, new Mapping(classe_avec_son_package, meth[j].getName()));
                    }
                }
            }
        }
        System.out.println("taille atoo nefa   " + resultat.size());
        return resultat;
    }

    public void verifInputName2(Class cible, String champ, Object o) {
        try {
            Field[] field = cible.getDeclaredFields();

            for (int i = 0; i < field.length; i++) {
                if (field[i].getName().equals(champ)) {

                    Method fonct;
                    fonct = cible.getMethod("set" + field[i].getName(), field[i].getType());
                    fonct.invoke(o, 78);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void verifInputName(Class cible, HttpServletRequest request, Object o) {
        try {
            Enumeration paramNames = request.getParameterNames();
            Field[] field = cible.getDeclaredFields();

            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
                for (int i = 0; i < field.length; i++) {
                    if (field[i].getName().equals(paramName)) {
                        Method fonct;
                        fonct = cible.getMethod("set" + paramName, field[i].getType());
                        fonct.invoke(o,
                                Fonction.convertirStringEnType(request.getParameter(paramName), field[i].getType()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object convertirStringEnType(String valeur, Class<?> type) {
        if (type == String.class) {
            return valeur;
        } else if (type == int.class || type == Integer.class) {
            return Integer.parseInt(valeur);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(valeur);
        } else if (type == float.class || type == Float.class) {
            return Float.parseFloat(valeur);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(valeur);
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(valeur);
        } else {
            throw new IllegalArgumentException("Type non pris en charge: " + type.getName());
        }
    }

    public void rules(HashMap<String, Mapping> mapping, HttpServletRequest request, HttpServletResponse response,
            String url_navigateur)
            throws Exception {
        for (String cle : mapping.keySet()) {
            if (cle.equals(url_navigateur)) {
                Class cls = Class.forName(mapping.get(url_navigateur).getClassName());

                Method meth = cls.getMethod(mapping.get(url_navigateur).getMethod());

                Object o = cls.newInstance();

                if (meth.getReturnType().getName().equals("etu1800.framework.ModelView")) {
                    verifInputName(cls, request, o);
                    String etu = (String) o.getClass().getMethod("getNom").invoke(o);
                    int aaa = (int) o.getClass().getMethod("getAge").invoke(o);

                    ModelView mv = (ModelView) meth.invoke(o);
                    // iteration de chaque cle et valeur Hashmap
                    this.addAttributeByHashmap(request, mv.getData());
                    // dispatcher la requete
                    RequestDispatcher dispatcher = request.getRequestDispatcher(mv.getView());
                    dispatcher.forward(request, response);
                }
            }
        }
    }

    protected void addAttributeByHashmap(HttpServletRequest request, HashMap<String, Object> hmap) {
        for (String cle : hmap.keySet()) {
            request.setAttribute(cle, hmap.get(cle));
        }
    }

}
