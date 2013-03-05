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
<p> Add question to question bank</p>
<form:form method="post" modelAttribute="selectedCourse">
    <form:errors path="" cssClass="error" />
    <table>
        <c:if test="${selectedCourse.createQuestion.newQuestion}">
        <tr>
            <td>Enter Question:</td>
            <td><form:textarea path="createQuestion.question.text" /></td>
            <td><form:errors path="createQuestion.question.text" cssClass="error" /></td>
        </tr>
        </c:if>
        <c:if test="${!selectedCourse.createQuestion.newQuestion}">
            <tr>
                <td>Question:</td>
                <td><c:out value="${selectedCourse.createQuestion.question.text}"/></td>
            </tr>
        </c:if>
        <c:if test="${selectedCourse.createQuestion.newQuestion}">
        <tr>
            <td>Difficulty Level:</td>
            <td><form:input path="createQuestion.question.difficultyLevel" /></td>
            <td><form:errors path="createQuestion.question.difficultyLevel" cssClass="error" /></td>
        </tr>
        <tr> <td>The difficulty level can be between 1 and 5 and is mandatory</td></tr>
        <tr>
            <td>Points awarded for correct answer:</td>
            <td><form:input path="createQuestion.question.pointCorrect" /></td>
            <td><form:errors path="createQuestion.question.pointCorrect" cssClass="error" /></td>
        </tr>
        <tr>
            <td>Points deducted for incorrect answer:</td>
            <td><form:input path="createQuestion.question.pointIncorrect" /></td>
            <td><form:errors path="createQuestion.question.pointIncorrect" cssClass="error" /></td>
        </tr>
        <tr>
            <td>Explanation for correct answer:</td>
            <td><form:textarea path="createQuestion.question.hint.text" /></td>
            <td><form:errors path="createQuestion.question.hint.text" cssClass="error" /></td>
        </tr>
        </c:if>
        <tr>
            <td>Fill in the correct answers:</td>
        </tr>
        <c:forEach var="i" begin="1" end="${selectedCourse.createQuestion.numCorrect}">
            <c:set var="txt_name" value="${'correct_ans'}${i}"/>
            <c:set var="hnt_name" value="${'correct_hint'}${i}"/>
            <tr>
                <td>Correct Answer:</td>
                <td><textarea name="${txt_name}"></textarea></td>
            </tr>
            <tr>
                <td>Answer explaination:</td>
                <td><textarea name="${hnt_name}"></textarea></td>
            </tr>
        </c:forEach>
        <tr>
            <td>Fill in the Incorrect answers:</td>
        </tr>
        <c:forEach var="i" begin="1" end="${selectedCourse.createQuestion.numIncorrect}">
            <c:set var="txt_name" value="${'incorrect_ans'}${i}"/>
            <c:set var="hnt_name" value="${'incorrect_hint'}${i}"/>
            <tr>
                <td>Incorrect Answer:</td>
                <td><textarea name="${txt_name}"></textarea></td>
            </tr>
            <tr>
                <td>Answer hint:</td>
                <td><textarea name="${hnt_name}"></textarea></td>
            </tr>
        </c:forEach>
        <tr>
            <c:if test="${selectedCourse.createQuestion.newQuestion}">
            <td colspan="3">
                <input type="hidden" value="2" name="_page"/>
                <input type="submit" value="Finish" name="_finish" />
                <input type="submit" value="Cancel" name="_cancel" />
            </td>
            </c:if>
            <c:if test="${!selectedCourse.createQuestion.newQuestion}">
            <td colspan="3">
                <input type="hidden" value="3" name="_page"/>
                <input type="submit" value="Finish" name="_finish" />
                <input type="submit" value="Cancel" name="_cancel" />
            </td>
            </c:if>

        </tr>
    </table>
</form:form>

</body>
</html>