import React from "react";
import backend from './configuration';
import Authentication from "./authentication";

export class PostNew extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            title: '',
            url: '',
            description: ''
        };

        // See https://medium.freecodecamp.org/this-is-why-we-need-to-bind-event-handlers-in-class-components-in-react-f7ea1a6f93eb
        // for a very detailed explanation of this.
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    // See https://medium.com/@tmkelly28/handling-multiple-form-inputs-in-react-c5eb83755d15 for working with forms
    // with multiple elements.
    handleChange(evt) {
        this.setState({[evt.target.name]: evt.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        fetch(backend + '/api/post', {
            method: 'post',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + Authentication.getToken()
            },
            body: JSON.stringify({
                title: this.state.title,
                url: this.state.url,
                description: this.state.description
            })
        })
            .then(response => {
                // Redirect only a successful update.
                this.props.history.push('/');
            });
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit} className='new-post'>
                <div>
                    <label>
                        <span>title</span>
                        <input type="text" name="title" autoFocus={true} value={this.state.title}
                               onChange={this.handleChange}
                               maxLength={1024}/>
                    </label>
                </div>
                <div>
                    <label>
                        <span>url</span>
                        <input type="text" name="url" value={this.state.url} maxLength={1024}
                               onChange={this.handleChange}/>
                    </label>
                </div>
                <span className='or'>or</span>
                <div>
                    <label>
                        <span>text</span>
                        <textarea name='description' value={this.state.description}
                                  onChange={this.handleChange}
                            // We will later use a value from the backend instead of a hardcoded one.
                                  maxLength={4096}
                        ></textarea>
                    </label>
                </div>
                <div className='button'>
                    <input type="submit" value="submit"/>
                </div>
                <div className='description'>
                    Leave url blank to submit a question for discussion. If there is no url, the text (if any) will
                    appear at the top of the thread.
                </div>
            </form>
        );
    }
}
