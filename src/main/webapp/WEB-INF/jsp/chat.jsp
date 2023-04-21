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
        .chat-box {
            height: 400px;
            overflow-y: scroll;
        }

        .chat-message {
            display: flex;
            margin-bottom: 10px;
        }

        .chat-avatar {
            width: 50px;
            height: 50px;
            margin-right: 10px;
        }

        .chat-avatar img {
            width: 100%;
            height: 100%;
            border-radius: 50%;
        }

        .chat-text {
            flex: 1;
        }

        .chat-text p {
            margin: 0;
            padding: 5px 10px;
            background-color: #f2f2f2;
            border-radius: 10px;
        }

        .card-footer form {
            margin-top: 10px;
        }

        .card-footer form .input-group {
            width: 100%;
        }

        .card-footer form .input-group input {
            border-radius: 20px;
        }

        .card-footer form .input-group-append button {
            border-radius: 20px;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-3">
            <div class="card">
                <div class="card-header">在线用户</div>
                <div class="card-body">
                    <ul class="list-group">
                        <li class="list-group-item">用户1</li>
                        <li class="list-group-item">用户2</li>
                        <li class="list-group-item">用户3</li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="col-md-9">
            <div class="card">
                <div class="card-header">聊天室</div>
                <div class="card-body">
                    <div class="chat-box">
                        <div class="chat-message">
                            <div class="chat-avatar">
                                <img src="avatar1.jpg" alt="Avatar">
                            </div>
                            <div class="chat-text">
                                <p>用户1: 你好啊！</p>
                            </div>
                        </div>
                        <div class="chat-message">
                            <div class="chat-avatar">
                                <img src="avatar2.jpg" alt="Avatar">
                            </div>
                            <div class="chat-text">
                                <p>用户2: 你好，有什么需要帮助的吗？</p>
                            </div>
                        </div>
                        <div class="chat-message">
                            <div class="chat-avatar">
                                <img src="avatar1.jpg" alt="Avatar">
                            </div>
                            <div class="chat-text">
                                <p>用户1: 我想问一下如何使用Bootstrap4。</p>
                            </div>
                        </div>
                        <div class="chat-message">
                            <div class="chat-avatar">
                                <img src="avatar2.jpg" alt="Avatar">
                            </div>
                            <div class="chat-text">
                                <p>用户2: Bootstrap4的使用方法可以参考官方文档，或者在网上搜索相关教程。</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card-footer">
                    <form>
                        <div class="input-group">
                            <input type="text" class="form-control" placeholder="输入消息...">
                            <div class="input-group-append">
                                <button class="btn btn-primary" type="submit">发送</button>
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
