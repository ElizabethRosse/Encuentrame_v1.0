/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function cargaMenu() {
    $('#menuIzqLogin').load('MenuIzq.xhtml div#menuIzq');
}
function cargaMisPublicaciones() {
    $('#contPrincipal').load('Publicaciones.xhtml div#misPublicaciones');
}
function cargaMisCitas() {
    $('#contPrincipal').load('contenidos.xhtml div#misCitas');
}
function cargaLogin() {
    $('#menuIzqLogin').load('index.xhtml div#menuIzqLogin');
    $('#contPrincipal').load('index.xhtml div#buscadorPublic');
}
function cargaNuevaPublicacion() {
    $('#contPrincipal').load('Publicaciones.xhtml div#nuevaPublicacion');
}
function cargaPublicacion() {
    $('#contPrincipal').load('index.xhtml div#buscadorPublic');
}
function cargaRegistro() {
    $('#contPrincipal').load('user_register.xhtml');
}

function modificarRegistro() {
    $('#contPrincipal').load('user_modify.xhtml');
}


