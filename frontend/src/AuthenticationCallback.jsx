import React from "react";

import backend from './configuration';
import {authenticate} from "./authentication";

export class AuthenticationCallback extends React.Component {
    constructor(props) {
        super(props);

        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get('code');
        this.state = {
            code
        }
    }

    componentDidMount() {
        // Call backend using submitted code.
        fetch(backend + '/api/authentication/callback?code=' + this.state.code)
            .then((response) => {
                return response.json()
            })
            .then((data) => {
                authenticate(data.token);
                this.props.history.push('/');
            })
    }

    render() {
        return <pre>{JSON.stringify(this.props)}</pre>;
    }
}
