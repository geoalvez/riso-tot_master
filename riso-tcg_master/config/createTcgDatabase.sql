CREATE TABLE dataentidades (
  nome_entidade   varchar(255),
  data           varchar(255)
);

CREATE INDEX I_dataentidades ON dataentidades (nome_entidade);

CREATE TABLE entidadesnomesalt (
  nome_entidade   varchar(255),
  nomealt           varchar(255)

);

CREATE INDEX I_entidadesnomesalt ON entidadesnomesalt (nome_entidade, nomealt);


create table datanorm (data varchar(255), datanormalizada varchar(255), id_documento bigint, seq_frase bigint, flg_extr_tm varchar(255));
CREATE INDEX I_datanorm ON datanorm (data, datanormalizada, id_documento, seq_frase, flg_extr_tm);


create table documentinfo (id_documento bigint, nome_documento varchar(255));
CREATE INDEX I_documentinfo ON documentinfo (id_documento, nome_documento);

