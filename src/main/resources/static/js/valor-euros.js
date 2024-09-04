function formatPrice(input) {
    let value = input.value;

    // Reemplazar coma por punto para validación y formato
    value = value.replace(",", ".");

    // Validar que el valor es un número válido y menor que 1000
    if (isNaN(value) || parseFloat(value) > 1000 || parseFloat(value) < 0.01) {
        input.value = ''; // Si no es válido, vaciar el campo
        return;
    }

    // Formatear el precio
    if (value) {
        input.value = parseFloat(value).toFixed(2); // Asegurarse que el valor tiene 2 decimales
    }
}

document.addEventListener("DOMContentLoaded", function() {
    const inputPrecio = document.getElementById("precio");

    // Limitar caracteres de entrada
    inputPrecio.addEventListener("input", function () {
        let value = this.value;

        // Remover caracteres no válidos
        this.value = value.replace(/[^0-9,.]/g, '');

        // Limitar a 4 dígitos antes de la coma/punto
        const parts = this.value.split(/[,\.]/);
        if (parts[0].length > 4) {
            this.value = parts[0].substring(0, 4) + (parts[1] ? ',' + parts[1] : '');
        }

        // Limitar a 2 dígitos después de la coma/punto
        if (parts[1] && parts[1].length > 2) {
            this.value = parts[0] + ',' + parts[1].substring(0, 2);
        }
    });

    // Formatear el precio al salir del campo
    inputPrecio.addEventListener("blur", function() {
        formatPrice(this); // Aplicar el formato cuando el usuario deja el campo
    });
});
