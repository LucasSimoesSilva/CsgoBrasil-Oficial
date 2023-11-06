var pontos = document.getElementById('pontos');
var total = 0;

function confirmarCompra() {
    if (total > 0) {
        Swal.fire({
            title: 'Confirmação de Compra',
            text: 'Você realmente deseja comprar ' + total + ' moedas ?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Confirmar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire('Compra Confirmada', 'Moedas adicionadas com sucesso!', 'success');
        
                const elementos = document.querySelectorAll('.selected');
                elementos.forEach((elemento) => {
                    elemento.classList.remove('selected');
                    elemento.style.color = 'rgb(236, 32, 32)';
                    elemento.style.backgroundColor = '#010405ab';
                });

                getUserById().then((result) => {
                    result.pontos += total;
                    updateUser(result);
                    total = 0;
                    pontos.textContent = 0;
                    setTimeout(() => {location.reload()},2000);
                });

            } else {
                Swal.fire('Compra Cancelada', 'A compra foi cancelada.', 'info');
            }
        });
    } else {
        Swal.fire('Compra inválida', 'Para realizar uma compra você precisa selecionar uma quantidade de pontos maior que 0.', 'info');
    }
}

function selecionarPontos(elemento, moedas) {

    if (elemento.classList.contains('selected')) {
        elemento.style.color = 'rgb(236, 32, 32)';
        elemento.style.backgroundColor = '#010405ab';
        total -= moedas;
        elemento.classList.remove('selected');
    } else {
        elemento.style.color = 'rgb(20, 19, 19)';
        elemento.style.backgroundColor = 'white';
        total += moedas;
        elemento.classList.add('selected');
    }

    pontos.textContent = total;
}

var id = localStorage.getItem("usuarioId");

async function getUserById() {
    const response = await fetch('http://localhost:8080/user/' + id, {
        method: 'GET',
        headers: {
            "content-type": "application/json"
        }
    });

    const myJson = await response.json();
    return myJson;
}

async function updateUser(user) {
    const response = await fetch('http://localhost:8080/user/' + id, {
        method: 'PUT',
        headers: {
            "content-type": "application/json"
        },
        body: JSON.stringify(user)
    });
}