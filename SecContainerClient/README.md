# secCon Client (Java)

## Installation

1. Le projet utilise maven, installez les dépendances en actualisant le fichier pom.xml
2. Pour pouvoir lancer l'application, ajouter une configuration maven et ajoutez la commande : 

> clean javafx:run

Voir illustration :

![image](https://github.com/NathanLombardelli/Helmo_TransfertFichiers_Java/assets/55028792/5bccc93d-9739-4585-9e11-88747ee419ec)


## Démonstration

### Sign Up & Sign In

![image](https://github.com/NathanLombardelli/Helmo_TransfertFichiers_Java/assets/55028792/4a6d998b-8c30-4252-976b-e80ab6d76540)

![image](https://github.com/NathanLombardelli/Helmo_TransfertFichiers_Java/assets/55028792/c2208cfd-5adf-41f5-837c-9f22112d6499)


### Sign Out

![image](https://github.com/NathanLombardelli/Helmo_TransfertFichiers_Java/assets/55028792/1fb2852c-3e2b-4b71-afe8-c9108f42a34c)


### Activate 2FA
Pour activer l'authentification a deux facteurs vous devez préalablement être inscrit, une fois inscrit vous devez vous déconnectez.
Cliquez sur "Activate" dans la barre d'outils (2FA).

![image](https://github.com/NathanLombardelli/Helmo_TransfertFichiers_Java/assets/55028792/6c9fce81-6b2a-4804-a02c-1998adec1278)


Une fois la double authentification activée, installez Google Authenticator et scannez le QRCODE.
Entrez le code à 6 chiffres qui est généré toutes les 30 secondes.
![image](https://github.com/NathanLombardelli/Helmo_TransfertFichiers_Java/assets/55028792/1fcea415-58c1-4bb3-ac83-9d9ccee09b95)


Maintenant que vous avez activé l'authentification 2 facteurs un code de 6 chiffres vous ait demandez a chaque connexion.
![image](https://github.com/NathanLombardelli/Helmo_TransfertFichiers_Java/assets/55028792/eefdcca1-3869-45ff-9a3e-9bf8e6f33af5)


## Dépendances
- **JavaFx** est un framework et une bibliothèque d'interface utilisateur
- **Jmetro** est un thème pour JavaFx
- **Totp** est une librairie qui permet de gérer les mots de passes basé sur le temps (QRCODE)
