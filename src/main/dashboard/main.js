
/**
 * Updates whether the dashboard has a connection to the robot.
 */
NetworkTables.addRobotConnectionListener((connected) => {
    document.getElementById("robot-connected").innerHTML = `Robot ${connected ? "Connected" : "Disconnected"}`;
    if(connected) {
        document.getElementById("robot-connected").classList.remove("red");
        document.getElementById("robot-connected").classList.add("green");
    } else {
        document.getElementById("robot-connected").classList.remove("green");
        document.getElementById("robot-connected").classList.add("red");
    }
}, true);

/**
 * Updates cargo ship/rocket scoring mode.
 */
NetworkTables.addKeyListener("/SmartDashboard/Is scoring on cargo ship?", (key, value) => {
    if(value) {
        document.getElementById("cargoship").style.display = "inline-block";
        document.getElementById("rocket").style.display = "none";
    } else {
        document.getElementById("cargoship").style.display = "none";
        document.getElementById("rocket").style.display = "inline-block";
    }
}, true);

/**
 * Updates whether the robot has a hatch or cargo.
 */
NetworkTables.addKeyListener("/SmartDashboard/Has hatch?", (key, value) => {
    if(value) {
        document.getElementById("hatch").style.display = "inline-block";
        document.getElementById("cargo").style.display = "none";
    } else {
        document.getElementById("hatch").style.display = "none";
        document.getElementById("cargo").style.display = "inline-block";
    }
}, true);

/**
 * Updates the action that performs when the trigger is actuated.
 */
NetworkTables.addKeyListener("/SmartDashboard/Trigger Mode", (key, value) => {
    const wristMode = Math.round(value);
    if (wristMode == 0) { // Limelight align
        document.getElementById("wrist").style.display = "none";
        document.getElementById("align").style.display = "inline-block";
        document.getElementById("climb").style.display = "none"
    }
    else if (wristMode == 1) { // Wrist Manual Control
        document.getElementById("wrist").style.display = "inline-block";
        document.getElementById("align").style.display = "none";
        document.getElementById("climb").style.display = "none"
    } else { // Climb
        document.getElementById("wrist").style.display = "none";
        document.getElementById("align").style.display = "none";
        document.getElementById("climb").style.display = "inline-block"
    }
}, true)

/**
 * Updates the current elevator position, in encoder units.
 */
NetworkTables.addKeyListener("/SmartDashboard/Elevator Position", (key, value) => {
    document.getElementById("elevatorPos").innerHTML = value;
}, true);

/**
 * Updates the current wrist position, in degrees rounded to one decimal place.
 */
NetworkTables.addKeyListener("/SmartDashboard/Wrist Position", (key, value) => {
    document.getElementById("wristPos").innerHTML = `${value.toFixed(1)} degreees`;
}, true);

/**
 * Updates the date of the last received networktable message from the robot.
 */
NetworkTables.addKeyListener("/SmartDashboard/date", (key, value) => {
        console.log(new Date(value).toString());
});

/**
 * Updates the current battery percentage of the kangaroo, if it is connected.
 */
setInterval(() => {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://10.10.72.12:5802", true);
    xhr.onload = () => {
        document.getElementById("kangaroo_battery").innerHTML = xhr.responseText;
    }
    xhr.onerror = () => {
        document.getElementById("kangaroo_battery").innerHTML = "<span style='color:red'>Kangaroo Not Connected</span>";
    }
    xhr.send();
}, 1000);


document.getElementById("cargoship").style.display = "inline-block";
document.getElementById("rocket").style.display = "none";
document.getElementById("hatch").style.display = "inline-block";
document.getElementById("cargo").style.display = "none";
document.getElementById("wrist").style.display = "none";
document.getElementById("align").style.display = "inline-block";
document.getElementById("climb").style.display = "none"


function drawLimelightFeed() {
    const ctx = document.getElementById("ll-feed-canvas").getContext("2d");
    ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
    ctx.textAlign = "center";
    ctx.fillStyle = "red";
    ctx.fillText("No Limelight Feed", ctx.canvas.width / 2, ctx.canvas.height / 2);
    ctx.drawImage(document.getElementById("ll-image"), 0, 0, ctx.canvas.width, ctx.canvas.height);
    requestAnimationFrame(drawLimelightFeed);
}

requestAnimationFrame(drawLimelightFeed);
