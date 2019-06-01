import React from "react";

export class AuthenticationCallback extends React.Component {
    constructor(props) {
        super(props);

        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get('code');
        console.log(code);
    }

    render() {
        return <pre>{JSON.stringify(this.props)}</pre>;
    }
}
