const listViewButton = document.getElementById("list");
const gridViewButton = document.getElementById("grid");
const productContainer = document.getElementById("products"); // Assuming this holds product elements

listViewButton.addEventListener("click", changeToListView);
gridViewButton.addEventListener("click", changeToGridView);

function changeToListView() {
    productContainer.classList.remove("view-grid");
    productContainer.classList.add("view-list");
}

function changeToGridView() {
    productContainer.classList.remove("view-list");
    productContainer.classList.add("view-grid");
}
listViewButton.addEventListener("click", () => {
    console.log("Switching to list view");
    changeToListView();
});

function changeToListView() {
    console.log("Adding view-list class");
    productContainer.classList.remove("view-grid");
    productContainer.classList.add("view-list");
}