<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
    <jsp:attribute name="customScript">
        <link rel="stylesheet" href="/webjars/flatpickr/4.6.13/dist/flatpickr.min.css">
        <script src="/webjars/flatpickr/4.6.13/dist/flatpickr.js"></script>
        <script>
            flatpickr("#birthDate", {});

            function previewUrl(input) {
                var url = input.value;
                var preview = document.getElementById('urlPreview');
                if (url) {
                    preview.src = url;
                    preview.style.display = 'block';
                    preview.onerror = function() {
                        preview.style.display = 'none';
                    };
                } else {
                    preview.style.display = 'none';
                }
            }
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>
            <c:if test="${pet['new']}">New </c:if> Pet
        </h2>
        <form:form modelAttribute="pet"
                   class="form-horizontal">
            <input type="hidden" name="id" value="${pet.id}"/>
            <div class="form-group has-feedback">
                <div class="form-group">
                    <label class="col-sm-2 control-label">Owner</label>
                    <div class="col-sm-10">
                        <c:out value="${pet.owner.firstName} ${pet.owner.lastName}"/>
                    </div>
                </div>
                <petclinic:inputField label="Name" name="name"/>
                <petclinic:inputField label="Birth Date" name="birthDate"/>
                <petclinic:inputField label="Photo URL" name="photoUrl" onchange="previewUrl(this)"/>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <c:if test="${not empty pet.photoUrl}">
                            <img id="urlPreview" src="${pet.photoUrl}" alt="Photo Preview" style="max-width: 150px; max-height: 150px; margin-top: 10px;"/>
                        </c:if>
                        <c:if test="${empty pet.photoUrl}">
                            <img id="urlPreview" src="" alt="Photo Preview" style="display: none; max-width: 150px; max-height: 150px; margin-top: 10px;"/>
                        </c:if>
                    </div>
                </div>
                <petclinic:inputField label="Microchip ID" name="microchipId"/>
                <petclinic:inputField label="Color" name="color"/>
                <petclinic:inputField label="Breed" name="breed"/>
                <div class="control-group">
                    <petclinic:selectField name="type" label="Type " names="${types}" size="5"/>
                </div>
                <c:if test="${not pet['new']}">
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <spring:url value="/owners/{ownerId}/pets/{petId}/photo" var="uploadPhotoUrl">
                            <spring:param name="ownerId" value="${pet.owner.id}"/>
                            <spring:param name="petId" value="${pet.id}"/>
                        </spring:url>
                        <a href="${fn:escapeXml(uploadPhotoUrl)}" class="btn btn-info">Upload Photo File</a>
                    </div>
                </div>
                </c:if>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${pet['new']}">
                            <button class="btn btn-primary" type="submit">Add Pet</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-primary" type="submit">Update Pet</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
    </jsp:body>
</petclinic:layout>
