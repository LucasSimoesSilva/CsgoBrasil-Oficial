document.querySelectorAll(".items").forEach((skin) => {
    skin.addEventListener('click', () => {
        const idVenda = skin.getAttribute('data-id');
        window.location.href = `/skinCompra/compra.html?id=${encodeURIComponent(idVenda)}`;
    })
});


const gradeDiv = document.querySelector('.grade');
const slideDiv = document.querySelector('#sliderDiv');



function showAllSkins() {
    getAllMovementsSkins().then((movements) => {
        movements.forEach((movementSkin) => {
            if (movementSkin.idVendedor == 4) {
                showFixedSkins(movementSkin.idVenda, movementSkin.nome, movementSkin.arma, movementSkin.imagem, movementSkin.preco, movementSkin.raridade, movementSkin.estadoVenda);
            } else {
                showDynamicSkins(movementSkin.idVenda, movementSkin.nome, movementSkin.arma, movementSkin.imagem, movementSkin.preco, movementSkin.raridade);
            }

        });
    });
}

function showFixedSkins(id_venda, nome_skin, arma_skin, imagem_skin, preco_skin, raridade_skin, estadoVenda) {
    const imagemSkin = document.createElement('img');
    const novaLista = document.createElement('ul');
    const nomeLi = document.createElement('li');
    const precoLi = document.createElement('li');
    const raridadeLi = document.createElement('li');

    const divItem = document.createElement('div');

    if (estadoVenda) {
        divItem.classList.add('soldout');
        imagemSkin.src = '/images/soldout.png';
    } else {
        divItem.classList.add('items');
        imagemSkin.src = '/skin_imagens/' + imagem_skin;
        divItem.style.cursor = 'pointer';
        divItem.setAttribute('data-id', id_venda);

        divItem.addEventListener('click', () => {

            if (idUsuario != 0 && idUsuario > 0) {
                const idVenda = divItem.getAttribute('data-id');
                window.location.href = `/skinCompra/compra.html?id=${encodeURIComponent(idVenda)}`;
            } else {
                Swal.fire('Faça login para poder comprar uma skin.', '', 'info');
            }


        });
    }

    var nome = arma_skin + ' ' + nome_skin;


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

    slideDiv.appendChild(divItem);
}

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
        } else {
            Swal.fire('Faça login para poder comprar uma skin.', '', 'info');
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



//carrousel
var carrouselItem = document.querySelectorAll('.carousel-item');
var contador = 1

function preencherSlider() {
    carrouselItem.forEach((item) => {
        if (contador <= carrouselItem.length) {
            var img = item.querySelector('img');
            img.src = '/images/0' + contador + '.jpeg'
        }
        contador++;
    })
}

preencherSlider();

var carouselInterval;

function autoCarousel() {
    var radios = document.getElementsByClassName('carousel-open');
    var length = radios.length;
    var currentIndex = 0;

    carouselInterval = setInterval(function () {
        radios[currentIndex].checked = false;
        currentIndex = (currentIndex + 1) % length;
        radios[currentIndex].checked = true;
    }, 5000);
}

// Função para resetar o intervalo de tempo
function resetInterval() {
    clearInterval(carouselInterval);
    autoCarousel();
}

// Adiciona o evento de clique aos itens com a classe "carousel-control"
var carouselControls = document.getElementsByClassName('carousel-control');
for (var i = 0; i < carouselControls.length; i++) {
    carouselControls[i].addEventListener('click', resetInterval);
}

document.addEventListener('DOMContentLoaded', function () {
    if (idUsuario == 0) {
        Swal.fire('NOVOS USUÁRIOS RECEBEM 1000 PONTOS AO SE CADASTRAR.', '', 'info');
    }
});

// Chama a função para iniciar o carrossel automático
autoCarousel();

if (!localStorage.getItem('usuarioId')) {
    localStorage.setItem('usuarioId', 0);
    location.reload();
}
var idUsuario = localStorage.getItem('usuarioId');
