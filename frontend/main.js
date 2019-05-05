/**
 * This is a class component with more options for lifecycle management etc.
 * See https://reactjs.org/docs/state-and-lifecycle.html how to move from a functional component to a class component.
 */
class GreeterPanel extends React.Component {
    constructor(props) {
        super(props);
        // This is the state of this component. You retrieve values with this.state.<variable> and set state
        // using this.setState
        this.state = {
            name: props.name
        }
    }

    render() {
        return (
            <h1>
                Hello, {this.state.name}!
            </h1>
        )
    }
}

// See e.g. https://reactjs.org/docs/hello-world.html
ReactDOM.render(
    <GreeterPanel name='Lecture'/>,
    document.getElementById('root')
);