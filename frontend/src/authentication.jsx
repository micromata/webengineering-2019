import React from "react";
import jwt_decode from 'jwt-decode';
import backend from './configuration';

export class Authentication {
    constructor() {
        this.listener = [];
        this.loadStoredToken();

        this.logout = this.logout.bind(this);
    }

    loadStoredToken() {
        let token = this.getCookie("token");
        window.jwt = token;
        if (token !== "") {
            const decoded = jwt_decode(token);
            window.token = decoded;
        }
    }

    isAuthenticated() {
        return window.token != undefined;
    }

    getToken() {
        return window.jwt;
    }

    getUser() {
        return window.token;
    }

    authenticate(token) {
        this.setCookie("token", token, 90);
        const decoded = jwt_decode(token);
        window.token = decoded;
        window.jwt = token;
        // TODO ML Rename this function
        this.listener.forEach(l => l.authenticated());
    }

    addAuthenticationListener(l) {
        this.listener.push(l);
    }

    logout() {
        window.token = null;
        this.eraseCookie("token");
        this.listener.forEach(l => l.authenticated());

        // We are logged out, go back to the main page.
        document.location.href = "/";
    }

    setCookie(cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    }

    getCookie(cname) {
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

    eraseCookie(name) {
        document.cookie = name + '=; Max-Age=-99999999;';
    }

    getAuthenticationURL(callback) {
        fetch(backend + '/api/authentication/url')
            .then((response) => {
                return response.json()
            })
            .then((data) => {
                callback(data.url);
            })
    }
}

const instance = new Authentication();
Object.freeze(instance);

export default instance;