# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table os (
  id                            bigserial not null,
  godina                        integer,
  ime                           varchar(255),
  mesto                         varchar(255),
  okrug                         varchar(255),
  likovno6                      float,
  tehnicko6                     float,
  geografija6                   float,
  biologija6                    float,
  sport6                        float,
  drugi_strani6                 float,
  matematika6                   float,
  istorija6                     float,
  engleski6                     float,
  muzicko6                      float,
  fizicko6                      float,
  vladanje6                     float,
  fizika6                       float,
  srpski6                       float,
  likovno7                      float,
  tehnicko7                     float,
  geografija7                   float,
  biologija7                    float,
  sport7                        float,
  drugi_strani7                 float,
  matematika7                   float,
  istorija7                     float,
  engleski7                     float,
  muzicko7                      float,
  fizicko7                      float,
  vladanje7                     float,
  fizika7                       float,
  srpski7                       float,
  hemija7                       float,
  likovno8                      float,
  tehnicko8                     float,
  geografija8                   float,
  biologija8                    float,
  sport8                        float,
  drugi_strani8                 float,
  matematika8                   float,
  istorija8                     float,
  engleski8                     float,
  muzicko8                      float,
  fizicko8                      float,
  vladanje8                     float,
  fizika8                       float,
  srpski8                       float,
  hemija8                       float,
  likovno_p                     float,
  tehnicko_p                    float,
  geografija_p                  float,
  biologija_p                   float,
  sport_p                       float,
  drugi_strani_p                float,
  matematika_p                  float,
  istorija_p                    float,
  engleski_p                    float,
  muzicko_p                     float,
  fizicko_p                     float,
  vladanje_p                    float,
  fizika_p                      float,
  srpski_p                      float,
  hemija_p                      float,
  prosek_sesti                  float,
  prosek_sedmi                  float,
  prosek_osmi                   float,
  prosek_ukupno                 float,
  matematika                    float,
  srpski                        float,
  kombinovani                   float,
  bodovi_iz_skole               float,
  bodovi_sa_zavrsnog            float,
  bodovi_ukupno                 float,
  bodovi_sa_prijemnog           float,
  constraint pk_os primary key (id)
);

create table smerovi (
  id                            bigserial not null,
  godina                        integer,
  sifra                         varchar(255),
  ime                           varchar(255),
  mesto                         varchar(255),
  okrug                         varchar(255),
  smer                          varchar(255),
  podrucje                      varchar(255),
  kvota                         integer,
  likovno6                      float,
  tehnicko6                     float,
  geografija6                   float,
  biologija6                    float,
  sport6                        float,
  drugi_strani6                 float,
  matematika6                   float,
  istorija6                     float,
  engleski6                     float,
  muzicko6                      float,
  fizicko6                      float,
  vladanje6                     float,
  fizika6                       float,
  srpski6                       float,
  likovno7                      float,
  tehnicko7                     float,
  geografija7                   float,
  biologija7                    float,
  sport7                        float,
  drugi_strani7                 float,
  matematika7                   float,
  istorija7                     float,
  engleski7                     float,
  muzicko7                      float,
  fizicko7                      float,
  vladanje7                     float,
  fizika7                       float,
  srpski7                       float,
  hemija7                       float,
  likovno8                      float,
  tehnicko8                     float,
  geografija8                   float,
  biologija8                    float,
  sport8                        float,
  drugi_strani8                 float,
  matematika8                   float,
  istorija8                     float,
  engleski8                     float,
  muzicko8                      float,
  fizicko8                      float,
  vladanje8                     float,
  fizika8                       float,
  srpski8                       float,
  hemija8                       float,
  likovno_p                     float,
  tehnicko_p                    float,
  geografija_p                  float,
  biologija_p                   float,
  sport_p                       float,
  drugi_strani_p                float,
  matematika_p                  float,
  istorija_p                    float,
  engleski_p                    float,
  muzicko_p                     float,
  fizicko_p                     float,
  vladanje_p                    float,
  fizika_p                      float,
  srpski_p                      float,
  hemija_p                      float,
  prosek_sesti                  float,
  prosek_sedmi                  float,
  prosek_osmi                   float,
  prosek_ukupno                 float,
  matematika                    float,
  srpski                        float,
  kombinovani                   float,
  bodovi_iz_skole               float,
  bodovi_sa_zavrsnog            float,
  bodovi_ukupno                 float,
  bodovi_sa_prijemnog           float,
  constraint uq_smerovi_sifra unique (sifra),
  constraint pk_smerovi primary key (id)
);

create table takmicenja (
  id                            bigserial not null,
  ucenik_id                     bigint,
  predmet                       varchar(255),
  bodova                        integer,
  mesto                         integer,
  rang                          integer,
  constraint pk_takmicenja primary key (id)
);

create table ucenici (
  id                            bigserial not null,
  godina                        integer,
  sifra                         integer,
  osnovna_id                    bigint,
  upisana_id                    bigint,
  drugi_strani_jezik            varchar(255),
  likovno6                      integer,
  tehnicko6                     integer,
  geografija6                   integer,
  biologija6                    integer,
  sport6                        integer,
  drugi_strani6                 integer,
  matematika6                   integer,
  istorija6                     integer,
  engleski6                     integer,
  muzicko6                      integer,
  fizicko6                      integer,
  vladanje6                     integer,
  fizika6                       integer,
  srpski6                       integer,
  likovno7                      integer,
  tehnicko7                     integer,
  geografija7                   integer,
  biologija7                    integer,
  sport7                        integer,
  drugi_strani7                 integer,
  matematika7                   integer,
  istorija7                     integer,
  engleski7                     integer,
  muzicko7                      integer,
  fizicko7                      integer,
  vladanje7                     integer,
  fizika7                       integer,
  srpski7                       integer,
  hemija7                       integer,
  likovno8                      integer,
  tehnicko8                     integer,
  geografija8                   integer,
  biologija8                    integer,
  sport8                        integer,
  drugi_strani8                 integer,
  matematika8                   integer,
  istorija8                     integer,
  engleski8                     integer,
  muzicko8                      integer,
  fizicko8                      integer,
  vladanje8                     integer,
  fizika8                       integer,
  srpski8                       integer,
  hemija8                       integer,
  likovno_p                     float,
  tehnicko_p                    float,
  geografija_p                  float,
  biologija_p                   float,
  sport_p                       float,
  drugi_strani_p                float,
  matematika_p                  float,
  istorija_p                    float,
  engleski_p                    float,
  muzicko_p                     float,
  fizicko_p                     float,
  vladanje_p                    float,
  fizika_p                      float,
  srpski_p                      float,
  hemija_p                      float,
  prosek_sesti                  float,
  prosek_sedmi                  float,
  prosek_osmi                   float,
  prosek_ukupno                 float,
  matematika                    float,
  srpski                        float,
  kombinovani                   float,
  bodovi_iz_skole               float,
  bodovi_sa_zavrsnog            float,
  bodovi_ukupno                 float,
  bodovi_sa_prijemnog           float,
  bodovi_sa_takmicenja          float,
  broj_zelja                    integer,
  upisana_zelja                 integer,
  krug                          integer,
  constraint uq_ucenici_sifra unique (sifra),
  constraint pk_ucenici primary key (id)
);

create table lista_zelja (
  id                            bigserial not null,
  smer_id                       bigint,
  ucenik_id                     bigint,
  constraint pk_lista_zelja primary key (id)
);

alter table takmicenja add constraint fk_takmicenja_ucenik_id foreign key (ucenik_id) references ucenici (id) on delete restrict on update restrict;
create index ix_takmicenja_ucenik_id on takmicenja (ucenik_id);

alter table ucenici add constraint fk_ucenici_osnovna_id foreign key (osnovna_id) references os (id) on delete restrict on update restrict;
create index ix_ucenici_osnovna_id on ucenici (osnovna_id);

alter table ucenici add constraint fk_ucenici_upisana_id foreign key (upisana_id) references smerovi (id) on delete restrict on update restrict;
create index ix_ucenici_upisana_id on ucenici (upisana_id);

alter table lista_zelja add constraint fk_lista_zelja_smer_id foreign key (smer_id) references smerovi (id) on delete restrict on update restrict;
create index ix_lista_zelja_smer_id on lista_zelja (smer_id);

alter table lista_zelja add constraint fk_lista_zelja_ucenik_id foreign key (ucenik_id) references ucenici (id) on delete restrict on update restrict;
create index ix_lista_zelja_ucenik_id on lista_zelja (ucenik_id);


# --- !Downs

alter table if exists takmicenja drop constraint if exists fk_takmicenja_ucenik_id;
drop index if exists ix_takmicenja_ucenik_id;

alter table if exists ucenici drop constraint if exists fk_ucenici_osnovna_id;
drop index if exists ix_ucenici_osnovna_id;

alter table if exists ucenici drop constraint if exists fk_ucenici_upisana_id;
drop index if exists ix_ucenici_upisana_id;

alter table if exists lista_zelja drop constraint if exists fk_lista_zelja_smer_id;
drop index if exists ix_lista_zelja_smer_id;

alter table if exists lista_zelja drop constraint if exists fk_lista_zelja_ucenik_id;
drop index if exists ix_lista_zelja_ucenik_id;

drop table if exists os cascade;

drop table if exists smerovi cascade;

drop table if exists takmicenja cascade;

drop table if exists ucenici cascade;

drop table if exists lista_zelja cascade;

