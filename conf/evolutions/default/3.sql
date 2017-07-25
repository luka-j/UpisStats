# --- !Ups

create table prijemni2017 (
  id                            bigserial not null,
  ucenik_id                     bigint,
  naziv_ispita                  text,
  bodova                        float,
  constraint pk_prijemni2017 primary key (id)
);

alter table prijemni2017 add constraint fk_prijemni2017_ucenik_id foreign key (ucenik_id) references ucenik2017 (id) on delete restrict on update restrict;
create index ix_prijemni2017_ucenik_id on prijemni2017 (ucenik_id);



# --- !Downs

drop table if exists prijemni2017 cascade;