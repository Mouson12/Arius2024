# Etap 1: Budowanie aplikacji
FROM node:18 AS build

# Ustaw katalog roboczy
WORKDIR /app

# Kopiuj pliki package.json i package-lock.json
COPY package*.json ./

# Zainstaluj zależności
RUN npm install

# Skopiuj pozostałe pliki projektu
COPY . .

# Zbuduj aplikację
RUN npm run build

# Etap 2: Serwowanie aplikacji przez Nginx
FROM nginx:latest

# Skopiuj zbudowaną aplikację do Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Skopiuj niestandardowy plik konfiguracji Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Otwórz port 80
EXPOSE 80

# Domyślna komenda uruchamiająca Nginx
CMD ["nginx", "-g", "daemon off;"]