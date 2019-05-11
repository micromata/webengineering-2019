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

    /**
     * Called when a post item is clicked. Currently we simply print the post it, later we will use this information
     * to display this post using routing.
     */
    itemClicked(postId) {
        console.log("Post clicked: " + postId);
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

    render() {
        return (
            // Ignore clicked item (for now)!
            <li onClick={() => this.itemClicked(this.props.post.id)}>
                <span className='number'>{this.props.index + 1}.</span>
                {this.props.post.title}
                <span className='date'>({this.props.post.createdAt.substring(0, 10)})</span>
            </li>
        )
    }
}

// See e.g. https://reactjs.org/docs/hello-world.html
ReactDOM.render(
    <PostList/>,
    document.getElementById('root')
);