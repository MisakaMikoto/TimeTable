/**
 * Created by MisakaMikoto on 2017-08-23.
 */
var app = angular.module('app', ['ngRoute']).
    config(function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        });

        $routeProvider
            .when("/", {templateUrl: "/component/login.html"})
            .when("/mypage", {templateUrl: "/component/mypage.html"})
            .when("/timetable", {templateUrl: "/component/timetable.html"})
    })