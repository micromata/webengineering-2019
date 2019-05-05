/**
 * This is a class component with more options for lifecycle management etc.
 * See https://reactjs.org/docs/state-and-lifecycle.html how to move from a functional component to a class component.
 */
class GreeterPanel extends React.Component {
    constructor(props) {
        super(props);
        // Define the 'data structure' for the state of this component.
        this.state = {
            name: ""
        }
    }

    /**
     * This function is called when the component is rendered in the DOM and can be used to initialize it.
     *
     * This is a lifecycle function, see https://reactjs.org/docs/state-and-lifecycle.html for an overview and a more
     * detailed description.
     */
    componentDidMount() {
        this.setState({
            name: new Date().toLocaleDateString()
        });
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
    <GreeterPanel/>,
    document.getElementById('root')
);