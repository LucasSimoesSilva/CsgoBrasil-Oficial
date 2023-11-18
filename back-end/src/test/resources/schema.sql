create table `usuario` (
                      id bigint not null auto_increment,
                      cargo varchar(255),
                      email varchar(255),
                      nome varchar(255),
                      pontos integer not null,
                      senha varchar(255),
                      primary key (id)
);


create table skin (
                      id bigint not null auto_increment,
                      arma varchar(255),
                      imagem varchar(255),
                      nome varchar(255),
                      preco integer not null,
                      raridade varchar(255),
                      primary key (id)
);

create table movement (
                          id_venda bigint not null auto_increment,
                          estado_venda boolean not null,
                          id_comprador bigint,
                          id_skin bigint,
                          id_vendedor bigint,
                          pontos integer not null,
                          primary key (id_venda)
);

create table usuario_skins_user (
                                 user_id bigint not null,
                                 skins_user_id bigint not null
);


alter table if exists `usuario`
    drop constraint if exists UK_ob8kqyqqgmefl0aco34akdtpe;

alter table if exists `usuario`
    add constraint UK_ob8kqyqqgmefl0aco34akdtpe unique (email);

alter table if exists `usuario`
    drop constraint if exists UK_roaq6sjqx87h4xweikt9uldf5;

alter table if exists `usuario`
    add constraint UK_roaq6sjqx87h4xweikt9uldf5 unique (nome);

alter table if exists usuario_skins_user
    drop constraint if exists UK_cvw1j99vgmttg3mmmyvwky5e;

alter table if exists usuario_skins_user
    add constraint UK_cvw1j99vgmttg3mmmyvwky5e unique (skins_user_id);

alter table if exists usuario_skins_user add constraint FKlneol2lrhckkysy0d6rhkyc8v foreign key (skins_user_id) references skin;

alter table if exists usuario_skins_user add constraint FKlxfrcbbvremrtl9ld2m73wh1 foreign key (user_id) references `usuario`;