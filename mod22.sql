 drop database imprexa2;
 create database imprexa2;
 use imprexa2;

create table cliente (idC int not null auto_increment, agente varchar(40), nom varchar(40), tel bigint, cel bigint, mail varchar(35), 
dir varchar(40), primary key(idC));
alter table cliente add column razSoc varchar(40);
alter table cliente add column rfc varchar(15);
alter table cliente add column col varchar(30);
alter table cliente add column codPos varchar(10);
alter table cliente add column ciu varchar(30);
alter table cliente add column usoCfd varchar(30);
alter table cliente add column metDePag varchar(20);
alter table cliente add column contacto varchar(30);
alter table cliente add column domDeEnt varchar(50);

create table pedido(folio int not null auto_increment, impresion varchar(40), desarrollo int, 
 fIngreso date, fCompromiso date, fPago date,  grabados float,  subtotal float, total float, anticipo float, resto float, 
 devolucion varchar(10), descuento float, costoGrabado float, costoDisenio float, idC_fk int not null,
 primary key(folio), foreign key(idC_fk) references cliente(idC));
 alter table pedido drop column desarrollo;
 alter table pedido drop column costoGrabado;
 alter table pedido drop column costoDisenio;
 alter table pedido add column kgDesperdicioPe float; 
 alter table pedido add column porcentajeDespPe float; 
 alter table pedido add column costoTotal float; /*agrege yo*/
 alter table pedido add column gastosFijos float; /*agrege yo*/
 alter table pedido add column perdidasYGanancias float; /*agrege yo*/
 alter table pedido add column autorizo varchar(40);
 alter table pedido add column sumatoriaBolseoP float; 
 alter table pedido add column matComPe float; 
 alter table pedido add column matProPe float;
 alter table pedido add column pagado varchar(3);
 alter table pedido add column kgFinalesRango float;
 alter table pedido add column fTermino date;

create table partida(idPar int not null auto_increment, diseno varchar(50), estatus varchar(20), medida varchar(15), piezas int, tipo varchar(40), 
sello varchar(40), pigmento varchar(40), mat1 varchar(30), calibre1 varchar(5), mat2 varchar(30), calibre2 varchar(5), tintaC1 varchar(50), 
tintaC2 varchar(50),hoja int, de int,  precioUnitaro float, importe float, folio_fk int not null, primary key(idPar), foreign key(folio_fk) references 
pedido(folio));
alter table partida add column kgPartida float;
alter table partida add column desarrollo int;
alter table partida drop column diseno;
alter table partida add column kgDesperdicio float; 
alter table partida add column porcentajeDesp float; 
alter table partida add column costoMaterialTotal float;
alter table partida add column costoPartida float; 
/*Cara1*/
alter table partida add column c1t1 varchar(15);
alter table partida add column c1t2 varchar(15);
alter table partida add column c1t3 varchar(15);
alter table partida add column c1t4 varchar(15);
alter table partida add column c1t5 varchar(15);
alter table partida add column c1t6 varchar(15);
/*Cara2*/
alter table partida add column c2t1 varchar(15);
alter table partida add column c2t2 varchar(15);
alter table partida add column c2t3 varchar(15);
alter table partida add column c2t4 varchar(15);
alter table partida add column c2t5 varchar(15);
alter table partida add column c2t6 varchar(15);

alter table partida drop column tintaC1;
alter table partida drop column tintaC2;

alter table partida add column modoMat varchar(15);
alter table partida add column pzFinales int;

alter table partida add column manera varchar(3);

create table extrusion(idExt int not null auto_increment, pocM1 float, pocM2 float/*Produccion o compra de material*/,  
prov1 varchar(40), precioKg1 float, prov2 varchar(40), precioKg2 float, idPar_fk int not null, primary key(idExt), foreign key(idPar_fk)
references partida(idPar));
alter table extrusion add column kgTotales float;
alter table extrusion add column costoOpTotalExt float; 
alter table extrusion add column greniaExt float;
alter table extrusion add column costoUnitarioExt float; /*agrege yo*/
alter table extrusion add column hrTotalesPar varchar(6);

create table impreso(idImp int not null auto_increment, produccion float,/*Produccion o compra de material*/ prov1 varchar(40), 
precioKg1 float, idPar_fk int not null, primary key(idImp), foreign key(idPar_fk)
references partida(idPar));
alter table impreso add column kgTotales float;
alter table impreso add column sticky float;
alter table impreso add column costoDiseno float;
alter table impreso add column costoGrab float;
alter table impreso add column costoOpTotalImp float;
alter table impreso add column greniaImp float;
alter table impreso add column costoUnitarioImp float;/*agrege yo*/
alter table impreso add column hrTotalesPar varchar(6);
select * from extrusion where idPar_fk = 77;
create table bolseo(idBol int not null auto_increment, produccion float,/*Produccion o compra de material*/ produccionPz int,
 prov1 varchar(40), precioKg1 float, idPar_fk int not null, primary key(idBol), foreign key(idPar_fk)
references partida(idPar));
alter table bolseo add column kgTotales float;
alter table bolseo add column costoOpTotalBol float;
alter table bolseo add column greniaBol float;
alter table bolseo add column costoUnitarioBol float;/*agrege yo*/
alter table bolseo add column hrTotalesPar varchar(6);

create table operadorExt(idOpExt int not null auto_increment, kgUniE float, grenia float, operador varchar(60), numMaquina int, horaIni time, 
fIni date, horaFin time, fFin date, tiempoMuerto time, totalHoras time, extras time, idExt_fk int not null, primary key(idOpExt), 
foreign key(idExt_fk) references extrusion(idExt));
alter table operadorExt add column costoOpExt float;

create table operadorImp(idOpImp int not null auto_increment, kgUniI float, grenia float, operador varchar(60), numMaquina int, horaIni time, 
fIni date, horaFin time, fFin date, tiempoMuerto time, totalHoras time, extras time, idImp_fk int not null, primary key(idOpImp), 
foreign key(idImp_fk) references impreso(idImp));
alter table operadorImp add column costoOpImp float;

create table operadorBol(idOpBol int not null auto_increment, kgUniB float, grenia float, suaje float, operador varchar(60) not null, numMaquina int, horaIni time, fIni date, horaFin time, fFin date, tiempoMuerto time, totalHoras time, 
extras time, idBol_fk int not null, primary key(idOpBol), foreign key(idBol_fk) references bolseo(idBol));
alter table operadorBol add column costoOpBol float;

create table tintas(idTinta int not null auto_increment, 
 tinta1 varchar(30), pIni1 float, pFin1 float, tinta2 varchar(30), pIni2 float, pFin2 float, 
 tinta3 varchar(30), pIni3 float, pFin3 float, tinta4 varchar(30), pIni4 float, pFin4 float, 
 tinta5 varchar(30), pIni5 float, pFin5 float, tinta6 varchar(30), pIni6 float, pFin6 float, 
 iniMezcla float, finMezcla float, 
 iniAcetato float, finAcetato float, 
 iniRetard float, finRetard float,
 idImp_fk int not null,
 primary key(idTinta), foreign key(idImp_fk) references impreso(idImp));
 
 create table costosMaterial(idMaterial int not null primary key auto_increment, tipo varchar(10), precio float);
 insert into costosMaterial values(null,'ALTA',25);
 insert into costosMaterial values(null,'BAJA',25);
 insert into costosMaterial values(null,'BOPP',45);
 insert into costosMaterial values(null,'CPP',45);
 insert into costosMaterial values(null,'BOPP/BOPP',45);
 insert into costosMaterial values(null,'BAJA/BOPP',35);
 insert into costosMaterial values(null,'BAJA/PET',35);
 insert into costosMaterial values(null,'BOPP/PET',50);
 insert into costosMaterial values(null,'CPP/PET',50); 
 insert into costosMaterial values(null,'prueba',1);
 delete from costosMaterial where tipo = "prueba";
 SET SQL_SAFE_UPDATES = 0;

/*Eliminar todas la tablas*/
 drop table tintas cascade;
 drop table operadorBol cascade;
 drop table operadorImp cascade;
 drop table operadorExt cascade;
 drop table bolseo cascade;
 drop table impreso cascade;
 drop table extrusion cascade;
 drop table partida cascade;
 drop table pedido cascade;
 drop table cliente cascade;