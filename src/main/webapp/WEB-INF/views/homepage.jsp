<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: dorotajachtoma
  Date: 28.07.19
  Time: 11:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Homepage</title>
</head>
<body>

<h2><a href="http://localhost:8080/barbershop_war_exploded/user/registration">Rejestracja jako użytkownik</a> </h2>
<h2><a href="http://localhost:8080/barbershop_war_exploded/barbershop/registration">Rejestracja jako barber</a> </h2>
<h2><a href="http://localhost:8080/barbershop_war_exploded/login">Logowanie jako użytkownik</a> </h2>
<h2><a href="http://localhost:8080/barbershop_war_exploded/login">Logowanie jako barber</a> </h2>


<table border="1">
    <tr>
        <th>Address</th>
        <th>City</th>
        <th>Phone Number</th>
        <th>Email</th>
        <th>Description</th>
        <th>NIP</th>
        <th>Open</th>
        <th>Close</th>


    </tr>
    <c:forEach var="barbershop" items="${barbershop}">
        <tr>
            <td>${barbershop.address}</td>
            <td>${barbershop.city}</td>
            <td>${barbershop.phoneNumber}</td>
            <td>${barbershop.email}</td>
            <td>${barbershop.description}</td>
            <td>${barbershop.nip}</td>
            <td>${barbershop.open}</td>
            <td>${barbershop.close}</td>
        </tr>
    </c:forEach>
</table>


</body>
</html>
