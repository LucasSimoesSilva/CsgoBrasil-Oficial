var id = localStorage.getItem("usuarioId");
const gradeDiv = document.querySelector('.grade');

function mostrarSkins() {
    getSkinsFromUser(id).then((skins) => {
        skins.forEach((skin) => {
            const alertMovement = document.createElement('p');
            const idSkin = skin.idSkin;
            const imagemSkin = document.createElement('img');
            const novaLista = document.createElement('ul');
            const nomeLi = document.createElement('li');
            const precoLi = document.createElement('li');
            const raridadeLi = document.createElement('li');

            const divItem = document.createElement('div');
            divItem.classList.add('items');
            divItem.style.cursor = 'pointer';

            var nome = skin.arma + ' ' + skin.nome;

            imagemSkin.src = '/skin_imagens/' + skin.imagem;
            imagemSkin.alt = nome;
            imagemSkin.classList.add('cardimg');


            nomeLi.textContent = 'Nome: ' + nome;

            precoLi.textContent = 'Preço: ' + skin.preco + ' pontos';

            raridadeLi.textContent = 'Raridade: ' + skin.raridade;

            alertMovement.textContent = 'Skin está anunciada no mercado';

            if (skin.isInMovement) {
                if(alertMovement.classList.contains('invisibleText')){
                    alertMovement.classList.remove('invisibleText');
                }
                alertMovement.classList.add('alertText');
            }else{
                if(alertMovement.classList.contains('alertText')){
                    alertMovement.classList.remove('alertText');
                }
                alertMovement.classList.add('invisibleText');
            }
            divItem.setAttribute('data-id', skin.idVenda);

            divItem.addEventListener('click', () => {
                const idVenda = divItem.getAttribute('data-id');
                window.location.href = `/skinVenda/venda.html?idVenda=${encodeURIComponent(idVenda)}&idSkin=${encodeURIComponent(idSkin)}`;
            });

            divItem.appendChild(imagemSkin);
            divItem.appendChild(novaLista);
            divItem.appendChild(alertMovement);
            novaLista.appendChild(nomeLi);
            novaLista.appendChild(precoLi);
            novaLista.appendChild(raridadeLi);

            gradeDiv.appendChild(divItem);
        });
    });
};

async function getSkinsFromUser(idUser) {
    const response = await fetch('http://localhost:8080/userskin/skinsState/' + idUser, {
        method: 'GET',
        headers: {
            "content-type": "application/json"
        }
    });

    const myJson = await response.json();
    return myJson;
}

document.addEventListener('DOMContentLoaded', function () {
    mostrarSkins();
});

