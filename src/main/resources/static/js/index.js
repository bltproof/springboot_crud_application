

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
                var option = new Option(roleValue.name, roleValue.name);
                $(fieldid).append(option);
            });

        }
    })
}

$(document).ready(function () {

    $("#create-submit").click(function () {

        $.ajax({
            url: '/api/users',
            async: true,
            dataType: 'json',
            contentType: "application/json",
            type: "POST",
            data:
                JSON.stringify({
                    //id: jQuery('#id').val(),
                    username: jQuery('#c_username').val(),
                    password: jQuery('#c_password').val(),
                    roles: jQuery('#c_roles').val()

                }),
            success: function (result) {

                showUserTable(result);

            }
        })
    });

    let navAdmin = $("#nav-admin");
    let navUser = $("#nav-user");
    let pageAdmin = $("#page-admin");
    let pageUser = $("#page-user");

    pageAdmin.show();
    navAdmin.click(() => {
        navUser.removeClass('active');
        navAdmin.addClass('active');
        pageUser.hide();
        pageAdmin.show();
    });
    navUser.click(() => {
        navAdmin.removeClass('active');
        navUser.addClass('active');
        pageUser.show();
        pageAdmin.hide();
        $('#currentUserTable').show();
        $('#randomUserTable').hide();
    });

    let navList = $("#nav-list");
    let navCreate = $("#nav-create");
    let pageList = $("#page-list");
    let pageCreate = $("#page-create");

    pageList.show();
    $("#caption-admin").show();
    navList.click(() => {
        navCreate.removeClass('active');
        navList.addClass('active');
        pageCreate.hide();
        pageList.show();
        $("#caption-admin").show();
        $("#caption-create").hide();
    });
    navCreate.click(() => {
        navList.removeClass('active');
        navCreate.addClass('active');
        pageCreate.show();
        pageList.hide();
        $("#caption-admin").hide();
        $("#caption-create").show();
        showroles($("#c_roles"));



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


    $("#ed_submit").click(function () {

        $.ajax({
            url: '/api/users',
            async: true,
            // dataType: 'json',
            contentType: "application/json",
            type: "PUT",
            data:
                JSON.stringify({
                    id: jQuery('#id').val(),
                    username: jQuery('#username').val(),
                    password: jQuery('#password').val(),
                    roles: jQuery('#roles').val()

                }),
            success: function (result) {


                showUserTable(result);


            }
        })

        $('#userEditModal').modal('hide');
    });







});

function showUserTable(result) {

    $('#userTableBody').empty();
    $(result).each(function (key, value) {
        var user_data = '';
        user_data += '<tr>'
        user_data += '<td>' + value.id + '</td>'
        user_data += "<td><a href=\"#\" id=\"showUser" + value.id + "\" text=\" " + value.username + "\" >" + value.username + "</a></td>"
        user_data += '<td>' + value.password + '</td>'
        user_data += '<td>' + '<table>'
        $(value.roles).each(function (rKey, rValue) {
            user_data += '<tr>'
            user_data += '<td>' + rValue.name + '</td>'
            user_data += '</tr>'
        })
        user_data += '</table>' + '</td>'

        user_data += "<td><button id=\"buttonUserEdit" + value.id + "\" type=\"button\" class=\"btn btn-primary btn-sm\" data-bs-toggle=\"modal\"\n" +
            "        data-bs-target=\"#userEditModal\" value=\"Edit user\">\n" +
            "    Edit\n" +
            "</button></td>"
        user_data += "<td><button id=\"buttonUserDelete" + value.id + "\" type=\"button\" class=\"btn btn-danger btn-sm\" \n" +
            "        value=\"" + value.id + "\">\n" +
            "    Delete\n" +
            "</button></td>"


        user_data += '</tr>'

        $("#nav-list").addClass('active');
        $("#nav-create").removeClass('active');

        $("#page-create").hide();
        $("#page-list").show();
        $("#caption-admin").show();
        $("#caption-create").hide();
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

        $("#showUser" + value.id).click(function () {
            $.ajax({
                url: '/api/users/' + value.id,
                method: 'get',
                dataType: 'json',
                contentType: "application/json",
                success: function (result) {

                    var user_data = '';
                    user_data += '<tr>'
                    user_data += '<td>' + result.id + '</td>'
                    user_data += "<td><a id=\"showUser" + result.id + "\" text=\" " + result.username + "\" >" + result.username + "</a></td>"
                    user_data += '<td>' + result.password + '</td>'
                    user_data += '<td>' + '<table>'
                    $(result.roles).each(function (ru_rKey, ru_rValue) {
                        user_data += '<tr>'
                        user_data += '<td>' + ru_rValue.name + '</td>'
                        user_data += '</tr>'
                    })
                    user_data += '</table>' + '</td>'
                    user_data += '</tr>'
                    $('#randomUserTableBody').empty();
                    $('#randomUserTableBody').append(user_data);
                    $("#nav-admin").removeClass('active');
                    $("#nav-user").addClass('active');;
                    $("#page-admin").hide();
                    $("#page-user").show();
                    $('#currentUserTable').hide();
                    $('#randomUserTable').show();


                }
            })
        });

    })


}



