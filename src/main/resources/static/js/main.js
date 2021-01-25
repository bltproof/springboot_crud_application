let $userModal = $('#user_modal');

$( document ).ready(function() {
    loadUsers();
});

$userModal.on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var user_id = button.data('id');
    var user_action = button.data('action');
    const user = setUserModalForm(user_id);
    let $modalButtonAction = $('#modal_button_action');
    if (user_action.indexOf('edit') > -1) {
        $modalButtonAction.text('Edit');
        $modalButtonAction.removeClass('btn-danger');
        $modalButtonAction.addClass('btn btn-primary');
        $modalButtonAction.removeAttr('onclick');
        $modalButtonAction.attr('onClick', 'sendEditUser()');
        $('#userForm :input').prop('disabled', false);
    } else {
        $modalButtonAction.text('Delete');
        $modalButtonAction.removeClass('btn-primary');
        $modalButtonAction.addClass('btn btn-danger');
        $modalButtonAction.removeAttr('onclick');
        $modalButtonAction.attr('onClick', 'sendDeleteUser()');
        $('#userForm :input').prop('disabled', true);
    }
});

async function sendDeleteUser() {
    let id = $('#modal_id').val();
    await fetch('/api/users/' + id);
    $userModal.modal('hide');
    loadUsers();
}

async function sendEditUser() {
    var formInput = $("#userForm").serializeArray();
    var data = {};
    var roles = [];

    $(formInput).each(function (index, obj) {
        if (obj.name === 'roles') {
            roles.push(obj.value)
            data['roles'] = roles;
        } else {
            data[obj.name] = obj.value;
        }
    });

    let userInput = JSON.stringify(data);
    let response = await fetch('/api/users', {
        method: 'PUT',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json'
        },
        body: userInput
    });
    $userModal.modal('hide');
    loadUsers();
}

async function sendNewUser() {
    var formInput = $("#newUser :input").serializeArray();
    var data = {};
    var roles = [];

    $(formInput).each(function (index, obj) {
        if (obj.name === 'roles') {
            roles.push(obj.value);
            data['roles'] = roles;
        } else {
            data[obj.name] = obj.value;
        }
    });

    let userInput = JSON.stringify(data);
    let response = await fetch('/api/users', {
        method: 'POST',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json'
        },
        body: userInput
    });
    $('#adminTabs a[href="#allUsers"]').tab('show')
    clearUserForm()
}

async function loadUsers() {
    $('#userTable').empty();
    const response = await fetch('api/users');
    const result = await response.json();
    result.forEach(user => {
        let userRow = `$(<tr>
                        <th scope="row">${user.id}</th>
                        <td>${user.username}</td>
                        <td>${user.age}</td>
                        <td>${user.email}</td>
                        <td>
                            <ul>
                                <li>${user.roles}</li>
                            </ul>
                        </td>
                        <td>
                            <button class="btn btn-info btn-sm" data-toggle="modal" data-id="${user.id}" data-target="#user_modal" data-action="editUser">Edit</button>
                        </td>
                        <td>
                            <button class="btn btn-danger btn-sm" data-toggle="modal" data-id="${user.id}" data-target="#user_modal" data-action="deleteUser">Delete</button>
                        </td>
                    </tr>)`;
        $('#userTable').append(userRow);
    });
}

async function setUserModalForm(id) {
    fetch('api/users/' + id)
        .then(function (response) {
            return response.json();
        }).then(function (user) {
        $('#modal_id').val(user.id);
        $('#modal_username').val(user.username);
        $('#modal_age').val(user.age);
        $('#modal_email').val(user.email);
        for (var role of user.roles) {
            $('#role_' + role.id).prop('selected', true);
        }
    });
}

$('.nav-tabs a[href="#allUsers"]').on('shown.bs.tab', function(event){
    loadUsers();
});

function clearUserForm() {
    let $newUser = $('#newUser');
    $newUser.find("input").val('');
    $newUser.find("option:selected").prop('selected', false);
}
