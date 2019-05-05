function call(form) {
    const value = form['string'].value;
    fetch('http://localhost:8080/api/uppercase', {
        method: 'post',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({string: value})
    })
        .then(function (response) {
            return response.json()
        })
        .then(function (json) {
            document.getElementById("result").innerText = json['result'];
            form['string'].value = '';
        });
    return false;
}