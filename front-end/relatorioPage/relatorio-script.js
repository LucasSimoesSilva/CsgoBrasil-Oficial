var tabela = document.getElementById('tabela');

getReport().then((element) => {
    element.forEach((dado) => {
        var novaLinha = tabela.insertRow();
    
        var celulaVenda = novaLinha.insertCell();
        var celulaVendedor = novaLinha.insertCell();
        var celulaComprador = novaLinha.insertCell();
        var celulaSkin = novaLinha.insertCell();
        var celulaEstado = novaLinha.insertCell();
        var celulaValor = novaLinha.insertCell();
    
        celulaVenda.textContent = dado.idVenda;
        celulaVendedor.textContent = dado.nomeVendedor;

        if(dado.nomeComprador == null){
            celulaComprador.textContent = 'Ainda sem comprador'
        }else{
            celulaComprador.textContent = dado.nomeComprador;
        }


        celulaSkin.textContent = dado.nomeSkin;

        if(dado.estadoVenda){
            celulaEstado.textContent = 'Comprada'
        }else{
            celulaEstado.textContent = 'Anunciada'
        }
        celulaValor.textContent = dado.pontos;
    });
})


async function getReport(){
    const response = await fetch('http://localhost:8080/movement/report', {
        method: 'GET',
        headers: {  
            "content-type": "application/json"
        }
    });

    var myJson = await response.json();
    return myJson;
};