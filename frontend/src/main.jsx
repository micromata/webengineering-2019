import ReactDOM from 'react-dom'
import React from "react";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";

import {PostDetail} from "./PostDetail";
import {PostList} from "./PostList";
import {Header} from "./Header";
import {PostNew} from "./PostNew";

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
