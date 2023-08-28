<div class="row">
    <div class="col-md-4">
        <h1>Horarios de Trabalho:</h1>

        <br/>
        <div class="row text-center" style="padding-left:25px">
            <button type="button" class="col-sm-3 btn btn-success" id="add-work-time">Adicionar</button>
        </div>
        <br/>
        <table class="table table-striped">
            <thead>
            <tr>
                <th scope="col">Entrada</th>
                <th scope="col">Saida</th>
                <th></th>
            </tr>
            </thead>
            <tbody id="work-hour-tbody">
            </tbody>
        </table>
    </div>
</div>
<script>
    const WORK_HOUR_URL = "${pageContext.request.contextPath}/api/work-hour"

    $(document).ready(() => {
        $("#add-work-time").click(appendNewWorkHour)

        $("body")
            .on('click', '#cancel-new-work-hour', () => $("#new-work-hour-tr-input").remove())
            .on('click', '#add-new-work-hour', addNewWorkHour)
            .on('click', '#delete-work-hour', (events) => removeWorkHour(events))
    })

    function appendNewWorkHour() {
        if ($("#new-work-hour-tr-input").length) {
            return
        }

        const inputEntry = $("<input type=\"text\" class=\"form-control\" id=\"new-work-hour-entry-input\" placeholder=\"00:00\"/>");
        const inputDeparture = $("<input type=\"text\" class=\"form-control\" id=\"new-work-hour-departure-input\" placeholder=\"00:00\"/>")
        const tdEntry = $("<td></td>").append($("<label></label>").append(inputEntry))
        const tdDeparture = $("<td></td>").append($("<label></label>").append(inputDeparture))
        const tdAddButton = $("<td></td>").append($("<button type=\"button\" class=\"btn btn-success\" id=\"add-new-work-hour\">Adicionar</button>"))
        const tdCancelButton = $("<td></td>").append($("<button type=\"button\" class=\"btn btn-secondary\" id=\"cancel-new-work-hour\">Cancelar</button>"))

        const tr = $("<tr id=\"new-work-hour-tr-input\"></tr>")
            .append(tdEntry).append(tdDeparture).append(tdAddButton).append(tdCancelButton)

        $("#work-hour-tbody").append(tr)
    }

    function addNewWorkHour() {
        const body = {};
        body.entry = $("#new-work-hour-entry-input").val()
        body.departure = $("#new-work-hour-departure-input").val()

        console.log(JSON.stringify(body));

        $.ajax({
            method: "POST",
            url: WORK_HOUR_URL,
            data: JSON.stringify(body),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (msg) {
            $("#work-hour-tbody").empty()

            msg.forEach((e, i) => addWorkHour(e, i))
        }).fail(function (jqXHR, textStatus, msg) {
            let alertMsg;
            if (jqXHR.responseJSON === null || jqXHR.responseJSON === undefined) {
                alertMsg = msg
            } else {
                alertMsg = jqXHR.responseJSON.errorMessage
            }

            alert(alertMsg)
        })
    }

    function addWorkHour(workHour) {
        const tdEntry = $("<td></td>").append(workHour.entry)
        const tdDeparture = $("<td></td>").append(workHour.departure)
        const tdDeleteButton = $("<td></td>").append($("<button type=\"button\" class=\"btn btn-danger\" id=\"delete-work-hour\">Deletar</button>"))

        const tr = $("<tr></tr>").append(tdEntry).append(tdDeparture).append(tdDeleteButton)

        $("#work-hour-tbody").append(tr)
    }

    function removeWorkHour(events) {
        const parent = $(events.target).parent().get(0)
        const tr = $(parent).parent().get(0)

        const entry = $($(tr).children().get(0)).text()
        const departure = $($(tr).children().get(1)).text()

        $.ajax({
            method: "PUT",
            url: WORK_HOUR_URL,
            data: JSON.stringify({entry, departure}),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (msg) {
            console.log(msg)
            $("#work-hour-tbody").empty()

            msg.forEach((e) => addWorkHour(e))
        }).fail(function (jqXHR, textStatus, msg) {
            alert("Ocorreu um erro inexperado ao deletar Horario de Trabalho: \n" + msg)
        })
    }
</script>
