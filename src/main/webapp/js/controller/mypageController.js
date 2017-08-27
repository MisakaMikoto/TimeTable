/**
 * Created by MisakaMikoto on 2017-08-23.
 */
app.controller("mypageController", ['$scope', '$location', '$http', function ($scope, $location, $http) {
    $scope.subjects = [];
    $scope.sendSubjects = [];

    $scope.id = $location.search().id;

    $scope.insert = function () {
        let lastAllCredit = getLastAllCredit();

        let name = $scope.name;
        let credit = $scope.credit;
        let allCredit = parseInt(lastAllCredit) + parseInt($scope.credit);

        if(validateInputCredit(credit) && validateInputName(name) && validateCreditOverflow(allCredit)) {
            $scope.subjects.push(
                {"name": name, "credit": credit, "allCredit": allCredit}
            );

            $scope.sendSubjects.push(
                {"name": name, "credit": credit}
            );
        }
    }

    $scope.delete = function(index) {
        let subject = $scope.subjects[index];
        let targetCredit = $scope.subjects[index].credit;

        for(var i = 0; i < $scope.subjects.length; i++) {
            if(i > index) {
                $scope.subjects[i].allCredit = parseInt($scope.subjects[i].allCredit) - parseInt(targetCredit);
            }
        }
        let target = $scope.subjects.indexOf(subject);
        $scope.subjects.splice(target, 1);
        $scope.sendSubjects.splice(target, 1);
    }

    $scope.complete = function() {
        getSchedule();
    }

    function getLastAllCredit() {
        let lastAllCredit = 0;

        let table = angular.element(document).find('table')[0];
        let trs = table.querySelectorAll('tr');

        if(trs.length > 1) {
            let tds = trs[trs.length - 1].querySelectorAll('td');
            lastAllCredit = tds[tds.length - 2].attributes['value'].value;
        }
        return lastAllCredit;
    }

    function validateInputCredit(credit) {
        let isOK = true;
        if(typeof credit == 'undefined') {
            isOK = false;
            alert("신청 학점의 범위는 1 - 3 학점입니다.");
        }
        return isOK;
    }

    function validateInputName(name) {
        let isOK = true;
        for(var i = 0; i < $scope.subjects.length; i++) {
            if($scope.subjects[i].name == name) {
                isOK = false;
                alert("동일한 수강 과목은 등록할 수 없습니다.");
                break;
            }
        }
        return isOK;
    }

    function validateCreditOverflow(allCredit) {
        let isOK = true;

        if(allCredit > 21) {
            isOK = false;
            alert("수강 학점은 21학점을 넘을 수 없습니다.");
        }
        return isOK;
    }

    function validateComplete() {
        let isOK = true;
        let lastAllCredit = getLastAllCredit();

        if(lastAllCredit < 18) {
            isOK = false;
            alert("수강 학점은 최소 18 학점이어야 합니다.");
        }
        return isOK;
    }

    function getSchedule() {
        if(validateComplete()) {
            $http({
                method: 'POST',
                url: '/schedule/get',
                data: $.param({
                    userId: $scope.id
                }),
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                }
            }).then(function success(response) {
                if (response.data.length > 0) {
                    let confirm = window.confirm("저장된 데이터가 있습니다. 업데이트 하시겠습니까?");
                    if (confirm) {
                        complete(true);
                    }

                } else {
                    complete(false);
                }

            }, function fail(error) {
                console.log(error);
            });
        }
    }

    function complete(isUpdate) {
        var url = "";
        if(isUpdate) {
            url = '/schedule/updateTimeTable';

        } else {
            url = '/schedule/createTimeTable'
        }

        $http({
            method: 'POST',
            url: url,
            data: {
                subjects: angular.fromJson($scope.sendSubjects)
            },
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        }).then(function success(response) {
            if (response.data) {
                alert("저장 되었습니다.");
                $location.path("/timetable").search({id:$scope.id, data: response.data});
            }

        }, function fail(error) {
            console.log(error);
        });
    }
}]);