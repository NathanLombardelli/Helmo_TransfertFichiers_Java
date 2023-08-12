# secCon Client (Java)

## Installation

1. Le projet utilise maven, installez les dépendances en actualisant le fichier pom.xml
2. Pour pouvoir lancer l'application, ajouter une configuration maven et ajoutez la commande : 

> clean javafx:run

Voir illustration :

![alt](/README/installation1.png)

## Démonstration

### Sign Up & Sign In

![alt](/README/demo1.png)
![alt](/README/demo2.png)

### Sign Out

![alt](/README/demo3.png)

### Activate 2FA
Pour activer l'authentification a deux facteurs vous devez préalablement être inscrit, une fois inscrit vous devez vous déconnectez.
Cliquez sur "Activate" dans la barre d'outils (2FA).

![alt](/README/demo4.png)

Une fois la double authentification activée, installez Google Authenticator et scannez le QRCODE.
Entrez le code à 6 chiffres qui est généré toutes les 30 secondes.
![alt](/README/demo5.png)

Maintenant que vous avez activé l'authentification 2 facteurs un code de 6 chiffres vous ait demandez a chaque connexion.
![alt](/README/demo6.png)

## Dépendances
- **JavaFx** est un framework et une bibliothèque d'interface utilisateur
- **Jmetro** est un thème pour JavaFx
- **Totp** est une librairie qui permet de gérer les mots de passes basé sur le temps (QRCODE)
