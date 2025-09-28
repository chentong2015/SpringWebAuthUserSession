// TODO. 前端不能设置HttpOnly的属性
// document.cookie = `AUTH-TOKEN=${data.access_token}; path=/; max-age=${data.expires_in}`;
function handleLoginClick(e) {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    fetch('http://localhost:8080/api/login?username=' + encodeURIComponent(username) + '&password=' + encodeURIComponent(password), {
       method: 'POST',
    }).then(response => {
       return response.json();
    }).then(data => {
       console.log(data);
       // 根据返回的LoginState状态进行跳转
       if (data.isForAdmin) {
          window.location.href="admin.html";
       } else {
          window.location.href="user.html";
       }
    }).catch(error => {
       alert("Login user failed", error);
    });
}

// 处理注册的方法: 如果注册成功这调整到login页面
function handleRegistration(e) {
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
         "username": username,
         "password": password,
         "firstname": firstname,
         "lastname": lastname
      })
   }).then(response => {
       return response.json();
   }).then(data => {
       console.log(data);
       // 直接在前端执行重定向操作
       window.location.href="login.html";
   }).catch(error => {
       alert("Register user failed");
   });
}

// 处理用户更改密码的请求
function handleChangePassword(e) {
    const oldPassword = document.getElementById("oldPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    fetch('http://localhost:8080/api/changePassword', {
       method: 'POST',
       credentials: 'include',
       headers: {
          'Content-Type': 'application/json; charset=utf-8'
       },
       body: JSON.stringify({
          "oldPassword": oldPassword,
          "newPassword": newPassword
       })
    }).then(response => {
        return response.json();
    }).then(data => {
        console.log(data);
    }).catch(error => {
        alert("Change password request failed");
    });
}

// TODO. 处理一些公共的请求逻辑(获取用户，管理用户)
// 即使跨域请求也要携带cookies、HTTP 认证信息(如Authorization header)等凭证
function makeGetRequest(url, operation) {
    return fetch(url, {
        method: 'GET',
        credentials: 'include'
    }).then(response => {
        return response.json();
    }).then(data => {
        console.log(operation, data);
    }).catch(error => {
        alert(operation + " request failed");
    });
}

// 处理关于Cookie中token的刷新(在有效期之内)
function handleRefreshSession(e) {
    fetch('http://localhost:8080/api/session/refresh', {
       method: 'GET',
       credentials: 'include'
    }).then(response => {
        return response.json();
    }).then(data => {
        console.log(data);
    }).catch(error => {
        alert("Refresh token failed");
    });
}

// 提取公共的logout共享方法
function handleLogoutClick(e) {
    fetch('http://localhost:8080/api/logout', {
        method: 'POST',
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        console.log(data);
        // 登出后退回到登录页面
        window.location.href="login.html";
    }).catch(error => {
        alert("Logout failed, please login first", error);
    });
}