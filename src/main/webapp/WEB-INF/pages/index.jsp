<%--
  Created by IntelliJ IDEA.
  User: MisakaMikoto
  Date: 2017-08-23
  Time: 오후 3:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

  <!-- create angular app -->
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.0rc1/angular-route.min.js"></script>

  <!-- jQuery -->
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  <!-- bootstrap -->
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

  <!-- app -->
  <script type="text/javascript" src="/js/app/app.js"></script>

  <!-- route -->
  <script type="text/javascript" src="/js/controller/loginController.js"></script>
  <script type="text/javascript" src="/js/controller/mypageController.js"></script>
  <script type="text/javascript" src="/js/controller/timeTableController.js"></script>

</head>

<body>
  <div ng-app="app">
    <a href="/"></a>
    <ng-view></ng-view>
  </div>
</body>

</html>
