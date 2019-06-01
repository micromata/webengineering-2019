import React from "react";

export default function isAuthenticated() {
    return window.token != undefined;
}

export function authenticate(token) {
    window.token = token;
}