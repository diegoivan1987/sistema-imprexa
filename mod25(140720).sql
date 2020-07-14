drop database imprexa2;
create database imprexa2;
 use imprexa2;
 /* SET SQL_SAFE_UPDATES = 0;*/
 
create table cliente (idC int not null auto_increment,
 agente varchar(40), 
 nom varchar(40), 
tel bigint, 
cel bigint, 
mail varchar(50), 
dir varchar(40), 
razSoc varchar(50), 
rfc varchar(15), 
col varchar(30), 
codPos varchar(10), 
ciu varchar(30), 
usoCfd varchar(30), 
metDePag varchar(20), 
contacto varchar(30), 
domDeEnt varchar(50), 
primary key(idC));

create table pedido(folio int not null auto_increment, 
impresion varchar(40), 
fIngreso date, 
fCompromiso date, 
fPago date,  
grabados float,  
subtotal float, 
total float, 
anticipo float, 
resto float, 
devolucion varchar(10), 
descuento float, 
costoDisenio float, 
idC_fk int not null,
kgDesperdicioPe float, 
porcentajeDespPe float, 
costoTotal float, 
gastosFijos float, 
perdidasYGanancias float, 
autorizo date, 
sumatoriaBolseoP float, 
matComPe float, 
matProPe float, 
kgFinalesRango float, 
fTermino date, 
porcentajeIVA int, 
estatus varchar(8),
primary key(folio), 
foreign key(idC_fk) references cliente(idC));
 
create table partida(idPar int not null auto_increment, 
medida varchar(15), 
piezas int, 
tipo varchar(40), 
sello varchar(40), 
pigmento varchar(40), 
mat1 varchar(30), 
calibre1 varchar(5),
mat2 varchar(30), 
calibre2 varchar(5),
hoja int, 
de int,  
precioUnitaro float, 
importe float, 
folio_fk int not null, 
/*cara 1*/
c1t1 varchar(15), c1t2 varchar(15), c1t3 varchar(15), c1t4 varchar(15), c1t5 varchar(15),
c1t6 varchar(15),
/*cara 2*/
c2t1 varchar(15), c2t2 varchar(15), c2t3 varchar(15), c2t4 varchar(15), c2t5 varchar(15),
c2t6 varchar(15),
kgPartida float,
desarrollo float, 
kgDesperdicio float, 
porcentajeDesp float, 
costoMaterialTotal float,/*costo de material en formulas*/
costoPartida float,
modoMat varchar(15),
pzFinales int,
manera varchar(3),
primary key(idPar), 
foreign key(folio_fk) references pedido(folio));

create table extrusion(idExt int not null auto_increment, 
/*Produccion o compra de material*/
pocM1 float, 
pocM2 float,  
prov1 varchar(40), 
precioKg1 float, 
prov2 varchar(40), 
precioKg2 float, 
kgTotales float,/*sumatoria de produccion por partida*/
costoOpTotalExt float,
greniaExt float,
costoUnitarioExt float,
hrTotalesPar varchar(6),
idPar_fk int not null, 
primary key(idExt), 
foreign key(idPar_fk) references partida(idPar));

create table impreso(idImp int not null auto_increment, 
produccion float,/*Produccion o compra de material*/ 
prov1 varchar(40), 
precioKg1 float, 
produccion2 float,
prov2 varchar(40),
precioKg2 float,
kgTotales float,
costoOpTotalImp float,
greniaImp float,
costoUnitarioImp float,
hrTotalesPar varchar(6),
idPar_fk int not null, 
primary key(idImp), 
foreign key(idPar_fk)
references partida(idPar));

create table bolseo(idBol int not null auto_increment, 
produccion float,/*Produccion o compra de material*/ 
produccionPz int,
prov1 varchar(40), 
precioKg1 float,
kgTotales float,
costoOpTotalBol float,
greniaBol float,
costoUnitarioBol float,
hrTotalesPar varchar(6),
idPar_fk int not null,
primary key(idBol), 
foreign key(idPar_fk) references partida(idPar));

create table operadorExt(idOpExt int not null auto_increment, 
kgUniE float, 
grenia float, 
operador varchar(60), 
numMaquina int, 
horaIni time, 
fIni date, 
horaFin time, 
fFin date, 
tiempoMuerto time, 
totalHoras time, 
extras time, 
costoOpExt float,
idExt_fk int not null, 
primary key(idOpExt), 
foreign key(idExt_fk) references extrusion(idExt));

create table operadorImp(idOpImp int not null auto_increment, 
kgUniI float, 
grenia float, 
operador varchar(60), 
numMaquina int, 
horaIni time, 
fIni date, 
horaFin time, 
fFin date, 
tiempoMuerto time, 
totalHoras time, 
extras time, 
costoOpImp float,
ayudante varchar(20),
idImp_fk int not null, 
primary key(idOpImp), 
foreign key(idImp_fk) references impreso(idImp));

create table operadorBol(idOpBol int not null auto_increment, 
kgUniB float, 
grenia float, 
suaje float, 
operador varchar(60) not null, 
numMaquina int, 
horaIni time, 
fIni date, 
horaFin time, 
fFin date, 
tiempoMuerto time, 
totalHoras time, 
extras time,
costoOpBol float, 
idBol_fk int not null,
primary key(idOpBol), 
foreign key(idBol_fk) references bolseo(idBol));

create table tintas(idTinta int not null auto_increment, 
 tinta1 varchar(30), pIni1 float, pFin1 float, 
 tinta2 varchar(30), pIni2 float, pFin2 float, 
 tinta3 varchar(30), pIni3 float, pFin3 float, 
 tinta4 varchar(30), pIni4 float, pFin4 float, 
 tinta5 varchar(30), pIni5 float, pFin5 float, 
 tinta6 varchar(30), pIni6 float, pFin6 float, 
 iniMezcla float, finMezcla float, 
 iniAcetato float, finAcetato float, 
 iniRetard float, finRetard float,
 iniSolvente float, finSolvente float,
 iniBarniz float, finBarniz float,
 costoDeTintas float,
 seSumoAPedido int,
 sticky float,
 folio_fk int not null,
 primary key(idTinta), foreign key(folio_fk) references pedido(folio));

 create table costosMaterial(idMaterial int not null primary key auto_increment, 
 tipo varchar (10), precio float);
 insert into costosMaterial values(null,'ALTA',25);
 insert into costosMaterial values(null,'BAJA',25);
 /*insert into costosMaterial values(null,'BOPP',45);
 insert into costosMaterial values(null,'CPP',45);
 insert into costosMaterial values(null,'BOPP/BOPP',45);
 insert into costosMaterial values(null,'BAJA/BOPP',35);
 insert into costosMaterial values(null,'BAJA/PET',35);
 insert into costosMaterial values(null,'BOPP/PET',50);
 insert into costosMaterial values(null,'CPP/PET',50);*/

/*guardara el nombre, en que maquina trabajara y si sera ayudante de impresion*/
 create table operadores(nombre varchar(20) primary key not null, 
 sueldo_48_hrs float not null, 
 sueldo_hr float not null, 
 ayudante bool);
 insert into operadores values('RICARDO',2300,47.916,false);
 insert into operadores values('GENARO',2300,47.916,false);
 insert into operadores values('CRISTOBAL',2100,43.75,false);
 insert into operadores values('MARIO',1500,31.25,false);
 insert into operadores values('VICTOR',1300,27.083,true);