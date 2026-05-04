<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Transfer Successful</title>
    <link rel="stylesheet" href="/resources/css/petclinic.css">
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<h2>Ownership Transfer Successful</h2>

<div class="alert alert-success">
    Pet ownership has been successfully transferred.
</div>

<c:if test="${not empty pet}">
    <h3>Updated Pet Details</h3>
    <table>
        <tr><td>Pet Name:</td><td><c:out value="${pet.name}"/></td></tr>
        <tr><td>New Owner:</td><td><c:out value="${newOwner.firstName} ${newOwner.lastName}"/></td></tr>
    </table>
</c:if>

<a href="/owners/${newOwner.id}">View New Owner Details</a> |
<a href="/owners/${pet.owner.id}/pets/${pet.id}">View Pet Details</a>

<jsp:include page="../fragments/footer.jsp"/>
</body>
</html>