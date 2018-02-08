# --- !Ups

alter table lista_zelja2017 add column krug smallint, add column redni_broj smallint;
alter table lista_zelja2016 add column redni_broj smallint;
alter table lista_zelja2015 add column redni_broj smallint;

# --- !Downs

alter table lista_zelja2017 drop column krug, drop column redni_broj;
alter table lista_zelja2016 drop column redni_broj;
alter table lista_zelja2015 drop column redni_broj;