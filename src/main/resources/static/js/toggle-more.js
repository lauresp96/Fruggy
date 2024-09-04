// toggle-more.js
function myFunction() {
    var moreText = document.getElementById("more");
    var btnText = document.getElementById("mas__subcategorias");

    if (moreText.style.display === "none") {
        moreText.style.display = "block";
        btnText.innerHTML = "Mostrar menos";
        btnText.parentNode.appendChild(btnText); // Move button to the end
    } else {
        moreText.style.display = "none";
        btnText.innerHTML = "Mostrar más";
        var parent = btnText.parentNode;
        parent.insertBefore(btnText, document.getElementById("more")); // Move button back to the middle
    }
}
