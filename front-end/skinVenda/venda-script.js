const urlParams = new URLSearchParams(window.location.search);
const id_venda = urlParams.get('idVenda');
const id_skin = urlParams.get('idSkin');

var nomeSkin = document.getElementById('nomeSkin');
var precoSkin = document.getElementById('precoSkin');
var raridadeSkin = document.getElementById('raridadeSkin');
var imagemSkin = document.getElementById('skinImage');
var btnAnunciar_cancelar = document.getElementById('anunciar_cancelar')
var id_vendedor = localStorage.getItem('usuarioId')
var data = null;

async function getSkinById(id) {
    const response = await fetch('http://localhost:8080/skin/' + id, {
        method: 'GET',
        headers: {
            "content-type": "application/json"
        }
    });

    await response.json().then((result) => {
        var nome = result.arma + ' ' + result.nome;
        completePage(nome, result.preco, result.raridade, result.imagem, result.id);
    });
}


async function controllMovement(id) {
    var estado_venda = false;
    getMovement(id).then((movement) => {
        estado_venda = movement.estadoVenda;
    }).then(async () => {
        if (id == 0 || estado_venda) {
            getSkinById(id_skin).then(() => {
                btnAnunciar_cancelar.addEventListener('click', () => {
                    alert('Anunciar skin', 'Deseja anunciar a sua skin?').then((result) => {

                        if (result.isConfirmed) {
                            Swal.fire('Ação confirmada', 'Controle da skin realizado.', 'success').then(() => {
                                    addMovement(data).then((result) => {
                                        if (result) {
                                            window.location.href = '/skinsPage/Skins.html';
                                        }
                                    }
                                    )
                            });
                        } else {
                            Swal.fire('Ação cancelada', 'Controle da skin não realizado.', 'info');
                        }


                    })


                });
            });
        } else {
            const response = await fetch('http://localhost:8080/movement/' + id, {
                method: 'GET',
                headers: {
                    "content-type": "application/json"
                }
            });

            await response.json().then((result) => {
                getSkinById(result.idSkin);
            }).then(() => {
                btnAnunciar_cancelar.textContent = 'REMOVER ANÚNCIO';
                btnAnunciar_cancelar.classList.remove('anuncio');
                btnAnunciar_cancelar.classList.add('remover');

                btnAnunciar_cancelar.addEventListener('click', () => {
                    alert('Cancelamento de anúncio', 'Deseja cancelar o anúncio da sua skin?').then((result) => {
                        if (result.isConfirmed) {
                            Swal.fire('Ação confirmada', 'Controle da skin realizado.', 'success').then(() => {
                                deleteMovement(id_venda).then(() => {
                                    window.location.href = '/skinsPage/Skins.html';
                                })
                            });
                        } else {
                            Swal.fire('Ação cancelada', 'Controle da skin não realizado.', 'info');
                        }
                    });

                })
            }
            );
        }
    })
};




function completePage(nome, preco, raridade, imagem, id_skin) {
    nomeSkin.textContent = nome;
    precoSkin.textContent = 'Preço: ' + preco + ' pontos';
    raridadeSkin.textContent = 'Raridade: ' + raridade;
    imagemSkin.src = '/skin_imagens/' + imagem;

    data = {
        idVendedor: id_vendedor,
        idComprador: 0,
        idSkin: id_skin,
        estadoVenda: false,
        pontos: preco
    }
}


async function addMovement(data) {
    const response = await fetch('http://localhost:8080/movement', {
        method: 'POST',
        headers: {
            "content-type": "application/json"
        },
        body: JSON.stringify(data)
    });

    const myJson = await response.json();
    return myJson;
}

async function deleteMovement(id) {
    await fetch('http://localhost:8080/movement/' + id, {
        method: 'DELETE',
        headers: {
            "content-type": "application/json"
        }
    });
}

async function getMovement(id) {
    const response = await fetch('http://localhost:8080/movement/' + id, {
        method: 'GET',
        headers: {
            "content-type": "application/json"
        }
    });

    var myJson = await response.json();
    return myJson;
};


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


controllMovement(id_venda);