package br.com.cohabfood.pagamentos.dto;

import br.com.cohabfood.pagamentos.model.ItemDoPedido;
import br.com.cohabfood.pagamentos.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PagamentoDTO {

    private Long id;
    private BigDecimal valor;
    private String nome;
    private String numero;
    private String expiracao;
    private String codigo;
    private Status status;
    private Long pedidoId;
    private Long formaDePagamentoId;
    private List<ItemDoPedido> itens = new ArrayList<>();

}
