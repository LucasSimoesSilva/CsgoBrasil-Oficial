# CSGOBrasil
- Projeto para a avaliação A3 das UCs de Gestão e Qualidade de Software; Sistemas Distribuídos e Mobile; Usabilidade, Desenvolvimento Web, Mobile e Jogos

## Integrantes
- Lucas Simões Carvalho da Silva | RA: 12522161179
- Bruna Machado Gravina | RA: 1252221676
- Samuel Benjamin Nascimento de Amorim | RA: 12522196859
- Gabriel Santos Pontes | RA: 12523214406
- Leonardo Rondam de Oliveira | RA: 1252329487
- Andy Hyong Tae Choi Youn | RA: 12522142446
- Felipe Daura | RA: 1252214120
- Giovanni Lopes Campos | RA: 12522149685
- Marcelo Henrique da Silva Ventura | RA: 12522128126

## Back-end
- Linguagem: Java JDK 17.0.3.1
- IDE: IntelliJ IDEA Communit Edition 2022.2.3
- Tecnologia utilizada: REST API
- Banco de dados: MySQL
- Interface usada para aplicação de scripts mysql: MySQL Workbench 8.0


## Front-end
- Linguagem: JavaScript
- IDE: Visual Studio Code 1.78.2
- Linguagens auxiliares: HTML5, CSS3
- Tecnologias auxiliares: Node.js v18.14.2

<hr>

## Como Rodar o programa
<br>

### Requisitos obrigatórios para executar a aplicação
- Java JDK 17.0.3.1; Link:https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
- Node versão 18; Link:https://nodejs.org
- MySQL server(caso não possua):
    - Para Windows recomendamos:https://dev.mysql.com/downloads/installer/; (Sua instalação já contém o MySQL Workbench);
    - Para Linux: https://dev.mysql.com/downloads/mysql/ (este é apenas o server)
        - Documentação de sistemas operacionais: https://dev.mysql.com/doc/refman/8.0/en/linux-installation.html
- IDE de manutenção de banco de dados e execução de scripts (caso não possua):
    - MySQL Workbench: https://dev.mysql.com/downloads/workbench/ (para Linux)
    - Dbeaver: https://dbeaver.io/download/

<hr>

**AVISO**<br>
Tenha certeza de utilizar as versões corretas do Java e do Node, pois há diversas funcionalidades sensíveis a versões, então é de grande importância que a configuração de versões esteja correta.

Passos para executar o código:

1 - Clone o repositório https://github.com/LucasSimoesSilva/CSGOBrasil/tree/main;

2 - Abra a pasta `back-end` em uma IDE que aceite a linguagem Java;

3 - Abra a pasta `front-end` em uma outra IDE (recomendado: Visual Studio Code);

4 - Execute em uma IDE que tenha suporte para a linguagem mysql o script `sql-executar.sql` da pasta `sql-scripts`;(recomendado: MySQL Workbench 8.0 ou DBeaver Community)

5 - Dentro da pasta `back-end` através da IDE, ajuste no arquivo `src/main/resources/application.properties` os atributos url, username e password de forma com que correspondam com as configurações de seu sistema para banco de dados;

6 - Inicialize através da IDE a classe em Java `src/main/java/com/sd/csgobrasil/CsgobrasilApplication.java`;

7 - Após finalizar por completo o passo anterior, vá para a IDE que está com a pasta `front-end` aberta e execute o comando: `npm install -g http-server` e logo após o comando `http-server -a 127.0.0.1 -p 'porta desejada'` no terminal na respectiva pasta do diretório `./front-end`;<br>
Exemplo:`http-server -a 127.0.0.1 -p 5500`

8 - Caso haja algum erro para abrir o servidor na porta padrão, execute novamente o comando, porém utilizando outra porta;

9 - Abra em seu navegador favorito a seguinte URL: `http://127.0.0.1: 'porta do servidor' /homePage/home-page.html`. 
<br>Exemplo:`http://127.0.0.1:5500/homePage/home-page.html`<br>
- Caso você possua o plugin `Live Server` em seu Visual Studio Code, você pode utilizar esta extensão para inicializar o server, abrindo a página home-page.html

Extra - Caso deseje fazer Login sem cadastrar um usuário no sistema, já há um usuário pré-cadastrado com o email:`ca@gmail` e senha:`9090` e um usuário administrador, email:`admin@admin.com` e senha:`admin`

10 - Usufrua do sistema!
