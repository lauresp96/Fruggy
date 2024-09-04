$(document).ready(function() {
    // Maneja el evento de clic en el botón de agregar a cesta
    $('.btn-warning').click(function() {
        const productoId = $(this).data('producto-id');
        $('#productoId').val(productoId);
    });
});