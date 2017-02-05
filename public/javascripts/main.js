var httpRequest;

window.onload = function () {
    var query = document.getElementById("query-area");
    if (query.value == "") initialExample(); //todo move caret to end after this
    else doQuery();
};

function doQuery() {
    var query = encodeURIComponent(document.getElementById("query-area").value);
    makeRequest('get?q=' + query, userQueryCallback);
}

function makeRequest(url, callback) {
    httpRequest = new XMLHttpRequest();

    if (!httpRequest) {
        alert('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }
    var button = document.getElementById("button-submit-query");
    var spinner = document.getElementById("query-spinner");
    spinner.style.display = 'block';
    button.style.display = 'none';
    document.getElementById('share-link-form').style.display = 'none';

    httpRequest.onreadystatechange = callback;
    httpRequest.open('GET', url);
    httpRequest.send();
}

function userQueryCallback() {
    var response = JSON.parse(httpRequest.responseText);
    displayContents(httpRequest, response);
}

function displayContents(httpRequest, response) {
    var errorsDisplay = document.getElementById("errors-text");
    errorsDisplay.innerHTML = "";
    var resultsDisplay = document.getElementById("results-text");
    resultsDisplay.innerHTML = "";

    var button = document.getElementById("button-submit-query");
    var spinner = document.getElementById("query-spinner");

    if (httpRequest.readyState === XMLHttpRequest.DONE) {
        if (httpRequest.status <= 400) {
            var data = [];
            for (var i = 0; i < response.length; i++) {
                if (response[i].error) {
                    errorsDisplay.innerHTML += "Upit " + (i + 1) + ", greška: " + response[i].error + "<br />";
                } else if (response[i].data[0].length == 0) {
                    resultsDisplay.innerHTML += "Upit " + (i + 1) + ": nema rezultata<br />";
                } else {
                    if (response[i].type == 0) { //plot
                        var counts = [];
                        var sizes = [];
                        for (var j = 0; j < response[i].data[0].length; j++) {
                            var count = response[i].data[0][j];
                            counts.push('' + count);
                            sizes.push(Math.max(2, count * response[i].factor));
                        }
                        var res = {
                            type: "scattergl",
                            marker: {
                                size: sizes,
                                color: '#' + response[i].color
                            },
                            text: counts,
                            mode: "markers",
                            x: response[i].data[1],
                            y: response[i].data[2]
                        };
                        if (response[i].data.length > 3) {
                            res.type = "scatter3d";
                            res.z = response[i].data[3];
                        }
                        data.push(res);
                    } else {
                        resultsDisplay.innerHTML += "Upit " + (i + 1) + ", rezultat: ";
                        if (response[i].type == 2 || response[i].type == 3) { //min & max
                            for (var k = 0; k < response[i].data[0].length; k++) {
                                resultsDisplay.innerHTML += " " + response[i].data[0][k] + "x" + response[i].data[1][k];
                            }
                        } else if (response[i].type == 1 || response[i].type == 4) { //avg & count
                            resultsDisplay.innerHTML += response[i].data[1];
                        } else { //dump (& sve ostalo)
                            resultsDisplay.innerHTML += response[i].data;
                        }
                        resultsDisplay.innerHTML += "<br />";
                    }
                }
            }

            var layout = {
                title: 'Grafikon',
                xaxis: {
                    title: ""
                },
                yaxis: {
                    title: ""
                },
                zaxis: {
                    title: ""
                }
            };
            if (response[0].xaxis != undefined)
                layout.xaxis.title = response[0].xaxis;
            if (response[0].yaxis != undefined)
                layout.yaxis.title = response[0].yaxis;
            if (response[0].zaxis != undefined)
                layout.zaxis.title = response[0].zaxis;
            //todo ne radi ako prvi upit nije "crtaj"

            Plotly.newPlot("plot", data, layout);
        } else {
            alert('Došlo je do greške pri obavljanju zahteva\n' + httpRequest.status + ": " + httpRequest.responseText);
        }
    }

    spinner.style.display = 'none';
    button.style.display = 'block';
}

function toggleHelp() {
    var helpText = document.getElementById('help-text');
    if (helpText.style.display == 'inline-block') {
        helpText.style.display = 'none';
    } else {
        helpText.style.display = 'inline-block';
    }
}

function exampleQueryCallback() {
    var response = JSON.parse(httpRequest.responseText);
    var queryArea = document.getElementById("query-area");
    queryArea.value = response.query;
    displayContents(httpRequest, response.result);
}

function randomExample() {
    makeRequest("/examples/random", exampleQueryCallback);
}

function initialExample() {
    makeRequest("/examples/initial", exampleQueryCallback);
}

function shareQuery() {
    var linkBox = document.getElementById("share-link");
    var query = document.getElementById("query-area").value;

    linkBox.value = "upis.ml/query?initial="+encodeURIComponent(query);
    document.getElementById("share-link-form").style.display='inline-block';
    linkBox.focus();
    linkBox.select();
}