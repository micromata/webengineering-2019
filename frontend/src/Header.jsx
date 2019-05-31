import React from "react";
import {Link} from "react-router-dom";

export function Header(props) {
    return (
        <div className="header">
            <Link to='/'>
                <span className="logo">L</span><span className="title">Lecture News</span>
            </Link>
            <Link to='/post/new' className='header-link'>new</Link>
        </div>
    );
}
