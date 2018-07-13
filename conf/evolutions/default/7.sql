# --- !Ups

alter table ucenici2018 rename column boodova_am to bodova_am;

# --- !Downs

alter table ucenici2018 rename column bodova_am to boodova_am;