// Variables to track full-screen exits
let fullScreenExitCount = 0;

function gofullscreen() {
    const element = document.documentElement;
    if (element.requestFullscreen) {
        element.requestFullscreen();
    } else if (element.webkitRequestFullscreen) {
        element.webkitRequestFullscreen();
    } else if (element.msRequestFullscreen) {
        element.msRequestFullscreen();
    } else {
        alert('Fullscreen mode is not supported by your browser.');
    }

    // Listen for exit from full screen
    document.addEventListener('fullscreenchange', checkFullScreenExit);
    document.addEventListener('webkitfullscreenchange', checkFullScreenExit);
    document.addEventListener('msfullscreenchange', checkFullScreenExit);
}

// Check if the user exits full screen
function checkFullScreenExit() {
    if (!document.fullscreenElement && !document.webkitIsFullScreen && !document.msFullscreenElement) {
        fullScreenExitCount++;
        if (fullScreenExitCount >= 2) {
            alert("You exited full screen twice. The test will now be submitted.");
			document.getElementById('totalInput').value = total;
			document.getElementById('scoreInput').value = score;
            document.getElementById('hiddenForm').submit(); // Automatically submit the test
        } else {
            alert(`You exited full screen. Please return to full screen to continue the test.`);
            gofullscreen(); // Prompt the user to return to full screen
        }
    }
}

// Delay timer until modal is closed
document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('modal');
    modal.style.display = 'flex';

    document.getElementById('closeModal').addEventListener('click', () => {
        modal.style.display = 'none';
        gofullscreen();
        startTest(); // Start the test when the modal is closed
    });
});

$(document).ready(function () {
    $('#ButtonNext').on('click', gofullscreen);
});