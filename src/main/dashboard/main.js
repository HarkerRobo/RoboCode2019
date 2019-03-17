const ctx = document.getElementById("canvas").getContext("2d");
let limelightLoaded = false;

const updateToggleModes = () => {
    let isCargoShipEnabled = NetworkTables.getValue("/SmartDashboard/Is scoring on cargo ship?");
    let isHatchEnabled = NetworkTables.getValue("/SmartDashboard/Has hatch?");
    let wristMode = Math.round(NetworkTables.getValue("/SmartDashboard/Trigger Mode"));
    let isExtenderExtended = NetworkTables.getValue("/SmartDashboard/Is extended?");

    // let isCargoShipEnabled = true;
    // let isHatchEnabled = false
    // let wristMode = 2;
    // let isExtenderExtended = true;

    if(isCargoShipEnabled) {
        document.getElementById("cargoship").style.display = "inline-block";
        document.getElementById("rocket").style.display = "none";
    } else {
        document.getElementById("cargoship").style.display = "none";
        document.getElementById("rocket").style.display = "inline-block";
    }

    if(isHatchEnabled) {
        document.getElementById("hatch").style.display = "inline-block";
        document.getElementById("cargo").style.display = "none";
    } else {
        document.getElementById("hatch").style.display = "none";
        document.getElementById("cargo").style.display = "inline-block";
    }

    if (wristMode == 0) { // Limelight align
        document.getElementById("wrist").style.display = "inline-block";
        document.getElementById("align").style.display = "none";
        document.getElementById("climb").style.display = "none"
    }
    else if (wristMode == 1) { // Wrist Manual Control
        document.getElementById("wrist").style.display = "none";
        document.getElementById("align").style.display = "inline-block";
        document.getElementById("climb").style.display = "none"
    } else { // Climb
        document.getElementById("wrist").style.display = "none";
        document.getElementById("align").style.display = "none";
        document.getElementById("climb").style.display = "inline-block"
    }

    document.getElementById("extender").innerHTML = `${isExtenderExtended ? "Extended" : "Retracted"}`
} 

const updateSmartDashFields = () => {
    let elevatorPosition = NetworkTables.getValue("/SmartDashboard/Elevator Position");
    let wristPosition = NetworkTables.getValue("/SmartDashboard/Wrist Position");
    console.log(new Date(NetworkTables.getValue("/SmartDashboard/date")).toString());

    document.getElementById("elevatorPos").innerHTML = elevatorPosition;
    document.getElementById("wristPos").innerHTML = Math.round(wristPosition * 10) / 10 + " degrees";
}

const updateKangarooBatteryPercentage = () => {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://10.10.72.12:5802", true);
    xhr.onload = () => {
        document.getElementById("kangaroo_battery").innerHTML = xhr.responseText;
    }
    xhr.onerror = () => {
        document.getElementById("kangaroo_battery").innerHTML = "<span style='color:red'>Kangaroo Not Connected</span>";
    }
    xhr.send();
}

const redrawLimelightFeed = () => {
    ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
    ctx.font = "100pt Comic Sans MS";
    ctx.textAlign = "center"
    ctx.fillStyle = "red";
    ctx.fillText("No Limelight Feed", ctx.canvas.width / 2, ctx.canvas.height / 2);
    ctx.drawImage(document.getElementById("limelight"), 0, 0, ctx.canvas.width, ctx.canvas.height);
}

const drawArrow = (direction, length) => {
    const arrowColor = "black";
    const centerMargin = 80; // How far offset the arrow is from the center of the canvas
    const arrowBodyHeight = 100; // The height of the linear part of the arrow
    const arrowHeadHeight = 150; // The max height of the triangular part of the arrow
    const bodyHeadRatio = 0.5; // The ratio of the length of the body to the length of the  head

    const sign = ((direction * 2 - 1) > 0) - ((direction * 2 - 1) < 0); // Safe signum
    const startX = ctx.canvas.width;
    const startY = ctx.canvas.height / 2;
    ctx.fillStyle = arrowColor;
    ctx.beginPath();
    ctx.moveTo(startX / 2 + centerMargin * sign, startY + arrowBodyHeight / 2);
    ctx.lineTo(startX / 2 + centerMargin * sign + length * bodyHeadRatio * sign, startY + arrowBodyHeight / 2);
    ctx.lineTo(startX / 2 + centerMargin * sign + length * bodyHeadRatio * sign, startY + arrowHeadHeight / 2);
    ctx.lineTo(startX / 2 + centerMargin * sign + length * sign, startY);
    ctx.lineTo(startX / 2 + centerMargin * sign + length * bodyHeadRatio * sign, startY - arrowHeadHeight / 2);
    ctx.lineTo(startX / 2 + centerMargin * sign + length * bodyHeadRatio * sign, startY - arrowBodyHeight / 2);
    ctx.lineTo(startX / 2 + centerMargin * sign, startY - arrowBodyHeight / 2);
    ctx.closePath()
    ctx.fill();
}

const networkTableInterval = setInterval(() => {
    updateToggleModes();
    updateSmartDashFields();
    updateKangarooBatteryPercentage();
}, 100);


const limelightDrawInterval = setInterval(() => {
    redrawLimelightFeed();
    // drawArrow(false, 500)
    // drawArrow(true, 500)
}, 50);

document.getElementById("limelight").onprogress = () => {
    limelightLoaded = true;
}
