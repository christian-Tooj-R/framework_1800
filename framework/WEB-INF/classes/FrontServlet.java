/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu1800.framework.servlet;

import etu1800.framework.Mapping;
import etu1800.framework.MethodAnnotation;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

            try {
                out.println("miseo ve     " + tafiditsa);
                out.println("<h1>Nom class " + this.MappingUrls.get(uri[uri.length - 1]).getClassName() + "</h1>");
                out.println("Result method  " + this.MappingUrls.get(uri[uri.length - 1]).getMethod());
                Class cls = Class.forName(this.MappingUrls.get(uri[uri.length - 1]).getClassName());
                Method meth = cls.getMethod(this.MappingUrls.get(uri[uri.length - 1]).getMethod());
                Object o = cls.newInstance();
                out.println("anatiny   " + meth.invoke(o));

            } catch (Exception e) {
                e.printStackTrace();
            }
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