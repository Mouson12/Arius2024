#!/bin/bash
set -e

host="$1"
shift
cmd="$@"

# Czekamy aż baza danych będzie dostępna
until pg_isready -h "garage_db" -p 5432 -U admin; do
  echo "Postgres is unavailable - sleeping"
  sleep 1
done

echo "Postgres is up - executing command"
exec $cmd
