# Use official MySQL Server image
FROM mysql/mysql-server:8.0

# Copy SQL file to auto-initialization folder
COPY world.sql /docker-entrypoint-initdb.d/

# Set database root password and grant access from any host
ENV MYSQL_ROOT_PASSWORD=example
ENV MYSQL_ROOT_HOST=%