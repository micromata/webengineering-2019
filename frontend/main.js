// See https://stackoverflow.com/questions/40764596/using-react-router-with-cdn-and-without-webpack-or-browserify
// Note that using a PROPER BUILD SYSTEM (and ES6 modules) is planned for one of the following lectures.
const Router = window.ReactRouterDOM.BrowserRouter;
const Route = window.ReactRouterDOM.Route;
const Link = window.ReactRouterDOM.Link;


/**
 * This is a class component with more options for lifecycle management etc.
 * See https://reactjs.org/docs/state-and-lifecycle.html how to move from a functional component to a class component.
 */
class PostList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            post: []
        }
    }

    componentDidMount() {
        // Use ES6 arrow functions for this-binding. The keyword this in JavaScript is ... complicated. See, e.g.
        // https://stackoverflow.com/questions/20279484/how-to-access-the-correct-this-inside-a-callback for the
        // beginning of an explanation. Fortunately, we can use ES6 and are fine, here.
        //
        fetch('http://localhost:8080/api/post')
            .then((response) => {
                return response.json()
            })
            .then((data) => {
                this.setState({
                    post: data
                });
            })
    }

    render() {
        // We can't render arbitrary arrays in React, hence we display each one separately as JSX.
        const posts = this.state.post.map((post, index) => {
            return <PostItem
                key={post.id}   // Necessary for react.
                post={post}     // The actual post.
                index={index}/> // Need to show continous numbering
        });

        return (
            <div className='postList'>
                <ul>
                    {posts}
                </ul>
            </div>
        )
    }
}

/**
 * Component for displaying a single post list value.
 */
class PostItem extends React.Component {
    // TODO ML Can this be a functional component again?
    constructor(props) {
        super(props);
    }

    render() {
        const {post} = this.props;
        return (
            <li>
                <span className='number'>{this.props.index + 1}.</span>
                <a href={post.url}>{post.title}</a>
                <Link to={'/post/' + post.id}
                      className='comment'>{post.numberOfComments} comments</Link>
            </li>
        )
    }
}

/**
 * Show details for a single post.
 */
class PostDetail extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            post: {
                comments: []
            }
        }
    }

    // Retrieve all post information.
    componentDidMount() {
        fetch('http://localhost:8080/api/post/' + this.props.match.params.id)
            .then((response) => {
                return response.json()
            })
            .then((data) => {
                this.setState({
                    post: data
                });
            })
    }

    render() {
        const {post} = this.state;
        if (!post.url) {
            return <div></div>;
        }

        // Render a single comment and all its sub-comments.
        const comments = post.comments.map(comment => {
            return <Comment key={comment.id} {...comment} margin={0} increase={30}/>
        });

        // Combine
        return (
            <div className='post'>
                <div className='title'>
                    <a href={post.url}>
                        <span className='title-header'>{post.title}</span>
                        <span className='host'>
                            ({new URL(post.url).hostname})
                        </span>
                    </a>
                </div>
                <div>
                    {comments}
                </div>
            </div>
        );
    }
}

function Comment(props) {
    const comments = props.comments.map(comment => {
        return <Comment key={comment.id} {...comment} margin={props.margin + props.increase}/>
    });

    const style = {marginLeft: props.margin};
    return (
        <div className='postcomment'>
            <div style={style}>
                <div className='commentDate'>{props.createdAt}</div>
                {props.comment}
            </div>
            {comments}
        </div>
    )
}

function Header(props) {
    return (
        <div className="header">
            <Link to='/'>
                <span className="logo">L</span><span className="title">Lecture News</span>
            </Link>
        </div>
    );
}

/**
 * Here we are going to define our routing from paths to shown components.
 */
const routing = (
    <Router>
        <div>
            <Header/>
            <Route exact path="/" component={PostList}/>
            <Route path="/post/:id" component={PostDetail}/>
        </div>
    </Router>
);

// See e.g. https://reactjs.org/docs/hello-world.html
ReactDOM.render(
    routing,
    document.getElementById('root')
);