import React from "react";
import backend from './configuration';
import {Date} from 'sugar-date';
import {isAuthenticated} from './authentication';

/**
 * Show details for a single post.
 */
export class PostDetail extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            post: {
                comments: []
            }
        };

        this.loadData = this.loadData.bind(this);
    }

    // Retrieve all post information.
    componentDidMount() {
        this.loadData();
    }

    /**
     * Load data for the WHOLE PAGE and updates the state.
     */
    loadData() {
        fetch(backend + '/api/post/' + this.props.match.params.id)
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
        if (post === undefined) {
            return <div></div>;
        }

        // Render a single comment and all its sub-comments.
        const comments = post.comments.map(comment => {
            return <Comment key={comment.id} {...comment} margin={0} increase={30} update={this.loadData}/>
        });

        // Combine
        return (
            <div className='post'>
                <div className='title'>
                    <a href={post.url}>
                        <span className='title-header'>{post.title}</span>
                        <span className='host'>
                            {post.url ? new URL(post.url).hostname : ''}
                        </span>
                    </a>
                    {
                        // Show conditional rendering. See https://reactjs.org/docs/conditional-rendering.html for
                        // multiple other options.
                        post.description &&
                        <div className='post-description'>
                            {post.description}
                        </div>
                    }
                </div>
                {
                    isAuthenticated() &&
                    <ReplyArea id={post.id} update={this.loadData} allowVisibilityToggle={false} target='post'/>
                }
                <div>
                    {comments}
                </div>
            </div>
        );
    }
}

function Comment(props) {
    const comments = props.comments.map(comment => {
        return <Comment key={comment.id} {...comment} margin={props.margin + props.increase}
                        increase={props.increase} update={props.update}/>
    });

    const date = new Date(props.createdAt).relative().raw;
    const style = {marginLeft: props.margin};
    return (
        <div className='postcomment'>
            <div style={style}>
                <div className='commentDate'>{date}</div>
                {props.comment}
                {
                    isAuthenticated() &&
                    <ReplyArea id={props.id} update={props.update} allowVisibilityToggle={true} target='comment'/>
                }
            </div>
            {comments}
        </div>
    )
}

/**
 * Note that this is the reply for comments. We will have a direct reply for
 * posts afterwards!
 */
class ReplyArea extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            comment: '',
            visible: props.allowVisibilityToggle ? false : true
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.toggleVisibility = this.toggleVisibility.bind(this);
    }

    toggleVisibility(evt) {
        this.setState({['visible']: true});
    }

    // See https://medium.com/@tmkelly28/handling-multiple-form-inputs-in-react-c5eb83755d15 for working with forms
    // with multiple elements.
    handleChange(evt) {
        this.setState({[evt.target.name]: evt.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        // TODO ML Prevent empty comments?
        fetch(backend + '/api/' + this.props.target + '/' + this.props.id + '/comment', {
            method: 'post',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                comment: this.state.comment,
            })
        })
            .then(response => {
                this.props.update();
                if (this.props.allowVisibilityToggle) {
                    this.setState({['visible']: false});
                }
                this.setState({['comment']: ''});
            });
    }

    render() {
        if (!this.state.visible) {
            return (
                <div className='reply' onClick={this.toggleVisibility}>reply</div>
            );
        }

        return (
            <form onSubmit={this.handleSubmit} className='comment-input'>
                <div>
                    <textarea name='comment' value={this.state.comment} onChange={this.handleChange}
                              autoFocus={true}></textarea>
                </div>
                <div className='button'>
                    <input type="submit" value="submit"/>
                </div>
            </form>
        );
    }
}
