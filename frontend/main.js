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
        // Use ES6 arrow functions for this-binding. The keyword this in JavaScript is ... complicated. See, e.g.
        // https://stackoverflow.com/questions/20279484/how-to-access-the-correct-this-inside-a-callback for the
        // beginning of an explanation. Fortunately, we can use ES6 and are fine, here.
        //
        fetch('http://localhost:8080/api/date')
            .then((response) => {
                return response.text()
            })
            .then((text) => {
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