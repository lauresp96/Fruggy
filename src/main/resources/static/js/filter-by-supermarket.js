function filterBySupermarket() {
    var radios = document.getElementsByName('supermarket');
    var selectedSupermarket = "";
    for (var i = 0; i < radios.length; i++) {
        if (radios[i].checked) {
            selectedSupermarket = radios[i].value;
            break;
        }
    }
    alert("Filtrando por supermercado: " + selectedSupermarket);
    // Aquí puedes agregar la lógica para filtrar los productos según el supermercado seleccionado.
}
