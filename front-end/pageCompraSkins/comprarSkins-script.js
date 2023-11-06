function showAllSkins() {
    getAllMovementsSkins().then((movements) => {
        movements.forEach((movementSkin) => {
            if (movementSkin.idVendedor != 4) {
                showDynamicSkins(movementSkin.idVenda, movementSkin.nome, movementSkin.arma, movementSkin.imagem, movementSkin.preco, movementSkin.raridade);
            }
        });
    });
}

async function getAllMovementsSkins() {
    const response = await fetch('http://localhost:8080/movement/skinMovements', {
        method: 'GET',
        headers: {
            "content-type": "application/json"
        }
    }).catch(() => {
        window.parent.location.href = "/errorPage/error.html";
    });

    const myJson = await response.json();
    return myJson;
}


document.addEventListener('DOMContentLoaded', function () {
    showAllSkins();
});


function showDynamicSkins(id_venda, nome_skin, arma_skin, imagem_skin, preco_skin, raridade_skin) {
    const imagemSkin = document.createElement('img');
    const novaLista = document.createElement('ul');
    const nomeLi = document.createElement('li');
    const precoLi = document.createElement('li');
    const raridadeLi = document.createElement('li');

    const divItem = document.createElement('div');
    divItem.classList.add('items');
    divItem.style.cursor = 'pointer';
    divItem.setAttribute('data-id', id_venda);

    divItem.addEventListener('click', () => {
        if (idUsuario != 0 && idUsuario > 0) {
            const idVenda = divItem.getAttribute('data-id');
            window.location.href = `/skinCompra/compra.html?id=${encodeURIComponent(idVenda)}`;
        }
    });


    var nome = arma_skin + ' ' + nome_skin;

    imagemSkin.src = '/skin_imagens/' + imagem_skin;
    imagemSkin.alt = nome;
    imagemSkin.classList.add('cardimg');


    nomeLi.classList.add('nomeSkin');
    nomeLi.textContent = 'Nome: ' + nome;

    precoLi.classList.add('precoSkin');
    precoLi.textContent = 'Preço: ' + preco_skin + ' pontos';

    raridadeLi.classList.add('raridadeSkin');
    raridadeLi.textContent = 'Raridade: ' + raridade_skin;
    divItem.appendChild(imagemSkin);
    divItem.appendChild(novaLista);
    novaLista.appendChild(nomeLi);
    novaLista.appendChild(precoLi);
    novaLista.appendChild(raridadeLi);

    gradeDiv.appendChild(divItem);
}

const gradeDiv = document.querySelector('.grade');
var idUsuario = localStorage.getItem('usuarioId');