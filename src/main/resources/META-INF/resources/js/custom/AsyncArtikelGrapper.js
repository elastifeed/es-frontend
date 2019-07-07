$(document).ready(function () {

    // handler to work in sandbox
    // grap link hits
    if (document.addEventListener)
        document.addEventListener('click', callback, false);
    else
        document.attachEvent('onclick', callback);

    function callback(e) {
        var e = window.e || e;

        if (e.target.tagName !== 'A')
            return;

        var element = e.target;

        if (element.getAttribute("targetfn") === "initMonth") {
            initMonth(element);
        } else if (element.getAttribute("targetfn") === "getMoreMonth") {
            getMoreMonth(element);
        }
    }

    // Initialisiere den angeforderten Monat mit startdaten
    function initMonth(element)
    {
        var selectedYear = element.getAttribute("jahr");
        var selectedMonth = element.getAttribute("monat");

        console.log("Month Clicked: " + selectedYear + " - " + selectedMonth);
    }

    // Initialisiere den angeforderten Monat mit startdaten
    function getMoreMonth(element)
    {
        var selectedYear = element.getAttribute("jahr");
        var selectedMonth = element.getAttribute("monat");

        console.log("MORE Month Clicked: " + selectedYear + " - " + selectedMonth);

        $.ajax({
            url: '/timeline/loadmoreartikel',
            type: 'GET',
            data: {
                year: selectedYear,
                month: selectedMonth
            },
            dataType: "json",
            success: function (response) {
                if(response.length == 0) {
                    alert("Keine Daten vorhanden")
                }
                else {
                    printArtikel(response,selectedYear,selectedMonth);
                }
            },
            error: function (error) {
                alert("error error");
            }
        });
    }

    // Gibt das Array mit Artikeln aus
    function printArtikel(response,selectedYear,selectedMonth){
        var result = "";
        var appendElement = $('#timelineArtikel' + selectedYear + '_' + selectedMonth);

        // Gibt jeden Artikel aus
        response.forEach(function (item) {
            result += '<li>'+
                '    <div class="row mt-12 mb-12">'+
                '        <div class="col-md-12">'+
                '            <div class="card flex-md-row">'+
                '                <img class="esf-article-thumbnail d-none d-md-block align-content-center" src="' + item.thumbnail + '" alt="Card image cap"></img>'+
                '                <div class="card-body">'+
                '                    <div class="row">'+
                '                        <div class="col-xl-12">'+
                '                            <h1 class="esf-preview-headline">'+
                '                                <a href="/artikel?id=' + item.id + '" class="text-dark">' + item.caption + '</a>'+
                '                            </h1>'+
                '                        </div>'+
                '                    </div>'+
                '                    <div class="row">'+
                '                        <div class="col-xl-12">'+
                '                            <label>' + item.date + '</label>'+
                '                        </div>'+
                '                    </div>'+
                ''+
                '                    <p class="card-text mb-auto esf-preview-text">'+
                '                        ' + item.content + ''+
                '                    </p>'+
                '                    <a href="/artikel?id=' + item.id + '">mehr lesen</a>'+
                '                </div>'+
                '            </div>'+
                '        </div>'+
                '    </div>'+
                '</li>';
        })

        appendElement.append(result);
    }

});