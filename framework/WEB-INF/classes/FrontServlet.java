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
    HashMap<Class,Object> all_instance;


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
            all_instance = function.addInstance(MappingUrls);     
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
                Fonction function = new Fonction();
                String nom_methode = this.MappingUrls.get(url_navigateur).getMethod();
                
                function.rules(this.all_instance,this.MappingUrls, request, response, url_navigateur);
            } catch (Exception e) {
                e.printStackTrace();
                // Capture de l'exception
                Throwable t = e;
                // Affichage de la trace de la pile d'exceptions
                out.println("<pre>");
                while (t != null) {
                    out.println(t.toString());
                    for (StackTraceElement element : t.getStackTrace()) {
                        out.println("&nbsp;&nbsp;&nbsp;&nbsp;" + element.toString());
                    }
                    t = t.getCause();
                }
                out.println("</pre>");
            }
        }
    }

    protected void addAttributeByHashmap(HttpServletRequest request, HashMap<String, Object> hmap) {
        for (String cle : hmap.keySet()) {
            request.setAttribute(cle, hmap.get(cle));
        }
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
