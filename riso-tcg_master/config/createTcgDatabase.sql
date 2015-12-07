CREATE TABLE dataentidades (
  nome_entidade   varchar(255),
  data           varchar(255)
);

CREATE INDEX I_dataentidades ON dataentidades (nome_entidade);

CREATE TABLE entidadesnomesalt (
  nome_entidade   varchar(255),
  nomealt           varchar(255)

);

CREATE INDEX I_entidadesnomesalt ON entidadesnomesalt (nome_entidade, file_offset);