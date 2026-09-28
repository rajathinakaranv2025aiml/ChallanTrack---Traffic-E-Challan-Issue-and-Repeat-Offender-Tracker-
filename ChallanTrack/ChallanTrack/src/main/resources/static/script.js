const API = "http://localhost:8080/api";


// =====================================================
// NAVIGATION
// =====================================================

function showSection(sectionName) {

    document.querySelectorAll(".section").forEach(section => {
        section.classList.remove("active");
    });

    document.getElementById(sectionName).classList.add("active");

    document.querySelectorAll(".menu-item").forEach(item => {
        item.classList.remove("active");
    });

    const menuItems = document.querySelectorAll(".menu-item");

    const sections = [
        "dashboard",
        "vehicles",
        "violations",
        "challans",
        "payments"
    ];

    const index = sections.indexOf(sectionName);

    if (index >= 0) {
        menuItems[index].classList.add("active");
    }

    const titles = {
        dashboard: "Dashboard",
        vehicles: "Vehicles",
        violations: "Violations",
        challans: "Challans",
        payments: "Payments"
    };

    document.getElementById("pageTitle").textContent =
        titles[sectionName];

    if (sectionName === "vehicles") {
        loadVehicles();
    }

    if (sectionName === "violations") {
        loadViolations();
    }

    if (sectionName === "challans") {
        loadChallans();
    }

    if (sectionName === "dashboard") {
        loadDashboard();
    }
}


// =====================================================
// DASHBOARD
// =====================================================

async function loadDashboard() {

    try {

        const vehiclesResponse =
            await fetch(`${API}/vehicles`);

        const violationsResponse =
            await fetch(`${API}/violations`);

        const challansResponse =
            await fetch(`${API}/challans`);

        if (!vehiclesResponse.ok ||
            !violationsResponse.ok ||
            !challansResponse.ok) {

            throw new Error("Backend request failed");
        }

        const vehicles = await vehiclesResponse.json();
        const violations = await violationsResponse.json();
        const challans = await challansResponse.json();

        document.getElementById("vehicleCount").textContent =
            vehicles.length;

        document.getElementById("violationCount").textContent =
            violations.length;

        document.getElementById("challanCount").textContent =
            challans.length;

        const paid = challans.filter(
            challan => challan.status === "PAID"
        );

        document.getElementById("paidCount").textContent =
            paid.length;

        document.getElementById("connectionStatus").textContent =
            "● Backend Connected";

    } catch (error) {

        console.error(error);

        document.getElementById("connectionStatus").textContent =
            "● Backend Offline";
    }
}


// =====================================================
// VEHICLES
// =====================================================

async function addVehicle() {

    const vehicleNumber =
        document.getElementById("vehicleNumber").value.trim();

    const ownerName =
        document.getElementById("ownerName").value.trim();

    const vehicleType =
        document.getElementById("vehicleType").value;

    const message =
        document.getElementById("vehicleMessage");


    if (!vehicleNumber || !ownerName) {

        message.textContent =
            "Vehicle number and owner name are required.";

        message.style.color = "#dc2626";

        return;
    }


    try {

        const response = await fetch(
            `${API}/vehicles`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    vehicleNumber: vehicleNumber,
                    ownerName: ownerName,
                    vehicleType: vehicleType
                })
            }
        );


        const data = await response.json();


        if (!response.ok) {

            throw new Error(
                data.message || "Failed to add vehicle"
            );
        }


        message.textContent =
            "Vehicle added successfully.";

        message.style.color = "#16a34a";


        document.getElementById("vehicleNumber").value = "";
        document.getElementById("ownerName").value = "";
        document.getElementById("vehicleType").value = "";


        loadVehicles();
        loadDashboard();

    } catch (error) {

        message.textContent = error.message;
        message.style.color = "#dc2626";
    }
}


async function loadVehicles() {

    try {

        const response =
            await fetch(`${API}/vehicles`);

        const vehicles =
            await response.json();

        const table =
            document.getElementById("vehicleTable");

        table.innerHTML = "";


        vehicles.forEach(vehicle => {

            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${vehicle.vehicleId}</td>
                <td><strong>${vehicle.vehicleNumber}</strong></td>
                <td>${vehicle.ownerName}</td>
                <td>${vehicle.vehicleType || "-"}</td>
            `;

            table.appendChild(row);

        });

    } catch (error) {

        console.error("Vehicle loading error:", error);
    }
}


// =====================================================
// VIOLATIONS
// =====================================================

async function addViolation() {

    const violationName =
        document.getElementById("violationName").value.trim();

    const baseFine =
        document.getElementById("baseFine").value;

    const escalationPercent =
        document.getElementById("escalationPercent").value;

    const message =
        document.getElementById("violationMessage");


    if (!violationName ||
        !baseFine ||
        !escalationPercent) {

        message.textContent =
            "Please fill all fields.";

        message.style.color = "#dc2626";

        return;
    }


    try {

        const response = await fetch(
            `${API}/violations`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({

                    violationName: violationName,

                    baseFine: Number(baseFine),

                    escalationPercent:
                        Number(escalationPercent)

                })
            }
        );


        const data = await response.json();


        if (!response.ok) {

            throw new Error(
                data.message || "Failed to add violation"
            );
        }


        message.textContent =
            "Violation added successfully.";

        message.style.color = "#16a34a";


        document.getElementById("violationName").value = "";
        document.getElementById("baseFine").value = "";
        document.getElementById("escalationPercent").value = "";


        loadViolations();
        loadDashboard();

    } catch (error) {

        message.textContent = error.message;
        message.style.color = "#dc2626";
    }
}


async function loadViolations() {

    try {

        const response =
            await fetch(`${API}/violations`);

        const violations =
            await response.json();

        const table =
            document.getElementById("violationTable");

        table.innerHTML = "";


        violations.forEach(violation => {

            const row =
                document.createElement("tr");

            row.innerHTML = `
                <td>${violation.violationTypeId}</td>
                <td><strong>${violation.violationName}</strong></td>
                <td>₹${violation.baseFine}</td>
                <td>${violation.escalationPercent}%</td>
            `;

            table.appendChild(row);

        });

    } catch (error) {

        console.error(
            "Violation loading error:",
            error
        );
    }
}


// =====================================================
// CHALLANS
// =====================================================

async function issueChallan() {

    const vehicleId =
        document.getElementById("challanVehicleId").value;

    const violationTypeId =
        document.getElementById("challanViolationId").value;

    const message =
        document.getElementById("challanMessage");


    if (!vehicleId || !violationTypeId) {

        message.textContent =
            "Vehicle ID and Violation ID are required.";

        message.style.color = "#dc2626";

        return;
    }


    try {

        const response = await fetch(
            `${API}/challans`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({

                    vehicleId: Number(vehicleId),

                    violationTypeId:
                        Number(violationTypeId)

                })
            }
        );


        const data = await response.json();


        if (!response.ok) {

            throw new Error(
                data.message || "Failed to issue challan"
            );
        }


        message.textContent =
            `Challan #${data.challanId} issued successfully. Fine: ₹${data.fineAmount}`;

        message.style.color = "#16a34a";


        document.getElementById("challanVehicleId").value = "";
        document.getElementById("challanViolationId").value = "";


        loadChallans();
        loadDashboard();

    } catch (error) {

        message.textContent = error.message;
        message.style.color = "#dc2626";
    }
}


async function loadChallans() {

    try {

        const response =
            await fetch(`${API}/challans`);

        const challans =
            await response.json();

        const table =
            document.getElementById("challanTable");

        table.innerHTML = "";


        challans.forEach(challan => {

            const row =
                document.createElement("tr");


            const statusClass =
                challan.status === "PAID"
                    ? "status-paid"
                    : "status-unpaid";


            const date =
                challan.issuedDate
                    ? new Date(challan.issuedDate)
                        .toLocaleString()
                    : "-";


            row.innerHTML = `
                <td>${challan.challanId}</td>

                <td>
                    ${challan.vehicle
                        ? challan.vehicle.vehicleNumber
                        : "-"}
                </td>

                <td>
                    ${challan.violationType
                        ? challan.violationType.violationName
                        : "-"}
                </td>

                <td>
                    ₹${challan.fineAmount}
                </td>

                <td>
                    ${date}
                </td>

                <td>
                    <span class="${statusClass}">
                        ${challan.status}
                    </span>
                </td>
            `;

            table.appendChild(row);

        });

    } catch (error) {

        console.error(
            "Challan loading error:",
            error
        );
    }
}


// =====================================================
// PAYMENT
// =====================================================

async function makePayment() {

    const challanId =
        document.getElementById("paymentChallanId").value;

    const paymentMethod =
        document.getElementById("paymentMethod").value;

    const message =
        document.getElementById("paymentMessage");


    if (!challanId || !paymentMethod) {

        message.textContent =
            "Challan ID and payment method are required.";

        message.style.color = "#dc2626";

        return;
    }


    try {

        const response = await fetch(
            `${API}/payments/${challanId}`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({

                    paymentMethod:
                        paymentMethod

                })
            }
        );


        const data = await response.json();


        if (!response.ok) {

            throw new Error(
                data.message || "Payment failed"
            );
        }


        message.textContent =
            "Payment completed successfully.";

        message.style.color = "#16a34a";


        document.getElementById("paymentChallanId").value = "";
        document.getElementById("paymentMethod").value = "";


        loadChallans();
        loadDashboard();

    } catch (error) {

        message.textContent = error.message;
        message.style.color = "#dc2626";
    }
}


// =====================================================
// INITIAL LOAD
// =====================================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        loadDashboard();

    }
);