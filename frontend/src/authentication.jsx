import React from "react";

export function isAuthenticated() {
    return window.token != undefined;
}

export function authenticate(token) {
    window.token = token;
    // TODO ML Rename this function
    listener.forEach(l => l.authenticated());
}

let listener = [];

export function addAuthenticationListener(l) {
    listener.push(l);
}

export function logout() {
    window.token = null;
    listener.forEach(l => l.authenticated());
}