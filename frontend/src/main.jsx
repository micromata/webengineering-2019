import ReactDOM from 'react-dom'
import React from "react";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";

import {Header, PostDetail, PostNew} from "./components";
import {PostList} from "./PostList";

// The environment variable BACKEND will be used, if defined.
const backend = process.env.BACKEND || "http://localhost:8080";

/**
 * Here we are going to define our routing from paths to shown components.
 */
const routing = (
    <Router>
        <div>
            <Header/>
            {/*See https://reacttraining.com/react-router/web/api/Switch*/}
            <Switch>
                <Route exact path="/" component={PostList}/>
                <Route exact path="/post/new" component={PostNew}/>
                <Route path="/post/:id" component={PostDetail}/>
            </Switch>
        </div>
    </Router>
);

// See e.g. https://reactjs.org/docs/hello-world.html
ReactDOM.render(
    routing,
    document.getElementById('root')
);
