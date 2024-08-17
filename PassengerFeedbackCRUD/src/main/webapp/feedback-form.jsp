<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Feedback Application</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            text-align: center;
            margin: 0;
            overflow: hidden; 
        }

        .background-image {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-image: url('https://trumpwallpapers.com/wp-content/uploads/Bus-Wallpaper-25-1607x856-1.jpg');
            background-size: cover;
            background-position: center;
            filter: blur(2px); 
            z-index: -1;
        }

        .form-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            max-width: 600px;
            width: 100%;
            margin: 20px auto;
            background-color: rgba(0, 0, 0, 0.5); 
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            z-index: 1;
        }

        .form-container h2 {
            margin-bottom: 20px;
            color: #fff; 
        }

        form table {
            width: 100%;
        }

        form th {
            text-align: left;
            padding: 10px;
            color: #fff; 
            font-weight: bold;
        }

        form td {
            padding: 10px;
        }

        form input[type="text"],
        form textarea {
            width: calc(100% - 22px);
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ced4da;
            border-radius: 4px;
            box-sizing: border-box;
            background-color: rgba(255, 255, 255, 0.8); 
        }

        form textarea {
            height: 200px; 
            resize: vertical; 
        }

        form input[type="submit"],
        form input[type="button"] {
            padding: 12px 24px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease, color 0.3s ease;
        }

        form input[type="submit"] {
            background-color: #28a745; 
            color: #fff;
        }

        form input[type="submit"]:hover {
            background-color: #218838;
        }

        form input[type="button"] {
            background-color: #ffc107; 
            color: #343a40;
        }

        form input[type="button"]:hover {
            background-color: #e0a800;
        }

        .error {
            color: red;
            font-size: 0.8em;
            margin-top: 5px;
        }
        
        form input[type="button"].cancel {
            background-color: #dc3545;
            color: #fff;
        }

        form input[type="button"].cancel:hover {
            background-color: #c82333;
        }

        .button-container {
            display: flex;
            justify-content: space-between;
            width: 100%;
            max-width: 300px; 
        }

       
        #messageCounter {
            color: white;
        }
    </style>
</head>
<body>
    <div class="background-image"></div>
    <div class="form-container">
        <c:if test="${feedback != null}">
            <form action="update" method="post" onsubmit="return validateForm() && confirmSubmit()">
        </c:if>
        <c:if test="${feedback == null}">
            <form action="insert" method="post" onsubmit="return validateForm() && confirmSubmit()">
        </c:if>
        
            <caption>
                <h2>
                    <c:if test="${feedback != null}">
                        Edit Feedback
                    </c:if>
                    <c:if test="${feedback == null}">
                        Passenger Feedback
                    </c:if>
                </h2>
            </caption>
            <c:if test="${feedback != null}">
                <input type="hidden" name="id" value="<c:out value='${feedback.id}' />" />
            </c:if>
            <table>
            <tr>
                <th><b>Name:</b> </th>
                <td>
                    <input type="text" name="name" id="name" size="45" value="<c:out value='${feedback.name}' />" />
                    <div id="nameError" class="error"></div>
                </td>
            </tr>
            <tr>
                <th><b>Email:</b> </th>
                <td>
                    <input type="text" name="email" id="email" size="45" value="<c:out value='${feedback.email}' />" />
                    <div id="emailError" class="error"></div>
                </td>
            </tr>
            <tr>
                <th><b>Subject:</b> </th>
                <td>
                    <input type="text" name="subject" id="subject" size="45" value="<c:out value='${feedback.subject}' />" />
                    <div id="subjectError" class="error"></div>
                </td>
            </tr>
            <tr>
                <th><b>Message:</b> </th>
                <td>
                   <textarea id="feedbackMessage" name="message" required maxlength="2000"><c:out value='${feedback.message}' /></textarea>
                   <div id="messageError" class="error"></div>
                   <div id="messageCounter"></div>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <div class="button-container">
                        <input type="submit" value="Submit" />
                        <input type="button" value="Clear" onclick="clearForm()" />
                        <input type="button" class="cancel" value="Cancel" onclick="cancelForm()" />
                    </div>
                </td>
            </tr>
            </table>
       
        </form>
    </div>
    <script>
    function validateForm() {
        var name = document.getElementById('name').value;
        var email = document.getElementById('email').value;
        var subject = document.getElementById('subject').value;
        var message = document.getElementById('feedbackMessage').value;
        var emailError = document.getElementById('emailError');
        var nameError = document.getElementById('nameError');
        var subjectError = document.getElementById('subjectError');
        var messageError = document.getElementById('messageError');
        var valid = true;
        var numberPattern = /\d/;

        if (name.trim() === "") {
            nameError.innerHTML = "Name is required.";
            valid = false;
        } else if (numberPattern.test(name)) {
            nameError.innerHTML = "Name cannot contain numbers.";
            valid = false;
        } else {
            nameError.innerHTML = "";
        }

        if (email.trim() === "" || email.indexOf('@') < 1) {
            emailError.innerHTML = "Please enter a valid email address.";
            valid = false;
        } else if (numberPattern.test(email.charAt(0))) {
            emailError.innerHTML = "Email cannot start with a number.";
            valid = false;
        } else {
            emailError.innerHTML = "";
        }

        if (subject.trim() === "") {
            subjectError.innerHTML = "Subject is required.";
            valid = false;
        } else if (numberPattern.test(subject)) {
            subjectError.innerHTML = "Subject cannot contain numbers.";
            valid = false;
        } else {
            subjectError.innerHTML = "";
        }

        if (message.trim() === "") {
            messageError.innerHTML = "Message is required.";
            valid = false;
        } else {
            messageError.innerHTML = "";
        }

        return valid;
    }
    
    function confirmSubmit() {
        return window.confirm("Are you sure you want to submit the form?");
    }

    function clearForm() {
        document.querySelectorAll('input[type="text"]').forEach(input => input.value = '');
        document.getElementById('feedbackMessage').value = '';
        document.querySelectorAll('.error').forEach(error => error.innerHTML = '');
        
        document.getElementById('messageCounter').textContent = '';
    }

    function cancelForm() {
        window.location.href = "home.jsp"; 
    }

    function toggleButtons() {
        var clearButton = document.querySelector('input[type="button"][value="Clear"]');
        var cancelButton = document.querySelector('input[type="button"][value="Cancel"]');
        
        
        clearButton.disabled = true;
        cancelButton.disabled = true;
    }

    document.getElementById('feedbackMessage').addEventListener('input', function() {
        var maxLength = 2000;
        var currentLength = this.value.length;
        var counterElement = document.getElementById('messageCounter');
        counterElement.textContent = maxLength - currentLength + ' characters remaining';
    });

    
    if (${feedback != null}) {
        toggleButtons();
    }
</script>

</body>

</html>