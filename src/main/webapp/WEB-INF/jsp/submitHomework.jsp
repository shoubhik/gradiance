<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <style>
        .error {
            color: #ff0000;
            font-weight: bold;
        }
    </style>

    <title>Add Question</title>
</head>
<body>
<p> Add Homework</p>
<form:form method="post" modelAttribute="selectedCourse">
    <form:errors path="" cssClass="error" />
    <table>
        <tr>
            <td>Homework Name</td>
            <c:if test="${selectedCourse.homework.newHomework}">
            <td><form:input path="homework.name" /></td>
            <td><form:errors path="homework.name" cssClass="error" /></td>
            </c:if>
            <c:if test="${!selectedCourse.homework.newHomework}">
                <td><c:out value="${selectedCourse.homework.name}"/></td>
            </c:if>
        </tr>
        <tr>
            <td>Start Date</td>
            <td><form:input path="homework.startDate" /></td>
            <td>specify date in 'yyyy-MM-dd' format</td>
            <td><form:errors path="homework.startDate" cssClass="error" /></td>
        </tr>
        <tr>
            <td>End Date</td>
            <td><form:input path="homework.endDate" /></td>
            <td>specify date in 'yyyy-MM-dd' format</td>
            <td><form:errors path="homework.endDate" cssClass="error" /></td>
        </tr>
        <tr>
            <td>Max Attempts</td>
            <td><form:input path="homework.numAttempts" /></td>
            <td><form:errors path="homework.numAttempts" cssClass="error" /></td>
        </tr>
        <tr>
            <td>Enter 0 for infinite attempts</td>
        </tr>
        <tr>
            <td>Score selection scheme</td>
            <td><form:select  path="homework.scoreSelectionScheme" items="${schemes}"/></td>
            <td><form:errors path="homework.scoreSelectionScheme" cssClass="error" /></td>
        </tr>
        <tr>
            <td>Correct Points for questions</td>
            <td><form:input path="homework.correctPts" /></td>
            <td><form:errors path="homework.correctPts" cssClass="error" /></td>
        </tr>
        <tr>
            <td>Incorrect Points for questions</td>
            <td><form:input path="homework.incorrectPts" /></td>
            <td><form:errors path="homework.incorrectPts" cssClass="error" /></td>
        </tr>
        <tr>
            <td>Number of questions</td>
            <td><form:input path="homework.numQuestions" /></td>
            <td><form:errors path="homework.numQuestions" cssClass="error" /></td>
        </tr>
        <tr>
            <td colspan="3">
                <c:if test="${selectedCourse.homework.newHomework}">
                    <input type="hidden" value="1" name="_page"/>
                </c:if>
                <c:if test="${!selectedCourse.homework.newHomework}">
                    <input type="hidden" value="2" name="_page"/>
                </c:if>
                <input type="submit" value="Finish" name="_finish"/>
                <input type="submit" value="Cancel" name="_cancel"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>