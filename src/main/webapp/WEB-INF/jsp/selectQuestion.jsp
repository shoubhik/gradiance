<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
    <title>Select Question</title>
    <style>
        .error {
            color: #ff0000;
            font-weight: bold;
        }
    </style>
</head>
<body>
Select the question for which you want to add the answers:<br/>
<form:form method="post" modelAttribute="selectedCourse">
    <table>
        <tr>
            <td>Select Question:</td>
            <td><form:select path="createQuestion.question" items="${question}" /></td>
            <td><form:errors path="createQuestion.question" cssClass="error" /></td>
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