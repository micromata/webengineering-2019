// See https://stackoverflow.com/questions/40764596/using-react-router-with-cdn-and-without-webpack-or-browserify
// Note that using a PROPER BUILD SYSTEM (and ES6 modules) is planned for one of the following lectures.
//
// Ideally, we would like to have URLs without hashes. For this to work, our server must always return the same
// resource, independently of the path. We will add this feature in the future.
// TODO ML Fix server URL handling
// const Router = window.ReactRouterDOM.BrowserRouter;
const Router = window.ReactRouterDOM.HashRouter;

const Route = window.ReactRouterDOM.Route;


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
    constructor(props) {
        super(props);
    }

    /**
     * Called when a post item is clicked. Currently we simply print the post it, later we will use this information
     * to display this post using routing.
     */
    linkClicked(postId) {
        console.log("Post clicked: " + postId);
    }

    render() {
        return (
            <li>
                <span className='number'>{this.props.index + 1}.</span>
                <a href={this.props.post.content}>{this.props.post.title}</a>
                <span className='comment'
                      onClick={() => this.linkClicked(this.props.post.id)}>
                        {this.props.post.numberOfComments} comments
                    </span>
            </li>
        )
    }
}

function PostDetail(props) {
    return <h1>PostDetails</h1>
}

/**
 * Here we are going to define our routing from paths to shown components.
 */
const routing = (
    <Router>
        <div>
            <Route exact path="/" component={PostList}/>
            <Route path="/post" component={PostDetail}/>
        </div>
    </Router>
);

// See e.g. https://reactjs.org/docs/hello-world.html
ReactDOM.render(
    routing,
    document.getElementById('root')
);