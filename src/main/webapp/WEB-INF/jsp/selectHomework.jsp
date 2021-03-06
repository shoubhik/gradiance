<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Select Topic</title>
    <style>
        .error {
            color: #ff0000;
            font-weight: bold;
        }
    </style>
</head>

<body>
<c:if test="${selectedCourse.editHomework}">
Select the homework  which you want to edit:<br/>
</c:if>
<c:if test="${selectedCourse.attemptHomework}">
    Select homework you want to attempt:<br/>
</c:if>

<form:form method="post" modelAttribute="selectedCourse">
    <td><form:errors path="" cssClass="error" /></td>
    <table>
        <tr>
            <td>Select Topic:</td>
            <td><form:select path="homework" items="${homeworks}" /></td>
            <td><form:errors path="homework" cssClass="error" /></td>
        </tr>
        <tr>
            <td colspan="3">
                <input type="hidden" value="1" name="_page"/>
                <input type="submit" value="Next" name="_target2" />
                <input type="submit" value="Cancel" name="_cancel" />
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>
