<%-- 
    Document   : Etudiant
    Author     : Christian
--%>
<%
    String nomEtud =(String) request.getAttribute("nom");
    int ageEtud =(int) request.getAttribute("age");
%>
<h4>Bonjour <% out.print(nomEtud); %>.Vous avez <% out.print(ageEtud); %> ans.</h4>