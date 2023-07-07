/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.lang.reflect.*;
import java.net.URL;

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

                for (int j = 0; j < meth.length; j++) {
                    if (meth[j].isAnnotationPresent(Urls.class)) {
                        String valeur_annotation = meth[j].getAnnotation(Urls.class).value();
                        resultat.put(valeur_annotation, new Mapping(classe_avec_son_package, meth[j].getName()));
                    }
                }
            }
        }
        return resultat;
    }
    public HashMap<Class,Object> addInstance(HashMap<String, Mapping> mappingUrls)throws Exception{
         HashMap<Class,Object> allinstance = new HashMap<>();
        for (Mapping mapping : mappingUrls.values()) {
            Class cls = Class.forName(mapping.getClassName());
        //    System.out.println("--> "+cls.getAnnotations()[0]+" <--");
            if(cls.isAnnotationPresent(Singleton.class)){
                allinstance.put(cls, cls.newInstance());
            }
        }
        return allinstance;
        
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
    public void paramInputField(Class cible,HttpServletRequest request,Field[] field,String paramName,Object o)throws Exception{//sprint 7
        for (int i = 0; i < field.length; i++) {
            if (field[i].getName().equals(paramName)) {
                Method fonct;
                fonct = cible.getMethod("set" + paramName, field[i].getType());
                fonct.invoke(o,
                        Fonction.convertirStringEnType(request.getParameter(paramName), field[i].getType()));
            }
        }
    }

/**
 * 
        for (int j = 0; j< tab_all.length; j++) {    
            for (int i = 0; i < allparam.length; i++) {
                if(request.getParameter(tab_all[j]) != null && String.valueOf(request.getParameter(tab_all[j])).equalsIgnoreCase(allparam[i])){
                    obj.add(convertirStringEnType(request.getParameter(tab_all[j]),parameterTypes[i]));
                }
            }
        }
 */

    public ModelView paramInput(HttpServletRequest request,String[] allparam,String allrequest,Method meth,Object o)throws Exception{//sprint 8
        Vector<Object> obj  = new Vector<>();
        String[] tab_all = allrequest.split(",");
        Parameter[] parameters = meth.getParameters();
        Class<?>[] parameterTypes = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parameterTypes[i] = parameters[i].getType();
        }
        String dd = "";

        
        for (int j = 0; j< tab_all.length; j++) {    
            for (int i = 0; i < allparam.length; i++) {
                if(request.getParameter(tab_all[j]) != null && tab_all[j].equalsIgnoreCase(allparam[i])){
                    obj.add(convertirStringEnType(request.getParameter(tab_all[j]),parameterTypes[i]));
                }
            }
        }
        
        //    throw new Exception("-->"+obj.get(0).toString()+"<--");
        return (ModelView) meth.invoke(o, obj.toArray(new Object[obj.size()]));
    }
        

    protected ModelView verifInputName(Class cible,Method meth, HttpServletRequest request, Object o) throws Exception{
     
            Enumeration paramNames = request.getParameterNames();
            Enumeration paramNames2 = request.getParameterNames();
            Field[] field = cible.getDeclaredFields();
            String allrequest = "";
            ModelView modelView = new ModelView();
        
            int count = 0;
            while (paramNames2.hasMoreElements()) {
                String paramName = (String) paramNames2.nextElement();
                if(count == 0){
                    allrequest = allrequest + paramName;
                }else{
                    allrequest = allrequest + "," + paramName;
                }
                count++;
            }
            boolean verif = false;

            if(!meth.getAnnotation(Urls.class).argName().equals("") && allrequest.equals("")){
                Vector<Object> obj = new Vector<>();
                Parameter[] parameters = meth.getParameters();
                Class<?>[] parameterTypes = new Class<?>[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    obj.add(setDefaultvalue(parameters[i].getType()));
                }
                modelView = (ModelView) meth.invoke(o,obj.toArray(new Object[obj.size()]));
                return modelView;
            }
            else if(!meth.getAnnotation(Urls.class).argName().equals("") && !allrequest.equals("")){
                String annot = meth.getAnnotation(Urls.class).argName();
                modelView = paramInput(request,annot.split(","),allrequest,meth,o);
                return modelView;
            }

            while (paramNames.hasMoreElements()) {
                    String paramName = (String) paramNames.nextElement();
                    paramInputField(cible,request,field,paramName,o);
            }

            modelView = (ModelView) meth.invoke(o);
            return modelView;
            
            
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
            return null;
        }
    }

    public void rules(HashMap<Class,Object> all_instance,HashMap<String, Mapping> mapping, HttpServletRequest request, HttpServletResponse response,
            String url_navigateur)
            throws Exception {
        for (String cle : mapping.keySet()) {
            if (cle.equals(url_navigateur)) {
                Class cls = Class.forName(mapping.get(url_navigateur).getClassName());

                Method meth =getMethodByName((mapping.get(url_navigateur).getMethod()),cls);

                Object o = verifSingleton(all_instance,cls);

                if (meth.getReturnType().getName().equals("etu1800.framework.ModelView")) {
                    
                //String etu = (String) o.getClass().getMethod("getNom").invoke(o);
                //int aaa = (int) o.getClass().getMethod("getAge").invoke(o);



                    ModelView mv = verifInputName(cls,meth, request, o);
                    // iteration de chaque cle et valeur Hashmap
                    this.addAttributeByHashmap(request, mv.getData());
                    // dispatcher la requete
                    RequestDispatcher dispatcher = request.getRequestDispatcher(mv.getView());
                    dispatcher.forward(request, response);
                }
            }
        }
    }
    /************SPRINT 10 */
    public Object verifSingleton(HashMap<Class,Object> all_instance,Class cls)throws Exception{
        if(all_instance.containsKey(cls)){
            return reset(all_instance.get(cls));
        }
        return cls.newInstance();
    }

    public Object reset(Object obj) throws Exception{
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            
            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                Object defaultValue = getDefaultValue(fieldType);
                field.set(obj, defaultValue);
            }
            return obj;
    }
    private  Object getDefaultValue(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return false;
            } else if (type == byte.class || type == short.class || type == int.class || type == long.class) {
                return 0;
            } else if (type == char.class) {
                return '\u0000';
            } else if (type == float.class || type == double.class) {
                return 0.0;
            }
        }
        return null;
    }
    /******************* */

    protected void addAttributeByHashmap(HttpServletRequest request, HashMap<String, Object> hmap) {
        for (String cle : hmap.keySet()) {
            request.setAttribute(cle, hmap.get(cle));
        }
    }
    
    public  Method getMethodByName(String methodName,Class<?> cls)throws Exception {
            Method[] methods = cls.getMethods();

            for (Method method : methods) {
                if (method.getName().equalsIgnoreCase(methodName)) {
                    Parameter[] parameters = method.getParameters();
                    Class<?>[] parameterTypes = new Class<?>[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        parameterTypes[i] = parameters[i].getType();
                    }
                    return cls.getMethod(method.getName(), parameterTypes);
                }

            }
        
        return null;
    }
    public Object setDefaultvalue(Class<?> type){
        if (type == String.class) {
            return "";
        } 
        return 0;
    }


    
}
