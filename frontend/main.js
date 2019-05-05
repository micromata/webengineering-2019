/**
 * This is a class component with more options for lifecycle management etc.
 * See https://reactjs.org/docs/state-and-lifecycle.html how to move from a functional component to a class component.
 */
class GreeterPanel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name: ""
        }
    }

    componentDidMount() {
        fetch('http://localhost:8080/api/date')
            .then(function (response) {
                return response.text()
            })
            .then(function (text) {
                this.setState({
                    name: text
                });
            })
    }

    render() {
        return (
            <h1>
                {this.state.name}!
            </h1>
        )
    }
}

// See e.g. https://reactjs.org/docs/hello-world.html
ReactDOM.render(
    <GreeterPanel/>,
    document.getElementById('root')
);