<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
Register New User
<form:form method="post" modelAttribute="user">
    <form:errors path="" cssClass="error"/>
    <table>
        <tr>
            <td>User Id<form:input path="uid"/></td>
            <td><form:errors path="uid" cssClass="error"/></td>
        </tr>
        <tr>
            <td>User Name<form:input path="uname"/></td>
            <td><form:errors path="uname" cssClass="error"/></td>
        </tr>
        <tr>
            <td>Password <form:password path="password"/></td>
            <td><form:errors path="password" cssClass="error"/></td>
        </tr>
        <tr>
            <td>Role
                <form:select path="roles" items="${roles}" multiple="false"/>
            </td>
            <td><form:errors path="roles" cssClass="error"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Register"/></td>
        </tr>
    </table>
</form:form>

</body>
</html>