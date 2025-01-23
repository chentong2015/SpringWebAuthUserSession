/**
 * 关于User 用户注册和登录相关操作
 */
function registerUser() {
    const id = document.getElementById("userId").value;
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const firstname = document.getElementById("firstname").value;
    const lastname = document.getElementById("lastname").value;

    fetch('http://localhost:8080/api/register', {
           method: 'POST',
           headers: {
              'Content-Type': 'application/json; charset=utf-8'
           },
           body: JSON.stringify({
              "id": id,
              "username": username,
              "password": password,
              "firstname": firstname,
              "lastname": lastname
           })
        })
        .then(response => response.json())
        .then(response => alert(JSON.stringify(response)))

    window.location.href="http://localhost:8080/login";
}

function loginUser() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch('http://localhost:8080/api/login?username=' + username + '&password=' + password,
        {
           method: 'POST',
        })
        .then(response => response.json())
        .then(response => alert(JSON.stringify(response)))
    
    // 根据用户的类型跳转到不同的User/Admin页面
    if (username == 'admin') {
        window.location.href="http://localhost:8080/admin";
    } else {
        window.location.href="http://localhost:8080/user";
    }
}

function logoutUser() {
    fetch('http://localhost:8080/api/logout',
        {
           method: 'POST',
        })
        .then(response => response.json())
        .then(response => alert(JSON.stringify(response)));

    window.location.href="http://localhost:8080/login";    
}


/**
 * 关于User Role相关的操作
 */
function showMeInfo() {
    fetch('http://localhost:8080/api/showme',
        {
           method: 'GET',
        })
        .then(response => response.json())
        .then(response => alert(JSON.stringify(response)));
}

function whoamiInfo() {
    fetch('http://localhost:8080/api/whoami',
        {
           method: 'GET',
        })
        .then(response => response.json())
        .then(response => alert(JSON.stringify(response)));
}


/**
 * 关于Admin Role相关的操作
 */
function getUserById() {
    const id = document.getElementById("userId").value;
    fetch('http://localhost:8080/api/user/' + id,
        {
           method: 'GET',
        })
        .then(response => response.json())
        .then(response => alert(JSON.stringify(response)));
}

function getAllUsers() {
    fetch('http://localhost:8080/api/user/all',
        {
           method: 'POST',
        })
        .then(response => response.json())
        .then(response => alert(JSON.stringify(response)))
}


/*
 * 关于Authentication相关的操作
 */
function updatePassword() {
    const oldPassword = document.getElementById("oldPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    fetch('http://localhost:8080/api/changePassword',
        {
           method: 'POST',
           headers: {
              'Content-Type': 'application/json; charset=utf-8'
           },
           body: JSON.stringify({"oldPassword": oldPassword, "newPassword": newPassword})
        })
        .then(response => response.json())
        .then(response => alert(JSON.stringify(response)))
}

function refreshSessioin() {
    fetch('http://localhost:8080/api/session/refresh',
        {
           method: 'GET',
        })
        .then(response => response.json())
        .then(response => alert(JSON.stringify(response)))
}