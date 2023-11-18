INSERT INTO `usuario`(nome, cargo, pontos, email, senha) VALUES ('Carlos', 'cliente', 200, 'ca@gmail', '9090'),
                                                           ('Administrador', 'admin', 100000, 'admin@admin.com', 'admin');
insert into `usuario`(nome, cargo, pontos, email, senha) values ('EstoqueDinamico', 'admin', 100000, 'estoqued@admin.com', 'admin'),
                                                               ('EstoqueEstatico', 'admin', 100000, 'estoques@admin.com', 'admin');




insert into skin(nome,arma,preco,raridade,imagem) values
                                                      ('Dragon Lore', 'AWP', 10000, 'Factory New','AWP_Dragon_Lore.png'),
                                                      ('Cobalt Quartz', 'Dual Berettas', 1500, 'Field-Tested','Dual_Berettas_Cobalt_Quartz.png'),
                                                      ('Cyrex', 'M4A1-S', 7000, 'Minimal Wear','M4A1-S_Cyrex.png'),
                                                      ('Hot Rod', 'M4A1-S', 6000, 'Factory New','M4A1-S_Hot_Rod.png'),
                                                      ('Bloodsport', 'SCAR-20', 1000, 'Well-Worn','SCAR-20_Bloodsport.png'),
                                                      ('Splash Jam', 'SCAR-20', 1400, 'Battle-Scarred','SCAR-20_Splash_Jam.png'),
                                                      ('Integrale', 'SG 553', 4000, 'Factory New','SG_553_Integrale.png'),
                                                      ('Hazard Pay', 'SG 553', 3000, 'Minimal Wear','SG_553_Hazard_Pay.png'),
                                                      ('Chatterbox', 'Galil AR', 5000, 'Field-Tested','Galil_AR_Chatterbox.png'),
                                                      ('Sugar Rush', 'Galil AR', 2500, 'Well-Worn','Galil_AR_Sugar_Rush.png'),
                                                      ('Roll Cage', 'FAMAS', 3000, 'Field-Tested','FAMAS_Roll_Cage.png'),
                                                      ('Commemoration', 'FAMAS', 2500, 'Minimal Wear','FAMAS_Commemoration.png'),
                                                      ('Fire Serpent', 'AK-47', 9500, 'Minimal Wear','AK-47_Fire_Serpent.png'),
                                                      ('Vulcan', 'AK-47', 8000, 'Factory New','AK-47_Vulcan.png'),
                                                      ('Asiimov', 'M4A4', 6000, 'Field-Tested','M4A4_Asiimov.png'),
                                                      ('Poseidon', 'M4A4', 4000, 'Minimal Wear','M4A4_Poseidon.png'),
                                                      ('Decimator', 'Tec-9', 3000, 'Factory New','Tec-9_Decimator.png'),
                                                      ('Avalanche', 'Tec-9', 2000, 'Field-Tested','Tec-9_Avalanche.png'),
                                                      ('Vogue', 'Glock-18', 5000, 'Factory New','Glock-18_Vogue.png'),
                                                      ('Moonrise', 'Glock-18', 2000, 'Field-Tested','Glock-18_Moonrise.png'),

                                                      ('Mehndi', 'P250', 2000,'Minimal Wear','P250_Mehndi.png'),
                                                      ('Muertos', 'P250', 3000,'Well-Worn','P250_Muertos.png'),
                                                      ('Triumvirate', 'Five-SeveN', 4500,'Field-Tested','Five-SeveN_Triumvirate.png'),
                                                      ('Angry Mob', 'Five-SeveN', 5000,'Well-Worn','Five-SeveN_Angry_Mob.png'),
                                                      ('Kill Confirmed', 'USP-S', 8000,'Factory New','USP-S_Kill_Confirmed.png'),
                                                      ('The Traitor', 'USP-S', 5000,'Field-Tested','USP-S_The_Traitor.png'),
                                                      ('Fade', 'R8 Revolver', 4000,'Battle-Scarred','R8_Revolver_Fade.png'),
                                                      ('Skull Crusher', 'R8 Revolver', 2000,'Well-Worn','R8_Revolver_Skull_Crusher.png'),
                                                      ('Golden Koi', 'Desert Eagle', 6000,'Battle-Scarred','Desert_Eagle_Golden_Koi.png'),
                                                      ('Code Red', 'Desert Eagle', 3000,'Field-Tested','Desert_Eagle_Code_Red.png'),
                                                      ('Bloodsport', 'MP7', 5000,'Factory New','MP7_Bloodsport.png'),
                                                      ('Impire', 'MP7', 1500,'Field-Tested','MP7_Impire.png'),
                                                      ('Whiteout', 'MP7', 2000,'Well-Worn','MP7_Whiteout.png');


insert into usuario_skins_user(user_id, skins_user_id) values(1,1), (1,3),
                                                          (2,4), (2,5), (2,6), (2,7), (2,8), (2,9), (2,10),
                                                          (3,11), (3,12), (3,13), (3,14), (3,15), (3,16), (3,17), (3,18), (3,19), (3,20),
                                                          (4,21),(4,22),(4,23),(4,24),(4,25),(4,26),(4,27),(4,28),(4,29),(4,30),(4,31),(4,32),(4,33);

insert into movement(id_comprador, id_vendedor,id_skin, estado_venda, pontos) values (1,3,3,true,7000), (1,3,1,true,10000), (2,3,4,true,6000),
                                                                                     (2,3,5,true,1000), (2,3,6,true,1400), (2,3,7,true,4000), (2,3,8,true,3000), (2,3,9,true,5000), (2,3,10,true,2500);

insert into movement(id_vendedor,id_skin, estado_venda, pontos) values (1,3,false,7000), (1,1,false,10000), (2,4,false,6000),
                                                                       (2,5,false,1000), (2,6,false,1400), (2,7,false,4000), (2,8,false,3000), (2,9,false,5000), (2,10,false,2500),
                                                                       (3,11,false,3000), (3,12,false,2500), (3,13,false,9500), (3,14,false,8000), (3,15,false,6000),
                                                                       (3,16,false,4000), (3,17,false,3000), (3,18,false,2000), (3,19,false,5000), (3,20,false,2000),
                                                                       (4,21,false,2000),(4,22,false,3000),(4,23,false,4500),(4,24,false,5000),(4,25,false,8000),
                                                                       (4,26,false,5000),(4,27,false,4000),(4,28,false,2000),(4,29,false,6000),(4,30,false,3000),(4,31,false,5000),(4,32,false,1500),(4,33,false,2000);