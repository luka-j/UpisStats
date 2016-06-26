var httpRequest;

window.onload = function () {
    var query = document.getElementById("query-area");
    presetQuery(query);
};

function presetQuery(queryArea) {
    queryArea.value = "x:zaokruzi#1.prosek.srpski, y:zaokruzi#1.prosek.matematika\n"+
    "crtaj zuto: osnovna prosek.ukupno<3.5 ili bodovi.zavrsni<20\n"+
    "crtaj #ee99ee: smer kvota<60\n"+
    "crtaj crveno: ucenik upisao skola.okrug='gradbeograd'\n"+
    "crtaj plavo: ucenik pohadjao skola.ime='matematicka gimnazija-ogled'\n"+
    "crtaj zeleno: smer kvota>=60";
    doQuery();
    queryArea.focus();
}

function doQuery() {
    var query = encodeURIComponent(document.getElementById("query-area").value);
    makeRequest('get?q=' + query);
}

function makeRequest(url) {
    httpRequest = new XMLHttpRequest();

    if (!httpRequest) {
        alert('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }
    var button = document.getElementById("button-submit-query");
    var spinner = document.getElementById("query-spinner");
    spinner.style.display = 'block';
    button.style.display = 'none';

    httpRequest.onreadystatechange = displayContents;
    httpRequest.open('GET', url);
    httpRequest.send();
}

function displayContents() {
    var errorsDisplay = document.getElementById("errors-text");
    errorsDisplay.innerHTML = "";
    var resultsText = document.getElementById("results-text");
    resultsText.innerHTML = "";
    var resArea = document.getElementById("result-area");
    resArea.value = "";

    var button = document.getElementById("button-submit-query");
    var spinner = document.getElementById("query-spinner");

    if (httpRequest.readyState === XMLHttpRequest.DONE) {
        if (httpRequest.status === 200) {

            var response = JSON.parse(httpRequest.responseText);
            var data = [];
            for (var i = 0; i < response.length; i++) {
                if (response[i].error) {
                    errorsDisplay.innerHTML += "Upit " + (i + 1) + ", greška: " + response[i].error + "<br>";
                } else if (response[i].data[0].length == 0) {
                    resultsText.innerHTML += "Upit " + (i + 1) + ": nema rezultata<br>";
                } else {
                    if (response[i].type == 0) {
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
                        resultsText.innerHTML += "Upit " + (i + 1) + ", rezultat:";
                        if (response[i].type != 5) {
                            for (var k = 0; k < response[i].data[0].length; k++) {
                                resultsText.innerHTML += " " + response[i].data[0][k] + "x" + response[i].data[1][k];
                            }
                        } else {
                            resultsText.innerHTML += response[i].data;
                        }
                        resultsText.innerHTML += "\n";
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


            resArea.value += "\n\nRAW:\n";
            resArea.value += httpRequest.responseText;
            document.getElementById("result-label").value = "";
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

function randomExample() {

}