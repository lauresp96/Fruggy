// Manejar el evento de abrir el modal para agregar a favoritos
$('#agregarFavoritoModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Botón que abre el modal
    var productoId = button.data('producto-id'); // Extraer el ID del producto
    var modal = $(this);
    modal.find('#productoFavoritoId').val(productoId); // Establecer el ID del producto en el campo oculto
});