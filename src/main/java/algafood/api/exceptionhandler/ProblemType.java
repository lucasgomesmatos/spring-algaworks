package algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {


    DADOS_INVALIDOS("Dados inválidos", "/dados-invalidos"),
    PARAMETRO_INVALIDO("Parâmetro inválido", "/parametro-invalido"),
    MENSAGEM_INCOMPREENSIVEL("Mensagem incompreensível", "/mensagem-imcompreensível"),
    RECURSO_NAO_ENCONTADO("Recurso não encontrado", "/recurso-nao-encontado"),
    ENTIDADE_EM_USO("Entidade em uso", "/entidade-em-uso"),
    ERRO_NEGOCIO("Violação de regra de negócio", "/erro-negocio"),
    ERRO_DE_SISTEMA("Erro de sistema", "/erro-de-sistema");

    private String title;
    private String path;

    ProblemType(String title, String path) {
        this.title = title;
        this.path = String.format("https://algafood.com.br%s", path);
    }


}
