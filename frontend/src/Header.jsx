import React from "react";
import {Link} from "react-router-dom";
import {addAuthenticationListener, isAuthenticated, logout} from "./authentication";

export class Header extends React.Component {
    constructor(props) {
        super(props);
        this.authenticated = this.authenticated.bind(this);
        addAuthenticationListener(this);
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
                    isAuthenticated() &&
                    <Link to='/post/new' className='header-link'>new</Link>
                }
                {
                    isAuthenticated() &&
                    <a className='header-link' onClick={logout}>logout</a>
                }
                {
                    !isAuthenticated() &&
                    <a href="https://github.com/login/oauth/authorize?response_type=code&client_id=ca9d6341a3ab314ccba4"
                       className='header-link'>login</a>
                }
            </div>
        )
    }
}
