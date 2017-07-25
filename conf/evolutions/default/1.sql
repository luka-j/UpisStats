# --- !Ups

create table os2015 (
  id                            bigserial not null,
  broj_ucenika                  integer,
  ime                           varchar(255),
  mesto                         varchar(255),
  okrug                         varchar(255),
  adresa                        varchar(255),
  telefon                       varchar(255),
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
  constraint pk_os2015 primary key (id)
);

create table os2016 (
  id                            bigserial not null,
  broj_ucenika                  integer,
  ime                           varchar(255),
  mesto                         varchar(255),
  okrug                         varchar(255),
  adresa                        varchar(255),
  telefon                       varchar(255),
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
  sajt_id                       integer,
  broj_ucenika_ukupno           integer,
  matematika6real               float,
  matematika7real               float,
  matematika8real               float,
  srpski6real                   float,
  srpski7real                   float,
  srpski8real                   float,
  srpski_preal                  float,
  matematika_preal              float,
  procenat_upisanih             float,
  constraint pk_os2016 primary key (id)
);

create table smerovi2015 (
  id                            bigserial not null,
  broj_ucenika                  integer,
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
  constraint uq_smerovi2015_sifra unique (sifra),
  constraint pk_smerovi2015 primary key (id)
);

create table smerovi2016 (
  id                            bigserial not null,
  broj_ucenika                  integer,
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
  constraint uq_smerovi2016_sifra unique (sifra),
  constraint pk_smerovi2016 primary key (id)
);

create table takmicenja2015 (
  id                            bigserial not null,
  predmet                       varchar(255),
  bodova                        integer,
  mesto                         integer,
  rang                          integer,
  ucenik_id                     bigint,
  constraint pk_takmicenja2015 primary key (id)
);

create table takmicenja2016 (
  id                            bigserial not null,
  predmet                       varchar(255),
  bodova                        integer,
  mesto                         integer,
  rang                          integer,
  ucenik_id                     bigint,
  constraint pk_takmicenja2016 primary key (id)
);

create table ucenici2015 (
  id                            bigserial not null,
  sifra                         integer,
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
  osnovna_id                    bigint,
  upisana_id                    bigint,
  constraint uq_ucenici2015_sifra unique (sifra),
  constraint pk_ucenici2015 primary key (id)
);

create table ucenici2016 (
  id                            bigserial not null,
  sifra                         integer,
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
  osnovna_id                    bigint,
  upisana_id                    bigint,
  constraint uq_ucenici2016_sifra unique (sifra),
  constraint pk_ucenici2016 primary key (id)
);

create table lista_zelja2015 (
  id                            bigserial not null,
  smer_id                       bigint,
  ucenik_id                     bigint,
  constraint pk_lista_zelja2015 primary key (id)
);

create table lista_zelja2016 (
  id                            bigserial not null,
  smer_id                       bigint,
  ucenik_id                     bigint,
  constraint pk_lista_zelja2016 primary key (id)
);

alter table takmicenja2015 add constraint fk_takmicenja2015_ucenik_id foreign key (ucenik_id) references ucenici2015 (id) on delete restrict on update restrict;
create index ix_takmicenja2015_ucenik_id on takmicenja2015 (ucenik_id);

alter table takmicenja2016 add constraint fk_takmicenja2016_ucenik_id foreign key (ucenik_id) references ucenici2016 (id) on delete restrict on update restrict;
create index ix_takmicenja2016_ucenik_id on takmicenja2016 (ucenik_id);

alter table ucenici2015 add constraint fk_ucenici2015_osnovna_id foreign key (osnovna_id) references os2015 (id) on delete restrict on update restrict;
create index ix_ucenici2015_osnovna_id on ucenici2015 (osnovna_id);

alter table ucenici2015 add constraint fk_ucenici2015_upisana_id foreign key (upisana_id) references smerovi2015 (id) on delete restrict on update restrict;
create index ix_ucenici2015_upisana_id on ucenici2015 (upisana_id);

alter table ucenici2016 add constraint fk_ucenici2016_osnovna_id foreign key (osnovna_id) references os2016 (id) on delete restrict on update restrict;
create index ix_ucenici2016_osnovna_id on ucenici2016 (osnovna_id);

alter table ucenici2016 add constraint fk_ucenici2016_upisana_id foreign key (upisana_id) references smerovi2016 (id) on delete restrict on update restrict;
create index ix_ucenici2016_upisana_id on ucenici2016 (upisana_id);

alter table lista_zelja2015 add constraint fk_lista_zelja2015_smer_id foreign key (smer_id) references smerovi2015 (id) on delete restrict on update restrict;
create index ix_lista_zelja2015_smer_id on lista_zelja2015 (smer_id);

alter table lista_zelja2015 add constraint fk_lista_zelja2015_ucenik_id foreign key (ucenik_id) references ucenici2015 (id) on delete restrict on update restrict;
create index ix_lista_zelja2015_ucenik_id on lista_zelja2015 (ucenik_id);

alter table lista_zelja2016 add constraint fk_lista_zelja2016_smer_id foreign key (smer_id) references smerovi2016 (id) on delete restrict on update restrict;
create index ix_lista_zelja2016_smer_id on lista_zelja2016 (smer_id);

alter table lista_zelja2016 add constraint fk_lista_zelja2016_ucenik_id foreign key (ucenik_id) references ucenici2016 (id) on delete restrict on update restrict;
create index ix_lista_zelja2016_ucenik_id on lista_zelja2016 (ucenik_id);


# --- !Downs

alter table if exists takmicenja2015 drop constraint if exists fk_takmicenja2015_ucenik_id;
drop index if exists ix_takmicenja2015_ucenik_id;

alter table if exists takmicenja2016 drop constraint if exists fk_takmicenja2016_ucenik_id;
drop index if exists ix_takmicenja2016_ucenik_id;

alter table if exists ucenici2015 drop constraint if exists fk_ucenici2015_osnovna_id;
drop index if exists ix_ucenici2015_osnovna_id;

alter table if exists ucenici2015 drop constraint if exists fk_ucenici2015_upisana_id;
drop index if exists ix_ucenici2015_upisana_id;

alter table if exists ucenici2016 drop constraint if exists fk_ucenici2016_osnovna_id;
drop index if exists ix_ucenici2016_osnovna_id;

alter table if exists ucenici2016 drop constraint if exists fk_ucenici2016_upisana_id;
drop index if exists ix_ucenici2016_upisana_id;

alter table if exists lista_zelja2015 drop constraint if exists fk_lista_zelja2015_smer_id;
drop index if exists ix_lista_zelja2015_smer_id;

alter table if exists lista_zelja2015 drop constraint if exists fk_lista_zelja2015_ucenik_id;
drop index if exists ix_lista_zelja2015_ucenik_id;

alter table if exists lista_zelja2016 drop constraint if exists fk_lista_zelja2016_smer_id;
drop index if exists ix_lista_zelja2016_smer_id;

alter table if exists lista_zelja2016 drop constraint if exists fk_lista_zelja2016_ucenik_id;
drop index if exists ix_lista_zelja2016_ucenik_id;

drop table if exists os2015 cascade;

drop table if exists os2016 cascade;

drop table if exists smerovi2015 cascade;

drop table if exists smerovi2016 cascade;

drop table if exists takmicenja2015 cascade;

drop table if exists takmicenja2016 cascade;

drop table if exists ucenici2015 cascade;

drop table if exists ucenici2016 cascade;

drop table if exists lista_zelja2015 cascade;

drop table if exists lista_zelja2016 cascade;

