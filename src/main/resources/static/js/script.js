// --------------------------------------------  change the color task --------------------------------------------
function checkTask(liElement, spanElement, checkIcon, modifyIcon, deleteIcon){
    liElement.classList.toggle("liTaskDone"); // alteramos a class na "li"
    spanElement.classList.toggle("spanTaskDone"); //alteramos a class no "span"
    checkIcon.classList.toggle("iconTaskDone");
    modifyIcon.classList.toggle("iconTaskDone");
    deleteIcon.classList.toggle("iconTaskDone");
}

//selecionamos todos os elementos com a class "tal"
const allCheckButtons = document.querySelectorAll(".checkIcon");

//percorremos cada elemento dessa lista. Cada elemento que tenha a clas "tal", vamos fazer o seguinte:
allCheckButtons.forEach(function(checkButton){
    
    checkButton.addEventListener("click", function(){
        //pegamos o elemento pai --> "li"
        const liElementoPai = checkButton.parentNode.parentNode;
        //pegamos o elemento span, dessa "li"
        const liSpan = liElementoPai.querySelector(".texto");
        //pegamos os elementos
        const checkIcon = liElementoPai.querySelector(".allIcons").querySelector(".checkIcon");
        const modifyIcon = liElementoPai.querySelector(".allIcons").querySelector(".modifyIconLink");
        const deleteIcon = liElementoPai.querySelector(".allIcons").querySelector(".removeIconLink");

        checkTask(liElementoPai, liSpan, checkIcon, modifyIcon, deleteIcon);
    });
});




// ----------------------  function to open the "MODAL EDIT TASK" automatically  ----------------------
$(document).ready(function() {
    $("#modalParaEditar").modal('show'); //se nós tivéssemos esse model em outros arquivos, a mesma coisa aconteceria. O modal seria aberto automaticamente.
});



