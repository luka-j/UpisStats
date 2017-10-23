# --- !Ups

alter table ucenik2017 RENAME TO ucenici2017;

alter table smerovi2017
  add column upisano_1k int,
  add column upisano_2k int,
  add column kvota_2k int,
  add column min_bodova_1k float,
  add column min_bodova_2k float;

alter table lista_zelja2017 add column bodova_za_upis float;

alter table ucenici2017
  add column najbolji_blizanac_bodovi float,
  add column blizanac_sifra int,
  add column bodova_am float,
  add column maternji_jezik varchar(255),
  add column prvi_strani_jezik varchar(255),
  add column vukova_diploma boolean,
  add column prioritet boolean;


# --- !Downs

alter table smerovi2017
  drop column upisano_1k,
  drop column upisano_2k,
  drop column kvota_2k,
  drop column min_bodova_1k,
  drop column min_bodova_2k;

alter table lista_zelja2017 drop column bodova_za_upis;

alter table ucenici2017
  drop column najbolji_blizanac_bodovi,
  drop column blizanac_sifra,
  drop column bodova_am,
  drop column maternji_jezik,
  drop column prvi_strani_jezik,
  drop column vukova_diploma,
  drop column prioritet;

alter table ucenici2017 RENAME TO ucenik2017;
