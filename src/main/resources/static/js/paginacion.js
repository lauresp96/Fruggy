document.addEventListener('DOMContentLoaded', () => {
    let currentPage = 1;
    const itemsPerPage = 10;
    const totalPages = 5; // Cambia esto según el total de páginas que tengas.

    function fetchProducts(page) {
        // Simula una llamada al servidor para obtener productos (reemplaza esto con una llamada real).
        // Aquí deberías hacer una solicitud AJAX para obtener productos de la página actual.
        // Por ejemplo, usando fetch:
        /*
        fetch(`/api/productos?page=${page}&size=${itemsPerPage}`)
            .then(response => response.json())
            .then(data => {
                renderProducts(data.products);
                updatePaginationInfo(page, data.totalPages);
            });
        */

        // Simulación de datos
        const simulatedProducts = Array.from({ length: itemsPerPage }, (_, index) => ({
            nombreProducto: `Producto ${((page - 1) * itemsPerPage) + index + 1}`,
            descripcion: `Descripción del producto ${((page - 1) * itemsPerPage) + index + 1}`,
            marca: `Marca ${((page - 1) * itemsPerPage) + index + 1}`,
            productoPrecios: { valor: (Math.random() * 100).toFixed(2) }
        }));

        renderProducts(simulatedProducts);
        updatePaginationInfo(page, totalPages);
    }

    function renderProducts(products) {
        const productsContainer = document.getElementById('products');
        productsContainer.innerHTML = ''; // Limpiar productos existentes.

        products.forEach(product => {
            const productHtml = `
                <div class="item col-xs-4 col-lg-4">
                    <div class="thumbnail card">
                        <div class="img-event">
                            <img class="group list-group-image img-fluid"
                                 src="/static/images/default-product.png"
                                 alt="Imagen del Producto"/>
                        </div>
                        <div class="caption card-body">
                            <h4 class="group card-title inner list-group-item-heading">
                                ${product.nombreProducto}
                            </h4>
                            <p class="group inner list-group-item-text">${product.descripcion}</p>
                            <p class="group inner list-group-item-text">${product.marca}</p>
                            <div class="row">
                                <div class="col-xs-12 col-md-6">
                                    <p class="lead">
                                        <span>${product.productoPrecios.valor} €</span>
                                    </p>
                                </div>
                                <div class="col-xs-12 col-md-6">
                                    <ul class="product__item__pic__hover">
                                        <li><a href="#"><i class="fa fa-heart"></i></a></li>
                                        <li><a href="#"><i class="fa fa-shopping-cart"></i></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            productsContainer.innerHTML += productHtml;
        });
    }

    function updatePaginationInfo(page, totalPages) {
        document.getElementById('page-info').textContent = `Página ${page} de ${totalPages}`;
        document.getElementById('prev').disabled = (page === 1);
        document.getElementById('next').disabled = (page === totalPages);
    }

    function changePage(direction) {
        const newPage = currentPage + direction;
        if (newPage >= 1 && newPage <= totalPages) {
            currentPage = newPage;
            fetchProducts(currentPage);
        }
    }

    // Inicializar la vista
    fetchProducts(currentPage);
});
