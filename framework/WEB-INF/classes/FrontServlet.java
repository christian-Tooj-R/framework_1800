/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu1800.framework.servlet;

import etu1800.framework.Etudiant;
import etu1800.framework.Mapping;
import etu1800.framework.MethodAnnotation;
import etu1800.framework.ModelView;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.io.File;

import java.util.HashMap;
import java.util.List;
import util.Fonction;

/**
 *
 * @author Christian
 */
public class FrontServlet extends HttpServlet {

    HashMap<String, Mapping> MappingUrls;
    protected Fonction func;
    String tafiditsa = "tsisy";
    String vue = "";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */

    public void init() throws ServletException {
        ServletContext context = getServletContext();
        String chemin_de_l_application = context.getRealPath("/");
        File file = new File(chemin_de_l_application + "WEB-INF\\classes\\");
        Fonction function = new Fonction();
        try {
            MappingUrls = function.tout_fichier(chemin_de_l_application + "WEB-INF\\classes\\", file,
                    new HashMap<String, Mapping>());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String url = request.getRequestURL().toString();
            String[] uri = request.getRequestURI().toString().split("/");
            out.println("url navigateur   " + uri[uri.length - 1]);
            String url_navigateur = uri[uri.length - 1];
            try {
                String nom_methode = this.MappingUrls.get(url_navigateur).getMethod();
                out.println("Result methodIvelany  " + nom_methode);
                this.rules(this.MappingUrls, request, response, url_navigateur);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void rules(HashMap<String, Mapping> mapping, HttpServletRequest request, HttpServletResponse response,
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

    protected void addAttributeByHashmap(HttpServletRequest request, HashMap<String, Object> hmap) {
        for (String cle : hmap.keySet()) {
            request.setAttribute(cle, hmap.get(cle));
        }
    }

    public static List<Class<?>> getClassesInPackage(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            for (java.net.URL resource : java.util.Collections.list(classLoader.getResources(path))) {
                for (String file : new java.io.File(resource.toURI()).list()) {
                    if (file.endsWith(".class")) {
                        String className = packageName + '.' + file.substring(0, file.length() - 6);
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
