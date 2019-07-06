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
                if (response.success) {
                    // Call next

                    // Kompaktansicht
                    if (response.result) {
                        alert("Success");
                    }
                } else {
                    alert("Error beim Abruf");
                }
            },
            error: function (error) {
                alert("error error");
            }
        });


    }

});