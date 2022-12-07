package com.algafood.alfaworksfoodapi.service;

import com.algafood.alfaworksfoodapi.modelo.Cliente;
import com.algafood.alfaworksfoodapi.notificacao.Notificador;


public class AtivacaoClienteService {

    private Notificador notificador;

    public AtivacaoClienteService(Notificador notificador) {

        this.notificador = notificador;
    }

    public void ativar(Cliente cliente) {

        notificador.notificar(cliente, "Seu cadastro no sistema está ativo");
    }
}
