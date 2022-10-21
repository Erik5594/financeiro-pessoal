package com.eqosoftware.financeiropessoal.dto.pagamento;

import com.eqosoftware.financeiropessoal.dto.BaseDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Created by erik on 01/04/2022.
 */
@Data
public class PagamentoDto extends BaseDto {

    private FormaPagamento formaPagamento;
    private CondicaoPagamento condicaoPagamento;
    private Double valorTotal;
    private LocalDate data;
    private ParcelamentoDto parcelamento;
    private UUID idConta;

}
