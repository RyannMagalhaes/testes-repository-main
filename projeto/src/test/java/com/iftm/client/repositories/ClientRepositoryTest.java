package com.iftm.client.repositories;

import com.iftm.client.entities.Client;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository repositorio;

    @Test
    @DisplayName("Verificar se a procura por ID retorna corretamente")
    public void testarBuscaPorIDRetornaClienteCorreto() {
        long idBuscado = 1; //corresponde ao primeiro registro do arquivo import.sql
        String nomeBuscado = "Conceição Evaristo";
        String cpfBuscado = "10619244881";

        Client respostaEsperada = new Client(1L, "Conceição Evaristo", "10619244881", null, null, null);

        Optional<Client> resposta = repositorio.findById(idBuscado);

        Assertions.assertThat(resposta.get().getName()).isEqualTo(nomeBuscado);
        Assertions.assertThat(resposta.get().getCpf()).isEqualTo(cpfBuscado);
    }

    @Test
    @DisplayName("Verificar se a busca por id nulo retorna algo")
    public void testarBuscaPorIdNaoRetornaObjetoParaIdInexistente() {
        long idBuscado = 100;

        Optional<Client> resultado = repositorio.findById(idBuscado);

        Assertions.assertThat(resultado).isEmpty();
        //assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Verificar se a exclusão está deletando um usuário corretamente")
    public void testarExcluirPorIdApagaRegistroExistente() {
        long idBuscado = 8;
        long quantidadeRegistrosEsperado = 11;

        repositorio.deleteById(idBuscado);
        Optional<Client> resultado = repositorio.findById(idBuscado);

        Assertions.assertThat(resultado).isEmpty();
        //assertTrue(resultado.isPresent());
        Assertions.assertThat(repositorio.count()).isEqualTo(quantidadeRegistrosEsperado);
        //assertEquals(quantidadeRegistrosEsperado, repositorio.count());

    }

    @Test
    @DisplayName("Verificar se método retorna o cliente correto através da busca com o nome")
    public void testarBuscarPorNomeExistente() {
        String nomeBuscado = "Conceição Evaristo";
        long idEsperado = 1;

        Optional<Client> resultado = repositorio.findByName(nomeBuscado);

        Assertions.assertThat(resultado).isNotEmpty();
        Assertions.assertThat(resultado.get().getId()).isEqualTo(idEsperado);
    }

    @Test
    @DisplayName("Veificar se método retorna algo buscando cliente inexistente")
    public void testarBuscarPorNomeNãoExistente() {
        String nomeBuscado = "Mariano Teste";

        Optional<Client> resultado = repositorio.findByName(nomeBuscado);

        Assertions.assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Testar o método que retorna vários clientes cujo nome inclui o parametro passado em qualquer posição")
    public void testarBuscarPorNomeSimilar() {
        String textoBuscado = "ama";
        // Jose Saramago
        long primeiroIdEsperado = 7;
        // Chimamanda Adichie
        long segundoIdEsperado = 10;
        // Jorge Amado
        long terceiroIdEsperado = 12;

        List<Client> listaResultado = repositorio.findByNameContains(textoBuscado);

        Assertions.assertThat(listaResultado.size()).isEqualTo(3);
        Assertions.assertThat(listaResultado.get(0).getId()).isEqualTo(primeiroIdEsperado);
        Assertions.assertThat(listaResultado.get(1).getId()).isEqualTo(segundoIdEsperado);
        Assertions.assertThat(listaResultado.get(2).getId()).isEqualTo(terceiroIdEsperado);
    }

    @Test
    @DisplayName("Testar metodo que busca por cliente cujo nome inclui o texto passado no parametro")
    public void testarBuscarPorNomeSimilarNaoExistente() {
        String textoBuscado = "magalhaes";

        List<Client> listaResultado = repositorio.findByNameContains(textoBuscado);

        Assertions.assertThat(listaResultado).isEmpty();
    }

    @Test
    @DisplayName("Testar o método que retorna todos os clientes com parte do nome similar a uma String vazia")
    public void testarBuscarPorNomeSimilarParametroVazio() {
        String textoVazio = "";

        List<Client> listaResultado = repositorio.findByNameContains(textoVazio);

        Assertions.assertThat(listaResultado.size()).isEqualTo(12);
    }

    @Test
    @DisplayName("Testar o método que busca clientes com salários superiores ao valor passado no parametro")
    public void testarBuscarPorSalarioMaiorQue() {
        Double salarioBuscado = 7000.0;
        Double primeiroSalarioEsperado = 7500.0;
        Double segundoSalarioEsperado = 10000.0;

        List<Client> listaResultado = repositorio.findByIncomeBiggerThan(salarioBuscado);

        Assertions.assertThat(listaResultado.size()).isEqualTo(2);
        Assertions.assertThat(listaResultado.get(0).getIncome()).isEqualTo(primeiroSalarioEsperado);
        Assertions.assertThat(listaResultado.get(1).getIncome()).isEqualTo(segundoSalarioEsperado);
    }

    @Test
    @DisplayName("Testar o método que busca clientes com salários inferiores ao valor passado no parametro")
    public void testarBuscarPorSalarioMenorQue() {
        Double salarioBuscado = 2500.0;
        Double salarioEsperado = 1500.0;

        List<Client> listaResultado = repositorio.findByIncomeSmallerThan(salarioBuscado);

        Assertions.assertThat(listaResultado.size()).isEqualTo(3);
        Assertions.assertThat(listaResultado.get(0).getIncome()).isEqualTo(salarioEsperado);
        Assertions.assertThat(listaResultado.get(1).getIncome()).isEqualTo(salarioEsperado);
        Assertions.assertThat(listaResultado.get(2).getIncome()).isEqualTo(salarioEsperado);
    }

    @Test
    @DisplayName("Testar o método que busca clientes com valor do salários entre dois valores informados.")
    public void testarBuscarSalarioPorIntervalo() {
        Double salarioInicioIntervalo = 2500.0;
        Double salarioFimIntervalo = 4000.0;

        List<Client> listaResultado = repositorio.findByIncomeInterval(salarioInicioIntervalo, salarioFimIntervalo);

        Assertions.assertThat(listaResultado.size()).isEqualTo(4);
        Assertions.assertThat(listaResultado.get(0).getIncome()).isEqualTo(2500.0);
        Assertions.assertThat(listaResultado.get(1).getIncome()).isEqualTo(3800.0);
        Assertions.assertThat(listaResultado.get(2).getIncome()).isEqualTo(2500.0);
        Assertions.assertThat(listaResultado.get(3).getIncome()).isEqualTo(2500.0);
    }

    @Test
    @DisplayName("Testar o método que retorna vários clientes procurando pela data de nascimento")
    public void testarBuscarPorDataAniversario() {
        Instant dataInicio = Instant.parse("2017-12-25T20:30:50Z");
        Instant dataFim = Instant.now();
        Instant dataNascimentoEsperada = Instant.parse("2020-07-13T20:50:00Z");

        List<Client> listaResultado = repositorio.findClientBybirthDateBetween(dataInicio, dataFim);

        Assertions.assertThat(listaResultado.size()).isEqualTo(1);
        Assertions.assertThat(listaResultado.get(0).getBirthDate()).isEqualTo(dataNascimentoEsperada);
    }

    @Test
    @DisplayName("Testar o metodo que realiza update no ClientRepository")
    public void testarSaveClient() {
        String nomeEsperado = "Fernanda Oliveira";
        Double salarioEsperado = 2500.0;
        Instant dataAniversarioEsperada = Instant.parse("2002-08-11T20:50:00Z");

        Optional<Client> resultadoClient = repositorio.findById(1L);
        if (resultadoClient.isPresent()){
            Client client = resultadoClient.get();
            client.setName(nomeEsperado);
            client.setBirthDate(dataAniversarioEsperada);
            client.setIncome(salarioEsperado);
            repositorio.save(client);
        }

        Assertions.assertThat(repositorio.findById(1L).get().getName()).isEqualTo(nomeEsperado);
        Assertions.assertThat(repositorio.findById(1L).get().getIncome()).isEqualTo(salarioEsperado);
        Assertions.assertThat(repositorio.findById(1L).get().getBirthDate()).isEqualTo(dataAniversarioEsperada);
    }



}
