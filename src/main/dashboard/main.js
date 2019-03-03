const ctx = document.getElementById("canvas").getContext("2d");

const updateToggleModes = () => {
    let isCargoShipEnabled = NetworkTables.getValue("/SmartDashboard/Is scoring on cargo ship?");
    let isHatchEnabled = NetworkTables.getValue("/SmartDashboard/Has hatch?");
    let isWristEnabled = NetworkTables.getValue("/SmartDashboard/Has wrist manual control?");

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

    if (isWristEnabled) {
        document.getElementById("wrist").style.display = "inline-block";
        document.getElementById("align").style.display = "none";
    }
    else {
        document.getElementById("wrist").style.display = "none";
        document.getElementById("align").style.display = "inline-block";
    }
} 

const updateSmartDashFields = () => {
    let elevatorPosition = NetworkTables.getValue("/SmartDashboard/Elevator Position");
    let wristPosition = NetworkTables.getValue("/SmartDashboard/Wrist Position");

    document.getElementById("elevatorPos").innerHTML = elevatorPosition;
    document.getElementById("wristPos").innerHTML = Math.round(wristPosition * 10) / 10 + " degrees";
}

const redrawLimelightFeed = () => {
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
}, 100);


const limelightDrawInterval = setInterval(() => {
    redrawLimelightFeed();
    drawArrow(false, 500)
    drawArrow(true, 500)
}, 50);
