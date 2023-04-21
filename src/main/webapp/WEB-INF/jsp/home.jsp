<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
    <!-- 引入 Bootstrap 的 CSS 文件 -->
    <link rel="stylesheet" href="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/4.6.0/css/bootstrap.min.css">
</head>
<body>
<div class="container-fluid h-100">
    <div class="row h-100">
        <div class="col-md-6 d-flex align-items-center justify-content-center" style="background: #1bbb99;" >
            <h1 class="text-white">Welcome Back!</h1>
        </div>
        <div class="col-md-6 d-flex align-items-center justify-content-center">
            <form class="login-form">
                <h2 class="text-center mb-4">Login</h2>
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" class="form-control" id="username" placeholder="Enter username">
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" class="form-control" id="password" placeholder="Enter password">
                </div>
                <button type="submit" class="btn btn-block btn-success"  style="background: #1bbb99;color:#fff;">Login</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
<style>
    body, html {
        height: 100%;
    }

    .login-form {
        max-width: 400px;
        width: 100%;
        padding: 40px;
        border: 1px solid #ddd;
        border-radius: 5px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }
    /*.btn-block{
        background: #1bbb99;
    }*/
</style>