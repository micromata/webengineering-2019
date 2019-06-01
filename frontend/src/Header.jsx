import React from "react";
import {Link} from "react-router-dom";
import isAuthenticated from './authentication';

export function Header(props) {
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
                !isAuthenticated() &&
                <a href="https://github.com/login/oauth/authorize?response_type=code&client_id=ca9d6341a3ab314ccba4"
                   class='header-link'>login</a>
            }
        </div>
    );
}
