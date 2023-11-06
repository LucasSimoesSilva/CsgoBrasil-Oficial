const urlParams = new URLSearchParams(window.location.search);
const id_venda = urlParams.get('id');

var nomeSkin = document.getElementById('nomeSkin');
var precoSkin = document.getElementById('precoSkin');
var raridadeSkin = document.getElementById('raridadeSkin');
var imagemSkin = document.getElementById('skinImage');
var btnComprar = document.getElementById('comprar')
var id_usuario = localStorage.getItem('usuarioId')
var usuario_pontos = 0;

var alertText_compra = document.getElementById('alertText_compra');

async function getSkinById(id) {
    const response = await fetch('http://localhost:8080/skin/' + id, {
        method: 'GET',
        headers: {
            "content-type": "application/json"
        }
    });

    await response.json().then((result) => {
        var nome = result.arma + ' ' + result.nome;
        completePage(nome, result.preco, result.raridade, result.imagem);
    });
}

async function getUsuarioById(id) {
    const response = await fetch('http://localhost:8080/user/' + id, {
        method: 'GET',
        headers: {
            "content-type": "application/json"
        }
    });

    await response.json().then((result) => {
        usuario_pontos = result.pontos
    });
}



async function getMovementById(id) {
    const response = await fetch('http://localhost:8080/movement/' + id, {
        method: 'GET',
        headers: {
            "content-type": "application/json"
        }
    });

    await response.json().then((result) => {
        getSkinById(result.idSkin);
        if (result.idVendedor == id_usuario) {
            btnComprar.disabled = true;
            btnComprar.style.backgroundColor = 'black';
            btnComprar.style.cursor = 'default';
            alertText_compra.classList.remove('invisibleText');
            alertText_compra.classList.add('alertText');
            alertText_compra.textContent = 'Esta skin já pertênce a você'
        } else if (usuario_pontos < result.pontos) {
            btnComprar.disabled = true;
            btnComprar.style.backgroundColor = 'black';
            alertText_compra.classList.remove('invisibleText');
            alertText_compra.classList.add('alertText');
            alertText_compra.textContent = 'Pontos insuficientes'
            btnComprar.style.cursor = 'default';
        }
    });
};

getUsuarioById(id_usuario).then(() => {
    getMovementById(id_venda);

});



function completePage(nome, preco, raridade, imagem) {
    nomeSkin.textContent = nome;
    precoSkin.setAttribute('preco', preco);
    precoSkin.textContent = 'Preço: ' + preco + ' pontos';
    raridadeSkin.textContent = 'Raridade: ' + raridade;
    imagemSkin.src = '/skin_imagens/' + imagem;
}


btnComprar.addEventListener('click', () => {
    var data = {
        idVenda: id_venda,
        idComprador: id_usuario
    }

    alert('Comprar skin', 'Deseja comprar essa skin por ' + precoSkin.getAttribute('preco') + '?').then((result) => {

        if (result.isConfirmed) {
            Swal.fire('Compra confirmada', 'Compra da skin realizada!.', 'success').then(() => {
                makeMovement(data).then((result) => {
                    alertText_compra.classList.remove('invisibleText');
                    alertText_compra.classList.add('alertText');
                    if (result) {
                        btnComprar.disabled = true;
                        btnComprar.style.backgroundColor = 'black';
                        alertText_compra.textContent = 'Compra realizada com sucesso'
                    } else {
                        alertText_compra.textContent = 'Pontos insuficientes'
                    }
                });
            });
        } else {
            Swal.fire('Ação cancelada', 'Controle da skin não realizado.', 'info');
        }


    })

});

async function makeMovement(data) {
    const response = await fetch('http://localhost:8080/movement', {
        method: 'PUT',
        headers: {
            "content-type": "application/json"
        },
        body: JSON.stringify(data)
    });

    const myJson = await response.json();
    return myJson;
}


async function alert(title, message) {
    return Swal.fire({
        title: title,
        text: message,
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Confirmar',
        cancelButtonText: 'Cancelar'
    });
}
