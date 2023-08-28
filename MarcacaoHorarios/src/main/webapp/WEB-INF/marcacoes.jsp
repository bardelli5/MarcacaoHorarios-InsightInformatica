<div class="row">
    <div class="col">
        <h1>Marcacoes: </h1>
        <br/>
        <div class="row text-center" style="padding-left:25px">
            <button type="button" class="col-sm-1 btn btn-success" id="add-new-clocking">Adicionar</button>
        </div>
        <br/>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Entrada</th>
                <th>Saida</th>
                <th>Atrasos</th>
                <th>Hora Extra</th>
            </tr>
            </thead>
            <tbody id="clocking-tbody">

            </tbody>
        </table>
    </div>
</div>
<script>
    const CLOCKING_URL = "${pageContext.request.contextPath}/api/clocking"

    $(document).ready(() => {
        $("#add-new-clocking").click(appendNewClocking)

        $("body")
            .on("click", "#cancel-new-clocking-button", () => $("#new-clocking-tr-input").remove())
            .on("click", "#add-new-clocking-button", addNewClocking)
    })

    function appendNewClocking() {
        if ($("#new-clocking-tr-input").length) {
            return
        }

        const tdInputEntry = $("<td></td>")
            .append("<div class=\"position-relative py-2 px-4 w-50\"></div>")
            .append("<input type=\"text\" class=\"form-control\" id=\"new-clocking-entry-input\" placeholder=\"00:00\"/>")

        const tdInputDeparture = $("<td></td>")
            .append("<div class=\"position-relative py-2 px-4 w-50\"></div>")
            .append("<input type=\"text\" class=\"form-control\" id=\"new-clocking-departure-input\" placeholder=\"00:00\"/>")

        const tdButtonAdd = $("<td></td>")
            .append("<button type=\"button\" class=\"btn btn-success\" id=\"add-new-clocking-button\">Adicionar</button>")

        const tdButtonCancel = $("<td></td>")
            .append("<button type=\"button\" class=\"btn btn-secondary\" id=\"cancel-new-clocking-button\">Cancelar</button>")

        const tr = $("<tr id=\"new-clocking-tr-input\"></tr>")
            .append(tdInputEntry)
            .append(tdInputDeparture)
            .append($("<td></td>")).append($("<td></td>"))
            .append(tdButtonAdd)
            .append(tdButtonCancel)

        $("#clocking-tbody").append(tr)
    }

    function addClocking(clocking) {
        const tdEntry = $("<td></td>").append(clocking.entry)
        const tdDeparture = $("<td></td>").append(clocking.departure)
        const tdDelays = $("<td></td>")

        clocking.delay.forEach((e) => tdDelays.append($("<p></p>").append(e)))

        const tdOvertime = $("<td></td>")

        clocking.overtime.forEach((e) => tdOvertime.append($("<p></p>").append(e)))

        const tr = $("<tr></tr>")
            .append(tdEntry).append(tdDeparture).append(tdDelays).append(tdOvertime)

        $("#clocking-tbody").append(tr)
    }

    const CONTENT_TYPE = "application/json; charset=utf-8"
    const DATA_TYPE = "json"

    function addNewClocking() {
        let body = {}
        body.entry = $("#new-clocking-entry-input").val()
        body.departure = $("#new-clocking-departure-input").val()

        $.ajax({
            method: "POST",
            url: CLOCKING_URL,
            data: JSON.stringify(body),
            contentType: CONTENT_TYPE,
            dataType: DATA_TYPE
        }).done((msg) => {
            console.log(msg)
            $("#clocking-tbody").empty()

            msg.forEach((e) => addClocking(e))
        }).fail((jqXHR, textStatus, msg) => {
            let alertMsg;
            if (jqXHR.responseJSON === null || jqXHR.responseJSON === undefined) {
                alertMsg = msg
            } else {
                alertMsg = jqXHR.responseJSON.errorMessage
            }

            alert(alertMsg)
        })
    }
</script>