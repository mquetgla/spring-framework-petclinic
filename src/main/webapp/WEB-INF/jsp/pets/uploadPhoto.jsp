<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="pets">
    <jsp:attribute name="customScript">
        <script>
            function previewImage(input) {
                if (input.files && input.files[0]) {
                    var reader = new FileReader();
                    reader.onload = function(e) {
                        document.getElementById('preview').src = e.target.result;
                        document.getElementById('preview').style.display = 'block';
                    };
                    reader.readAsDataURL(input.files[0]);
                }
            }

            function uploadPhoto() {
                var formData = new FormData();
                var fileInput = document.getElementById('photoFile');
                if (fileInput.files.length === 0) {
                    alert('Please select a file');
                    return;
                }
                formData.append('file', fileInput.files[0]);

                fetch('/pet/photo/${pet.id}', {
                    method: 'POST',
                    body: formData
                })
                .then(response => response.json())
                .then(data => {
                    if (data.photoUrl) {
                        document.getElementById('currentPhoto').src = data.photoUrl;
                        document.getElementById('currentPhoto').style.display = 'block';
                        alert('Photo uploaded successfully');
                    } else if (data.error) {
                        alert('Error: ' + data.error);
                    }
                })
                .catch(error => {
                    alert('Error uploading photo: ' + error);
                });
            }

            function deletePhoto() {
                if (!confirm('Are you sure you want to delete this photo?')) {
                    return;
                }

                fetch('/pet/photo/${pet.id}', {
                    method: 'DELETE'
                })
                .then(response => response.json())
                .then(data => {
                    document.getElementById('currentPhoto').style.display = 'none';
                    document.getElementById('currentPhoto').src = '';
                    alert('Photo deleted successfully');
                })
                .catch(error => {
                    alert('Error deleting photo: ' + error);
                });
            }
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>Pet Photo</h2>

        <table class="table table-striped">
            <tr>
                <th>Pet Name</th>
                <td><c:out value="${pet.name}"/></td>
            </tr>
            <tr>
                <th>Type</th>
                <td><c:out value="${pet.type.name}"/></td>
            </tr>
            <tr>
                <th>Current Photo</th>
                <td>
                    <c:choose>
                        <c:when test="${not empty pet.photoUrl}">
                            <img id="currentPhoto" src="${pet.photoUrl}" alt="${pet.name}" style="max-width: 200px; max-height: 200px;"/>
                        </c:when>
                        <c:otherwise>
                            <img id="currentPhoto" src="" alt="No photo" style="display: none; max-width: 200px; max-height: 200px;"/>
                            <span>No photo</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>

        <h3>Upload New Photo</h3>
        <div class="form-group">
            <label for="photoFile">Select Image:</label>
            <input type="file" id="photoFile" name="file" accept="image/jpeg,image/png,image/gif,image/webp"
                   onchange="previewImage(this)" class="form-control"/>
            <small class="form-text text-muted">Allowed formats: JPEG, PNG, GIF, WebP. Maximum size: 5MB.</small>
        </div>

        <div class="form-group">
            <img id="preview" src="" alt="Preview" style="display: none; max-width: 200px; max-height: 200px;"/>
        </div>

        <div class="form-group">
            <button type="button" onclick="uploadPhoto()" class="btn btn-primary">Upload Photo</button>
            <c:if test="${not empty pet.photoUrl}">
                <button type="button" onclick="deletePhoto()" class="btn btn-danger">Delete Photo</button>
            </c:if>
        </div>

        <div class="form-group">
            <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petEditUrl">
                <spring:param name="ownerId" value="${pet.owner.id}"/>
                <spring:param name="petId" value="${pet.id}"/>
            </spring:url>
            <a href="${fn:escapeXml(petEditUrl)}" class="btn btn-default">Back to Edit Pet</a>
        </div>
    </jsp:body>
</petclinic:layout>