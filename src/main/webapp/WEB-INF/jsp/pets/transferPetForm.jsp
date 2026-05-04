<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Transfer Pet Ownership</title>
    <link rel="stylesheet" href="/resources/css/petclinic.css">
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<h2>Transfer Pet Ownership</h2>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">
        <c:out value="${errorMessage}"/>
    </div>
</c:if>

<c:if test="${not empty pet}">
    <h3>Pet Details</h3>
    <table>
        <tr><td>Name:</td><td><c:out value="${pet.name}"/></td></tr>
        <tr><td>Type:</td><td><c:out value="${pet.type.name}"/></td></tr>
        <tr><td>Birth Date:</td><td><c:out value="${pet.birthDate}"/></td></tr>
        <tr><td>Current Owner:</td><td><c:out value="${pet.owner.firstName} ${pet.owner.lastName}"/></td></tr>
    </table>
</c:if>

<c:choose>
    <c:when test="${showConfirmation}">
        <div class="alert alert-warning">
            <strong>Warning:</strong> All historical data (visits, medical records, weight history) will remain with the pet and be visible to the new owner.
        </div>

        <h3>Confirm Transfer</h3>
        <table>
            <tr><td>Pet Name:</td><td><c:out value="${pet.name}"/></td></tr>
            <tr><td>Current Owner:</td><td><c:out value="${pet.owner.firstName} ${pet.owner.lastName}"/></td></tr>
            <tr><td>New Owner:</td><td><c:out value="${newOwner.firstName} ${newOwner.lastName}"/></td></tr>
        </table>

        <form:form action="/owners/${pet.owner.id}/pets/${pet.id}/transfer" method="post">
            <input type="hidden" name="newOwnerId" value="${newOwner.id}"/>
            <input type="hidden" name="confirm" value="true"/>
            <button type="submit">Confirm Transfer</button>
        </form:form>
        <a href="/owners/${pet.owner.id}/pets/${pet.id}/transfer">Cancel</a>
    </c:when>
    <c:otherwise>
        <h3>Search New Owner</h3>
        <form:form modelAttribute="owner" action="/owners/${pet.owner.id}/pets/${pet.id}/transfer/search" method="get">
            <label for="lastName">Last Name:</label>
            <input type="text" name="lastName" id="lastName" value=""/>
            <button type="submit">Search</button>
        </form:form>

        <c:if test="${not empty results}">
            <h3>Search Results</h3>
            <table>
                <tr><th>Name</th><th>Address</th><th>City</th><th>Telephone</th><th>Action</th></tr>
                <c:forEach items="${results}" var="owner">
                    <tr>
                        <td><c:out value="${owner.firstName} ${owner.lastName}"/></td>
                        <td><c:out value="${owner.address}"/></td>
                        <td><c:out value="${owner.city}"/></td>
                        <td><c:out value="${owner.telephone}"/></td>
                        <td>
                            <form:form action="/owners/${pet.owner.id}/pets/${pet.id}/transfer" method="post">
                                <input type="hidden" name="newOwnerId" value="${owner.id}"/>
                                <button type="submit">Transfer to this Owner</button>
                            </form:form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </c:otherwise>
</c:choose>

<jsp:include page="../fragments/footer.jsp"/>
</body>
</html>