package br.com.cohabfood.pagamentos.service;

import br.com.cohabfood.pagamentos.dto.PagamentoDTO;
import br.com.cohabfood.pagamentos.http.PedidoClient;
import br.com.cohabfood.pagamentos.model.Pagamento;
import br.com.cohabfood.pagamentos.model.Status;
import br.com.cohabfood.pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PagamentoService {
    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private PedidoClient pedido;
    @Autowired
    private ModelMapper modelMapper;

    public Page<PagamentoDTO> obterTodos (Pageable paginacao) {
        return repository.findAll(paginacao)
                .map(p -> modelMapper.map(p, PagamentoDTO.class));
    }

    public PagamentoDTO obterPorId (Long id) {
        Pagamento pagamento = repository.findById(id).orElseThrow(()-> new EntityNotFoundException());
        PagamentoDTO dto = modelMapper.map(pagamento, PagamentoDTO.class);
        dto.setItens(pedido.consultarItensPedido(id).getItens());
        return dto;
    }

    public PagamentoDTO criarPagamento (PagamentoDTO dto) {
        Pagamento pagamento = modelMapper.map(dto,Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        repository.save(pagamento);

        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    public PagamentoDTO atualizarPagamento (Long id, PagamentoDTO dto) {
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setId(id);
        pagamento = repository.save(pagamento);
        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    public void excluirPagamento (Long id) {
        repository.deleteById(id);
    }


    public void confirmarPagamento(Long id){
        Optional<Pagamento> pagamento = repository.findById(id);

        if (!pagamento.isPresent()) {
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO);
        repository.save(pagamento.get());
        pedido.atualizarPagamento(pagamento.get().getPedidoId());
    }



    public void alteraStatus(Long id) {
        Optional<Pagamento> pagamento = repository.findById(id);

        if (!pagamento.isPresent()) {
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
        repository.save(pagamento.get());

    }
}
