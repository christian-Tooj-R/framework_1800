<%-- 
    Document   : Etudiant
    Author     : Christian
--%>
<%
    String nomEtud =(String) request.getAttribute("nom");
    int ageEtud =(int) request.getAttribute("age");
%>
<h4>Bonjour <% out.print(nomEtud); %>.Vous avez <% out.print(ageEtud); %> ans.</h4>

<form action="framework/test.do"> 
Entrer le Nom : <input type="text" name="Nom"> 
Entrer l'Age : <input type="number" name="Age"> 
<input type="submit" value="Valider"> 
</form>
<br><hr>
<form action="framework/test.do"> 
Entrer le Nom : <input type="text" name="anarana"> 
Entrer l'Age : <input type="number" name="taona"> 
<input type="submit" value="Valider"> 
</form>
