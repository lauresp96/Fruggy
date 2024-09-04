$(document).ready(function(){
    $('#update-products').click(function(){
        $.ajax({
            url: '/productos/ajax',
            method: 'GET',
            success: function(data) {
                $('#productos-container').empty();
                $.each(data, function(index, producto){
                    let descuentoHtml = producto.porcentajeDescuento > 0 ? '<div class="product__discount__percent">-' + producto.porcentajeDescuento + '%</div>' : '';
                    let productoHtml = '<div class="col-lg-4">' +
                        '<div class="product__discount__item">' +
                        '<div class="product__discount__item__pic set-bg" style="background-image: url(' + producto.imagen + ');">' +
                        descuentoHtml +
                        '<ul class="product__item__pic__hover">' +
                        '<li><a href="#"><i class="fa fa-heart"></i></a></li>' +
                        '<li><a href="#"><i class="fa fa-retweet"></i></a></li>' +
                        '<li><a href="#"><i class="fa fa-shopping-cart"></i></a></li>' +
                        '</ul>' +
                        '</div>' +
                        '<div class="product__discount__item__text">' +
                        '<span>' + producto.categoria + '</span>' +
                        '<h5><a href="#">' + producto.nombre + '</a></h5>' +
                        '<h7>' + producto.supermercado.nombre + '</h7>' +
                        '<div class="product__item__price">' +
                        '<span>' + producto.precioConDescuento + '€</span>' +
                        '<span>' + producto.productoPrecios.valor + '€</span>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>';
                    $('#productos-container').append(productoHtml);
                });
            }
        });
    });
});
