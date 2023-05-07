<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Chat Window</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous">
    <!-- Custom CSS -->
    <style>
        body {
            background-color: #f8f9fa;
        }

        .chat-window {
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            height: 500px;
            overflow-y: scroll;
        }

        .chat-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: #1bbb99;
            color: #f1f3f5;
            padding: 10px 20px;
            border-top-left-radius: 5px;
            border-top-right-radius: 5px;
        }

        .chat-header-left {
            display: flex;
            align-items: center;
        }

        .chat-header-left i {
            font-size: 24px;
            margin-right: 10px;
        }

        .chat-header-left span {
            font-size: 18px;
            font-weight: bold;
        }

        .chat-header-right {
            display: flex;
            align-items: center;
        }

        .chat-header-right i {
            font-size: 24px;
            margin-right: 10px;
        }

        .chat-header-right span {
            font-size: 18px;
            font-weight: bold;
        }

        .chat-body {
            padding: 20px;
        }

        .chat-message {
            display: flex;
            margin-bottom: 20px;
        }

        .chat-message-avatar {
            margin-right: 10px;
        }

        .chat-message-avatar img {
            width: 50px;
            height: 50px;
            border-radius: 50%;
        }

        .chat-message-content {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            width: 100%;
        }

        .chat-message-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .chat-message-author {
            font-weight: bold;
        }

        .chat-message-time {
            font-size: 12px;
        }

        .chat-message-text {
            background-color: #f8f9fa;
            border-radius: 5px;
            padding: 10px;
        }

        .chat-footer {
            background-color: #f8f9fa;
            padding: 20px;
            border-bottom-left-radius: 5px;
            border-bottom-right-radius: 5px;
        }

        .chat-footer form {
            display: flex;
            align-items: center;
        }

        .chat-footer form .input-group {
            width: 100%;
        }

        .chat-footer form input {
            border: none;
            border-radius: 5px;
            box-shadow: none;
            padding: 10px;
        }

        .chat-footer form button {
            border: none;
            border-radius: 5px;
            box-shadow: none;
            padding: 10px;
        }
        .avatar {
            width: 45px;
            height: 45px;
            margin-right: 40px;
            border-radius: 4px;
            background: #1bbb99;
            /*background: rgb(117,169,156);*/
            line-height: 45px;
            text-align: center;
            color: #f1f3f5;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <div class="row">
        <div class="col-md-8 offset-md-2">
            <div class="chat-window">
                <div class="chat-header">
                    <div class="chat-header-left">
                        <i class="fas fa-comments"></i>
                        <span>聊天室</span>
                    </div>
                    <div class="chat-header-right">
                        <i class="fas fa-users"></i>
                        <span>在线用户：10</span>
                    </div>
                </div>
                <div class="chat-body">
                    <div class="chat-message">
                        <div class="chat-message-content">
                            <div class="chat-message-header">
                                <span class="avatar">John Doe</span>
                                <span class="chat-message-time">10:30 AM</span>
                            </div>
                            <div class="chat-message-text">
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, urna quis aliquet bibendum, odio urna bibendum mauris, vel bibendum enim risus vel nunc.
                            </div>
                        </div>
                    </div>
                    <div class="chat-message">
                        <div class="chat-message-content">
                            <div class="chat-message-header">
                                <span class="avatar">Jane Smith</span>
                                <span class="chat-message-time">10:35 AM</span>
                            </div>
                            <div class="chat-message-text">
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, urna quis aliquet bibendum, odio urna bibendum mauris, vel bibendum enim risus vel nunc.
                            </div>
                        </div>
                    </div>
                    <div class="chat-message">
                        <div class="chat-message-content">
                            <div class="chat-message-header">
                                <span class="avatar">John Doe</span>
                                <span class="chat-message-time">10:40 AM</span>
                            </div>
                            <div class="chat-message-text">
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, urna quis aliquet bibendum, odio urna bibendum mauris, vel bibendum enim risus vel nunc.
                            </div>
                        </div>
                    </div>
                </div>
                <div class="chat-footer">
                    <form>
                        <div class="input-group">
                            <input type="text" class="form-control" placeholder="请输入消息...">
                            <div class="input-group-append">
                                <button class="btn btn-primary" type="submit"><i class="fas fa-paper-plane"></i></button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
