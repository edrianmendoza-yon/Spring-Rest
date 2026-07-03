/* import { getStudentsAPI, addStudentAPI } from './api.js';

let localStudents = [];

const studentForm = document.getElementById("studentForm");
const studentTableBody = document.getElementById("studentTableBody");

const renderTable = (dataArray) => {
    studentTableBody.innerHTML = "";

    if (!Array.isArray(dataArray) || dataArray.length === 0) {
        studentTableBody.innerHTML = "<tr><td colspan='4' style='text-align:center;'>No student records found.</td></tr>";
        return;
    }

    dataArray.forEach((student, index) => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${index + 1}</td>
            <td>${student.firstName || ""}</td>
            <td>${student.lastName || ""}</td>
            <td>${student.email || ""}</td>
        `;
        studentTableBody.appendChild(row);
    });
}

studentForm.addEventListener('submit', (event) => {
    event.preventDefault();

    // Get values from DOM
    const firstName = document.getElementById("firstName").value.trim();
    const lastName = document.getElementById("lastName").value.trim();
    const email = document.getElementById("email").value.trim();

    // Validate all fields are filled
    if (!firstName || !lastName || !email) {
        alert("All input fields must be filled out.");
        return;
    }

    // Create student data object
    const studentData = {
        firstName: firstName,
        lastName: lastName,
        email: email
    };

    // Pass data to API function
    addStudentAPI(studentData);

    // Refresh table
    renderTable(localStudents);

    // Reset form fields
    studentForm.reset();
});

const initApp = () => {
    localStudents = getStudentsAPI();
    renderTable(localStudents);
};

// Display without populated database
renderTable(localStudents);
// Display the initial populated database
initApp(); */

const API_URL = "http://localhost:8080/api/students";
const API_AUTH = "http://localhost:8080/api/auth";

async function createStudent(studentData) {
    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(studentData),
            credentials: "include" // Include cookies for session management
        });

        /* if (!response.ok) {
            alert("Failed to create student");
        }
        else {
            alert("Student created successfully");
            resetForm();
        } */

        if(response.ok) {
            alert("Student created successfully");
            resetForm();
            getAllStudents(); // Refresh the student list
        }
        else {
            alert("Failed to create student");
        }

    } catch (error) {
        console.error("Error creating student:", error);
    }
}

document.getElementById("studentForm").addEventListener("submit", function(event) {
    event.preventDefault();

    if(!validateStudentForm()) {
        return; // Stop submission if validation fails
    }

    const id = document.getElementById("studentId").value;
    const studentData = {
        firstName: document.getElementById("firstName").value.trim(),
        lastName: document.getElementById("lastName").value.trim(),
        email: document.getElementById("email").value.trim(),
    }

    //createStudent(studentData);

    if (id) {
        updateStudent(id, studentData);
    }
    else {
        createStudent(studentData);
    }
});

function resetForm()
{
    document.getElementById("studentId").value = "";
    document.getElementById("studentForm").reset();

    const submitButton = document.getElementById("submitBtn");
    submitButton.textContext = "Add Student";
    submitButton.style.backgroundColor = "#4CAF50"; // Reset to original color
}

async function getAllStudents()
{
    try {
        const response = await fetch(API_URL, {
            credentials: "include" // Include cookies for session management
        });
        if (!response.ok) {
            throw new Error("Failed to fetch students. Network does not respond.");
        }

        const students = await response.json();
        const studentTableBody = document.getElementById("studentTableBody");
        studentTableBody.innerHTML = ""; // Clear existing rows

        students.forEach((student, index) => {
            const row = `
                <tr>
                    <td>${student.id}</td>
                    <td>${student.firstName}</td>
                    <td>${student.lastName}</td>
                    <td>${student.email}</td>
                    <td>
                        <button class = "btn-edit" onclick="initializeStudentFormById(${student.id})">Edit</button>
                        <button class = "btn-delete" onclick="deleteStudent(${student.id})">Delete</button>
                    </td>
                </tr>
            `;
            studentTableBody.innerHTML += row;
        });
    } catch (error) {
        console.error("Error fetching students:", error);
        alert("Error fetching students. Please check the console for more details.");
    }
}

document.addEventListener("DOMContentLoaded", getAllStudents); // Fetch and display students when the page loads

async function initializeStudentFormById(studentId)
{
    try {
        const response = await fetch(`${API_URL}/${studentId}`, {
            credentials: "include" // Include cookies for session management
        });

        if (!response.ok) {
            throw new Error("Failed to fetch student data.");
        }

        const student = await response.json();
        document.getElementById("studentId").value = student.id;
        document.getElementById("firstName").value = student.firstName;
        document.getElementById("lastName").value = student.lastName;
        document.getElementById("email").value = student.email;
        
        const submitButton = document.getElementById("submitBtn");
        submitButton.textContent = "Update";
        submitButton.style.backgroundColor = "#008CBA"; // Change color to indicate update mode

        clearValidationStyles(); // Clear any previous validation styles
    } catch (error) {
        console.error("Error fetching student data:", error);
        //alert("Error fetching student data. Please check the console for more details.");
    }
}

async function updateStudent(studentId, studentData)
{
    try {
        const response = await fetch(`${API_URL}/${studentId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(studentData),
            credentials: "include" // Include cookies for session management
        });

        if (response.ok) {
            alert("Student updated successfully");
            resetForm();
            getAllStudents(); // Refresh the student list
        }
        else {
            alert("Failed to update student");
        }
    } catch (error) {
        console.error("Error updating student:", error);
    }
}

async function deleteStudent(studentId)
{
    if (confirm("Are you sure you want to delete this student?"))
    {
        try {
            const response = await fetch(`${API_URL}/${studentId}`, {
                method: "DELETE",
                credentials: "include"
            });

            if (response.ok)
            {
                const message = await response.text();
                alert(message);
                getAllStudents();
            }
            else
            {
                alert("Failed to delete student");
            }
        } catch (error) {
            console.error("Error deleting student:", error);
            //alert("Error deleting student. Please check the console for more details.");
        }
    }
}

async function loginUser(loginDto)
{
    try {
        const response = await fetch(`${API_AUTH}/auth_login_session`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(loginDto),
            credentials: "include" // Include cookies for session management
        });

        if (!response.ok) {
            alert("Login failed");
        }
        else {
            alert("Login successful");
            window.location.href = "index.html"; // Redirect to dashboard on successful login
        }
    } catch (error) {
        console.error("Error logging in:", error);
        //throw error;
    }
}

async function logoutUser()
{
    try {
        fetch(`${API_AUTH}/logout`, {
            method: "POST",
            credentials: "include" // Include cookies for session management
        });

        document.getElementById("studentTableBody").innerHTML = ""; // Clear the student table
        resetForm(); // Reset the form fields
        alert("Logout successful");

        try {
            await fetch(API_URL, {
                method: "GET",
                headers: { "Authorization": "Basic " + btoa("invalid_user:wrong_password") 
                } 
        });
        } catch (error) {
            console.log("Error logging out:", error);
            //throw error;
        }
        window.location.reload(); // Reload the page to reflect the logged-out state
    } catch (error) {
        alert("Error logging out:", error);
        //throw error;
    }
}