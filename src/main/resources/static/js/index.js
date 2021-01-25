$(document).ready(function () {

    let navAdmin = $("#nav-admin");
    let navAbout = $("#nav-about");
    let adminPanel = $("#admin-panel");
    let about = $("#about");

    adminPanel.show();
    navAdmin.click(() => {
        navAbout.removeClass('active');
    navAdmin.addClass('active');
    about.hide();
    adminPanel.show();
});
    navAbout.click(() => {
        navAdmin.removeClass('active');
    navAbout.addClass('active');
    about.show();
    adminPanel.hide();
    $('#currentUserTable').show();
});

    $.ajax({
        url: '/api/users',
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
            url: '/api/users',
            async: true,
            dataType: 'json',
            contentType: "application/json",
            type: "POST",
            data:
                JSON.stringify({
                    firstname: jQuery('#c_firstname').val(),
                    lastname: jQuery('#c_lastname').val(),
                    age: jQuery('#c_age').val(),
                    email: jQuery('#c_email').val(),
                    password: jQuery('#c_password').val(),
                    username: jQuery('#c_username').val(),
                    roles: jQuery('#c_roles').val()
                }),
            success: function (result) {
                showUserTable(result);
            }
        })
    });

    $("#editBtn").click(function () {
        $.ajax({
            url: '/api/users',
            async: true,
            contentType: "application/json",
            type: "PUT",
            data:
                JSON.stringify({
                    id: jQuery('#id').val(),
                    firstname: jQuery('#firstname').val(),
                    lastname: jQuery('#lastname').val(),
                    age: jQuery('#age').val(),
                    email: jQuery('#email').val(),
                    password: jQuery('#password').val(),
                    username: jQuery('#username').val(),
                    roles: jQuery('#roles').val()
                }),
            success: function (result) {
                showUserTable(result);
            }
        })
        $('#userEditModal').modal('hide');
    });


});

function showroles(fieldid) {
    $.ajax({
        url: '/api/roles/',
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
        user_data += '<td class="align-middle">' + value.firstname + '</td>'
        user_data += '<td class="align-middle">' + value.lastname + '</td>'
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
            "</button></td>"
        user_data += "<td class=\"align-middle\"><button id=\"buttonUserDelete" + value.id + "\" type=\"button\" class=\"btn btn-danger btn-sm\" \n" +
            "        value=\"" + value.id + "\">\n" +
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
                url: '/api/users/' + value.id,
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
                url: "/api/users/" + value.id,
                async: true,
                type: "DELETE",

                success: function (result) {

                    showUserTable(result);
                }
            })

        });


    })


}