$(document).ready(function () {

    let navAdmin = $("#nav-admin");
    let navUser = $("#nav-user");
    let adminPanel = $("#admin-panel");
    let about = $("#about");

    adminPanel.show();
    navAdmin.click(() => {
        navUser.removeClass('active');
    navAdmin.addClass('active');
    about.hide();
    adminPanel.show();
});
    navUser.click(() => {
        navAdmin.removeClass('active');
    navUser.addClass('active');
    about.show();
    adminPanel.hide();
    $('#currentUserTable').show();
});

    $.ajax({
        url: '/rest/users',
        method: 'get',
        dataType: 'json',
        contentType: "application/json",

        success: function (result) {
            showUserTable(result);

        }
    })

    let navAllUsers = $("#navAllUsers");
    let navNewUser = $("#navNewUser");
    let allUsersPage = $("#allUsersPage");
    let newUserPage = $("#newUserPage");

    allUsersPage.show();
    navAllUsers.click(() => {
        navNewUser.removeClass('active');
    navAllUsers.addClass('active');
    newUserPage.hide();
    allUsersPage.show();
});

    navNewUser.click(() => {
        navAllUsers.removeClass('active');
    navNewUser.addClass('active');
    newUserPage.show();
    allUsersPage.hide();
    showroles($("#c_roles"));
});

    $("#createBtn").click(function () {

        $.ajax({
            url: '/rest/newUser',
            async: true,
            dataType: 'json',
            contentType: "application/json",
            type: "POST",
            data:
                JSON.stringify({
                    username: jQuery('#c_username').val(),
                    password: jQuery('#c_password').val(),
                    age: jQuery('#c_age').val(),
                    email: jQuery('#c_email').val(),
                    roles: jQuery('#c_roles').val()
                }),
            error: function(jqXHR) {
                console.log('jqXHR = ' + jqXHR);
                alert('Ошибка при создании пользователя - Internal server error: ' + jqXHR.status);
            },
            success: function (result) {
                showUserTable(result);
            }
        })
    });

    $("#editBtn").click(function () {
        $.ajax({
            url: '/rest/updateUser',
            async: true,
            contentType: "application/json",
            type: "PUT",
            data:
                JSON.stringify({
                    id: jQuery('#id').val(),
                    username: jQuery('#username').val(),
                    password: jQuery('#password').val(),
                    age: jQuery('#age').val(),
                    email: jQuery('#email').val(),
                    roles: jQuery('#roles').val()
                }),
            error: function(jqXHR) {
                alert('Ошибка при редактировании пользователя - Internal server error: ' + jqXHR.status);
            },
            success: function (result) {
                showUserTable(result);
            }
        })
        $('#userEditModal').modal('hide');
    });


});

function showroles(fieldid) {
    $.ajax({
        url: '/rest/roles/',
        method: 'get',
        dataType: 'json',
        contentType: "application/json",
        success: function (result) {
            $(fieldid).empty();
            $.each(result, function (roleKey, roleValue) {
                console.log(roleValue.name + ' ' + roleValue.id);
                let option = new Option(roleValue.name, roleValue.name);
                $(fieldid).append(option);
            });

        }
    })
}

function showUserTable(result) {

    $('#userTableBody').empty();
    $(result).each(function (key, value) {
        let user_data = '';
        user_data += '<tr>'
        user_data += '<td class="align-middle">' + value.id + '</td>'
        user_data += '<td class="align-middle">' + value.username + '</td>'
        user_data += '<td class="align-middle">' + value.age + '</td>'
        user_data += '<td class="align-middle">' + value.email + '</td>'
        user_data += '<td class="align-middle">' + '<table>' + '<tr>'
        $(value.roles).each(function (rKey, rValue) {
            user_data += ' <div>' + rValue.name + '</div>'
        })
        user_data += '</tr>' + '</table>' + '</td>'

        user_data += "<td class=\"align-middle\"><button id=\"buttonUserEdit" + value.id + "\" type=\"button\" class=\"btn btn-info btn-sm\" data-bs-toggle=\"modal\"\n" +
            "        data-bs-target=\"#userEditModal\" value=\"Edit user\">\n" +
            "    Edit\n" +
            "</button>&nbsp;&nbsp;&nbsp;"
        user_data += "<button id=\"buttonUserDelete" + value.id + "\" type=\"button\" class=\"btn btn-danger btn-sm\" data-bs-toggle=\"modal\" data-bs-target=\"#userDeleteModal\" \n" +
            "        value=\"Delete User\">\n" +
            "    Delete\n" +
            "</button></td>"
        user_data += '</tr>'

        $("#navAllUsers").addClass('active');
        $("#nav-create").removeClass('active');

        $("#newUserPage").hide();
        $("#allUsersPage").show();
        $('#userTableBody').append(user_data);

        $("#buttonUserEdit" + value.id).click(function () {

            $.ajax({
                url: '/rest/user/' + value.id,
                method: 'get',
                dataType: 'json',
                contentType: "application/json",
                success: function (result) {

                    $.each(result, function (k, v) {
                        $("input[name='" + k + "']", '#userEditForm').val(v);
                        console.log(k + " " + v);
                    });

                }
            })
            showroles($("#roles"));
        })

        $("#buttonUserDelete" + value.id).click(function () {

            $.ajax({
                url: '/rest/user/' + value.id,
                method: 'get',
                dataType: 'json',
                contentType: "application/json",
                success: function (result) {

                    $.each(result, function (k, v) {
                        $("input[name='" + k + "']", '#userDeleteForm').val(v);
                        console.log(k + " " + v);
                    });
                    showroles($("#d_roles"));
                }
            })

            $("#deleteBtn").click(function () {
                $.ajax({
                    url: "/rest/deleteUser/" + value.id,
                    async: true,
                    type: "DELETE",

                    success: function (result) {
                        showUserTable(result);
                    }
                })
                $('#userDeleteModal').modal('hide');
            });

        });

    })
}