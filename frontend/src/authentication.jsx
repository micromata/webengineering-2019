import React from "react";
import jwt_decode from 'jwt-decode';
import backend from './configuration';

export class Authentication {
    constructor() {
        this.listener = [];
        this.token = null;
        this.jwt = null;

        this.logout = this.logout.bind(this);
        this.loadStoredToken();
    }

    loadStoredToken() {
        let token = this.getCookie("token");
        this.jwt = token;
        if (token !== "") {
            const decoded = jwt_decode(token);
            this.token = decoded;
        }
    }

    isAuthenticated() {
        return this.token != undefined;
    }

    getToken() {
        return this.jwt;
    }

    getUser() {
        return this.token;
    }

    authenticate(token) {
        this.setCookie("token", token, 90);
        const decoded = jwt_decode(token);
        this.token = decoded;
        this.jwt = token;
        // TODO ML Rename this function
        this.listener.forEach(l => l.authenticated());
    }

    addAuthenticationListener(l) {
        this.listener.push(l);
    }

    logout() {
        this.token = null;
        this.eraseCookie("token");
        this.listener.forEach(l => l.authenticated());

        // We are logged out, go back to the main page.
        document.location.href = "/";
    }

    setCookie(cname, cvalue, exdays) {
        let d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        let expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    }

    getCookie(cname) {
        let name = cname + "=";
        let decodedCookie = decodeURIComponent(document.cookie);
        let ca = decodedCookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
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
export default instance;