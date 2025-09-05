CREATE TABLE instrucoes(

    id bigint NOT NULL AUTO_INCREMENT,
    aluno_id bigint NOT NULL,
    instrutor_id bigint NOT NULL,
    data_hora DATETIME NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    motivo_cancelamento VARCHAR(100),

    PRIMARY KEY(id),
    
    FOREIGN KEY (aluno_id) REFERENCES alunos(id),
    FOREIGN KEY (instrutor_id) REFERENCES instrutores(id)

)
