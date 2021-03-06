<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <style>
        .error {
            color: #ff0000;
            font-weight: bold;
        }
    </style>

    <title>Enter Number Of Questions</title>
</head>
<body>
<c:if test="${selectedCourse.createQuestion.newQuestion}">
<p>Input number of correct and incorrect answers for this question.
    there should be at minimum 1 correct and 3 incorrect answers.
</p>
</c:if>

<c:if test="${!selectedCourse.createQuestion.newQuestion}">
    <p>you need to add at least one answer</p>
</c:if>
<form:form method="post" modelAttribute="selectedCourse">

    <table>
        <tr>
            <td>Number of correct answers:</td>
            <td><form:input path="createQuestion.numCorrect" /></td>
            <td><form:errors path="createQuestion.numCorrect" cssClass="error" /></td>
        </tr>
        <tr>
            <td>Number of incorrect answers:</td>
            <td><form:input path="createQuestion.numIncorrect" /></td>
            <td><form:errors path="createQuestion.numIncorrect" cssClass="error" /></td>
        </tr>
        <tr>
            <c:if test="${selectedCourse.createQuestion.newQuestion}">
                <td colspan="3">
                    <input type="hidden" value="1" name="_page"/>
                    <input type="submit" value="Next" name="_target2"/>
                    <input type="submit" value="Cancel" name="_cancel"/>
                </td>
            </c:if>
            <c:if test="${!selectedCourse.createQuestion.newQuestion}">
                <td colspan="3">
                    <input type="hidden" value="2" name="_page"/>
                    <input type="submit" value="Next" name="_target3"/>
                    <input type="submit" value="Cancel" name="_cancel"/>
                </td>
            </c:if>
        </tr>
    </table>
</form:form>

</body>
</html>