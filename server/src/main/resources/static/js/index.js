class FrontendController {
    constructor() {
        this.sensors = []
    }

    async loadSensorsAsync() {
        this.sensors = await $.get("sensors");
    }

    createTableRows() {
        const $tableBody = $("#sensor-table-body")
        this.sensors.forEach((sensorId, index) => {
            $tableBody.append($(document.createElement("tr"))
                // table index
                .append($(document.createElement("th"))
                    .attr({
                        scope: "row",
                    })
                    .text(index + 1)
                )
                // sensor id
                .append($(document.createElement("td")).text(sensorId))
                // sensor name
                .append($(document.createElement("td"))
                    .attr({
                        id: `${sensorId}-name`,
                    })
                    .text("n/a")
                )
                // current value
                .append($(document.createElement("td"))
                    .attr({
                        id: `${sensorId}-current`,
                    })
                    .text("n/a")
                )
                // avg value
                .append($(document.createElement("td"))
                    .attr({
                        id: `${sensorId}-avg`,
                    })
                    .text("n/a")
                )
                // max value
                .append($(document.createElement("td"))
                    .attr({
                        id: `${sensorId}-min`,
                    })
                    .text("n/a")
                )
                // min value
                .append($(document.createElement("td"))
                    .attr({
                        id: `${sensorId}-max`,
                    })
                    .text("n/a")
                )
            ) // end tr
        })
    }

    async loadSensorDetailsAsync() {
        for (const sensorId of this.sensors) {
            const sensorDetails = await $.get(`sensors/${sensorId}`)
            $(`#${sensorId}-name`).text(sensorDetails.name);
            $(`#${sensorId}-current`).text(sensorDetails.currentTemperature.toFixed(2));
            $(`#${sensorId}-avg`).text(sensorDetails.averageTemperature.toFixed(2));
            $(`#${sensorId}-max`).text(sensorDetails.maxTemperature.toFixed(2));
            $(`#${sensorId}-min`).text(sensorDetails.minTemperature.toFixed(2));
        }
    }
}

// const loadChart = (sensorId) => {
//     // const startOfDay = moment.utc().startOf("day")
//     // const endOfDay = moment.utc().endOf("day")

//     $.get("sensors/0517a1bf82ff/values?from=2018-04-03T00:00:00Z&to=2018-04-03T23:59:00Z")
//         .done((rawData) => {
//             const ctx = document.getElementById("sensor-chart")
//             const data = rawData.map((valueEntry) => {
//                 return {
//                     x: valueEntry.timestamp,
//                     y: valueEntry.value,
//                 }
//             })
//             new Chart(ctx, {
//                 type: "line",
//                 data: {
//                     // labels: years,
//                     datasets: [{
//                         label: "0517a1bf82ff",
//                         data: data,
//                         fill: false,
//                         pointRadius: 0,
//                         borderColor: "rgb(75, 192, 192)",
//                     }],
//                 },
//                 options: {
//                     scales: {
//                         xAxes: [{
//                             type: "time",
//                             time: {
//                                 unit: "minute",
//                                 displayFormats: {
//                                     day: "HH:mm",
//                                 },
//                             },
//                         }],
//                         yAxes: [{
//                             display: true,
//                             ticks: {
//                                 // minimum will be 0, unless there is a lower value.
//                                 suggestedMin: -20,
//                                 suggestedMax: 100,
//                             },
//                         }],
//                     },
//                 },
//             })
//         })
// }

$(async () => {
    $("#datepicker").datepicker({
        format: "dd.mm.yyyy",
        todayBtn: "linked",
        language: "de",
    });

    const controller = new FrontendController()
    await controller.loadSensorsAsync();
    controller.createTableRows();
    const detailsPromise = controller.loadSensorDetailsAsync();

    await detailsPromise;
})
