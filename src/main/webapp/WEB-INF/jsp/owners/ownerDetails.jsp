<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">

    <h2 id="ownerInformation">Owner Information</h2>

    <table class="table table-striped" aria-describedby="ownerInformation">
        <tr>
            <th id="name">Name</th>
            <td headers="name"><strong><c:out value="${owner.firstName} ${owner.lastName}"/></strong></td>
        </tr>
        <tr>
            <th id="address">Address</th>
            <td headers="address"><c:out value="${owner.address}"/></td>
        </tr>
        <tr>
            <th id="city">City</th>
            <td headers="city"><c:out value="${owner.city}"/></td>
        </tr>
        <tr>
            <th id="telephone">Telephone</th>
            <td headers="telephone"><c:out value="${owner.telephone}"/></td>
        </tr>
    </table>

    <spring:url value="{ownerId}/edit" var="editUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-primary">Edit Owner</a>

    <spring:url value="{ownerId}/pets/new" var="addUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-primary">Add New Pet</a>

    <br/>
    <br/>
    <br/>
    <h2 id="petsAndVisits">Pets and Visits</h2>

    <table class="table table-striped" aria-describedby="petsAndVisits">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <th scope="col">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:choose>
                                <c:when test="${not pet.active}">
                                    <del><c:out value="${pet.name}"/></del>
                                    <span class="label label-warning">Inactive</span>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${pet.name}"/>
                                </c:otherwise>
                            </c:choose>
                        </dd>
                        <dt>Birth Date</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                        <dt>Photo</dt>
                        <dd>
                            <c:if test="${not empty pet.photoUrl}">
                                <img src="${pet.photoUrl}" alt="${pet.name}" style="max-width: 100px; max-height: 100px;"/>
                            </c:if>
                            <c:if test="${empty pet.photoUrl}">
                                <c:out value="-"/>
                            </c:if>
                        </dd>
                        <dt>Microchip</dt>
                        <dd>
                            <c:if test="${not empty pet.microchip}">
                                <c:out value="${pet.microchip}"/>
                            </c:if>
                            <c:if test="${empty pet.microchip}">
                                <c:out value="-"/>
                            </c:if>
                        </dd>
                        <dt>Gender</dt>
                        <dd><c:out value="${pet.gender != null ? pet.gender : 'UNKNOWN'}"/></dd>
                        <dt>Color</dt>
                        <dd><c:out value="${not empty pet.color ? pet.color : '-'}"/></dd>
                        <dt>Breed</dt>
                        <dd><c:out value="${not empty pet.breed ? pet.breed : '-'}"/></dd>
                        <dt>Weight</dt>
                        <dd><c:out value="${pet.weight != null ? pet.weight : '-'}"/></dd>
                        <dt>Notes</dt>
                        <dd><c:out value="${not empty pet.notes ? pet.notes : '-'}"/></dd>
                    </dl>
                </th>
                <td>
                    <table class="table-condensed" aria-describedby="petsAndVisits">
                        <thead>
                        <tr>
                            <th id="visitDate">Visit Date</th>
                            <th id="visitDescription">Description</th>
                            <th id="visitVet">Vet</th>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td headers="visitDate"><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td headers="visitDescription"><c:out value="${visit.description}"/></td>
                                <td headers="visitVet">
                                    <c:if test="${visit.vet != null}">
                                        <c:out value="${visit.vet.firstName} ${visit.vet.lastName}"/>
                                    </c:if>
                                    <c:if test="${visit.vet == null}">-</c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}">Edit Pet</a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}">Add Visit</a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/weightRecords/new" var="weightUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(weightUrl)}">Add Weight Record</a>
                            </td>
                        </tr>
                    </table>

                    <c:if test="${not empty pet.weightRecords}">
                        <h4>Weight History</h4>
                        <table class="table-condensed" aria-describedby="petsAndVisits">
                            <thead>
                            <tr>
                                <th>Date</th>
                                <th>Weight (kg)</th>
                            </tr>
                            </thead>
                            <c:forEach var="wr" items="${pet.weightRecords}">
                                <tr>
                                    <td><petclinic:localDate date="${wr.measureDate}" pattern="yyyy-MM-dd"/></td>
                                    <td><c:out value="${wr.weight}"/></td>
                                </tr>
                            </c:forEach>
                        </table>
                        <canvas id="weightChart${pet.id}" width="400" height="200"></canvas>
                    </c:if>
                </td>
            </tr>

        </c:forEach>
    </table>

    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
    <script>
        <c:forEach var="pet" items="${owner.pets}">
            <c:if test="${not empty pet.weightRecords}">
                (function() {
                    var labels = [<c:forEach var="wr" items="${pet.weightRecords}" varStatus="s">'${wr.measureDate}'<c:if test="${!s.last}">,</c:if></c:forEach>];
                    var data = [<c:forEach var="wr" items="${pet.weightRecords}" varStatus="s">${wr.weight}<c:if test="${!s.last}">,</c:if></c:forEach>];
                    var ctx = document.getElementById('weightChart${pet.id}');
                    if (ctx) {
                        new Chart(ctx, {
                            type: 'line',
                            data: {
                                labels: labels,
                                datasets: [{
                                    label: 'Weight (kg)',
                                    data: data,
                                    borderColor: 'rgb(75, 192, 192)',
                                    tension: 0.1
                                }]
                            },
                            options: {
                                responsive: false,
                                scales: { y: { beginAtZero: false } }
                            }
                        });
                    }
                })();
            </c:if>
        </c:forEach>
    </script>

</petclinic:layout>
