import React from "react";
import {Link} from "react-router-dom";
import {addAuthenticationListener, getAuthenticationURL, getUser, isAuthenticated, logout} from "./authentication";

export class Header extends React.Component {
    constructor(props) {
        super(props);
        this.authenticated = this.authenticated.bind(this);
        addAuthenticationListener(this);

        this.state = {url: ''}
    }

    componentDidMount() {
        // Retrieve URL for GithHub authentication from the backend.
        getAuthenticationURL(url => {
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
                    isAuthenticated() &&
                    <Link to='/post/new' className='header-link'>new</Link>
                }
                {
                    isAuthenticated() &&
                    <a className='header-link user-info' onClick={logout}>logout</a>
                }
                {
                    !isAuthenticated() &&
                    <a href={this.state.url}
                       className='header-link'>login</a>
                }
                {
                    isAuthenticated() &&
                    <span className='user-info'>{getUser().sub}</span>
                }
            </div>
        )
    }
}
