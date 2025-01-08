#!/bin/bash

# Enable exit on error
set -e

# Store command line arguments
host="$1"
shift
cmd="$@"

# Wait for PostgreSQL database to become available
# Continuously checks if the database is accepting connections
until pg_isready -h "garage_db" -p 5432 -U admin; do
    echo "Postgres is unavailable - sleeping"
    sleep 1  # Wait for 1 second before retrying
done

# Database is now available
echo "Postgres is up - executing command"

# Execute the provided command
exec $cmd