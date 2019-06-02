// TODO ML Create class for this set of methods for easier import and more structure.
// TODO ML Create singleton for this class using e.g. http://www.adam-bien.com/roller/abien/entry/singleton_pattern_in_es6_and
// TODO ML Refactor this file!
import React from "react";
import jwt_decode from 'jwt-decode';
import backend from './configuration';


export function loadStoredToken() {
    let token = getCookie("token");
    window.jwt = token;
    if (token !== "") {
        const decoded = jwt_decode(token);
        window.token = decoded;
    }
}

export function isAuthenticated() {
    return window.token != undefined;
}

export function getToken() {
    return window.jwt;
}

export function getUser() {
    return window.token;
}

export function authenticate(token) {
    setCookie("token", token, 90);
    const decoded = jwt_decode(token);
    window.token = decoded;
    window.jwt = token;
    // TODO ML Rename this function
    listener.forEach(l => l.authenticated());
}

let listener = [];

export function addAuthenticationListener(l) {
    listener.push(l);
}

export function logout() {
    window.token = null;
    eraseCookie("token");
    listener.forEach(l => l.authenticated());

    // We are logged out, go back to the main page.
    document.location.href = "/";
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function eraseCookie(name) {
    document.cookie = name + '=; Max-Age=-99999999;';
}

export function getAuthenticationURL(callback) {
    fetch(backend + '/api/authentication/url')
        .then((response) => {
            return response.json()
        })
        .then((data) => {
            callback(data.url);
        })
}