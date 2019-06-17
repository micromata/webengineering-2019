import React from "react";
import {Link} from "react-router-dom";
import Authentication from "./Authentication";

export class Header extends React.Component {
    constructor(props) {
        super(props);
        this.authenticated = this.authenticated.bind(this);
        Authentication.addAuthenticationListener(this);

        this.state = {url: ''}
    }

    componentDidMount() {
        // Retrieve URL for GithHub authentication from the backend.
        Authentication.getAuthenticationURL(url => {
            this.setState({url});
        });
    }

    authenticated() {
        this.forceUpdate();
    }

    render() {
        return (
            <div className="header">
                <Link to='/'>
                    <span className="logo">L</span><span className="title">Lecture News</span>
                </Link>
                {
                    Authentication.isAuthenticated() &&
                    <Link to='/post/new' className='header-link'>new</Link>
                }
                {
                    Authentication.isAuthenticated() &&
                    <a className='header-link user-info' onClick={Authentication.logout}>logout</a>
                }
                {
                    !Authentication.isAuthenticated() &&
                    <a href={this.state.url}
                       className='header-link'>login</a>
                }
                {
                    Authentication.isAuthenticated() &&
                    <span className='user-info'>{Authentication.getUser().sub}</span>
                }
            </div>
        )
    }
}
