function addStudent() {
    let firstName = $('#firstName').val();
    let lastName = $('#lastName').val();
    let middleName = $('#middleName').val();
    let birthDate = $('#birthDate').val();
    let groupName = $('#groupName').val();

    if (!firstName || !lastName || !birthDate || !groupName) {
        alert('Please fill in all required fields.');
        return;
    }

    let studentData = {
        firstName: firstName,
        lastName: lastName,
        middleName: middleName,
        birthDate: birthDate,
        groupName: groupName,
    };

    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/students',
        contentType: 'application/json',
        data: JSON.stringify(studentData),
        success: function () {
            alert('Student added successfully!');
            $('#addStudentForm')[0].reset();
            getStudents();
        },
        error: function (error) {
            alert('Error adding student: ' + error.responseText);
        }
    });
}

function getStudents() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/students',
        success: function (students) {
            displayStudents(JSON.parse(students));
        },
        error: function (error) {
            alert('Error fetching students: ' + error.responseText);
        }
    });
}

function displayStudents(students) {
    let studentTable = $('<table>');

    let tableHeader = $('<tr>');
    tableHeader.append('<th>Name</th>');
    tableHeader.append('<th>Group</th>');
    tableHeader.append('<th>Unique Number</th>');
    tableHeader.append('<th>Action</th>');
    studentTable.append(tableHeader);

    if (students && Array.isArray(students)) {
        students.forEach(function (student) {
            let tableRow = $('<tr>');
            tableRow.append('<td>' + student.firstName + ' ' + student.lastName + '</td>');
            tableRow.append('<td>' + student.groupName + '</td>');
            tableRow.append('<td>' + student.id + '</td>');

            let deleteButton = $('<button>').text('Delete');
            deleteButton.addClass('warning');
            deleteButton.click(function () {
                deleteStudent(student.id);
            });

            let actionCell = $('<td>').append(deleteButton);
            tableRow.append(actionCell);

            studentTable.append(tableRow);
        });
    } else {
        console.error('Invalid or missing students data');
    }

    $('#studentList').empty().append(studentTable);
}


function deleteStudent(id) {
    $.ajax({
        type: 'DELETE',
        url: 'http://localhost:8080/students/' + id,
        success: function () {
            getStudents();
        },
        error: function (error) {
            console.error('Error deleting student: ' + error.responseText);
        }
    });
}


getStudents();