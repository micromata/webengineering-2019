/**
 * This is a functional component defined by a single returned function. Note that we can't store state in a
 * functional component.
 */
function GreeterPanel(props) {
    return (
        <h1>
            Hello, {props.name}!
        </h1>
    )
}

// See e.g. https://reactjs.org/docs/hello-world.html
ReactDOM.render(
    <GreeterPanel name='Lecture'/>,
    document.getElementById('root')
);