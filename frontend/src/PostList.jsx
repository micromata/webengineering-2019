import React from "react";
import {Link} from "react-router-dom";
import backend from './configuration';

export class PostList extends React.Component {
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
        fetch(backend + '/api/post')
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
function PostItem(props) {
    const {post} = props;
    let url = undefined;
    if (post.url) {
        url = <a href={post.url}>{post.title}</a>
    } else {
        url = <Link to={'/post/' + post.id}>{post.title}</Link>
    }

    return (
        <li>
            <span className='number'>{props.index + 1}.</span>
            {url}
            <Link to={'/post/' + post.id} className='comment'>{post.numberOfComments} comments</Link>
        </li>
    );
}


