<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Course Added</title>
</head>
<body>
  the course  with id <c:out value="${course.courseId}"/> and name <c:out value="${course.courseName}"/>  has been added to your account
 <br/>
<a href="home">Back</a>

</body>
</html>