/*
Procesos
generarProActionPerformed
    comprobarVacio
    comprobarProcesos
        llenarListasOperadores
        llenarListaAyudantes
        vaciarCamposMaquila
        comprobarModoMat
        establecerCamposPartida
agEActionPerformed
    comprobarVacio
    sumaKilosEx
    sumarCostosOperacionales
    sumarGrenias
    actualizarKgDes
    actualizarPorcentajeDes
    calculaCostoMaterialTotalExt
    calculaHrTotalesProceso
    calculaCostoPartida
    calcularCostoUnitarioExt
    calculaKgDesperdicioPedido
    calculaPorcentajeDesperdicioPe
    calcularCostoTotalPe
    calculaPyG
    vaciarOpE
agIActionPerformed
    comprobarVacio
    sumarKilosIm
    sumarCostosOperacionales
    sumarGrenias
    calculaCostoPartida
    calculaHrTotalesProceso
    float material = queryForPesosMaterialImpreso
    calcularCostoUnitarioImpreso(material)
    calcularCostoTotalPe
    calculaPyG
        calculaKgFinalesPedido
        calculaGfKg
    vaciarOpI
agBActionPerformed
    comprobarVacio
    sumarKilosBol
    sumarCostosOperacionales
    sumarGrenias
    actualizarKgDes
    actualizarPorcentajeDes
    calculaCostoPartida
    calculaHrTotalesProceso
    calcularCostoUnitarioBol
    calculaKgDesperdicioPedido
    calculaPorcentajeDesperdicioPe
    calcularCostoTotalPe
    calculaPyG
    vaciarOpB
gdExActionPerformed
    comprobarVacio
    actualizarKgDes
    actualizarPorcentajeDes
    calculaCostoMaterialTotalExt
    calculaCostoPartida
    calcularCostoUnitarioExt
    calculaKgDesperdicioPedido
    calculaPorcentajeDesperdicioPe
    calcularCostoTotalPe
    calculaPyG
gdImActionPerformed
    comprobarVacio
    calcularCostoUnitarioImp
    calculaPyG
gdBoActionPerformed
    comprobarVacio
    actualizarKgDes
    actualizarPorcentajeDes
    calcularCostoUnitarioBol
    calculaKgDesperdicioPedido
    calculaPorcentajeDesperdicioPe
    calculaPyG
eliminarPartida
sumaKilosEx
sumarKilosIm
sumarKilosBol
sumarGrenias
soloFlotantes
soloEnteros
limitarInsercion
proM1KeyTyped & proM2KeyTyped & porKg1ExtKeyTyped & porKg2ExtKeyTyped & kgImpKeyTyped &
porKgImpKeyTyped & kgBolKeyTyped & porKgBolKeyTyped & greExtKeyTyped & kgOpExtKeyTyped &
greImpKeyTyped & kgOpImKeyTyped & greBolKeyTyped & suajeKeyTyped & kgOpBolKeyTyped &
    soloFlotantes
pzsBolKeyTyped & maqExtKeyTyped & maqImpKeyTyped & maqBolKeyTyped
    limitarInsercion
    soloEnteros
impBusMousePressed
impBusKeyPressed
tablaPartMouseClicked

Formulas
COSTO DE OPERACION
obtenerSueldoXHora
sumarCostosOperacionales

KILOS DESPERDICIO PARTIDA
actualizarKgDes
queryForPesosMaterialPartida
queryForGreniaExtPartida
queryForKgTotalesBolPartida

PORCENTAJE DE DESPERDICIO DE PARTIDA
actualizarPorcentajeDes

COSTO DE MATERIAL COMPLETA
calculaCostoMaterialTotalExt
    calculaCostoMaterialMaquilaExt
    calculaCostoMaterialProducidoExt
        consultaCostoMaterial

COSTO DE PARTIDA
calculaCostoPartida

COSTO UNITARIO COMPLETA
calcularCostoUnitarioExt
    sumatoriaMaquilaProduccionExtrusion
calcularCostoUnitarioImp
    sumatoriaMaquilaProduccionImpreso
calcularCostoUnitarioBol
    sumatoriaMaquilaProduccionBolseo

KG DESPERDICIO PEDIDO COMPLETA
calculaKgDesperdicioPedido

PORCENTAJE DESPERDICIO PEDIDO COMPLETA
calculaPorcentajeDesperdicioPe
    sumaMaterialesPedido

PERDIDAS Y GANANCIAS
calculaPyG
calculaKgFinalesPedido
calculaGfKg
*/