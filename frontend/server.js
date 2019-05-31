const express = require('express');
const app = express();

// Serve all static files from here.
app.use(express.static('./dist'));

// Serve all other URLs by returning the main page.
app.get('*', (req, res) => {
    res.sendFile(__dirname + '/dist/index.html');
});

// Start listening.
const port = process.env.PORT || 8081;
app.listen(port);