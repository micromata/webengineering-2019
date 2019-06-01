import React from "react";

export function isAuthenticated() {
    return window.token != undefined;
}

export function authenticate(token) {
    window.token = token;
    listener.forEach(l => l.authenticated());
}

let listener = [];

export function addAuthenticationListener(l) {
    listener.push(l);
}