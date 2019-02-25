var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'AduDevice', 'coolMethod', [arg0]);
};

exports.aduWrite = function (arg0, success, error) {
    exec(success, error, 'AduDevice', 'aduWrite', [arg0]);
};

exports.aduRead = function (arg0, success, error) {
    exec(success, error, 'AduDevice', 'aduRead', [arg0]);
};

exports.requestPermission = function (arg0, success, error) {
    exec(success, error, 'AduDevice', 'requestPermission', [arg0]);
};

exports.openAduDevice = function (arg0, success, error) {
    exec(success, error, 'AduDevice', 'requestPermission', [arg0]);
};
